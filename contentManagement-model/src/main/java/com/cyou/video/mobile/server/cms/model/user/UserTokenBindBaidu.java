package com.cyou.video.mobile.server.cms.model.user;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.cyou.video.mobile.server.cms.model.BaseModel;

/**
 * 用户token绑定百度 id
 * 
 * @author lusi
 */
@XmlRootElement
public class UserTokenBindBaidu extends BaseModel {

	private static final long serialVersionUID = 2063737012499058725L;

	private int id; // id

	private int tokenId; // 令牌ID

	private String baiduUserId; // baidu user id

	private String baiduChannelId; // baidu channel id

	private String baiduAppId; // baidu channel id

	private Date bindDate; // 创建时间

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTokenId() {
    return tokenId;
  }

  public void setTokenId(int tokenId) {
    this.tokenId = tokenId;
  }

  public String getBaiduUserId() {
    return baiduUserId;
  }

  public void setBaiduUserId(String baiduUserId) {
    this.baiduUserId = baiduUserId;
  }

  public String getBaiduChannelId() {
    return baiduChannelId;
  }

  public void setBaiduChannelId(String baiduChannelId) {
    this.baiduChannelId = baiduChannelId;
  }

  public String getBaiduAppId() {
    return baiduAppId;
  }

  public void setBaiduAppId(String baiduAppId) {
    this.baiduAppId = baiduAppId;
  }

  public Date getBindDate() {
		return bindDate;
	}

	public void setBindDate(Date bindDate) {
		this.bindDate = bindDate;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this,
				ToStringStyle.DEFAULT_STYLE);
		builder.append("id", id);
		builder.append("tokenID", tokenId);
		builder.append("baiduUserID", baiduUserId);
		builder.append("baiduChannelID", baiduChannelId);
		builder.append("baiduAppID", baiduAppId);
		builder.append("bindDate", bindDate);
		return builder.toString();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(id);
		out.writeObject(tokenId);
		out.writeObject(baiduUserId);
		out.writeObject(baiduChannelId);
		out.writeObject(baiduAppId);
		out.writeObject(bindDate);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		id = in.readInt();
		tokenId = in.readInt();
		bindDate = (Date) in.readObject();
		baiduUserId = (String) in.readObject();
		baiduChannelId = (String) in.readObject();
		baiduAppId = (String) in.readObject();
	}

	@Override
	public boolean equals(Object other) {
		boolean equals = false;
		if (other instanceof UserToken) {
			if (this == other) {
				equals = true;
			} else {
				UserTokenBindBaidu cast = (UserTokenBindBaidu) other;
				EqualsBuilder builder = new EqualsBuilder();
				builder.append(id, cast.id);
				builder.append(tokenId, cast.tokenId);
				builder.append(baiduUserId, cast.baiduUserId);
				builder.append(baiduChannelId, cast.baiduChannelId);
				builder.append(baiduAppId, cast.baiduAppId);
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
		builder.append(baiduUserId);
		builder.append(baiduChannelId);
		builder.append(baiduAppId);
		builder.append(bindDate);
		return builder.toHashCode();
	}

}
