package com.cyou.video.mobile.server.cms.model;

/**
 * 数据校验自定义Exception
 * @author jyz
 */
public class VerifyException extends Exception {

  private static final long serialVersionUID = 970173476840539998L;

  private String errorCode;

  public VerifyException(String errorCode) {
    super();
    this.errorCode = errorCode;
  }

  public VerifyException(String errorCode, String errorMessage) {
    super(errorMessage);
    this.errorCode = errorCode;
  }

  public VerifyException(String errorCode, Throwable throwable) {
    super(throwable);
    this.errorCode = errorCode;
  }

  public VerifyException(String errorCode, String errorMessage, Throwable throwable) {
    super(errorMessage, throwable);
    this.errorCode = errorCode;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }
}
