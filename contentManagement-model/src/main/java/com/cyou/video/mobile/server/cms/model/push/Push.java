package com.cyou.video.mobile.server.cms.model.push;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.cyou.video.mobile.server.cms.common.Consts.CLIENT_TYPE;
import com.cyou.video.mobile.server.cms.common.Consts.COLLECTION_ITEM_TYPE;
import com.cyou.video.mobile.server.cms.common.Consts.PUSH_JOB_STATE;
import com.cyou.video.mobile.server.cms.common.Consts.PUSH_PLATFORM_TYPE;
import com.cyou.video.mobile.server.cms.common.Consts.PUSH_SEND_STATE;
import com.cyou.video.mobile.server.cms.common.Consts.PUSH_TYPE;
import com.cyou.video.mobile.server.cms.common.Consts.PUSH_USER_SCOPE;
import com.cyou.video.mobile.server.cms.model.BaseModel;
import com.cyou.video.mobile.server.common.adapter.JaxbDatetimeAdapter;
import com.cyou.video.mobile.server.common.adapter.JaxbEnumAdapter;

@Document(collection = "Push")
public class Push extends BaseModel {

  @Id
  private String id;

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
  private COLLECTION_ITEM_TYPE contentType;
  
  @Transient
  private String contentTy;
  
  @Transient
  private String clientTypes;

  private List<PushTagCollection> tags=new ArrayList<PushTagCollection>();

  private String tagRelation;//标签关系
  
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
  private Map<String, String> keyValue;

  /**
   * 时段控制
   */
  private Map<String, Integer> interval;
  /**
   * 客户端类型
   */
  private CLIENT_TYPE clientType;

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
  private Date sendDate=new Date();

  /**
   * 推送消息点击去向
   */
  @Transient
  private String target;

  /**
   * 定时任务时间规则
   */
  private String cronExp;

  /**
   * 是否删除
   */
  @Transient
  private UseYn useYn;

  /**
   * trigger start
   */
  @Transient
  private String startTime;

  /**
   * trigger name
   */
  @Transient
  private String tirggerName;

  /**
   * trigger next fire time
   */
  @Transient
  private String nextFireTime;

  /**
   * trigger previous time
   */
  @Transient
  private String previousFireTime;

  /**
   * trigger previous time
   */
  @Transient
  private String cronExpression;

  /**
   * user id
   */
  @Transient
  private String userId;

  @Transient
  private String channelId;

  private Integer appId;

  @Transient
  private String appKey;

  @Transient
  private String secretKey;

  /**
   * 推送第三方平台
   */
  private PUSH_PLATFORM_TYPE platForm;

  
  public Map<String, String> getKeyValue() {
    return keyValue;
  }

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

  public String getAppKey() {
    return appKey;
  }

  public void setAppKey(String appKey) {
    this.appKey = appKey;
  }

  public String getSecretKey() {
    return secretKey;
  }

  public void setSecretKey(String secretKey) {
    this.secretKey = secretKey;
  }


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public List<PushTagCollection> getTags() {
    return tags;
  }

  public void setTags(List<PushTagCollection> tags) {
    this.tags = tags;
  }

  public void setKeyValue(Map<String, String> keyValue) {
    this.keyValue = keyValue;
  }

  public String getSentLogs() {
    return sentLogs == null ? "" : sentLogs;
  }

  public void setSentLogs(String sentLogs) {
    this.sentLogs = sentLogs;
  }

  public CLIENT_TYPE getClientType() {
    return clientType;
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

  @XmlJavaTypeAdapter(JaxbEnumAdapter.class)
  public COLLECTION_ITEM_TYPE getContentType() {
    return contentType;
  }

  public void setContentType(COLLECTION_ITEM_TYPE contentType) {
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

  // @XmlJavaTypeAdapter(JaxbEnumAdapter.class)
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


  public String getContentTy() {
    return contentTy;
  }

  public void setContentTy(String contentTy) {
    this.contentTy = contentTy;
  }

  public Integer getAppId() {
    return appId;
  }

  public void setAppId(Integer appId) {
    this.appId = appId;
  }

  public PUSH_PLATFORM_TYPE getPlatForm() {
    return platForm;
  }

  public void setPlatForm(PUSH_PLATFORM_TYPE platForm) {
    this.platForm = platForm;
  }
  
  public Map<String, Integer> getInterval() {
    return interval;
  }

  public void setInterval(Map<String, Integer> interval) {
    this.interval = interval;
  }

  public String getTagRelation() {
    if(tags.size()<=1)
      return "OR";
    else
      return tagRelation;
  }

  public void setTagRelation(String tagRelation) {
    this.tagRelation = tagRelation;
  }

  /**
   * @param cronExpression
   *          the cronExpression to set
   */
  public void setCronExpression(String cronExpression) {
    this.cronExpression = cronExpression;
  }

  @Override
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    title = (String) in.readObject();
    content = (String) in.readObject();
    contentType = (COLLECTION_ITEM_TYPE) in.readObject();
    clientType = (CLIENT_TYPE) in.readObject();
  }

  @Override
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeObject(title);
    out.writeObject(content);
    out.writeObject(contentType);
    out.writeObject(clientType);
  }

  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE);
    builder.append("id", id);
    builder.append("title", title);
    builder.append("content", content);
    return builder.toString();
  }

  @Override
  public boolean equals(Object other) {
    boolean equals = false;
    if(other instanceof Push) {
      if(this == other) {
        equals = true;
      }
      else {
        Push cast = (Push) other;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(id, cast.id);
        builder.append(title, cast.title);
        builder.append(content, cast.content);
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
    return builder.toHashCode();
  }

  public Push clone() {
    Push push = null;
    try {
      push = (Push) super.clone();
    }
    catch(CloneNotSupportedException e) {
      e.printStackTrace();
    }

    return push;
  }

}
