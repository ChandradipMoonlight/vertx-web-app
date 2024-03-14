package com.moonlight.models.mapper;

import com.moonlight.models.sql.Employee;

public enum EmployeeMapper {
	INSTANCE;

	public EmployeeResponse createEmployeeResponse(Employee employee) {
		return EmployeeResponse.builder()
				.employeeId(employee.getEmployeeId())
				.employeeName(employee.getEmployeeName())
				.employeeEmail(employee.getEmployeeEmail())
				.employeeSalary(employee.getEmployeeSalary())
				.employeeAge(employee.getEmployeeAge())
				.createdAt(employee.getCreatedAt().getTime())
				.updatedAt(employee.getUpdatedAt().getTime())
				.build();
	}
}
