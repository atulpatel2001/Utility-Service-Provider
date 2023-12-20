package com.exe.online.dao;

import com.exe.online.model.Service;
import com.exe.online.model.ServiceMan;
import com.exe.online.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServiceManRepository extends JpaRepository<ServiceMan, Long> {

    @Query("from ServiceMan as s where s.serviceman_status = false")
    public List<ServiceMan> findByStatus1();

    @Query("SELECT COUNT(s) FROM ServiceMan as s WHERE s.serviceman_status = false")
    public int countServiceMan();

    @Query("from ServiceMan as s where s.service =:service AND s.serviceman_status =true AND s.serviceman_pincode =:pincode")
    public ServiceMan findServiceManForBooking(@Param("service") Service service, @Param("pincode") String pincode);

    public ServiceMan findByUser(User user);

    @Query("from ServiceMan as s where s.serviceman_status = true")
    public Page<ServiceMan> findByServiceManForPagination(Pageable pageable);

    @Query("SELECT s FROM ServiceMan s WHERE s.user.user_name LIKE %:query% OR s.serviceman_pincode LIKE %:query% ")
    List<ServiceMan> findByQueryContaining(@Param("query") String query);


    @Query("from ServiceMan as s where s.serviceman_pincode =:pincode")
    public ServiceMan findByPincode(@Param("pincode") String pincode);

}
