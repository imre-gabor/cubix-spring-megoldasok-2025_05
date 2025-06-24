package com.cubixedu.hr.sample.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cubixedu.hr.sample.model.Employee;
import java.time.LocalDateTime;


public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	List<Employee> findBySalaryGreaterThan(int minSalary);
	
	List<Employee> findByJobTitle(String title);
	
	List<Employee> findByNameStartingWithIgnoreCase(String name);
	
	List<Employee> findByDateOfStartWorkBetween(LocalDateTime from, LocalDateTime until);

}
