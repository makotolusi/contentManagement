package com.cyou.video.mobile.server.cms.model.sys;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.cyou.video.mobile.server.common.IEnumDisplay;

@Document(collection = "Config_ContentType_Action")
public class ContentTypeActionAndTag {

  @Id
  private String id;

  private String name;

  private String condition;

  private List<ContentType> action;

  private List<ContentType> tags;

  private TAG_OPERATION operation;

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

  public String getCondition() {
    return condition;
  }

  public void setCondition(String condition) {
    this.condition = condition;
  }

  public TAG_OPERATION getOperation() {
    return operation;
  }

  public void setOperation(TAG_OPERATION operation) {
    this.operation = operation;
  }

  public enum TAG_OPERATION implements IEnumDisplay {
    SET_TAG("增加标签", 0), DEL_TAG("删除标签", 1);
    public String name;

    public int index;

    private TAG_OPERATION(String name, int index) {
      this.name = name;
      this.index = index;
    }

    public String getName() {
      return this.name;
    }

    public int getIndex() {
      return this.index;
    }

    @Override
    public String toString() {
      return this.name;
    }
  }
}
