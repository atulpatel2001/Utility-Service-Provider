package com.exe.online.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "transcation_detail")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Transcation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long transction_Id;

    private String orderId;
    private String amount;
    private String receipt;
    private String order_status;
    private String transcation_status;
    private String payment_Id;
    private String transcation_mode;
    private LocalDateTime transcation_Date;

    @OneToOne
    @JoinColumn(name = "booking_Id")
    private BookingDetail bookingDetail;
}
