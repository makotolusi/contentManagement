package com.cyou.video.mobile.server.cms.model.push;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.cyou.video.mobile.server.cms.model.BaseModel;

/**
 * 标签
 * @author lusi
 *
 */
@Document(collection = "PushTagCollection")
public class PushTagCollection extends BaseModel {

	@Id
	private String id;

	private String tagId; //tagid
	
	private String tagName; //tag name
	
	private int tagType; //tag name 0大标签 1小标签 2搜索
	
//	private float pv; // pv
	
//	private List<Map<String,String>> userId=new ArrayList<Map<String,String>>(); // user id

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	
//
//	public float getPv() {
//		return pv;
//	}
//
//	public void setPv(float pv) {
//		this.pv = pv;
//	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public String toString() {
		return "PushTagCollection [id=" + id + ", tagId=" + tagId
				+ ", tagName=" + tagName + ", userId=]";
	}

	@Override
	public boolean equals(Object other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getTagType() {
		return tagType;
	}

	public void setTagType(int tagType) {
		this.tagType = tagType;
	}


	
	
	
}
