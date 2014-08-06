package com.cyou.video.mobile.server.cms.model.push;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.cyou.video.mobile.server.cms.common.Consts.PUSH_PLATFORM_TYPE;

@Document(collection = "PushApp")
public class PushApp {

  @Id
  private String id;

  private String name;

  private String tag;// app tag

  private String desc;

  private PUSH_PLATFORM_TYPE pushPlatForm;

  private String appKey;

  private String secretKey;
  
  private String appKey_ios;

  private String secretKey_ios;
  
  private Integer appId;

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public PUSH_PLATFORM_TYPE getPushPlatForm() {
    return pushPlatForm;
  }

  public void setPushPlatForm(PUSH_PLATFORM_TYPE pushPlatForm) {
    this.pushPlatForm = pushPlatForm;
  }

  public String getAppKey() {
    return appKey;
  }

  public void setAppKey(String appKey) {
    this.appKey = appKey;
  }

  public String getSecretKey() {
    return secretKey;
  }

  public void setSecretKey(String secretKey) {
    this.secretKey = secretKey;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public Integer getAppId() {
    return appId;
  }

  public void setAppId(Integer appId) {
    this.appId = appId;
  }

  public String getAppKey_ios() {
    return appKey_ios;
  }

  public void setAppKey_ios(String appKey_ios) {
    this.appKey_ios = appKey_ios;
  }

  public String getSecretKey_ios() {
    return secretKey_ios;
  }

  public void setSecretKey_ios(String secretKey_ios) {
    this.secretKey_ios = secretKey_ios;
  }

  
}
