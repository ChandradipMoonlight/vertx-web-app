package com.moonlight.models.repos;

import com.moonlight.models.sql.Employee;

import java.util.*;

public enum InMemoryEmployeeRepo {
	INSTANCE;

	private static final Map<Integer, Employee> employeeData = new HashMap<>();

	public Employee save(Employee employee) {
		employeeData.put(employee.getId(), employee);
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
		addInitData();
		if (employeeData.values().isEmpty()) {
			return new ArrayList<>();
		} else {
			return new ArrayList<>(employeeData.values());
		}
	}
	public void addInitData() {
		employeeData.put(1, new Employee("Dipak S", "d@gamil.com", 24, 43543 ));
		employeeData.put(2, new Employee("Ram", "r@gmail.com", 32, 45645));
	}
}
