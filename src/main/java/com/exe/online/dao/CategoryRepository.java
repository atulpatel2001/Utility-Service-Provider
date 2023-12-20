package com.exe.online.dao;

import com.exe.online.model.Category;
import com.exe.online.model.ServiceMan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c WHERE c.category_name LIKE %:query%")
    List<Category> searchCategory(String query);

}
