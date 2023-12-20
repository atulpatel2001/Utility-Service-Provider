package com.exe.online.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_feedback")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedBack {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long feedBackId;

    private String rating;

    private String description;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private BookingDetail bookingDetail;
}
