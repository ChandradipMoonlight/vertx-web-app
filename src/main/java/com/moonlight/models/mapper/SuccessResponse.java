package com.moonlight.models.mapper;

import lombok.Data;

@Data
public class SuccessResponse {
  private boolean status = false;

  public static SuccessResponse generateSuccessResponse() {
    SuccessResponse response = new SuccessResponse();
    response.status = true;
    return response;
  }
}
