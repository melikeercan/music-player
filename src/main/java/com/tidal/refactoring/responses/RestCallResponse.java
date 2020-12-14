package com.tidal.refactoring.responses;

import java.util.Date;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

public class RestCallResponse {
  private HttpStatus status;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String message;
  private Date timestamp;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Object content;

  private RestCallResponse() { }

  public RestCallResponse(HttpStatus status, Object content) {
    this.status = status;
    this.content = content;
    this.timestamp = new Date();
  }

  public RestCallResponse(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
    this.timestamp = new Date();
  }

  public RestCallResponse(HttpStatus status, String message, Object content) {
    this.status = status;
    this.message = message;
    this.content = content;
    this.timestamp = new Date();
  }

  public HttpStatus getStatus() {
    return status;
  }

  public void setStatus(HttpStatus status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  public Object getContent() {
    return content;
  }

  public void setContent(Object content) {
    this.content = content;
  }
}
