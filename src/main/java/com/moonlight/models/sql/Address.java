package com.moonlight.models.sql;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "address")
public class Address extends BaseModel{
	private String city;
	private String state;
	private Integer pinCode;
	@OneToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;
}
