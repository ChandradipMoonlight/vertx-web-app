package com.moonlight.models.sql;

import com.moonlight.factory.SqlBeanFactory;
import io.ebean.annotation.SoftDelete;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;
import lombok.Data;

import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;

@Data
@MappedSuperclass
public class BaseModel {

	@WhenCreated
	private Timestamp createdAt;

	@WhenModified
	private Timestamp updatedAt;

	@SoftDelete
	private boolean deleted;

	public void save() {
		try{
			if (getCreatedAt()==null) {
				SqlBeanFactory.INSTANCE.save(this);
			} else {
				SqlBeanFactory.INSTANCE.updateBean(this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void update() {
		SqlBeanFactory.INSTANCE.update(this);
	}

	public void delete() {
		SqlBeanFactory.INSTANCE.delete(this);
	}

}
