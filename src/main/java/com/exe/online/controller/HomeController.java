package com.exe.online.controller;

import com.exe.online.dao.ServiceManRepository;
import com.exe.online.dao.ServiceRepository;
import com.exe.online.dao.UserRepository;
import com.exe.online.helper.FileUploadHelper;
import com.exe.online.helper.Message;
import com.exe.online.model.Service;
import com.exe.online.model.ServiceMan;
import com.exe.online.model.User;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ServiceManRepository serviceManRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    Logger logger = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/signup")

    public String signup(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("title", "User-Register");
        return "signup";
    }

    @PostMapping("/doSignup")
    @ResponseBody
    public ResponseEntity<?> doSignup(@ModelAttribute("user") User user) {
        try {
            User user2 = this.userRepository.getUserByUserName(user.getUser_email());
            if (user2 == null) {
                user.setUser_joinDate(LocalDate.now());
                user.setUser_isActive(true);
                user.setUser_role("ROLE_USER");
                user.setUser_password(this.bCryptPasswordEncoder.encode(user.getUser_password()));
                User user1 = this.userRepository.save(user);

                this.logger.info("SuccessFull Register " + user1.getUser_name());
            } else {
                return ResponseEntity.ok("Already Register This Email!!");
            }

        } catch (Exception e) {
            this.logger.info("Something Went Wrong " + user.getUser_name());
            return ResponseEntity.ok("error");
        }

        return ResponseEntity.ok("success");

    }

    @GetMapping("/signin")
    public String signUp(Model model) {
        model.addAttribute("title", "User-Login");
        return "signin";
    }

    @GetMapping("/service-provider")
    public String serviceManRegister(Model model) {
        List<Service> services = this.serviceRepository.findAll();
        model.addAttribute("services", services);
        model.addAttribute("title", "ServiceProvider-Register");
        return "serviceman_register";
    }

    @PostMapping("/serviceman")
    public String handleServiceManData(@ModelAttribute("user") User user, @ModelAttribute("serviceman") ServiceMan serviceMan, @RequestParam("serviceId") long serviceId, @RequestParam("profile") MultipartFile profile, @RequestParam("adharcard") MultipartFile adharcard, HttpSession httpSession) {
        try {
            Service service = this.serviceRepository.findById(serviceId).get();
            User forMatch = this.userRepository.getUserByUserName(user.getUser_email());
            ServiceMan serviceMan2 = this.serviceManRepository.findServiceManForBooking(service, serviceMan.getServiceman_pincode());
            if (forMatch == null && serviceMan2 == null) {
                user.setUser_role("ROLE_SERVICEMAN");
                user.setUser_joinDate(LocalDate.now());
                user.setUser_isActive(true);
                user.setUser_password(this.bCryptPasswordEncoder.encode(user.getUser_password()));
                serviceMan.setServiceman_profile(profile.getOriginalFilename());
                serviceMan.setServiceman_adharcard(adharcard.getOriginalFilename());
                serviceMan.setService(service);
                serviceMan.setServiceman_status(false);
                boolean b = FileUploadHelper.uploadFile(profile, "static/back_image/serviceman_profile");
                boolean b1 = FileUploadHelper.uploadFile(adharcard, "static/back_image/serviceman_adharcard");
                if (b == true && b1 == true) {
                    User user2 = this.userRepository.save(user);
                    serviceMan.setUser(user2);
                    this.serviceManRepository.save(serviceMan);
                    httpSession.setAttribute("message", new Message("Successfully Register!!", "success"));
                    return "serviceman_register";
                } else {
                    httpSession.setAttribute("message", new Message("Something Went Wrong!!", "danger"));
                    return "serviceman_register";
                }

            } else {
                httpSession.setAttribute("message", new Message("In Company Do Not Have Any Job For Your..", "danger"));
                return "serviceman_register";
            }
        } catch (Exception e) {
            e.printStackTrace();
            httpSession.setAttribute("message", new Message("Something Went Wrong!!", "danger"));
            return "serviceman_register";
        }

    }


    //about us
    @GetMapping("/about-us")
    public String aboutUs(Model model) {
        model.addAttribute("title", "About-Us");
        return "about-us";
    }
}
