package com.bank.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.application.entity.notifications.SmsEntity;

public interface SmsRepository extends JpaRepository<SmsEntity, Long> {

}
