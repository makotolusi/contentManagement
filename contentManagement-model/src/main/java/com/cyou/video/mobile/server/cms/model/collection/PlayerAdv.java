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

@Document(collection = "PlayerAdv")
public class PlayerAdv extends BaseModel {
	
	@Id
	private String id; // i d

	private String type; //目前只有'trdparty'

	private String state; //status:0表示停用，1表示启用
	
	private String advcode;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAdvcode() {
		return advcode;
	}

	public void setAdvcode(String advcode) {
		this.advcode = advcode;
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		id = (String) in.readObject();
		type = (String) in.readObject();
		state = (String) in.readObject();
		advcode = (String) in.readObject();
		
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(id);
		out.writeObject(type);
		out.writeObject(state);
		out.writeObject(advcode);
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this,
				ToStringStyle.DEFAULT_STYLE);
		builder.append("id", id);
		builder.append("type", type);
		builder.append("state", state);
		builder.append("advcode", advcode);
		return builder.toString();
	}

	@Override
	public boolean equals(Object other) {
		boolean equals = false;
		if (other instanceof ClientLogCollection) {
			if (this == other) {
				equals = true;
			} else {
				PlayerAdv cast = (PlayerAdv) other;
				EqualsBuilder builder = new EqualsBuilder();
				builder.append(id, cast.id);
				builder.append(type, cast.type);
				builder.append(state, cast.state);
				builder.append(advcode, cast.advcode);
				equals = builder.isEquals();
			}
		}
		return equals;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		builder.append(id);
		builder.append(type);
		builder.append(state);
		builder.append(advcode);
		return builder.toHashCode();
	}


}
