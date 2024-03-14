package com.moonlight.models.mapper;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class EmployeeResponse {
	private Integer employeeId;
	private String employeeName;
	private String employeeEmail;
	private Integer employeeAge;
	private double employeeSalary;
	private Long createdAt;
	private Long updatedAt;
}
