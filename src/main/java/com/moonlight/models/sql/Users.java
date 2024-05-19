package com.moonlight.models.sql;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@Data
public class Users extends BaseModel{
	private String email;
	private String fName;
	private String mName;
	private String lName;
	private String mobileNo;
	private String password;
}
