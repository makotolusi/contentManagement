package com.cyou.video.mobile.server.cms.model.user;

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
import com.cyou.video.mobile.server.common.Constants;
import com.cyou.video.mobile.server.common.adapter.JaxbDatetimeAdapter;

/**
 * CMS用户令牌
 * 
 * @author jyz
 */
@XmlRootElement
public class UserToken extends BaseModel {

  private static final long serialVersionUID = 2063737012499058725L;

  private int id; // 令牌id

  private String token; // 令牌值

  private Date createDate = new Date(); // 创建时间

  private int plat; // 令牌来源

  private Date lastReqDate = new Date(); // 最近请求时间

  private int reqCount; // 请求次数

  private int status = Constants.STATUS.ON.getValue(); // 令牌状态

  private String os; // 操作系统

  private String ua = ""; // 设备信息

  private String res = ""; // 设备分辨率

  private String channel = ""; // 所属渠道

  private String mid = ""; // 设备标识

  private String baiduUserID; // baidu user id

  private String curUsedVersion; // 当前使用版本

  private Date lastVcTime; // 当version channel 更新时的最新时间戳
  
  private String email = "";
  
  private String mobileNumber = "";

  public String getBaiduUserID() {
    return baiduUserID;
  }

  public void setBaiduUserID(String baiduUserID) {
    this.baiduUserID = baiduUserID;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  @XmlJavaTypeAdapter(JaxbDatetimeAdapter.class)
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public int getPlat() {
    return plat;
  }

  public void setPlat(int plat) {
    this.plat = plat;
  }

  @XmlJavaTypeAdapter(JaxbDatetimeAdapter.class)
  public Date getLastReqDate() {
    return lastReqDate;
  }

  public void setLastReqDate(Date lastReqDate) {
    this.lastReqDate = lastReqDate;
  }

  public int getReqCount() {
    return reqCount;
  }

  public void setReqCount(int reqCount) {
    this.reqCount = reqCount;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getOs() {
    return os;
  }

  public void setOs(String os) {
    this.os = os;
  }

  public String getUa() {
    return ua;
  }

  public void setUa(String ua) {
    this.ua = ua;
  }

  public String getRes() {
    return res;
  }

  public void setRes(String res) {
    this.res = res;
  }

  public String getChannel() {
    return channel;
  }

  public void setChannel(String channel) {
    this.channel = channel;
  }

  public String getMid() {
    return mid;
  }

  public void setMid(String mid) {
    this.mid = mid;
  }

  public String getCurUsedVersion() {
    return curUsedVersion;
  }

  public void setCurUsedVersion(String curUsedVersion) {
    this.curUsedVersion = curUsedVersion;
  }

  public Date getLastVcTime() {
    return lastVcTime;
  }

  public void setLastVcTime(Date lastVcTime) {
    this.lastVcTime = lastVcTime;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE);
    builder.append("id", id);
    builder.append("token", token);
    builder.append("createDate", createDate);
    builder.append("plat", plat);
    builder.append("lastReqDate", lastReqDate);
    builder.append("reqCount", reqCount);
    builder.append("status", status);
    builder.append("os", os);
    builder.append("ua", ua);
    builder.append("res", res);
    builder.append("channel", channel);
    builder.append("mid", mid);
    builder.append("curUsedVersion", curUsedVersion);
    builder.append("lastVcTime", lastVcTime);
    builder.append("email", email);
    builder.append("mobileNumber", mobileNumber);
    return builder.toString();
  }

  @Override
  public boolean equals(final Object other) {
    boolean equals = false;
    if(other instanceof UserToken) {
      if(this == other) {
        equals = true;
      }
      else {
        UserToken cast = (UserToken) other;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(id, cast.id);
        builder.append(token, cast.token);
        builder.append(createDate, cast.createDate);
        builder.append(plat, cast.plat);
        builder.append(lastReqDate, cast.lastReqDate);
        builder.append(reqCount, cast.reqCount);
        builder.append(status, cast.status);
        builder.append(os, cast.os);
        builder.append(ua, cast.ua);
        builder.append(res, cast.res);
        builder.append(channel, cast.channel);
        builder.append(mid, cast.mid);
        builder.append(curUsedVersion, cast.curUsedVersion);
        builder.append(lastVcTime, cast.lastVcTime);
        builder.append(email, cast.email);
        builder.append(mobileNumber, cast.mobileNumber);
        equals = builder.isEquals();
      }
    }
    return equals;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(id);
    builder.append(token);
    builder.append(createDate);
    builder.append(plat);
    builder.append(lastReqDate);
    builder.append(reqCount);
    builder.append(status);
    builder.append(os);
    builder.append(ua);
    builder.append(res);
    builder.append(channel);
    builder.append(mid);
    builder.append(curUsedVersion);
    builder.append(lastVcTime);
    builder.append(email);
    builder.append(mobileNumber);
    return builder.toHashCode();
  }

  @Override
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(id);
    out.writeObject(token);
    out.writeObject(createDate);
    out.writeInt(plat);
    out.writeObject(lastReqDate);
    out.writeInt(reqCount);
    out.writeInt(status);
    out.writeObject(os);
    out.writeObject(ua);
    out.writeObject(res);
    out.writeObject(channel);
    out.writeObject(mid);
    out.writeObject(curUsedVersion);
    out.writeObject(lastVcTime);
    out.writeObject(email);
    out.writeObject(mobileNumber);
  }

  @Override
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    id = in.readInt();
    token = (String) in.readObject();
    createDate = (Date) in.readObject();
    plat = in.readInt();
    lastReqDate = (Date) in.readObject();
    reqCount = in.readInt();
    status = in.readInt();
    os = (String) in.readObject();
    ua = (String) in.readObject();
    res = (String) in.readObject();
    channel = (String) in.readObject();
    mid = (String) in.readObject();
    curUsedVersion = (String) in.readObject();
    lastVcTime = (Date) in.readObject();
    email = (String) in.readObject();
    mobileNumber = (String) in.readObject();
  }

}
