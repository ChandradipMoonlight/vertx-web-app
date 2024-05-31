package com.moonlight.models.sql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name = "employee")
public class Employee extends BaseModel {

	private String employeeName;
	private String employeeEmail;
	private Integer employeeAge;
	private double employeeSalary;
	private String gender;

}
