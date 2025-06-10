package com.cubixedu.hr.sample.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cubixedu.hr.sample.dto.CompanyDto;
import com.cubixedu.hr.sample.dto.EmployeeDto;
import com.cubixedu.hr.sample.dto.Views.BaseData;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

	private Map<Long, CompanyDto> companies = new HashMap<>();
	
	@GetMapping
	public List<CompanyDto> findAll(@RequestParam Optional<Boolean> full) {
		if(full.orElse(false)) {
			return new ArrayList<>(companies.values());
		} else {
			return companies.values().stream()
			.map(this::copyWithoutEmployees)
			.toList();
		}
	}
	
	//2. megoldás
//	@GetMapping(params="full=true")
//	public List<CompanyDto> findAll() {
//		return new ArrayList<>(companies.values()); 
//	}
//	
//	@GetMapping
//	@JsonView(BaseData.class)
//	public List<CompanyDto> findAllWithoutEmployees(@RequestParam(required = false) Boolean full) {
//		return new ArrayList<>(companies.values());
//	}


	private CompanyDto copyWithoutEmployees(CompanyDto c) {
		return new CompanyDto(c.getId(), c.getRegistrationNumber(), c.getName(), c.getAddress(), null);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<CompanyDto> findById(@PathVariable long id, @RequestParam Optional<Boolean> full) {
		CompanyDto companyDto = companies.get(id);
		if(companyDto == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(
			full.orElse(false) 
				? companyDto
				: copyWithoutEmployees(companyDto));
	}
	
	@PostMapping
	public ResponseEntity<CompanyDto> create(@RequestBody CompanyDto company) {
		if(companies.containsKey(company.getId()))
			return ResponseEntity.badRequest().build();
			
		companies.put(company.getId(), company);
		return ResponseEntity.ok(company);
	}
	
	
	@PutMapping("/{id}")
	public ResponseEntity<CompanyDto> update(@PathVariable long id, @RequestBody CompanyDto company) {
		company.setId(id);
		if(!companies.containsKey(id))
			return ResponseEntity.notFound().build();
		
		companies.put(id, company);
		return ResponseEntity.ok(company);
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable long id) {
		companies.remove(id);
	}
	
	@PostMapping("/{id}/employees")
	public CompanyDto addNewEmployee(@PathVariable long id, @RequestBody EmployeeDto employeeDto) {
		CompanyDto companyDto = findByIdOrThrow(id);
		companyDto.getEmployees().add(employeeDto);
		return companyDto;
	}

	private CompanyDto findByIdOrThrow(long id) {
		CompanyDto companyDto = companies.get(id);
		if(companyDto == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return companyDto;
	}
	
	@DeleteMapping("/{id}/employees/{employeeId}")
	public CompanyDto deleteEmployee(@PathVariable long id, @PathVariable long employeeId) {
		CompanyDto companyDto = findByIdOrThrow(id);
		companyDto.getEmployees().removeIf(emp -> emp.getId() == employeeId);
		return companyDto;
	}
	
	@PutMapping("/{id}/employees")
	public CompanyDto deleteEmployee(@PathVariable long id, @RequestBody List<EmployeeDto> newEmployees) {
		CompanyDto companyDto = findByIdOrThrow(id);
		companyDto.setEmployees(newEmployees);
		return companyDto;
	}
	
	
}
