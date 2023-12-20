package com.exe.online.controller;


import com.exe.online.dao.AllocateBookingRepository;
import com.exe.online.dao.BookingDetailRepository;
import com.exe.online.dao.TranscationRepository;
import com.exe.online.entity.Email;
import com.exe.online.helper.Message;
import com.exe.online.helper.UUID;
import com.exe.online.model.AllocateBooking;
import com.exe.online.model.BookingDetail;
import com.exe.online.model.Transcation;
import com.exe.online.services.EmailService;
import com.exe.online.services.PdfService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Map;

@Controller
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    private TranscationRepository transcationRepository;

    @Autowired
    private BookingDetailRepository bookingDetailRepository;
    @Autowired
    private AllocateBookingRepository allocateBookingRepository;
    private Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private PdfService pdfService;
    @Autowired
    private EmailService emailService;

    @PostMapping("/create_order")
    @ResponseBody
    public String createOrder(@RequestBody Map<String, Object> data, Principal principal) throws RazorpayException {


        System.out.println(data);
        int amount = Integer.parseInt(data.get("amount").toString());
        long bookingId = Long.parseLong(data.get("booking_id").toString());
        var client = new RazorpayClient("rzp_test_ADEnLyqI9oALQY", "dtZFd3HrvdyoXYGLpfTrEDUv");
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount * 100); // amount in the smallest currency unit
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "txn_235425");
        Order order = client.orders.create(orderRequest);

        Transcation transcation = new Transcation();
        transcation.setOrderId(order.get("id"));
        transcation.setAmount(String.valueOf(amount));
        transcation.setPayment_Id(null);
        transcation.setReceipt(order.get("receipt"));
        transcation.setOrder_status("created");
        BookingDetail bookingDetail = this.bookingDetailRepository.findById(bookingId).get();
        transcation.setBookingDetail(bookingDetail);
        transcation.setTranscation_mode("ONLINE");
        transcation.setTranscation_Date(LocalDateTime.now());

        this.transcationRepository.save(transcation);
        logger.info(String.valueOf(order));


        return order.toString();
    }

    @PostMapping("/update_order")
    @ResponseBody
    public ResponseEntity<?> updateOrder(@RequestBody Map<String, Object> data) {
        System.out.println(data);

        Transcation transcation = this.transcationRepository.findByOrderId(data.get("order_id").toString());
        transcation.setPayment_Id(data.get("payment_id").toString());
        transcation.setTranscation_status("true");
        this.transcationRepository.save(transcation);

        AllocateBooking allocateBooking = this.allocateBookingRepository.findByBookigId((Integer) data.get("booking_id"));
        String paymentEmail = "<html><head>" + "<style>" + "body { font-family: 'Arial', sans-serif; }" + ".container { max-width: 600px; margin: 0 auto; }" + ".header { background-color: #232f3e; color: #ffffff; padding: 20px; text-align: center; }" + ".content { padding: 20px; }" + ".order-summary { border-bottom: 1px solid #e0e0e0; padding-bottom: 20px; margin-bottom: 20px; }" + ".product { display: flex; justify-content: space-between; }" + ".total { font-weight: bold; }" + ".footer { text-align: center; padding: 10px; background-color: #f2f2f2; }" + "</style></head><body>" + "<div class='container'>" + "<div class='header'><h2>Payment Confirmation</h2></div>" + "<div class='content'>" + "<p>Dear " + allocateBooking.getBookingDetail().getBooking_person() + ",</p>" + "<p>Thank you for Booked Service on our platform. Your payment has been successfully processed.</p>" + "<div class='order-summary'>" + "<h3>Order Summary</h3>" + "<div class='product'><span>Service Name:</span><span>" + allocateBooking.getBookingDetail().getService().getService_name() + "</span></div>" + "<div class='product'><span>Quantity:</span><span>1</span></div>" + "<div class='product'><span>Price:</span><span>" + allocateBooking.getBookingDetail().getService().getService_amount() + "</span></div>" + "<div class='total'><span>Total:</span><span>$" + allocateBooking.getBookingDetail().getService().getService_amount() + "</span></div>" + "</div>" + "<p>Your Service will be provided to the address associated with your account.</p>" + "</div>" + "<div class='footer'>" + "<p>If you have any questions, please contact our customer support.</p>" + "<p>Thank you for Booking Service  with us!</p>" + "</div>" + "</div>" + "</body></html>";
        ByteArrayInputStream pdf = this.pdfService.createPdf(allocateBooking);
        this.emailService.sendAttachEmail(new Email(allocateBooking.getBookingDetail().getBooking_email(), "Payment Confirmation", paymentEmail, allocateBooking.getBookingDetail().getService_request_number() + ".pdf"), pdf);


        return ResponseEntity.ok(Map.of("msg", "updated"));
    }

    @GetMapping("/cash-payment/{booking_id}")
    public String cashOnDelvery(@PathVariable("booking_id") String booking_id) {

        BookingDetail bookingDetail = this.bookingDetailRepository.findById(Long.valueOf(booking_id)).get();
        Transcation transcation = new Transcation();
        transcation.setOrderId("CASH" + UUID.getSixDigitID(UUID.generateUniqueID()));
        transcation.setAmount(String.valueOf(bookingDetail.getService().getService_amount()));
        transcation.setPayment_Id(null);
        transcation.setReceipt("txn_235425");
        transcation.setOrder_status("created");
        transcation.setTranscation_mode("CASH");
        transcation.setTranscation_status("false");
        transcation.setTranscation_Date(LocalDateTime.now());

        transcation.setBookingDetail(bookingDetail);
        this.transcationRepository.save(transcation);
        String emailContent = "<html><head>" + "<style>" + "body { font-family: 'Arial', sans-serif; }" + ".container { max-width: 600px; margin: 0 auto; }" + ".header { background-color: #232f3e; color: #ffffff; padding: 20px; text-align: center; }" + ".content { padding: 20px; }" + ".order-summary { border-bottom: 1px solid #e0e0e0; padding-bottom: 20px; margin-bottom: 20px; }" + ".product { display: flex; justify-content: space-between; }" + ".total { font-weight: bold; }" + ".footer { text-align: center; padding: 10px; background-color: #f2f2f2; }" + "</style></head><body>" + "<div class='container'>" + "<div class='header'><h2>Payment Confirmation</h2></div>" + "<div class='content'>" + "<p>Dear " + bookingDetail.getBooking_person() + ",</p>" + "<p>Thank you for Booked Service on our platform. Selected a Cash On Delvary After Complete Service Pay Money to our Service Man.</p>" + "<div class='order-summary'>" + "<h3>Order Summary</h3>" + "<div class='product'><span>Service Name:</span><span>" + bookingDetail.getService().getService_name() + "</span></div>" + "<div class='product'><span>Quantity:</span><span>1</span></div>" + "<div class='product'><span>Price:</span><span>" + bookingDetail.getService().getService_amount() + "</span></div>" + "<div class='total'><span>Total:</span><span>$" + bookingDetail.getService().getService_amount() + "</span></div>" + "</div>" + "<p>Your Service will be provided to the address associated with your account.</p>" + "</div>" + "<div class='footer'>" + "<p>If you have any questions, please contact our customer support.</p>" + "<p>Thank you for Booking Service  with us!</p>" + "</div>" + "</div>" + "</body></html>";
        Email email = new Email();
        email.setTo(bookingDetail.getBooking_email());
        email.setSubject("Pending Payment");
        email.setMassage(emailContent);
        this.emailService.sendEmail(email);
        return "redirect:/user/booking-pending/0";
    }

    @GetMapping("/cash-payment-update/{booking_id}")
    public String updateCashOnDelvery(@PathVariable("booking_id") String booking_id, HttpSession httpSession) {

        Transcation transcation = this.transcationRepository.findTranscationByBookingId(Long.parseLong(booking_id));
        transcation.setPayment_Id("Pay_M" + UUID.getSixDigitID(UUID.generateUniqueID()));
        transcation.setTranscation_status("true");
        transcation.setTranscation_Date(LocalDateTime.now());
        this.transcationRepository.save(transcation);
        BookingDetail bookingDetail = this.bookingDetailRepository.findById(Long.parseLong(booking_id)).get();
        bookingDetail.setService_status("Completed");
        this.bookingDetailRepository.save(bookingDetail);
        AllocateBooking allocateBooking = this.allocateBookingRepository.findByBookigId(Integer.parseInt(booking_id));
        String paymentEmail = "<html><head>" + "<style>" + "body { font-family: 'Arial', sans-serif; }" + ".container { max-width: 600px; margin: 0 auto; }" + ".header { background-color: #232f3e; color: #ffffff; padding: 20px; text-align: center; }" + ".content { padding: 20px; }" + ".order-summary { border-bottom: 1px solid #e0e0e0; padding-bottom: 20px; margin-bottom: 20px; }" + ".product { display: flex; justify-content: space-between; }" + ".total { font-weight: bold; }" + ".footer { text-align: center; padding: 10px; background-color: #f2f2f2; }" + "</style></head><body>" + "<div class='container'>" + "<div class='header'><h2>Payment Confirmation</h2></div>" + "<div class='content'>" + "<p>Dear " + allocateBooking.getBookingDetail().getBooking_person() + ",</p>" + "<p>Thank you for Book Service on our platform. Your payment has been successfully processed.</p>" + "<div class='order-summary'>" + "<h3>Order Summary</h3>" + "<div class='product'><span>Payment Mode:</span><span>" + allocateBooking.getBookingDetail().getTranscation().getTranscation_mode() + "</span></div>" + "<div class='product'><span>Service Name:</span><span>" + allocateBooking.getBookingDetail().getService().getService_name() + "</span></div>" + "<div class='product'><span>Quantity:</span><span>1</span></div>" + "<div class='product'><span>Price:</span><span>" + allocateBooking.getBookingDetail().getService().getService_amount() + "</span></div>" + "<div class='total'><span>Total:</span><span>$" + allocateBooking.getBookingDetail().getService().getService_amount() + "</span></div>" + "</div>" + "<p>please fill up feedback form .</p>" + "</div>" + "<div class='footer'>" + "<p>If you have any questions, please contact our customer support.</p>" + "<p>Thank you for Booking Service  with us!</p>" + "</div>" + "</div>" + "</body></html>";
        ByteArrayInputStream pdf = this.pdfService.createPdf(allocateBooking);
        this.emailService.sendAttachEmail(new Email(allocateBooking.getBookingDetail().getBooking_email(), "Service Completed", paymentEmail, allocateBooking.getBookingDetail().getService_request_number() + ".pdf"), pdf);
        httpSession.setAttribute("message", new Message("Successfully Complete this Service:- " + transcation.getBookingDetail().getService_request_number(), "success"));


        return "redirect:/service-provider/index";
    }

}
