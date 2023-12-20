package com.exe.online.dao;

import com.exe.online.model.AllocateBooking;
import com.exe.online.model.ServiceMan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AllocateBookingRepository extends JpaRepository<AllocateBooking, Long> {

    @Query("from AllocateBooking as a where a.bookingDetail.booking_id =:bookingId")
    public AllocateBooking findByBookigId(@Param("bookingId") int bookingId);

    //Request accept
    @Query("from AllocateBooking as a where a.bookingDetail.booking_status=false And a.serviceMan =:serviceMan")
    public List<AllocateBooking> findByNameId(@Param("serviceMan") ServiceMan serviceMan);

    @Query("SELECT COUNT(a) FROM AllocateBooking as a where a.bookingDetail.service_status='Pending' And a.serviceMan =:serviceMan")
    public int countServiceServiceStatusFalse(@Param("serviceMan") ServiceMan serviceMan);

    @Query("SELECT COUNT(a) FROM AllocateBooking as a where a.bookingDetail.service_status='Completed' And a.serviceMan =:serviceMan")
    public int countServiceServiceStatusTrue(@Param("serviceMan") ServiceMan serviceMan);


    @Query("from AllocateBooking as a where a.bookingDetail.service_status='Pending' AND a.serviceMan =:serviceman")
    public Page<AllocateBooking> findPendingBooking(@Param("serviceman") ServiceMan serviceMan, Pageable pageable);

    @Query("from AllocateBooking as a where a.bookingDetail.service_status='Completed' AND a.serviceMan =:serviceman")
    public Page<AllocateBooking> findBookingHistory(@Param("serviceman") ServiceMan serviceMan, Pageable pageable);

    @Query("from AllocateBooking as a where a.bookingDetail.booking_id =:bookingId")
    public AllocateBooking findByBookingId(@Param("bookingId") int bookingId);


    @Query("from AllocateBooking as a where a.bookingDetail.booking_status = true AND a.bookingDetail.service_status = 'Pending'")
    public Page<AllocateBooking> findBookingStatusPending(Pageable pageable);

    @Query("from AllocateBooking as a where a.bookingDetail.booking_status = true AND a.bookingDetail.service_status = 'Completed'")
    public Page<AllocateBooking> findBookingStatusCompleted(Pageable pageable);


    @Query("from AllocateBooking as a where a.bookingDetail.booking_person LIKE %:query% OR a.bookingDetail.booking_city LIKE %:query% OR a.bookingDetail.booking_pincode LIKE %:query% AND a.bookingDetail.service_status = 'Pending' ")
    public List<AllocateBooking> searchPendingService(@Param("query") String query);

    @Query("from AllocateBooking as a where (a.bookingDetail.booking_person LIKE %:query% OR a.bookingDetail.booking_city LIKE %:query% OR a.bookingDetail.booking_pincode LIKE %:query%) AND a.bookingDetail.service_status = 'Completed'")
    public List<AllocateBooking> searchCompletedService(@Param("query") String query);


    @Query("SELECT a FROM AllocateBooking a WHERE a.schedule IS NOT NULL AND a.serviceMan =:serviceman AND a.bookingDetail.service_status = 'Pending'")
    List<AllocateBooking> findAllocateBookingsWithSchedule(@Param("serviceman") ServiceMan serviceMan);

    @Query("from AllocateBooking as a where (a.bookingDetail.booking_person LIKE %:query% OR a.bookingDetail.booking_city LIKE %:query% OR a.bookingDetail.booking_pincode LIKE %:query%) AND a.bookingDetail.service_status = 'Pending' AND a.serviceMan =:serviceman")
    public List<AllocateBooking> searchPendingServiceForServiceMan(@Param("query") String query, @Param("serviceman") ServiceMan serviceMan);


    @Query("from AllocateBooking as a where (a.bookingDetail.booking_person LIKE %:query% OR a.bookingDetail.booking_city LIKE %:query% OR a.bookingDetail.booking_pincode LIKE %:query%) AND a.bookingDetail.service_status = 'Completed' AND a.serviceMan =:serviceman")
    public List<AllocateBooking> searchCompletedServiceForServiceMan(@Param("query") String query, @Param("serviceman") ServiceMan serviceMan);

}
