package com.bank.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.application.entity.configuration.SchedulerEntity;

public interface SchedulerRepository extends JpaRepository<SchedulerEntity, Long> {

}
