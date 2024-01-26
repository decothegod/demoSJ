package com.example.demoSmartJob.repository;

import com.example.demoSmartJob.model.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneRepository extends JpaRepository<Phone, Long> {
}
