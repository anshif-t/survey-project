package com.anc.surveyservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class ResponseDTO<T> {

  private T data;
  private String status;
  private String code;
  private String message;

  public ResponseDTO(String status, String code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }

  public ResponseDTO setData(T data) {
    this.data = data;
    return this;
  }
}
