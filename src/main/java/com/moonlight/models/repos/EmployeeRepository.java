package com.moonlight.models.repos;

import com.moonlight.models.sql.Employee;

import java.util.List;

public enum EmployeeRepository {
	INSTANCE;

	private SqlFinder<Employee, Integer> employeeFinder = new SqlFinder<>(Employee.class);

	public Employee findById(Integer id) {
		return employeeFinder.findById(id);
	}

	public List<Employee> findAll() {
		return employeeFinder.findAll();
	}

}
