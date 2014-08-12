package com.cyou.video.mobile.server.cms.model.sys;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Config_ContentType")
public class ContentType {

  @Id
  private String id;

  private String name;

  private String desc;

  private String index;

  @DBRef
  private List<ContentType> subContentType;

  private String items;// 前端展现代码

  private String resourceUri;

  private String code;
  
  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getIndex() {
    return index;
  }

  public void setIndex(String index) {
    this.index = index;
  }

  public String getItems() {
    return items;
  }

  public void setItems(String items) {
    this.items = items;
  }

  public String getResourceUri() {
    return resourceUri;
  }

  public void setResourceUri(String resourceUri) {
    this.resourceUri = resourceUri;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<ContentType> getSubContentType() {
    return subContentType;
  }

  public void setSubContentType(List<ContentType> subContentType) {
    this.subContentType = subContentType;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

}
