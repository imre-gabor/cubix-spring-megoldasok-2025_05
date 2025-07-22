package com.cubixedu.hr.sample.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cubixedu.hr.sample.model.Company;
import com.cubixedu.hr.sample.model.Employee;
import com.cubixedu.hr.sample.repository.CompanyRepository;

@Service
public class CompanyService {

	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private EmployeeService employeeService;
		
	@Transactional
	public Company save(Company company) {	
		return companyRepository.save(company);
	}

	@Transactional
	public Company update(Company company) {
		if(!companyRepository.existsById(company.getId()))
			return null;
		return companyRepository.save(company);
	}

	public List<Company> findAll(Boolean full) {
		return full ? companyRepository.findAllWithEmployees()
				: companyRepository.findAll();
	}

	public Optional<Company> findById(long id, boolean full) {
		return full? companyRepository.findByIdWithEmployees(id)
				: companyRepository.findById(id);
	}

	@Transactional
	public void delete(long id) {
		companyRepository.deleteById(id);
	}
	
	@Transactional
	public Company addEmployee(long id, Employee employee) {
		Company company = findById(id, true).get();
		company.addEmployee(employeeService.save(employee));		
		return company;
	}
	
	@Transactional
	public Company deleteEmployee(long id, long employeeId) {
		Company company = findById(id, true).get();
		Employee employee = employeeService.findById(employeeId).get();
		employee.setCompany(null);
		company.getEmployees().remove(employee);
		//employeeService.update(employee); @Transactional miatt nem kell
		return company;
	}
	
	@Transactional
	public Company replaceEmployees(long id, List<Employee> employees) {
		Company company = findById(id, true).get();
		company.getEmployees().forEach(emp -> {
			emp.setCompany(null);
			//employeeService.save(emp); @Transactional miatt nem kell
		});
		
		company.getEmployees().clear();
		
		List<Employee> newEmployees = new ArrayList<>();
		employees.forEach(emp -> {
			company.addEmployee(emp);
			newEmployees.add(employeeService.save(emp));
		});
		company.setEmployees(newEmployees);
		return company;
	}

}
