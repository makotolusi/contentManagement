package com.cyou.video.mobile.server.cms.model.sys;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.cyou.video.mobile.server.cms.model.BaseModel;
import com.cyou.video.mobile.server.common.adapter.JaxbDatetimeAdapter;
import com.cyou.video.mobile.server.common.utils.JacksonUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * 邮件信息
 * @author jyz
 */
@XmlRootElement
public class MailInfo extends BaseModel {

  private int id; //自增id

  private String title; //邮件标题

  private String content; //邮件正文

  //private List<Map<String, String>> addressee; //邮件接收者称呼及邮箱地址
  private String addressee;

  private Date createDate; //邮件创建时间

  private Date sendDate; //邮件发送时间

  private int status; //邮件状态

  private int times; //尝试次数

  private String project; //应用项目

  private String type; //应用标识

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
  
  public String getAddressee() {
    return addressee;
  }

  public void setAddressee(String addressee) {
    this.addressee = addressee; 
  }

  public List<Map<String, String>> getAddresseeMap() {
    List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    if(StringUtils.isNotBlank(addressee)) {
      try {
        list = JacksonUtil.getJsonMapper().readValue(addressee, new TypeReference<List<Map<String, String>>>() {});
      }
      catch(JsonParseException e) {
        e.printStackTrace();
      }
      catch(JsonMappingException e) {
        e.printStackTrace();
      }
      catch(IOException e) {
        e.printStackTrace();
      }
    }
    return list;
  }
  
  public void setAddressee(List<Map<String, String>> addressee) {
    if(addressee != null && addressee.size() > 0) {
      try {
        this.addressee = JacksonUtil.getJsonMapper().writeValueAsString(addressee);
      }
      catch(JsonProcessingException e) {
        e.printStackTrace();
      }
    }
  }

  @XmlJavaTypeAdapter(JaxbDatetimeAdapter.class)
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  @XmlJavaTypeAdapter(JaxbDatetimeAdapter.class)
  public Date getSendDate() {
    return sendDate;
  }

  public void setSendDate(Date sendDate) {
    this.sendDate = sendDate;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public int getTimes() {
    return times;
  }

  public void setTimes(int times) {
    this.times = times;
  }

  public String getProject() {
    return project;
  }

  public void setProject(String project) {
    this.project = project;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(id);
    out.writeObject(title);
    out.writeObject(content);
    out.writeObject(getAddressee());
    out.writeObject(createDate);
    out.writeObject(sendDate);
    out.writeInt(status);
    out.writeInt(times);
    out.writeObject(project);
    out.writeObject(type);
  }

  @Override
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    id = in.readInt();
    title = (String) in.readObject();
    content = (String) in.readObject();
    setAddressee((String) in.readObject());
    createDate = (Date) in.readObject();
    sendDate = (Date) in.readObject();
    status = in.readInt();
    times = in.readInt();
    project = (String) in.readObject();
    type = (String) in.readObject();
  }

  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE);
    builder.append("id", id);
    builder.append("title", title);
    builder.append("content", content);
    builder.append("addressee", addressee);
    builder.append("createDate", createDate);
    builder.append("sendDate", sendDate);
    builder.append("status", status);
    builder.append("times", times);
    builder.append("project", project);
    builder.append("type", type);
    return builder.toString();
  }

  @Override
  public boolean equals(Object other) {
    boolean equals = false;
    if(other instanceof MailInfo) {
      if(this == other) {
        equals = true;
      }
      else {
        MailInfo cast = (MailInfo) other;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(id, cast.id);
        builder.append(title, cast.title);
        builder.append(content, cast.content);
        builder.append(addressee, cast.addressee);
        builder.append(createDate, cast.createDate);
        builder.append(sendDate, cast.sendDate);
        builder.append(status, cast.status);
        builder.append(times, cast.times);
        builder.append(project, cast.project);
        builder.append(type, cast.type);
        equals = builder.isEquals();
      }
    }
    return equals;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(id);
    builder.append(title);
    builder.append(content);
    builder.append(addressee);
    builder.append(createDate);
    builder.append(sendDate);
    builder.append(status);
    builder.append(times);
    builder.append(project);
    builder.append(type);
    return builder.toHashCode();
  }

}
