package com.cyou.video.mobile.server.cms.model.collection;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.cyou.video.mobile.server.cms.model.BaseModel;

@Document(collection = "PlayerApps")
public class PlayerApps extends BaseModel {

	@Id
	private String id; // i d
	
	private String appid;

	private String appName;
	
	private String appNameNew;
	
	private String localState; //0:非官方    1：官方
	
	private String advState;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	public String getAppNameNew() {
		return appNameNew;
	}

	public void setAppNameNew(String appNameNew) {
		this.appNameNew = appNameNew;
	}

	public String getLocalState() {
		return localState;
	}

	public void setLocalState(String localState) {
		this.localState = localState;
	}
	
	public String getAdvState() {
		return advState;
	}

	public void setAdvState(String advState) {
		this.advState = advState;
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		id = (String) in.readObject();
		appid = (String) in.readObject();
		appName = (String) in.readObject();
		appNameNew = (String) in.readObject();
		localState = (String) in.readObject();
		advState = (String) in.readObject();
		
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(id);
		out.writeObject(appid);
		out.writeObject(appName);
		out.writeObject(appNameNew);
		out.writeObject(localState);
		out.writeObject(advState);
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this,
				ToStringStyle.DEFAULT_STYLE);
		builder.append("id", id);
		builder.append("appid", appid);
		builder.append("appName", appName);
		builder.append("appNameNew", appNameNew);
		builder.append("localState", localState);
		builder.append("advState", advState);
		return builder.toString();
	}

	@Override
	public boolean equals(Object other) {
		boolean equals = false;
		if (other instanceof ClientLogCollection) {
			if (this == other) {
				equals = true;
			} else {
				PlayerApps cast = (PlayerApps) other;
				EqualsBuilder builder = new EqualsBuilder();
				builder.append(id, cast.id);
				builder.append(appid, cast.appid);
				builder.append(appName, cast.appName);
				builder.append(appNameNew, cast.appNameNew);
				builder.append(localState, cast.localState);
				builder.append(advState, cast.advState);
				equals = builder.isEquals();
			}
		}
		return equals;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		builder.append(id);
		builder.append(appid);
		builder.append(appName);
		builder.append(appNameNew);
		builder.append(localState);
		builder.append(advState);
		return builder.toHashCode();
	}


}
