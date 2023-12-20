package com.exe.online.dao;


import com.exe.online.model.Transcation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TranscationRepository extends JpaRepository<Transcation, Long> {
    public Transcation findByOrderId(String orderId);

    @Query("from Transcation as t where t.bookingDetail.booking_id =:booking_id")
    Transcation findTranscationByBookingId(@Param("booking_id") long booking_id);


}
