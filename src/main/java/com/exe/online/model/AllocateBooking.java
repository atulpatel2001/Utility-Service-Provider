package com.exe.online.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.engine.internal.Cascade;

@Entity
@Table(name = "tbl_Allocate_Booking")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllocateBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long allocate_booking_id;

    @OneToOne
    @JoinColumn(name = "serviceman_id")
    private ServiceMan serviceMan;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bookingdetail_id")
    private BookingDetail bookingDetail;

    @OneToOne(mappedBy = "allocateBooking", cascade = CascadeType.ALL)
    @JsonIgnore
    private Schedule schedule;

    public boolean hasSchedule() {
        return schedule != null;
    }
}
