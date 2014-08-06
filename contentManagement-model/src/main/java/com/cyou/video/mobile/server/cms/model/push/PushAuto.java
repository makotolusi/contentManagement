package com.cyou.video.mobile.server.cms.model.push;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.cyou.video.mobile.server.cms.common.Consts.COLLECTION_ITEM_TYPE;
import com.cyou.video.mobile.server.cms.common.Consts.PUSH_JOB_STATE;

@Document(collection = "PushAuto")
public class PushAuto {

	@Id
	private String id; // i d
	private String name;
	private COLLECTION_ITEM_TYPE itemType;
	private PUSH_JOB_STATE state;
	
	public PushAuto(String name, COLLECTION_ITEM_TYPE itemType,
			PUSH_JOB_STATE state) {
		super();
		this.name = name;
		this.itemType = itemType;
		this.state = state;
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
	public COLLECTION_ITEM_TYPE getItemType() {
		return itemType;
	}
	public void setItemType(COLLECTION_ITEM_TYPE itemType) {
		this.itemType = itemType;
	}
	public PUSH_JOB_STATE getState() {
		return state;
	}
	public void setState(PUSH_JOB_STATE state) {
		this.state = state;
	}
	
	
}
