package com.cyou.video.mobile.server.cms.model.collection;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.cyou.video.mobile.server.cms.common.Consts.COLLECTION_ITEM_TYPE;
import com.cyou.video.mobile.server.cms.common.Consts.COLLECTION_OPERATOR_TYPE;
import com.cyou.video.mobile.server.cms.common.Consts.GAME_PLATFORM_TYPE;
import com.cyou.video.mobile.server.cms.model.BaseModel;
import com.cyou.video.mobile.server.common.adapter.JaxbDatetimeAdapter;
import com.cyou.video.mobile.server.common.adapter.JaxbEnumAdapter;

/**
 * 信息收集
 * 
 * @author lu si
 */
@Document(collection = "ClientLogCollection")
public class ClientLogCollection extends BaseModel {

	@Id
	private String id; // i d

	private String uid; // 消息标题
	@Transient
	private String pushToken; // baidu uid

	@Transient
	private String eventId; // 点击事件id 如“下载中”tab的id

	private String eventName; // 点击事件名称id 如 “下载中” 这个tab

	private String serviceId; // cms 业务 id

	private String serviceName; // cms 业务 id

	private String gameCode; // game code

	private Integer gamePlatForm; // 游戏平台
	@Transient
	private String gamePlatFormE; // 点击事件id 如“下载中”tab的id

	private String gameType=""; // 游戏分类

	private String gameStatus=""; // 游戏特征

	@Transient
	private COLLECTION_ITEM_TYPE itemTypeE; // 类型

	@Transient
	private COLLECTION_OPERATOR_TYPE operatorTypeE; // 操作类型

	private String itemType; // mongo中只保存编号

	private String operatorType;

	private Date operatorDate; // 操作时间

	private Date uploadDate = new Date(); // 上传时间

	private String keyWord; // 关键字 排行榜id 囧图 id

	private Integer otherWay; //  搜索关键字、分享平台名称、播放时间、通过某排行榜的ID、通过某囧图栏目ID

	@Transient
	private String otherWayE; // 操作类型
	
	private String keyWord2; //排行榜名称,囧图名称

	// // @Transient
	// private float pv;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}


	public String getKeyWord2() {
		return keyWord2;
	}

	public void setKeyWord2(String keyWord2) {
		this.keyWord2 = keyWord2;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	
	public String getOtherWayE() {
		return otherWayE;
	}

	public void setOtherWayE(String otherWayE) {
		this.otherWayE = otherWayE;
	}

	@XmlJavaTypeAdapter(JaxbEnumAdapter.class)
	public COLLECTION_ITEM_TYPE getItemTypeE() {
		try {
			return itemTypeE.values()[Integer.parseInt(itemType)];
		} catch (Exception e) {
			return null;
		}
	}

	public void setItemTypeE(COLLECTION_ITEM_TYPE itemTypeE) {
		this.itemType = itemTypeE.index + "";
		this.itemTypeE = itemTypeE;
	}

	@XmlJavaTypeAdapter(JaxbEnumAdapter.class)
	public COLLECTION_OPERATOR_TYPE getOperatorTypeE() {
		try {
			return operatorTypeE.values()[Integer.parseInt(operatorType)];
		} catch (Exception e) {
			return null;
		}
	}

	public void setOperatorTypeE(COLLECTION_OPERATOR_TYPE operatorTypeE) {
		this.operatorType = operatorTypeE.index + "";
		this.operatorTypeE = operatorTypeE;
	}

	@XmlJavaTypeAdapter(JaxbDatetimeAdapter.class)
	public Date getOperatorDate() {
		return operatorDate;
	}

	public void setOperatorDate(Date operatorDate) {
		this.operatorDate = operatorDate;
	}

	@XmlJavaTypeAdapter(JaxbDatetimeAdapter.class)
	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public Integer getOtherWay() {
		return otherWay;
	}

	public void setOtherWay(Integer otherWay) {
		this.otherWay = otherWay;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
	}



	public String getPushToken() {
    return pushToken;
  }

  public void setPushToken(String pushToken) {
    this.pushToken = pushToken;
  }

  public String getGameCode() {
		return gameCode;
	}

	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}

	public Integer getGamePlatForm() {
		return gamePlatForm;
	}

	public void setGamePlatForm(Integer gamePlatForm) {
		this.gamePlatForm = gamePlatForm;
	}

	public String getGamePlatFormE() {
		return gamePlatFormE;
	}
	
	public void setGamePlatFormE(String gamePlatFormE) {
		this.gamePlatFormE = gamePlatFormE;
	}
	
	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public String getGameStatus() {
		return gameStatus;
	}

	public void setGameStatus(String gameStatus) {
		this.gameStatus = gameStatus;
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		id = (String) in.readObject();
		uid = (String) in.readObject();
		uploadDate = (Date) in.readObject();
		serviceId = (String) in.readObject();
		serviceName = (String) in.readObject();
		eventName = (String) in.readObject();
		operatorDate = (Date) in.readObject();
		operatorType = (String) in.readObject();
		itemType = (String) in.readObject();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(id);
		out.writeObject(uid);
		out.writeObject(eventId);
		out.writeObject(eventName);
		out.writeObject(serviceId);
		out.writeObject(serviceName);
		out.writeObject(itemType);
		out.writeObject(operatorType);
		out.writeObject(operatorDate);
		out.writeObject(uploadDate);
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this,
				ToStringStyle.DEFAULT_STYLE);
		builder.append("id", id);
		builder.append("uid", uid);
		builder.append("serviceId", serviceId);
		builder.append("eventName", eventName);
		builder.append("itemType", itemType);
		builder.append("operatorType", operatorType);
		builder.append("serviceName", serviceName);
		builder.append("serviceId", serviceId);
		builder.append("operatorDate", operatorDate);
		builder.append("uploadDate", uploadDate);
		return builder.toString();
	}

	@Override
	public boolean equals(Object other) {
		boolean equals = false;
		if (other instanceof ClientLogCollection) {
			if (this == other) {
				equals = true;
			} else {
				ClientLogCollection cast = (ClientLogCollection) other;
				EqualsBuilder builder = new EqualsBuilder();
				builder.append(id, cast.id);
				builder.append(uid, cast.uid);
				builder.append(eventId, cast.eventId);
				builder.append(eventName, cast.eventName);
				builder.append(serviceId, cast.serviceId);
				builder.append(serviceName, cast.serviceName);
				builder.append(operatorDate, cast.operatorDate);
				builder.append(uploadDate, cast.uploadDate);
				equals = builder.isEquals();
			}
		}
		return equals;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		builder.append(id);
		builder.append(uid);
		builder.append(eventId);
		builder.append(eventName);
		builder.append(serviceId);
		builder.append(serviceName);
		builder.append(itemType);
		builder.append(operatorDate);
		builder.append(uploadDate);
		return builder.toHashCode();
	}

}
