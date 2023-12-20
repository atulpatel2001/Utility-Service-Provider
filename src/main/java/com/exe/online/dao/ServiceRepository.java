package com.exe.online.dao;

import com.exe.online.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Service, Long> {
    @Query("from Service as s where s.category.category_id =:id")
    public List<Service> findByCategoryId(@Param("id") int id);


    @Query("SELECT s FROM Service s WHERE s.category.category_name LIKE %:query% OR s.service_name LIKE %:query% ")
    List<Service> searchServices(String query);
}
