package com.moonlight.models.mongo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class CustomFieldMapping {
	private Long companyId;
	private Long formId;
	private String formType;
	private String requestId;
	private String fieldId;
	private String fieldValue;
	private boolean deleted;
}
