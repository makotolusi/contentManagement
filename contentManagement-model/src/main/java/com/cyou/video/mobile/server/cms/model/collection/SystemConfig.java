package com.cyou.video.mobile.server.cms.model.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "SystemConfig")
public class SystemConfig {

  @Id
  private String id;

  /**
   * 关键字
   */
  private String key;

  /**
   * 值
   */
  private String value;

  /**
   * 中文名
   */
  private String zhName;

  /**
   * 描述
   */
  private String desc;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getZhName() {
    return zhName;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

}
