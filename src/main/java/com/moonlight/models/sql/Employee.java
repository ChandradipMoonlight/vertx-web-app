package com.moonlight.models.sql;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.moonlight.factory.SqlBeanFactory;
import com.moonlight.models.repos.EmployeeRepository;
import io.ebean.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name = "employees")
public class Employee extends BaseModel {
//	@Id
//	private Integer employeeId;
	private String employeeName;
	private String employeeEmail;
	private Integer employeeAge;
	private double employeeSalary;

	public void save() {
		SqlBeanFactory.INSTANCE.saveBean(this);
	}
}
