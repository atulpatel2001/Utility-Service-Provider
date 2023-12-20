package com.exe.online.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_services")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long service_id;
    private String service_name;

    @Column(length = 60000)
    private String discription;

    private String image;

    private long service_amount;

    private LocalDate addServiceDate;


    @ManyToOne
    @JsonIgnore
    private Category category;

    @OneToMany(mappedBy = "service")
    private List<BookingDetail> bookingDetail;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "service")
    private List<ServiceMan> serviceMan = new ArrayList<>();

}
