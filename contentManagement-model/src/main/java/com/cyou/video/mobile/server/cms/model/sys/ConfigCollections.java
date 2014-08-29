package com.cyou.video.mobile.server.cms.model.sys;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Config_Collections")
public class ConfigCollections {

  @Id
  private String id;

  private String name;

  private String chName;

  private String code;

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

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getChName() {
    return chName;
  }

  public void setChName(String chName) {
    this.chName = chName;
  }

}
