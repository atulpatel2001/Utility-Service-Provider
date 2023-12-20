package com.exe.online.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "tbl_serviceMan")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ServiceMan implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long serviceman_id;
    private String serviceman_phoneNumber;
    private String serviceman_gender;
    private String serviceman_profile;

    private String serviceman_adharcard;

    private String serviceman_city;
    private String serviceman_state;
    private String serviceman_pincode;

    private boolean serviceman_status;

    @ManyToOne
    @JsonIgnore
    private Service service;
    @OneToOne(cascade = CascadeType.ALL)
    private User user;
}
