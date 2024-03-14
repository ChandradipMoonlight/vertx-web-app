package com.moonlight.models.sql;

import com.moonlight.factory.SqlBeanFactory;
import io.ebean.annotation.*;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;

@Data
@MappedSuperclass
public class BaseModel {

	@NotNull
	@Id
	@Column(length = 40)
	private Integer employeeId;

	@WhenCreated
	private Timestamp createdAt;

	@WhenModified
	private Timestamp updatedAt;

	@SoftDelete
	private boolean deleted;

	public void save() {
		if (createdAt==null) {
			SqlBeanFactory.INSTANCE.saveBean(this);
		} else {
			SqlBeanFactory.INSTANCE.update(this);
		}
	}

	public void delete() {
		SqlBeanFactory.INSTANCE.delete(this);
	}

}
