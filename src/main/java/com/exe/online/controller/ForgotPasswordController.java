package com.exe.online.controller;

import com.exe.online.dao.UserRepository;
import com.exe.online.entity.Email;
import com.exe.online.helper.GeneratUniqueRandomNumber;
import com.exe.online.helper.Message;
import com.exe.online.model.User;
import com.exe.online.services.EmailService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/password")
public class ForgotPasswordController {

    @Autowired
    private UserRepository userRepository;
    private Logger logger = LoggerFactory.getLogger(ForgotPasswordController.class);
    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/check-valid-email")
    public String checkValidEmail(@RequestParam("email") String email, HttpSession session) {
        int otp = GeneratUniqueRandomNumber.generateRandomNumber(1000, 9999);
        try {
            User user = this.userRepository.getUserByUserName(email);
            if (user == null) {
                session.setAttribute("message", new Message("Do Not Register Email or Email is Wrong !!", "danger"));
                return "redirect:/home/signin";
            } else {
                String subject = " Reset Your Password";
                String text = "<html><head><style>" +
                        "h3 { color: #007BFF; }" +
                        "</style></head><body>" +
                        "<p>Dear " + user.getUser_name() + ",</p>" +
                        "<br>" +
                        "<p>We have received a request to reset your password for the Utility Service Provider.</p>" +
                        "<p>To ensure the security of your account, please enter the following One-Time Password (OTP) within the Utility Service  application:</p>" +
                        "<br>" +
                        "<h3>OTP: <span style=\"color: #007BFF; font-weight: bold;\">" + otp + "</span></h3>" +
                        "<br>" +
                        "<p>Please note that the OTP is valid for a limited time period only. If you don't enter the OTP within 10 minutes, you may need to restart the password reset process.</p>" +
                        "</body></html>";
                Email email1 = new Email();
                email1.setTo(user.getUser_email());
                email1.setSubject(subject);
                email1.setMassage(text);
                boolean isDeliverd = emailService.sendEmail(email1);
                if (isDeliverd) {
                    session.setAttribute("myOtp", otp);
                    session.setAttribute("email", email);
                    session.setAttribute("message", new Message(" OTP Sent Successfully!!", "success"));
                    return "otp";
                } else {
                    session.setAttribute("message", new Message("Something Went Wrong Try Again!!!", "danger"));
                    return "redirect:/home/signin";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Something went wrong!!", "danger"));
            return "redirect:/home/signin";
        }
    }

    @PostMapping("/veryfieotp")
    public String veryFileEmailOtp(@RequestParam("otp") int otp, Model model, HttpSession session) {
        model.addAttribute("title", "Forgot-Password");

        int otp1 = (int) session.getAttribute("myOtp");


        if (otp1 == otp) {
            session.setAttribute("message", new Message("OTP is Valid ,You Can Create New Password", "success"));
            return "newpassword";
        } else {
            session.setAttribute("message", new Message("Doesn't Match OTP so Give Valid OTP!!", "warning"));
            this.logger.info("not match----------------------------------");
            return "otp";
        }


    }

    @PostMapping("/new-password")
    public String newpasswordhandler(@RequestParam("password") String newPassword, Model model, HttpSession session) {
        model.addAttribute("title", "New-Password");
        try {
            String email = (String) session.getAttribute("email");
            User user = this.userRepository.getUserByUserName(email);
            user.setUser_password(this.passwordEncoder.encode(newPassword));
            this.userRepository.save(user);
            return "redirect:/home/signin?change=Successfully Create New Password!!";

        } catch (Exception e) {
            session.setAttribute("message", new Message("Something Went Wrong TryAgain!!", "warning"));
            return "redirect:/home/signin";
        }

    }
}
