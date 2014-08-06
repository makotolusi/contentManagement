package com.cyou.video.mobile.server.cms.model.sys;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.cyou.video.mobile.server.cms.model.BaseModel;

/**
 * 邮件接收者
 * @author jyz
 */
@XmlRootElement
public class MailAddressee extends BaseModel {

  private int id; //自增id

  private String project; //应用项目

  private String type; //应用标识

  private String nickname; //接收人昵称

  private String email; //接收人邮箱地址

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
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

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(id);
    out.writeObject(project);
    out.writeObject(type);
    out.writeObject(nickname);
    out.writeObject(email);
  }

  @Override
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    id = in.readInt();
    project = (String) in.readObject();
    type = (String) in.readObject();
    nickname = (String) in.readObject();
    email = (String) in.readObject();
  }

  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE);
    builder.append("id", id);
    builder.append("project", project);
    builder.append("type", type);
    builder.append("nickname", nickname);
    builder.append("email", email);
    return builder.toString();
  }

  @Override
  public boolean equals(Object other) {
    boolean equals = false;
    if(other instanceof MailAddressee) {
      if(this == other) {
        equals = true;
      }
      else {
        MailAddressee cast = (MailAddressee) other;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(id, cast.id);
        builder.append(project, cast.project);
        builder.append(type, cast.type);
        builder.append(nickname, cast.nickname);
        builder.append(email, cast.email);
        equals = builder.isEquals();
      }
    }
    return equals;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(id);
    builder.append(project);
    builder.append(type);
    builder.append(nickname);
    builder.append(email);
    return builder.toHashCode();
  }

}
