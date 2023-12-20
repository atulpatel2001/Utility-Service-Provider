package com.exe.online.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "tbl_schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long schedule_id;
    private LocalDate schedule_date;

    private String schedule_discription;

    @OneToOne
    @JoinColumn(name = "allocate_booking_id")
    @JsonIgnore
    private AllocateBooking allocateBooking;
}
