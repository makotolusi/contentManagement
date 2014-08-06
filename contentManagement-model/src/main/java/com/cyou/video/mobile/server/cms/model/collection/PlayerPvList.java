package com.cyou.video.mobile.server.cms.model.collection;

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

@Document(collection = "PlayerPvList")
public class PlayerPvList extends BaseModel {
	
	@Id
	private String id; // i d

	private String appid;

	private int pv;
	
	private int uv;
	
	private int vv;
	
	private int time;
	
	private String statdate ;
	
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

	public int getPv() {
		return pv;
	}

	public void setPv(int pv) {
		this.pv = pv;
	}

	public int getUv() {
		return uv;
	}

	public void setUv(int uv) {
		this.uv = uv;
	}
	
	public int getVv() {
		return vv;
	}

	public void setVv(int vv) {
		this.vv = vv;
	}
	
	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
	
	public String getStatdate() {
		return statdate;
	}

	public void setStatdate(String statdate) {
		this.statdate = statdate;
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		id = (String) in.readObject();
		appid = (String) in.readObject();
		pv = (Integer) in.readObject();
		uv = (Integer) in.readObject();
		vv = (Integer) in.readObject();
		time = (Integer) in.readObject();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(id);
		out.writeObject(appid);
		out.writeObject(pv);
		out.writeObject(uv);
		out.writeObject(vv);
		out.writeObject(time);
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this,
				ToStringStyle.DEFAULT_STYLE);
		builder.append("id", id);
		builder.append("appid", appid);
		builder.append("pv", pv);
		builder.append("uv", uv);
		builder.append("vv", vv);
		builder.append("time", time);
		return builder.toString();
	}

	@Override
	public boolean equals(Object other) {
		boolean equals = false;
		if (other instanceof ClientLogCollection) {
			if (this == other) {
				equals = true;
			} else {
				PlayerPvList cast = (PlayerPvList) other;
				EqualsBuilder builder = new EqualsBuilder();
				builder.append(id, cast.id);
				builder.append(appid, cast.appid);
				builder.append(pv, cast.pv);
				builder.append(uv, cast.uv);
				builder.append(vv, cast.vv);
				builder.append(time, cast.time);
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
		builder.append(pv);
		builder.append(uv);
		builder.append(vv);
		builder.append(time);
		return builder.toHashCode();
	}


}
