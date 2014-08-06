package com.cyou.video.mobile.server.cms.model.user;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.cyou.video.mobile.server.cms.model.BaseModel;

/**
 * 用户token绑定百度 id
 * 
 * @author lusi
 */
@Document(collection = "UserTokenBindXinge")
public class UserTokenBindXinge extends BaseModel {

  private static final long serialVersionUID = 2063737012499058725L;

  @Id
  private String id; // id

  private int tokenId; // 令牌ID

  private long accessId; // accessId

  private String deviceId; // deviceId

  private String account; // ticket
  
  private String ticket; // ticket

  private String ticketType; // ticketType

  private String xgToken; // token

  private String appId; // token
  
  private Date bindDate=new Date(); // 创建时间

  


  public String getAppId() {
    return appId;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public int getTokenId() {
    return tokenId;
  }

  public void setTokenId(int tokenId) {
    this.tokenId = tokenId;
  }

  public long getAccessId() {
    return accessId;
  }

  public void setAccessId(long accessId) {
    this.accessId = accessId;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public String getTicket() {
    return ticket;
  }

  public void setTicket(String ticket) {
    this.ticket = ticket;
  }

  public String getTicketType() {
    return ticketType;
  }

  public void setTicketType(String ticketType) {
    this.ticketType = ticketType;
  }

  public String getXgToken() {
    return xgToken;
  }

  public void setXgToken(String xgToken) {
    this.xgToken = xgToken;
  }

  public Date getBindDate() {
    return bindDate;
  }

  public void setBindDate(Date bindDate) {
    this.bindDate = bindDate;
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE);
    builder.append("id", id);
    builder.append("tokenID", tokenId);
    builder.append("bindDate", bindDate);
    return builder.toString();
  }

  @Override
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeObject(tokenId);
    out.writeObject(bindDate);
  }

  @Override
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    tokenId = in.readInt();
    bindDate = (Date) in.readObject();
  }

  @Override
  public boolean equals(Object other) {
    boolean equals = false;
    if(other instanceof UserToken) {
      if(this == other) {
        equals = true;
      }
      else {
        UserTokenBindXinge cast = (UserTokenBindXinge) other;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(id, cast.id);
        builder.append(tokenId, cast.tokenId);
        builder.append(bindDate, cast.bindDate);
        equals = builder.isEquals();
      }
    }
    return equals;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(id);
    builder.append(tokenId);
    builder.append(bindDate);
    return builder.toHashCode();
  }

}
