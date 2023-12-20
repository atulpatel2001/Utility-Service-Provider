package com.exe.online.dao;

import com.exe.online.model.AllocateBooking;
import com.exe.online.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SceduleRepository extends JpaRepository<Schedule, Long> {

    public Schedule findByAllocateBooking(AllocateBooking allocateBooking);
}
