package com.moonlight.models.mongo;

import lombok.Data;

@Data
public class CustomFieldMapping {
	private Long companyId;
	private Long formId;
	private String formType;
	private String requestId;
	private String fieldId;
	private String fieldValue;
	private boolean deleted;
}
