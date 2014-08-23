package com.cyou.video.mobile.server.cms.model.sys;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Config_ContentType_Action")
public class ContentTypeActionAndTag {

  @Id
  private String id;

  private String name;

  private List<ContentType> action;

  private List<ContentType> tags;

  public List<ContentType> getAction() {
    return action;
  }

  public void setAction(List<ContentType> action) {
    this.action = action;
  }

  public List<ContentType> getTags() {
    return tags;
  }

  public void setTags(List<ContentType> tags) {
    this.tags = tags;
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

  
}
