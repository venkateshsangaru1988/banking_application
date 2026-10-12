package com.bank.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.application.entity.notifications.EmailEntity;

public interface EmailRepository extends JpaRepository<EmailEntity, Long> {

}
