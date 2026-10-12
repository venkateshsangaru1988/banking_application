package com.bank.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.application.entity.master.Users;

public interface UsersRepository extends JpaRepository<Users, Long> {

	Users findByUserCustomerId(String userCustomerId);

	Users findTopByUserCustomerIdStartingWithOrderByUserCustomerIdDesc(String prefix);

	Users findByEmailContainingIgnoreCase(String email);

	Users findByMobileNoContainingIgnoreCase(String mobileNo);

	Users findByUserName(String username);

	List<Users> findAllByOrderByIdDesc();

	List<Users> findAllRoleByOrderByIdDesc(String role);

}
