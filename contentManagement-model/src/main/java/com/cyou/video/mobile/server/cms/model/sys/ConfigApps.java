package com.cyou.video.mobile.server.cms.model.sys;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Config_Apps")
public class ConfigApps {

  @Id
  private String id;

  private String name;

  private List<Integer> appIds;

  @DBRef
  private List<ContentType> contentTypes;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public List<Integer> getAppIds() {
    return appIds;
  }

  public void setAppIds(List<Integer> appIds) {
    this.appIds = appIds;
  }

  public List<ContentType> getContentTypes() {
    return contentTypes;
  }

  public void setContentTypes(List<ContentType> contentTypes) {
    this.contentTypes = contentTypes;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
