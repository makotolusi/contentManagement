package com.cyou.video.mobile.server.cms.model.sys;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.cyou.video.mobile.server.cms.model.BaseModel;

/**
 * 系统参数表
 * @author zs
 */
@XmlRootElement
public class SysProperty extends BaseModel{
	
	private int id ; //序列号
	
	private String desc; //参数描述
	
	private String key; //参数键
	
	private String value; //参数值
	
	private String version; //系统版本号
	
	private int type; //参数使用者
	
	private int status; //参数状态
	
	private int plat; //参数使用平台
	
	private String channel = ""; //渠道标识
	
	private int appType;//所属哪种app 1:173app；2:最强攻略app
	
	private String appSx;//app缩写的list字符串
	
	private List<String> appSxList = new ArrayList<String>();//app缩写
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getPlat() {
		return plat;
	}

	public void setPlat(int plat) {
		this.plat = plat;
	}
	
	public String getChannel() {
    return channel;
  }

  public void setChannel(String channel) {
    this.channel = channel;
  }

	public int getAppType() {
    return appType;
  }

  public void setAppType(int appType) {
    this.appType = appType;
  }
  
  public List<String> getAppSxList() {
    return appSxList;
  }

  public void setAppSxList(List<String> appSxList) {
    this.appSxList = appSxList;
  }

  public String getAppSx() {
    return appSx;
  }

  public void setAppSx(String appSx) {
    this.appSx = appSx;
    this.appSxList = Arrays.asList(appSx.split(","));
  }

  @Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(id);
	    out.writeObject(desc);
	    out.writeObject(key);
	    out.writeObject(value);;
	    out.writeObject(version);
	    out.writeInt(status);
	    out.writeInt(type);
	    out.writeInt(plat);
	    out.writeObject(channel);
	    out.writeInt(appType);
	    out.writeObject(appSx);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		id = in.readInt();
	    desc = (String) in.readObject();
	    key = (String) in.readObject();
	    value = (String) in.readObject();
	    version = (String) in.readObject();
	    status = in.readInt();
	    type = in.readInt();
	    plat = in.readInt();
	    channel = (String) in.readObject();
	    appType = in.readInt();
	    appSx = (String) in.readObject();
		
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE);
	    builder.append("id", id);
	    builder.append("desc", desc);
	    builder.append("key", key);
	    builder.append("value", value);
	    builder.append("status", status);
	    builder.append("type", type);
	    builder.append("plat", plat);
	    builder.append("version",version);
	    builder.append("channel",channel);
	    builder.append("appType",appType);
	    builder.append("appSx",appSx);
	    return builder.toString();
	}

	@Override
	public boolean equals(Object other) {
		 boolean equals = false;
		    if(other instanceof SysProperty) {
		      if(this == other) {
		        equals = true;
		      }
		      else {
		    	SysProperty sys = (SysProperty) other;
		        EqualsBuilder builder = new EqualsBuilder();
		        builder.append("id", sys.id);
			    builder.append("desc", sys.desc);
			    builder.append("key", sys.key);
			    builder.append("value", sys.value);
			    builder.append("status", sys.status);
			    builder.append("type", sys.type);
			    builder.append("plat", sys.plat);
			    builder.append("version",sys.version);
			    builder.append("channel", sys.channel);
			    builder.append("appType", sys.appSx);
			    
		        equals = builder.isEquals();
		      }
		    }
		    return equals;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
	    builder.append(id);
	    builder.append(desc);
	    builder.append(key);
	    builder.append(value);
	    builder.append(status);
	    builder.append(type);
	    builder.append(plat);
	    builder.append(version);
	    builder.append(channel);
	    builder.append(appType);
	    builder.append(appSx);
	    return builder.toHashCode();
	}

}
