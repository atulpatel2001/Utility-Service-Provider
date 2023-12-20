package com.exe.online.controller;

import com.exe.online.dao.*;
import com.exe.online.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {


    @Autowired
    private ServiceManRepository serviceManRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AllocateBookingRepository allocateBookingRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @GetMapping("/search-user/{query}")
    public ResponseEntity<?> searchUser(@PathVariable("query") String query) {
        List<User> user = this.userRepository.findByUserNameContaining(query);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/search-service-man/{query}")
    public ResponseEntity<?> searchServiceMan(@PathVariable("query") String query) {
        List<ServiceMan> serviceMan = this.serviceManRepository.findByQueryContaining(query);
        return ResponseEntity.ok(serviceMan);
    }

    @GetMapping("/search-pending-service/{query}")
    public ResponseEntity<?> searchPendingService(@PathVariable("query") String query) {
        List<AllocateBooking> pendingService = this.allocateBookingRepository.searchPendingService(query);
        return ResponseEntity.ok(pendingService);
    }

    @GetMapping("/search-completed-service/{query}")
    public ResponseEntity<?> searchCompletedService(@PathVariable("query") String query) {
        List<AllocateBooking> pendingService = this.allocateBookingRepository.searchCompletedService(query);
        return ResponseEntity.ok(pendingService);
    }

    @GetMapping("/search-category/{query}")
    public ResponseEntity<?> searchCategory(@PathVariable("query") String query) {
        List<Category> category = this.categoryRepository.searchCategory(query);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/search-service/{query}")
    public ResponseEntity<?> searchService(@PathVariable("query") String query) {
        List<Service> service = this.serviceRepository.searchServices(query);
        return ResponseEntity.ok(service);
    }


    @GetMapping("/search-pending-service-for-serviceman/{query}")
    public ResponseEntity<?> pendingServiceForServiceMan(@PathVariable("query") String query, Principal principal) {

        User user = this.userRepository.getUserByUserName(principal.getName());
        ServiceMan serviceMan = this.serviceManRepository.findByUser(user);
        List<AllocateBooking> allocateBookings = this.allocateBookingRepository.searchPendingServiceForServiceMan(query, serviceMan);
        return ResponseEntity.ok(allocateBookings);
    }

    @GetMapping("/search-completed-service-for-serviceman/{query}")
    public ResponseEntity<?> completedServiceForServiceMan(@PathVariable("query") String query, Principal principal) {

        User user = this.userRepository.getUserByUserName(principal.getName());
        ServiceMan serviceMan = this.serviceManRepository.findByUser(user);
        List<AllocateBooking> allocateBookings = this.allocateBookingRepository.searchCompletedServiceForServiceMan(query, serviceMan);
        return ResponseEntity.ok(allocateBookings);
    }

}
