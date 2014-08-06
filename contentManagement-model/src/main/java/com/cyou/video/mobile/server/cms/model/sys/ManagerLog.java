package com.cyou.video.mobile.server.cms.model.sys;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.cyou.video.mobile.server.cms.model.BaseModel;
import com.cyou.video.mobile.server.common.adapter.JaxbDatetimeAdapter;

/**
 * 用户日志表
 * 
 * @author zs
 */
@XmlRootElement
public class ManagerLog extends BaseModel {
  private int id;// id

  private int type;// 操作类型：1，增 2，删 3， 改

  private String name="";// 作操名字

  private String desc= "";// 作操描述

  private String params;// 请求参数

  private String url="";// 求请地址

  private String modelName="";// 于属哪个模块

  private Date createDate = new Date();// 入录时间

  private String createUser="";// 作操人名姓

  private String userIp="";// 操作人的ip地址

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public String getParams() {
    return params;
  }

  public void setParams(String params) {
    this.params = params;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getModelName() {
    return modelName;
  }

  public void setModelName(String modelName) {
    this.modelName = modelName;
  }

  @XmlJavaTypeAdapter(JaxbDatetimeAdapter.class)
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public String getCreateUser() {
    return createUser;
  }

  public void setCreateUser(String createUser) {
    this.createUser = createUser;
  }

  public String getUserIp() {
    return userIp;
  }

  public void setUserIp(String userIp) {
    this.userIp = userIp;
  }

  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE);
    builder.append("id", id);
    builder.append("name", name);
    builder.append("desc", desc);
    builder.append("params", params);
    builder.append("url", url);
    builder.append("modelName", modelName);
    builder.append("createDate", createDate);
    builder.append("createUser", createUser);
    builder.append("type", type);
    builder.append("userIp", userIp);
    return builder.toString();
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(id);
    builder.append(name);
    builder.append(desc);
    builder.append(params);
    builder.append(url);
    builder.append(modelName);
    builder.append(createDate);
    builder.append(createUser);
    builder.append(type);
    builder.append(userIp);
    return builder.toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    boolean equals = false;
    if(other instanceof ManagerLog) {
      if(this == other) {
        equals = true;
      }
      else {
        ManagerLog info = (ManagerLog) other;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append("id", info.id);
        builder.append("name", info.name);
        builder.append("desc", info.desc);
        builder.append("params", info.params);
        builder.append("url", info.url);
        builder.append("modelName", info.modelName);
        builder.append("createDate", info.createDate);
        builder.append("createUser", info.createUser);
        builder.append("type", info.type);
        builder.append("userIp", info.userIp);
        equals = builder.isEquals();
      }
    }
    return equals;
  }

  @Override
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(id);
    out.writeObject(desc);
    out.writeObject(name);
    out.writeObject(params);
    ;
    out.writeObject(url);
    out.writeObject(modelName);
    out.writeInt(type);
    out.writeObject(createDate);
    out.writeObject(createUser);
    out.writeObject(userIp);
  }

  @Override
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    id = in.readInt();
    desc = (String) in.readObject();
    name = (String) in.readObject();
    params = (String) in.readObject();
    url = (String) in.readObject();
    modelName = (String) in.readObject();
    type = in.readInt();
    createDate = (Date) in.readObject();
    createUser = (String) in.readObject();
    userIp = (String) in.readObject();

  }

}
