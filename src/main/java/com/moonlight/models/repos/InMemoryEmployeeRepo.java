package com.moonlight.models.repos;

import com.moonlight.models.sql.Employee;

import java.util.*;
import java.util.stream.Collectors;

public enum InMemoryEmployeeRepo {
	INSTANCE;

	private static final Map<Integer, Employee> employeeData = new HashMap<>();

	public Employee save(Employee employee) {
		employeeData.put(employee.getEmployeeId(), employee);
		return employee;
	}
	public List<Employee> saveAll(List<Employee> employees) {
		employees.forEach(this::save);
		return employees;
	}

	public Optional<Employee> findById(Integer id) {
		return Optional.ofNullable(employeeData.get(id));
	}

	public void deleteById(Integer id) {
		employeeData.remove(id);
	}
	public List<Employee> findAll() {
		if (employeeData.values().isEmpty()) {
			return new ArrayList<>();
		} else {
			return new ArrayList<>(employeeData.values());
		}
	}
}
