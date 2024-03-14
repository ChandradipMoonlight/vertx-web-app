package com.moonlight.models.mapper;

import lombok.Data;

@Data
public class EmployeeRequest {
	private String employeeName;
	private String employeeEmail;
	private Integer employeeAge;
	private double employeeSalary;
}
