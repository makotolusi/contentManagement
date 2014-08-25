package com.cyou.video.mobile.server.cms.model.sys;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.cyou.video.mobile.server.common.IEnumDisplay;

@Document(collection = "Config_ContentType")
public class ContentType {

  @Id
  private String id;

  private String name;

  private String desc;

  private String index;

  private String tag;
  
  @DBRef
  private List<ContentTypeActionAndTag> action;

  private String items;// 前端展现代码

  private String resourceUri;

  private String code;

  private CONTENT_TYPE_CATE cate;

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

  public List<ContentTypeActionAndTag> getAction() {
    return action;
  }

  public void setAction(List<ContentTypeActionAndTag> action) {
    this.action = action;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getTag() {
    return tag;
  }

  public CONTENT_TYPE_CATE getCate() {
    return cate;
  }

  public void setCate(CONTENT_TYPE_CATE cate) {
    this.cate = cate;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }
  
  public enum CONTENT_TYPE_CATE implements IEnumDisplay {
    SERVICE("业务", 0), ACTION("行为", 1);
    public String name;

    public int index;

    private CONTENT_TYPE_CATE(String name, int index) {
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
