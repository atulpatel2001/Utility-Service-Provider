package com.exe.online.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tbl_user")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long user_id;

    private String user_name;
    @Column(unique = true)
    private String user_email;
    private String user_password;

    private String user_role;

    private boolean user_isActive;

    private boolean user_termAndCondition;
    private LocalDate user_joinDate;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonBackReference
    private ServiceMan serviceMan;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<BookingDetail> bookingDetail;


}
