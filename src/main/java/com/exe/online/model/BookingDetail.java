package com.exe.online.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "tbl_booking_detail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BookingDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long booking_id;
    private String service_request_number;

    //    @NotBlank(message = "Name must be required")
//    @Size(min = 7, max = 10, message = "Name must be between 7 and 10 characters")
    private String booking_person;
    //    @NotBlank(message = "Email must be Required")
//    @Email(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Email is Not Valid")
    private String booking_email;

    //    @NotBlank(message = "Phone Number must be Required")
//    @Pattern(regexp = "\\d{10,11}",message = "Phone Number is Not Valid")
    private String booking_phone;

    // @NotBlank(message = "City must be Required")
    private String booking_city;
    // @NotBlank(message = "State must be Required")
    private String booking_state;
    // @NotBlank(message = "Pincode must be Required")
    // @Pattern(regexp = "\\d{6}",message = "Pincode is Not Valid")
    private String booking_pincode;
    @Column(length = 5000)
//    @NotBlank(message = "City must be Required")
    private String booking_address;

    private boolean booking_status;
    private LocalDate booking_date;
    private String service_status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
    @ManyToOne
    @JsonIgnore
    private Service service;
    @OneToOne(mappedBy = "bookingDetail")
    @JsonIgnore
    private Transcation transcation;


}
