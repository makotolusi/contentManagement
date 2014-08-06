package com.cyou.video.mobile.server.cms.model.security;


import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * CMS操作项类
 * 
 * @author lusi
 */
@Document(collection = "Security_Operation")
public class Operation {

  private static final long serialVersionUID = -9031731862085772713L;

  private int orderId; // 操作项id


  private String name; // 操作项名称

  private String url; // 操作项地址

  private String manageItemId; // 所属管理项id

  // private int assignable = Constants.STATUS.TRUE.getValue(); //是否可配置
  private List<String> roleIds;


  public String getName() {
    return name;
  }

  public int getOrderId() {
    return orderId;
  }

  public void setOrderId(int orderId) {
    this.orderId = orderId;
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

  public String getManageItemId() {
    return manageItemId;
  }

  public void setManageItemId(String manageItemId) {
    this.manageItemId = manageItemId;
  }

  public List<String> getRoleIds() {
    return roleIds;
  }

  public void setRoleIds(List<String> roleIds) {
    this.roleIds = roleIds;
  }

  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE);
    builder.append("id", orderId);
    return builder.toString();
  }
}