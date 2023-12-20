package com.exe.online.controller;

import com.exe.online.dao.*;
import com.exe.online.entity.Email;
import com.exe.online.helper.Message;
import com.exe.online.model.*;
import com.exe.online.services.EmailService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;


@Controller
@RequestMapping("/service-provider")
public class ProviderController {
    private Logger logger = LoggerFactory.getLogger(ProviderController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceManRepository serviceManRepository;

    @Autowired
    private AllocateBookingRepository allocateBookingRepository;

    @Autowired
    private BookingDetailRepository bookingDetailRepository;

    @Autowired
    private SceduleRepository sceduleRepository;
    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @ModelAttribute
    public void addCommanData(Model model, Principal principal) {
        String userName = principal.getName();
        logger.info("Login By " + userName);

        // get User Detail By UserName(EmailId)
        User user = this.userRepository.getUserByUserName(userName);
        ServiceMan serviceMan = this.serviceManRepository.findByUser(user);
        int count = this.allocateBookingRepository.countServiceServiceStatusFalse(serviceMan);
        if (count == 0) {
            model.addAttribute("count", 0);
        } else {
            model.addAttribute("count", count);
        }
        model.addAttribute("user", serviceMan);
    }

    @GetMapping("/index")
    public String index(Model model, Principal principal) {
        model.addAttribute("title", "ServiceProvider DashBoard");
        try {
            User user = this.userRepository.getUserByUserName(principal.getName());
            ServiceMan serviceMan = this.serviceManRepository.findByUser(user);

            List<AllocateBooking> bookingWithSchedule = this.allocateBookingRepository.findAllocateBookingsWithSchedule(serviceMan);
            model.addAttribute("bookingWithSchedule", bookingWithSchedule);
            int cCount = this.allocateBookingRepository.countServiceServiceStatusTrue(serviceMan);

            if (cCount == 0) {
                model.addAttribute("cCount", 0);
            } else {
                model.addAttribute("cCount", cCount);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return "service_man/serviceman_dashboard";
    }

    @GetMapping("/pending-service/{page}")
    public String showPendingService(@PathVariable("page") int page, Model model, Principal principal) {
        model.addAttribute("title", "Pending Service");
        try {
            Pageable pageable = PageRequest.of(page, 5);
            String name = principal.getName();
            User user = this.userRepository.getUserByUserName(name);
            ServiceMan serviceMan = this.serviceManRepository.findByUser(user);

            Page<AllocateBooking> pendingBooking = this.allocateBookingRepository.findPendingBooking(serviceMan, pageable);
            if (pendingBooking.isEmpty()) {
                this.logger.info("booking pending is null");
            } else {

                model.addAttribute("pendingBooking", pendingBooking.getContent());
                model.addAttribute("currentPage", page);
                model.addAttribute("totalPages", pendingBooking.getTotalPages());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "service_man/pending-service";
    }

    @GetMapping("/service-history/{page}")
    public String showHistory(@PathVariable("page") int page, Model model, Principal principal) {
        model.addAttribute("title", "History");
        try {
            Pageable pageable = PageRequest.of(page, 5);
            String name = principal.getName();
            User user = this.userRepository.getUserByUserName(name);
            ServiceMan serviceMan = this.serviceManRepository.findByUser(user);
            Page<AllocateBooking> bookingHistory = this.allocateBookingRepository.findBookingHistory(serviceMan, pageable);
            if (bookingHistory.isEmpty()) {
                this.logger.info("booking history is null");
            } else {
                model.addAttribute("bookingHistory", bookingHistory.getContent());
                model.addAttribute("currentPage", page);
                model.addAttribute("totalPages", bookingHistory.getTotalPages());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "service_man/service-history";
    }

    //service info pending or completed
    @GetMapping("/{bookingId}/service-info")
    public String serviceInfo(@PathVariable("bookingId") int bookingId, Model model) {
        model.addAttribute("title", "Service-Info");
        try {
            BookingDetail bookingDetail = this.bookingDetailRepository.findById((long) bookingId).get();
            model.addAttribute("bookingDetail", bookingDetail);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "service_man/service_info";

    }

    @GetMapping("/{bookingId}/complete-service")
    public String completeService(@PathVariable("bookingId") long bookingId, HttpSession httpSession) {
        try {
            BookingDetail bookingDetail = this.bookingDetailRepository.findById(bookingId).get();
            bookingDetail.setService_status("Completed");
            this.bookingDetailRepository.save(bookingDetail);
            //Email-----------------
            String completionEmailTemplate = "<html>\n" + "<head>\n" + "    <style>\n" + "        body {\n" + "            font-family: Arial, sans-serif;\n" + "            margin: 0;\n" + "            padding: 0;\n" + "            background-color: #f4f4f4;\n" + "        }\n" + "\n" + "        .container {\n" + "            max-width: 600px;\n" + "            margin: 20px auto;\n" + "            padding: 20px;\n" + "            background-color: #ffffff;\n" + "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" + "            border-radius: 5px;\n" + "        }\n" + "\n" + "        h2 {\n" + "            color: #333333;\n" + "        }\n" + "\n" + "        p {\n" + "            color: #555555;\n" + "        }\n" + "\n" + "        .order-details {\n" + "            margin-bottom: 20px;\n" + "        }\n" + "\n" + "        .order-details p {\n" + "            margin: 5px 0;\n" + "        }\n" + "\n" + "        .confirmation-message {\n" + "            background-color: #f0f0f0;\n" + "            padding: 10px;\n" + "            border-radius: 5px;\n" + "        }\n" + "    </style>\n" + "</head>\n" + "<body>\n" + "    <div class=\"container\">\n" + "        <h2>Service Completed</h2>\n" + "\n" + "        <div class=\"Service-details\">\n" + "            <p><strong>Request ID:</strong> " + bookingDetail.getService_request_number() + "</p>\n" + "            <p><strong>Service Name:</strong> " + bookingDetail.getService().getService_name() + "</p>\n" + "            <p><strong>Booking Date:</strong> " + bookingDetail.getBooking_date() + "</p>\n" + "            <p><strong>Booking Status:</strong> " + bookingDetail.getService_status() + "</p>\n" + "        </div>\n" + "\n" + "        <div class=\"confirmation-message\">\n" + "            <p>Your service has been successfully completed. Thank you for choosing our service!</p>\n" + "        </div>\n" + "\n" + "        <p>If you have any questions or concerns, please contact us.</p>\n" + "    </div>\n" + "</body>\n" + "</html>";
            Email email = new Email();
            email.setTo(bookingDetail.getBooking_email());
            email.setSubject("Service Completed");
            email.setMassage(completionEmailTemplate);
            this.emailService.sendEmail(email);
            httpSession.setAttribute("message", new Message("Complete Booking", "success"));
            this.logger.info("Completed Service");
        } catch (Exception e) {
            httpSession.setAttribute("message", new Message("Something Went Wrong!!!", "danger"));
            e.printStackTrace();
        }
        return "redirect:/service-provider/pending-service/0";
    }

    //schedule
    @PostMapping("/generate-schedule")
    public String generateSchedule(@ModelAttribute("schedule") Schedule schedule, @RequestParam("bookingId") int bookingId, HttpSession httpSession) {
        try {
            AllocateBooking allocateBooking = this.allocateBookingRepository.findByBookigId(bookingId);
            schedule.setAllocateBooking(allocateBooking);
            this.sceduleRepository.save(schedule);

            httpSession.setAttribute("message", new Message("Schedule is Generated in this Requesst Id" + allocateBooking.getBookingDetail().getService_request_number() + " ", "success"));
            String serviceScheduleEmailTemplate = "<html>\n" + "<head>\n" + "    <style>\n" + "        body {\n" + "            font-family: Arial, sans-serif;\n" + "            margin: 0;\n" + "            padding: 0;\n" + "            background-color: #f4f4f4;\n" + "        }\n" + "\n" + "        .container {\n" + "            max-width: 600px;\n" + "            margin: 20px auto;\n" + "            padding: 20px;\n" + "            background-color: #ffffff;\n" + "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" + "            border-radius: 5px;\n" + "        }\n" + "\n" + "        h2 {\n" + "            color: #333333;\n" + "        }\n" + "\n" + "        p {\n" + "            color: #555555;\n" + "        }\n" + "\n" + "        .service-details {\n" + "            margin-bottom: 20px;\n" + "        }\n" + "\n" + "        .service-man-details {\n" + "            background-color: #f0f0f0;\n" + "            padding: 10px;\n" + "            border-radius: 5px;\n" + "        }\n" + "    </style>\n" + "</head>\n" + "<body>\n" + "    <div class=\"container\">\n" + "        <h2>Service Schedule Details</h2>\n" + "\n" + "        <div class=\"service-details\">\n" + "            <p><strong>Service Request Number :</strong>" + allocateBooking.getBookingDetail().getService_request_number() + " </p>\n" + "            <p><strong>Service Name:</strong> " + allocateBooking.getBookingDetail().getService().getService_name() + "</p>\n" + "            <p><strong>Booking Date:</strong> " + allocateBooking.getBookingDetail().getBooking_date() + "</p>\n" + "            <p><strong>Service Amount:</strong> " + allocateBooking.getBookingDetail().getService().getService_amount() + "</p>\n" + "        </div>\n" + "\n" + "        <h2>Service Man Details</h2>\n" + "\n" + "        <div class=\"service-man-details\">\n" + "            <p><strong>Name:</strong> " + allocateBooking.getServiceMan().getUser().getUser_name() + "</p>\n" + "            <p><strong>Email:</strong> " + allocateBooking.getServiceMan().getUser().getUser_email() + "</p>\n" + "            <p><strong>Contact Number:</strong> " + allocateBooking.getServiceMan().getServiceman_phoneNumber() + "</p>\n" + "        </div>\n" + "\n" + "        <h2>Schedule Details</h2>\n" + "\n" + "        <div class=\"Details\">\n" + "            <p><strong>Date:</strong> " + schedule.getSchedule_date() + "</p>\n" + "            <p><strong>Description:</strong> " + schedule.getSchedule_discription() + "</p>\n" + "        </div>\n" + "\n" + "        <p>Please Ready for Service at 9:00 AM to 7:00 PM</p>\n" + "        <p>If you have any questions or concerns, please contact us. Thank you for choosing our service!</p>\n" + "    </div>\n" + "</body>\n" + "</html>";
            Email email = new Email();
            email.setTo(allocateBooking.getBookingDetail().getBooking_email());
            email.setSubject("Service Detail");
            email.setMassage(serviceScheduleEmailTemplate);
            this.emailService.sendEmail(email);

            //email
        } catch (Exception e) {
            httpSession.setAttribute("message", new Message("Something Went Wrong For Allocate Schedule ", "danger"));
            e.printStackTrace();
        }

        return "redirect:/service-provider/pending-service/0";
    }

    @PostMapping("/update-schedule")
    public String updateSchedule(@RequestParam("schedule_id") long schedule_id, @RequestParam("date") LocalDate date, HttpSession httpSession) {
        try {
            Schedule schedule = this.sceduleRepository.findById(schedule_id).get();
            schedule.setSchedule_date(date);
            this.sceduleRepository.save(schedule);
            logger.info("Successfully reschedule");
            //Email-------------
            httpSession.setAttribute("message", new Message("Schedule is Update in this Requesst Id" + schedule.getAllocateBooking().getBookingDetail().getService_request_number() + " ", "success"));

        } catch (Exception e) {
            httpSession.setAttribute("message", new Message("Something Went Wrong For Update Schedule ", "danger"));
            e.printStackTrace();
        }
        return "redirect:/service-provider/index";
    }

    //profile

    @GetMapping("/profile")
    public String profilePage(Model model, Principal principal) {
        model.addAttribute("title", "Your Profile");
        ServiceMan serviceMan = this.serviceManRepository.findByUser(this.userRepository.getUserByUserName(principal.getName()));
        model.addAttribute("serviceMan", serviceMan);
        return "service_man/profile";
    }

    @GetMapping("/adhar-card/{serviceMan_id}")
    public ResponseEntity<byte[]> downloadPDF(@PathVariable("serviceMan_id") String serviceMan_id) throws IOException {
        ServiceMan serviceMan = this.serviceManRepository.findById(Long.valueOf(serviceMan_id)).get();
        ClassPathResource pdfFile = new ClassPathResource("static/back_image/serviceman_adharcard/" + serviceMan.getServiceman_adharcard()); // Replace "your-pdf-file.pdf" with the actual PDF file name
        byte[] pdfBytes = StreamUtils.copyToByteArray(pdfFile.getInputStream());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", serviceMan.getUser().getUser_name() + ".pdf"); // Change the filename as needed

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }


    @PostMapping("/update-serviceman")
    public String updateServiceMan(@RequestParam("user_name") String userName, @RequestParam("user_email") String userEmail, @ModelAttribute("serviceman") ServiceMan serviceMan,HttpSession session) {
        ServiceMan serviceMan1 = this.serviceManRepository.findById(serviceMan.getServiceman_id()).get();
        try {
                serviceMan1.getUser().setUser_email(userEmail);
                serviceMan1.getUser().setUser_name(userName);
                this.serviceManRepository.save(serviceMan1);
                session.setAttribute("message", new Message("Successfully Data is Updated", "success"));
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Something Went Wrong !!! ", "danger"));
        }
        return "redirect:/service-provider/profile";
    }

    @GetMapping("/setting")
    public String setting(Model model){
        model.addAttribute("title", "Setting");

        return "service_man/setting";
    }

    @PostMapping("/change-password")
    @ResponseBody
    public ResponseEntity<String> changePassword(@RequestBody String password,Principal principal){

        try{

            User user = this.userRepository.getUserByUserName(principal.getName());
            user.setUser_password(this.passwordEncoder.encode(password));
            this.userRepository.save(user);
            return new ResponseEntity<>("Password changed successfully", HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred while processing the form.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
