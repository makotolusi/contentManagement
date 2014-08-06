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
import org.springframework.data.mongodb.core.mapping.Document;

import com.cyou.video.mobile.server.cms.model.BaseModel;
import com.cyou.video.mobile.server.common.adapter.JaxbDatetimeAdapter;

@Document(collection = "PushTagLastUpdateTime")
public class PushTagLastUpdateTime extends BaseModel {

	@Id
	private String id; // ID

	private String name; // 统计job的名字

	private Date lastUpdateTime; // 该任务最后更新时间

	public PushTagLastUpdateTime(String name,
			Date lastUpdateTime) {
		super();
		this.name = name;
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlJavaTypeAdapter(JaxbDatetimeAdapter.class)
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		id = (String) in.readObject();
		name = (String) in.readObject();
		lastUpdateTime = (Date) in.readObject();

	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(lastUpdateTime);

	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this,
				ToStringStyle.DEFAULT_STYLE);
		builder.append("id", id);
		builder.append("statisicJobName", name);
		builder.append("lastUpdateTime", lastUpdateTime);
		return builder.toString();
	}

	@Override
	public boolean equals(Object other) {
		boolean equals = false;
		if (other instanceof PushTagLastUpdateTime) {
			if (this == other) {
				equals = true;
			} else {
				PushTagLastUpdateTime cast = (PushTagLastUpdateTime) other;
				EqualsBuilder builder = new EqualsBuilder();
				builder.append(id, cast.id);
				builder.append(name, cast.name);
				builder.append(lastUpdateTime, cast.lastUpdateTime);
				equals = builder.isEquals();
			}
		}
		return equals;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		builder.append(id);
		builder.append(name);
		builder.append(lastUpdateTime);
		return builder.toHashCode();
	}
}
