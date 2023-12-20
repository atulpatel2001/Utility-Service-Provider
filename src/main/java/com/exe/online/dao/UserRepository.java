package com.exe.online.dao;

import com.exe.online.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.user_email = :email")
    User getUserByUserName(@Param("email") String email);

    @Query("select u from User u where u.user_role = 'ROLE_USER' OR u.user_role = 'ROLE_ADMIN'")
    Page<User> getNormalUser(Pageable pageable);


    @Query("SELECT u FROM User u WHERE u.user_name LIKE %:userName% AND u.user_role = 'ROLE_USER'")
    List<User> findByUserNameContaining(String userName);


}
