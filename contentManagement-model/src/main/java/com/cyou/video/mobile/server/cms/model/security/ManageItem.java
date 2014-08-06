package com.cyou.video.mobile.server.cms.model.security;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.cyou.video.mobile.server.common.Constants;

/**
 * CMS管理项类
 * 
 * @author lusi
 */
@Document(collection = "Security_ManageItem")
public class ManageItem implements Serializable {
  private static final long serialVersionUID = 1L;

  public static final String COLLECTION_NAME = "Security_ManageItem";

  @Id
  private String id; // 管理项id

  private String name; // 管理项名称

  private String url = ""; // 管理项地址

  private Constants.STATUS status = Constants.STATUS.ON; // 管理项状态

  @DBRef
  private List<Operation> operations;

  private int orderNum; // 管理项顺序

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

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public int getOrderNum() {
    return orderNum;
  }

  public void setOrderNum(int orderNum) {
    this.orderNum = orderNum;
  }

  public Constants.STATUS getStatus() {
    return status;
  }

  public void setStatus(Constants.STATUS status) {
    this.status = status;
  }

  public List<Operation> getOperations() {
    return operations;
  }

  public void setOperations(List<Operation> operations) {
    this.operations = operations;
  }

  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE);
    builder.append("id", id);
    builder.append("name", name);
    builder.append("url", url);
    builder.append("status", status);
    builder.append("orderNum", orderNum);
    return builder.toString();
  }
}
