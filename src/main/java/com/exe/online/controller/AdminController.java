package com.exe.online.controller;

import com.exe.online.dao.*;
import com.exe.online.entity.Email;
import com.exe.online.helper.FileUploadHelper;
import com.exe.online.helper.Message;
import com.exe.online.model.*;
import com.exe.online.services.EmailService;
import jakarta.persistence.EntityNotFoundException;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Controller
@RequestMapping("/admin")
public class AdminController {


    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private ServiceManRepository serviceManRepository;

    @Autowired
    private BookingDetailRepository bookingDetailRepository;

    @Autowired
    private TranscationRepository transcationRepository;
    @Autowired
    private AllocateBookingRepository allocateBookingRepository;
    private Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @GetMapping("/index")
    public String adminDashboard(Model model) {
        model.addAttribute("title", "Admin-Dashboard");
        int count = this.serviceManRepository.countServiceMan();
        model.addAttribute("count", count);
        return "admin/admin_dashboard";
    }


    //----------------------------category manage start------------------

    //show category
    @GetMapping("/add-category/{page}")
    public String addCategory(@PathVariable("page") int page, Model model) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<Category> categories = this.categoryRepository.findAll(pageable);
        model.addAttribute("categories", categories.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", categories.getTotalPages());
        model.addAttribute("title", "Manage-Category");
        return "admin/add-category";
    }


    //handle catagory data and add category
    @PostMapping("/add-categoryData")
    public String handleCategory(@ModelAttribute("category") Category category, @RequestParam("image1") MultipartFile file, HttpSession httpSession) {
        try {
            category.setAddCategoryDate(LocalDate.now());
            category.setImage(file.getOriginalFilename());
            boolean b = FileUploadHelper.uploadFile(file, "static/back_image/category_image");
            if (b) {
                this.categoryRepository.save(category);
                this.logger.info("succfully add category with image");
                httpSession.setAttribute("message", new Message("Successfully Add Category!!", "success"));
                return "redirect:/admin/add-category/0";
            } else {
                this.logger.info("error with image");
                httpSession.setAttribute("message", new Message("Something Went Wrong ! Try Again!!", "danger"));
                return "redirect:/admin/add-category/0";
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.logger.info("error with image");
            httpSession.setAttribute("message", new Message("Something Went Wrong ! Try Again!!", "danger"));
            return "redirect:/admin/add-category/0";
        }
    }


    //update category
    @PostMapping("/update-categoryData")
    public String handleUpadteCategory(@ModelAttribute("category") Category category, @RequestParam("image1") MultipartFile file, HttpSession httpSession) {
        try {
            Category fetchCategory = this.categoryRepository.findById(category.getCategory_id()).get();
            if (file.isEmpty()) {
                category.setImage(fetchCategory.getImage());
            } else {
                category.setImage(file.getOriginalFilename());
                boolean b1 = FileUploadHelper.deleteFile("static/back_image/category_image", fetchCategory.getImage());
                if (b1) {
                    logger.info("Old file deleted");
                    boolean b = FileUploadHelper.uploadFile(file, "static/back_image/category_image");
                    if (b) {
                        logger.info("new file uploaded");
                    }
                }

            }

            this.categoryRepository.save(category);
            this.logger.info("successfull Update category with image");
            httpSession.setAttribute("message", new Message("Successfully Update Category!!", "success"));

            return "redirect:/admin/add-category/0";


        } catch (Exception e) {
            e.printStackTrace();
            this.logger.info("error with image");
            httpSession.setAttribute("message", new Message("Something Went Wrong ! Try Again!!", "danger"));
            return "redirect:/admin/add-category/0";
        }

    }


    //delete category
    @PostMapping("/delete-category")
    @ResponseBody
    public String deleteCategory(@RequestParam String category_id) {

        Category category = this.categoryRepository.findById(Long.valueOf(category_id)).get();
        FileUploadHelper.deleteFile("static/back_image/category_image", category.getImage());
        this.categoryRepository.delete(category);
        return "Service deleted successfully";
    }


    //-----------------category manage end----------------------------


    //---------------------------------  service manage start----------------------------------

    //show service page
    @GetMapping("/add-service/{page}")
    public String addservice(@PathVariable("page") int page, Model model) {

        Pageable pageable = PageRequest.of(page, 2);

        Page<Service> serviceTotalPage = this.serviceRepository.findAll(pageable);
        model.addAttribute("services", serviceTotalPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", serviceTotalPage.getTotalPages());
        List<Category> categories = this.categoryRepository.findAll();
        model.addAttribute("categories", categories);

        model.addAttribute("title", "Manage-Service");
        return "admin/add-service";
    }


    //----------------------------------------------------------------------------add service data -------------------
    @PostMapping("/add-serviceData")
    public String handleService(@ModelAttribute("service") Service service, @RequestParam("categoryId") long categoryId, @RequestParam("image1") MultipartFile file, HttpSession httpSession) {
        try {
            Category category = this.categoryRepository.findById(categoryId).get();
            service.setCategory(category);
            service.setImage(file.getOriginalFilename());
            service.setAddServiceDate(LocalDate.now());
            boolean b = FileUploadHelper.uploadFile(file, "static/back_image/service_image");
            if (b) {
                this.serviceRepository.save(service);
                this.logger.info("succfully add service with image");
                httpSession.setAttribute("message", new Message("Successfully Add Service!!", "success"));
                return "redirect:/admin/add-service/0";
            } else {
                this.logger.info("error with image");
                httpSession.setAttribute("message", new Message("Something Went Wrong ! Try Again!!", "danger"));
                return "redirect:/admin/add-service/0";
            }

        } catch (Exception e) {
            e.printStackTrace();
            this.logger.info("error with image");
            httpSession.setAttribute("message", new Message("Something Went Wrong ! Try Again!!", "danger"));
            return "admin/add-service";
        }

    }


    //------------------------------------------update category-----------------------------------------------------------------------------
    @PostMapping("/update-serviceData")
    public String handleUpadteService(@ModelAttribute("service") Service service, @RequestParam("categoryId") long categoryId, @RequestParam("image1") MultipartFile file, HttpSession httpSession) {
        try {
            Service oldService = this.serviceRepository.findById(service.getService_id()).get();

            if (file.isEmpty()) {
                service.setImage(oldService.getImage());
            } else {
                service.setImage(file.getOriginalFilename());
                boolean b1 = FileUploadHelper.deleteFile("static/back_image/service_image", oldService.getImage());
                if (b1) {
                    logger.info("Old file deleted");
                    boolean b = FileUploadHelper.uploadFile(file, "static/back_image/service_image");
                    if (b) {
                        logger.info("updated");
                    }
                }
            }
            Category category = this.categoryRepository.findById(categoryId).get();
            service.setCategory(category);
            service.setAddServiceDate(LocalDate.now());
            this.serviceRepository.save(service);
            this.logger.info("successfull Update service with image");
            httpSession.setAttribute("message", new Message("Successfully Update Service!!", "success"));
            return "redirect:/admin/add-service/0";


        } catch (Exception e) {
            e.printStackTrace();
            this.logger.info("error with image");
            httpSession.setAttribute("message", new Message("Something Went Wrong ! Try Again!!", "danger"));
            return "redirect:/admin/add-service/0";
        }

    }


    //delete service
    @PostMapping("/delete-service")
    @ResponseBody
    public String deleteService(@RequestParam String service_id) {
        Service service = serviceRepository.findById(Long.valueOf(service_id)).orElseThrow(() -> new EntityNotFoundException("Service not found with ID: " + service_id));
        FileUploadHelper.deleteFile("static/back_image/service_image", service.getImage());
        // Remove the association with bookingDetail records
        for (BookingDetail bookingDetail : service.getBookingDetail()) {
            bookingDetail.setService(null);
        }
        // Save the modified bookingDetail records (removing the association)
        bookingDetailRepository.saveAll(service.getBookingDetail());
        // Delete the service entity
        serviceRepository.delete(service);
        return "Service deleted successfully";
    }


//-------------------------------------------end of service manage ---------------------------------------------------------


    //show service man request
    @GetMapping("/send-request")
    @ResponseBody
    public ResponseEntity<?> getRequest() {
        List<ServiceMan> serviceMEN = null;
        try {
            serviceMEN = this.serviceManRepository.findByStatus1();
            System.out.println(serviceMEN + "---------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(serviceMEN);
    }


    //accept serviceman request

    @GetMapping("/{service_id}/accept-request")
    public String acceptRequest(@PathVariable("service_id") long service_id, HttpSession httpSession) {
        try {
            ServiceMan serviceMan = this.serviceManRepository.findById(service_id).get();
            serviceMan.setServiceman_status(true);
            this.serviceManRepository.save(serviceMan);
            String verificationSuccessEmailTemplate = "<html>\n" + "<head>\n" + "    <style>\n" + "        body {\n" + "            font-family: Arial, sans-serif;\n" + "            margin: 0;\n" + "            padding: 0;\n" + "            background-color: #f4f4f4;\n" + "        }\n" + "\n" + "        .container {\n" + "            max-width: 600px;\n" + "            margin: 20px auto;\n" + "            padding: 20px;\n" + "            background-color: #ffffff;\n" + "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" + "            border-radius: 5px;\n" + "        }\n" + "\n" + "        h2 {\n" + "            color: #333333;\n" + "        }\n" + "\n" + "        p {\n" + "            color: #555555;\n" + "        }\n" + "\n" + "        .verification-message {\n" + "            background-color: #e0f7fa;\n" + "            padding: 10px;\n" + "            border-radius: 5px;\n" + "        }\n" + "    </style>\n" + "</head>\n" + "<body>\n" + "    <div class=\"container\">\n" + "        <h2>Welcome to Our Company!</h2>\n" + "\n" + "        <div class=\"verification-message\">\n" + "            <p>Congratulations! You have successfully verified your documents, and we are pleased to inform you that you have been selected to join our company.</p>\n" + "        </div>\n" + "\n" + "        <p>We look forward to having you as part of our team. If you have any further instructions or onboarding processes, please follow the provided guidelines.</p>\n" + "\n" + "        <p>Best Regards,</p>\n" + "        <p>The Ram </p>\n" + "    </div>\n" + "</body>\n" + "</html>";
            Email email = new Email();
            email.setTo(serviceMan.getUser().getUser_email());
            email.setSubject("Congratulations " + serviceMan.getUser().getUser_name());
            email.setMassage(verificationSuccessEmailTemplate);
            this.emailService.sendEmail(email);


            this.logger.info("Accept request");
            httpSession.setAttribute("message", new Message("Serviceman is Joined in This Company", "success"));
        } catch (Exception e) {
            httpSession.setAttribute("message", new Message("Something Went Wrong!!", "danger"));
            e.printStackTrace();
        }
        return "redirect:/admin/index";
    }

    @GetMapping("/{service_id}/decline-request")
    public String declineRequest(@PathVariable("service_id") long service_id, HttpSession httpSession) {
        try {
            ServiceMan serviceMan = this.serviceManRepository.findById(service_id).get();
            boolean b1 = FileUploadHelper.deleteFile("static/back_image/serviceman_adharcard", serviceMan.getServiceman_adharcard());
            boolean b = FileUploadHelper.deleteFile("static/back_image/serviceman_profile", serviceMan.getServiceman_profile());
            if (b == true && b1 == true) {
                this.serviceManRepository.delete(serviceMan);
                httpSession.setAttribute("message", new Message("ServiceMan is Rejected in This Company", "danger"));
                this.logger.info("Decline request");
                String verificationSuccessEmailTemplate = "<html>\n" + "<head>\n" + "    <style>\n" + "        body {\n" + "            font-family: Arial, sans-serif;\n" + "            margin: 0;\n" + "            padding: 0;\n" + "            background-color: #f4f4f4;\n" + "        }\n" + "\n" + "        .container {\n" + "            max-width: 600px;\n" + "            margin: 20px auto;\n" + "            padding: 20px;\n" + "            background-color: #ffffff;\n" + "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" + "            border-radius: 5px;\n" + "        }\n" + "\n" + "        h2 {\n" + "            color: #333333;\n" + "        }\n" + "\n" + "        p {\n" + "            color: #555555;\n" + "        }\n" + "\n" + "        .verification-message {\n" + "            background-color: #e0f7fa;\n" + "            padding: 10px;\n" + "            border-radius: 5px;\n" + "        }\n" + "    </style>\n" + "</head>\n" + "<body>\n" + "    <div class=\"container\">\n" + "        <h2>Reject Application!</h2>\n" + "\n" + "        <div class=\"verification-message\">\n" + "            <p>Rejected ! You have successfully Submited your documents, Our Company is verify document but You are not Eligible for Our Company .</p>\n" + "        </div>\n" + "\n" + "        <p>Thanks for Visit our Comapny and Apply for Job,in Company Have oppertunity for you,Our Company Interact With You</p>\n" + "\n" + "        <p>Best Regards,</p>\n" + "        <p>The Ram </p>\n" + "    </div>\n" + "</body>\n" + "</html>";
                Email email = new Email();
                email.setTo(serviceMan.getUser().getUser_email());
                email.setSubject("Reject Application " + serviceMan.getUser().getUser_name());
                email.setMassage(verificationSuccessEmailTemplate);
                this.emailService.sendEmail(email);
                return "redirect:/admin/index";
            } else {
                this.logger.info("problem");
                httpSession.setAttribute("message", new Message("Something Went Wrong!!", "danger"));
                return "redirect:/admin/index";
            }
        } catch (Exception e) {
            e.printStackTrace();
            httpSession.setAttribute("message", new Message("Something Went Wrong!!", "danger"));
            return "redirect:/admin/index";
        }

    }

    @GetMapping("/{service_id}/serviceman-detail")
    public String readMoreInfo(@PathVariable("service_id") long service_id, Model model) {
        model.addAttribute("title", "ServiceMan Detail");

        try {
            ServiceMan serviceMan = this.serviceManRepository.findById(service_id).get();
            model.addAttribute("serviceMan", serviceMan);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "admin/show_serviceman";
    }


    @GetMapping("/manage-user/{page}")
    public String manageUser(@PathVariable("page") Integer page, Model model) {
        model.addAttribute("title", "Manage-User");
        PageRequest pageable = PageRequest.of(page, 5);
        Page<User> userPage = this.userRepository.getNormalUser(pageable);
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        return "admin/manage-user";
    }


    //update user role
    @PostMapping("/updateUserRole")
    @ResponseBody
    public String changeUserRole(@RequestParam("newRole") String newRole, @RequestParam("userId") String user_id) {

        User user = this.userRepository.findById(Long.parseLong(user_id)).get();
        user.setUser_role(newRole);

        this.userRepository.save(user);
        logger.info("Role Updated!!");
        return "Role Succefully Update";

    }

    //delete user
    @PostMapping("/deleteUser")
    @ResponseBody
    public String deleteUser(@RequestParam String userId) {
        User user = this.userRepository.findById(Long.parseLong(userId)).get();
        this.userRepository.delete(user);
        return "User deleted successfully";
    }


    //service provider management

    @GetMapping("/manage-service-provider/{page}")
    public String manageServiceProvider(@PathVariable("page") int page, Model model) {
        model.addAttribute("title", "Manage-ServiceMan");
        Pageable pageable = PageRequest.of(page, 5);

        Page<ServiceMan> serviceMens = this.serviceManRepository.findByServiceManForPagination(pageable);
        model.addAttribute("serviceMens", serviceMens.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", serviceMens.getTotalPages());
        return "admin/manage-service-provider";
    }

    //delete Service Man
    @PostMapping("/deleteServiceMan")
    @ResponseBody
    public String deletServiceMan(@RequestParam String serviceman_id) {
        ServiceMan serviceMan = this.serviceManRepository.findById(Long.valueOf(serviceman_id)).get();
        boolean b1 = FileUploadHelper.deleteFile("static/back_image/serviceman_adharcard", serviceMan.getServiceman_adharcard());
        boolean b2 = FileUploadHelper.deleteFile("static/back_image/serviceman_profile", serviceMan.getServiceman_profile());
        this.serviceManRepository.delete(serviceMan);
        return "Service deleted successfully";
    }

    //Adharcard
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


    //Pending Booking Detail
    @GetMapping("/Pending-Booking/{page}")
    public String pendingBooking(@PathVariable("page") int page, Model model) {
        model.addAttribute("title", "Pending-Booking");
        Pageable pageable = PageRequest.of(page, 5);
        Page<AllocateBooking> pendingBookingDetail = this.allocateBookingRepository.findBookingStatusPending(pageable);
        model.addAttribute("pendingBookingDetail", pendingBookingDetail.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pendingBookingDetail.getTotalPages());
        return "admin/pending-booking";
    }

    //completed booking
    @GetMapping("/Completed-Booking/{page}")
    public String bookingHistory(@PathVariable("page") int page, Model model) {
        model.addAttribute("title", "Completed-Booking");
        Pageable pageable = PageRequest.of(page, 5);
        Page<AllocateBooking> pendingBookingDetail = this.allocateBookingRepository.findBookingStatusCompleted(pageable);
        model.addAttribute("pendingBookingDetail", pendingBookingDetail.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pendingBookingDetail.getTotalPages());
        return "admin/completed-booking";
    }


    //show all booking detail
    @GetMapping("/{allocatebooking_id}/booking-detail")
    public String pendingBookingInfo(@PathVariable("allocatebooking_id") String allocatebooking_Id, Model model) {
        model.addAttribute("title", "Booking-Detail");
        try {
            AllocateBooking allocateBooking = this.allocateBookingRepository.findById(Long.valueOf(allocatebooking_Id)).get();
            model.addAttribute("allocateBooking", allocateBooking);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "admin/booking_info";
    }


    //show transcation Detail
    @GetMapping("/{transcation_id}/transcation-detail")
    public String transcationDetail(@PathVariable("transcation_id") String transcation_id, Model model) {
        model.addAttribute("title", "Transcation-Detail");
        try {
            Transcation transcation = this.transcationRepository.findById(Long.valueOf(transcation_id)).get();

            model.addAttribute("transcation", transcation);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "admin/transcation_info";
    }


    //delete Booking
    @PostMapping("/deleteBooking")
    @ResponseBody
    public String deletBooking(@RequestParam String booking_id) {
        AllocateBooking allocateBooking = this.allocateBookingRepository.findById(Long.valueOf(booking_id)).get();
        allocateBooking.getBookingDetail().getTranscation().setBookingDetail(null);
        this.allocateBookingRepository.delete(allocateBooking);
        return "Service deleted successfully";
    }
}


