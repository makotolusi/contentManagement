package com.cyou.video.mobile.server.cms.model.push;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.cyou.video.mobile.server.cms.common.Consts.CLIENT_TYPE;
import com.cyou.video.mobile.server.cms.common.Consts.PUSH_JOB_STATE;
import com.cyou.video.mobile.server.cms.common.Consts.PUSH_SEND_STATE;
import com.cyou.video.mobile.server.cms.common.Consts.PUSH_TYPE;
import com.cyou.video.mobile.server.cms.common.Consts.PUSH_USER_SCOPE;
import com.cyou.video.mobile.server.cms.model.BaseModel;
import com.cyou.video.mobile.server.common.adapter.JaxbDatetimeAdapter;
import com.cyou.video.mobile.server.common.adapter.JaxbEnumAdapter;

public class PushOld extends BaseModel {

  /**
   * 推送任务编号
   */
  private int id;

  /**
   * 推送消息标题
   */
  private String title;

  /**
   * 推送消息内容
   */
  private String content;

  /**
   * 推送消息内容类型
   */
  private String contentType;

  private String clientTypes;

  private String tags;

  private String sentLogs;
  /**
   * 任务状态
   */
  private PUSH_JOB_STATE jobState = PUSH_JOB_STATE.DISABLE;

  /**
   * 任务类型
   */
  private PUSH_TYPE pushType;

  /**
   * 收到通知后打开方式: "key1":"value1", "key2":"value2"
   */
  private String keyValue;

  /**
   * 客户端类型
   */
  private CLIENT_TYPE clientType = CLIENT_TYPE.ANDROID;

  /**
   * 推送状态
   */
  private PUSH_SEND_STATE sendState;

  /**
   * 推送用户范围
   */
  private PUSH_USER_SCOPE userScope = PUSH_USER_SCOPE.ALL;

  /**
   * 任务创建时间
   */
  private Date sendDate;

  /**
   * 推送消息点击去向
   */
  private String target;

  /**
   * 定时任务时间规则
   */
  private String cronExp;

  /**
   * 是否删除
   */
  private UseYn useYn;

  /**
   * trigger start
   */
  private String startTime;

  /**
   * trigger name
   */
  private String tirggerName;

  /**
   * trigger next fire time
   */
  private String nextFireTime;

  /**
   * trigger previous time
   */
  private String previousFireTime;

  /**
   * trigger previous time
   */
  private String cronExpression;

  /**
   * user id
   */
  private String userId;

  /**
   * trigger previous time
   */
  private String channelId;

  private String apiKey;

  private String secretKey;

  /**
   * 是否删除
   * 
   * @author kevin
   * 
   */
  public enum UseYn {
    Y("在使用"), N("已删除");

    private String name;

    // 构造方法
    private UseYn(String name) {
      this.name = name;
    }

    // 覆盖方法
    @Override
    public String toString() {
      return this.name;
    }
  }

  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  public String getSecretKey() {
    return secretKey;
  }

  public void setSecretKey(String secretKey) {
    this.secretKey = secretKey;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTags() {
    return tags == null ? "" : tags;
  }

  public void setTags(String tags) {
    this.tags = tags;
  }

  public String getSentLogs() {
    return sentLogs == null ? "" : sentLogs;
  }

  public void setSentLogs(String sentLogs) {
    this.sentLogs = sentLogs;
  }

  public CLIENT_TYPE getClientType() {
    return clientType == null ? CLIENT_TYPE.ANDROID : clientType;
  }

  public void setClientType(CLIENT_TYPE clientType) {
    this.clientType = clientType;
  }

  @XmlJavaTypeAdapter(JaxbEnumAdapter.class)
  public PUSH_USER_SCOPE getUserScope() {
    return userScope;
  }

  public void setUserScope(PUSH_USER_SCOPE userScope) {
    this.userScope = userScope;
  }

  @XmlJavaTypeAdapter(JaxbDatetimeAdapter.class)
  public Date getSendDate() {
    return sendDate;
  }

  public void setSendDate(Date sendDate) {
    this.sendDate = sendDate;
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

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public String getTarget() {
    return target;
  }

  public void setTarget(String target) {
    this.target = target;
  }

  public String getCronExp() {
    return cronExp;
  }

  public void setCronExp(String cronExp) {
    this.cronExp = cronExp;
  }

  public UseYn getUseYn() {
    return useYn;
  }

  public void setUseYn(UseYn useYn) {
    this.useYn = useYn;
  }

  @XmlJavaTypeAdapter(JaxbEnumAdapter.class)
  public PUSH_JOB_STATE getJobState() {
    return jobState;
  }

  public void setJobState(PUSH_JOB_STATE jobState) {
    this.jobState = jobState;
  }

  @XmlJavaTypeAdapter(JaxbEnumAdapter.class)
  public PUSH_SEND_STATE getSendState() {
    return sendState;
  }

  public void setSendState(PUSH_SEND_STATE sendState) {
    this.sendState = sendState;
  }

//  @XmlJavaTypeAdapter(JaxbEnumAdapter.class)
  public PUSH_TYPE getPushType() {
    return pushType;
  }

  // @ (JaxbEnumAdapter.class)
  public void setPushType(PUSH_TYPE pushType) {
    this.pushType = pushType;
  }

  public String getClientTypes() {
    return clientTypes;
  }

  public void setClientTypes(String clientTypes) {
    this.clientTypes = clientTypes;
  }

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public String getTirggerName() {
    return tirggerName;
  }

  public void setTirggerName(String tirggerName) {
    this.tirggerName = tirggerName;
  }

  public String getNextFireTime() {
    return nextFireTime;
  }

  public void setNextFireTime(String nextFireTime) {
    this.nextFireTime = nextFireTime;
  }

  public String getPreviousFireTime() {
    return previousFireTime;
  }

  public void setPreviousFireTime(String previousFireTime) {
    this.previousFireTime = previousFireTime;
  }

  public String getKeyValue() {
    return keyValue == null ? "" : keyValue;
  }

  public void setKeyValue(String keyValue) {
    this.keyValue = keyValue;
  }

  /**
   * @return the cronExpression
   */
  public String getCronExpression() {
    return cronExpression;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getChannelId() {
    return channelId;
  }

  public void setChannelId(String channelId) {
    this.channelId = channelId;
  }

  /**
   * @param cronExpression
   *            the cronExpression to set
   */
  public void setCronExpression(String cronExpression) {
    this.cronExpression = cronExpression;
  }

  @Override
  public void readExternal(ObjectInput in) throws IOException,
      ClassNotFoundException {
    id = in.readInt();
    title = (String) in.readObject();
    content = (String) in.readObject();
    contentType = (String) in.readObject();
    clientType = (CLIENT_TYPE) in.readObject();
  }

  @Override
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(id);
    out.writeObject(title);
    out.writeObject(content);
    out.writeObject(contentType);
    out.writeObject(clientType);
  }

  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this,
        ToStringStyle.DEFAULT_STYLE);
    builder.append("id", id);
    builder.append("title", title);
    builder.append("content", content);
    return builder.toString();
  }

  @Override
  public boolean equals(Object other) {
    boolean equals = false;
    if (other instanceof Push) {
      if (this == other) {
        equals = true;
      } else {
        Push cast = (Push) other;
        EqualsBuilder builder = new EqualsBuilder();
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
    return builder.toHashCode();
  }

  public Push clone() {
    Push push = null;
    try {
      push = (Push) super.clone();
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }

    return push;
  }

}
