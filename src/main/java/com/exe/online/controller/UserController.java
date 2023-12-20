package com.exe.online.controller;

import com.exe.online.dao.*;
import com.exe.online.entity.Email;
import com.exe.online.helper.Message;
import com.exe.online.helper.UUID;
import com.exe.online.model.*;
import com.exe.online.services.EmailService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private ServiceManRepository serviceManRepository;
    @Autowired
    private BookingDetailRepository bookingDetailRepository;
    @Autowired
    private AllocateBookingRepository allocateBookingRepository;
    @Autowired
    private SceduleRepository sceduleRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private FeedBackRepository feedBackRepository;

    @ModelAttribute
    public void addCommanData(Model model, Principal principal) {
        String userName = principal.getName();
        logger.info("Login By " + userName);

        // get User Detail By UserName(EmailId)
        User user = this.userRepository.getUserByUserName(userName);
        model.addAttribute("user", user);
    }

    @GetMapping("/index")
    public String dashboard(Model model) {

        List<Category> categories = this.categoryRepository.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("title", "User DashBoard");
        return "user/user_dashboard";
    }

    @GetMapping("/{category_id}/selected-services")
    public String selectedService(@PathVariable("category_id") long category_id, Model model) {
        model.addAttribute("title", "All Services");
        List<Service> services = this.serviceRepository.findByCategoryId((int) category_id);
        Category category = this.categoryRepository.findById(category_id).get();
        model.addAttribute("category", category);
        model.addAttribute("services", services);

        return "user/show_services";
    }

    @GetMapping("/{service_id}/apply-service")
    public String applyService(@PathVariable("service_id") long service_id, Model model) {
        Service service = this.serviceRepository.findById(service_id).get();
        model.addAttribute("service", service);
        model.addAttribute("title", "Apply Service");
        model.addAttribute("bookingDetail", new BookingDetail());
        return "user/applyservice";
    }

    //booking data
    @PostMapping("/handle-applydata")
    @ResponseBody
    public ResponseEntity<?> handleApplyData(@ModelAttribute("bookingDetail") BookingDetail bookingDetail, @RequestParam("service_id") long service_id, Principal principal) {
        logger.info(bookingDetail.getBooking_email());
        Service service = this.serviceRepository.findById(service_id).get();
        String name = principal.getName();
        User user = this.userRepository.getUserByUserName(name);
        long booking_id = 0;
        long amount = 0;
        try {
            bookingDetail.setBooking_date(LocalDate.now());
            bookingDetail.setBooking_status(true);
            bookingDetail.setService_status("Pending");
            bookingDetail.setService(service);
            bookingDetail.setService_request_number("TOKEN" + UUID.getSixDigitID(UUID.generateUniqueID()));
            bookingDetail.setUser(user);
            BookingDetail bookingDetail1 = this.bookingDetailRepository.save(bookingDetail);
            booking_id = bookingDetail1.getBooking_id();
            amount = bookingDetail1.getService().getService_amount();
            ServiceMan serviceManForBooking = this.serviceManRepository.findServiceManForBooking(bookingDetail1.getService(), bookingDetail1.getBooking_pincode());
            if (serviceManForBooking == null) {
                this.logger.info("do not provide any service");

                String text = "<html><head><style>" + "h3 { color: #007BFF; }" + "</style></head><body>" + "<p>Dear " + bookingDetail.getBooking_person() + ",</p>" + "<br>" + "<p>We have received a request to Booking Your Service.</p>" + "<p>To ensure  In your city Do Not have Any Service Available so Give Suggestion For Service in your Address:</p>" + "<br>" + "<h3>Order Information: <span style=\"color: #007BFF; font-weight: bold;\">Your Order Rejected This Time </span></h3>" + "<br>" + "<p>After Sometime visit our Website And Book Service.</p>" + "<p>Thank You For Visit Our Website.</p>" + "</body></html>";
                Email email1 = new Email();
                email1.setTo(bookingDetail.getBooking_email());
                email1.setSubject("Order Rejected - Order #" + bookingDetail.getService_request_number());
                email1.setMassage(text);
                this.emailService.sendEmail(email1);
                this.bookingDetailRepository.delete(bookingDetail);
                return ResponseEntity.ok("error");
            } else {
                this.allocateBookingRepository.save(AllocateBooking.builder().bookingDetail(bookingDetail1).serviceMan(serviceManForBooking).build());
                this.logger.info("Booking Successfully And Allocate Service Man");
                Map<String, Long> data = new HashMap<>();
                data.put("success", 1L);

                data.put("booking_id", booking_id);
                data.put("amount", amount);
                String bookedContent = "<html><head><style>" + "h3 { color: #007BFF; }" + ".order-details { border: 1px solid #ddd; padding: 10px; margin-bottom: 20px; }" + "</style></head><body>" + "<p>Dear User,</p>" + "<p>Your order with Utility Service Provider has been successfully booked. Below are the details:</p>" + "<div class=\"order-details\">" + "<p><strong>Order Number:</strong> " + bookingDetail.getService_request_number() + "</p>" + "<p><strong>Product:</strong> " + bookingDetail.getService().getService_name() + "</p>" + "<p><strong>Total Amount:</strong> $" + bookingDetail.getService().getService_amount() + "</p>" + "</div>" + "<p>Thank you for choosing Utility Service Provider! If you have any questions, feel free to contact us And fill further information.</p>" + "</body></html>";
                Email email2 = new Email();
                email2.setTo(bookingDetail.getBooking_email());
                email2.setSubject("Order Confirm - Order #" + bookingDetail.getService_request_number());
                email2.setMassage(bookedContent);
                this.emailService.sendEmail(email2);
                return ResponseEntity.ok(data);

            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok("Something went wrong");
        }


    }

    //pending
    @GetMapping("/booking-pending/{page}")
    public String bookingPending(@PathVariable("page") int page, Model model, Principal principal) {

        model.addAttribute("title", "Pending-Booking");
        try {
            Pageable pageable = PageRequest.of(page, 5);
            User user = this.userRepository.getUserByUserName(principal.getName());
            Page<BookingDetail> ServiceStatusPending = this.bookingDetailRepository.findByServiceStatusPending(user, pageable);

            model.addAttribute("ServiceStatusPending", ServiceStatusPending.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", ServiceStatusPending.getTotalPages());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "user/pending-booking";
    }

    //histrory
    @GetMapping("/booking-history/{page}")
    public String bookingHistory(@PathVariable("page") int page, Model model, Principal principal) {

        model.addAttribute("title", "Booking History");
        try {
            Pageable pageable = PageRequest.of(page, 5);
            User user = this.userRepository.getUserByUserName(principal.getName());
            //Page<BookingDetail> ServiceStatusPending = this.bookingDetailRepository.findByServiceStatusPending(user);
            Page<BookingDetail> ServiceStatusCompleted = this.bookingDetailRepository.findByServiceStatusCompleted(user, pageable);
            model.addAttribute("", ServiceStatusCompleted);
            model.addAttribute("ServiceStatusCompleted", ServiceStatusCompleted.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", ServiceStatusCompleted.getTotalPages());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "user/booking-history";
    }


    //cancle booking
    @GetMapping("/{bookingId}/cancle-booking")
    public String cancleBooking(@PathVariable("bookingId") int bookingId, Model model, HttpSession httpSession) {
        try {
            AllocateBooking byBookigId = this.allocateBookingRepository.findByBookigId(bookingId);
            Transcation transcation = byBookigId.getBookingDetail().getTranscation();
            if (transcation == null) {

            } else {
                transcation.setBookingDetail(null);
            }
            this.allocateBookingRepository.delete(byBookigId);
            httpSession.setAttribute("message", new Message("Successfully Cancel Booking!!", "success"));
            this.logger.info("successfull cancel booking");
        } catch (Exception e) {
            e.printStackTrace();
            httpSession.setAttribute("message", new Message("Something Went Wrong!!", "danger"));
        }
        return "redirect:/user/booking-pending/0";
    }

    //status
    @GetMapping("set-booking-status/{bookingId}")
    public ResponseEntity<?> setbookingStatus(@PathVariable("bookingId") long bookingId) {
        BookingDetail bookingDetail = null;
        Map<String, Object> map = new HashMap<>();

        try {
            bookingDetail = this.bookingDetailRepository.findById(bookingId).get();
            AllocateBooking allocateBooking = this.allocateBookingRepository.findByBookigId((int) bookingId);
            Schedule schedule = this.sceduleRepository.findByAllocateBooking(allocateBooking);
            if (schedule == null) {
                map.put("bookingDetail", bookingDetail);
                return ResponseEntity.ok(map);
            } else {
                map.put("bookingDetail", bookingDetail);
                map.put("schedule", schedule);
                return ResponseEntity.ok(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // You can add a default response here in case of an exception
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PostMapping("/feedback-data")
    public String feedback(@ModelAttribute("feedBack") FeedBack feedBack,@RequestParam("booking_id") String bookingId,HttpSession session){
       try{

           BookingDetail bookingDetail = this.bookingDetailRepository.findById(Long.valueOf(bookingId)).get();
           feedBack.setBookingDetail(bookingDetail);
           this.feedBackRepository.save(feedBack);

           //Email Integration
           session.setAttribute("message", new Message("Successfully Give FeedBack!!", "success"));

       }catch (Exception e){
           e.printStackTrace();
           session.setAttribute("message", new Message("Something Webt Wrong!!", "danger"));

       }

        return "redirect:/user/booking-history/0";
    }
}
