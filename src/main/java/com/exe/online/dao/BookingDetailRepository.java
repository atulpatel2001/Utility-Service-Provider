package com.exe.online.dao;

import com.exe.online.model.BookingDetail;
import com.exe.online.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingDetailRepository extends JpaRepository<BookingDetail, Long> {
    @Query("from BookingDetail as b where b.booking_status = true AND b.service_status = 'Pending' AND b.user =:user")
    public Page<BookingDetail> findByServiceStatusPending(@Param("user") User user, Pageable pageable);

    @Query("from BookingDetail as b where b.booking_status = true AND b.service_status = 'Completed' AND b.user =:user")
    public Page<BookingDetail> findByServiceStatusCompleted(@Param("user") User user, Pageable pageable);

    @Query("from BookingDetail as b where b.user =:user")
    public List<BookingDetail> findByUser(@Param("user") User user);


}
