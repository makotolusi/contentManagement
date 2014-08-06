package com.cyou.video.mobile.server.cms.model.push;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.cyou.video.mobile.server.cms.common.Consts.PUSH_SEND_TAG_STATE;
import com.cyou.video.mobile.server.cms.model.BaseModel;

/**
 * 标签
 * 
 * @author lusi
 * 
 */
@Document(collection = "PushTagCombination")
public class PushTagCombination extends BaseModel {

	@Id
	private String id;

	private String tagId; // tag name

	private String tagName; // tag name

	private int threadNum; // tag name

	private String tagType; // tag name 0 big 1 small 2 search

	private String tagsop; // ADN OR

	private String cvtagsop; // ADN OR

	private List<PushTagCollection> tags = new ArrayList<PushTagCollection>(); // user

	private List<PushTagCollection> ctags = new ArrayList<PushTagCollection>(); // cahnnel

	private List<PushTagCollection> vtags = new ArrayList<PushTagCollection>(); // cahnnel

	private Date updateTime = new Date();

	private String process;
	
	private int setTotal;

	@Transient
	private PUSH_SEND_TAG_STATE state;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public List<PushTagCollection> getTags() {
		return tags;
	}

	public void setTags(List<PushTagCollection> tags) {
		this.tags = tags;
	}

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
	public boolean equals(Object other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getTagType() {
		return tagType;
	}

	public void setTagType(String tagType) {
		this.tagType = tagType;
	}

	public String getTagId() {
		tagId =  id+"_C";
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public List<PushTagCollection> getCtags() {
		return ctags;
	}

	public void setCtags(List<PushTagCollection> ctags) {
		this.ctags = ctags;
	}

	public List<PushTagCollection> getVtags() {
		return vtags;
	}

	public void setVtags(List<PushTagCollection> vtags) {
		this.vtags = vtags;
	}

	public String getTagsop() {
		return tagsop;
	}

	public void setTagsop(String tagsop) {
		this.tagsop = tagsop;
	}

	public String getCvtagsop() {
		return cvtagsop;
	}

	public void setCvtagsop(String cvtagsop) {
		this.cvtagsop = cvtagsop;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public int getThreadNum() {
		return threadNum;
	}

	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public int getSetTotal() {
		return setTotal;
	}

	public void setSetTotal(int setTotal) {
		this.setTotal = setTotal;
	}

	
	public PUSH_SEND_TAG_STATE getState() {
		return state;
	}

	public void setState(PUSH_SEND_TAG_STATE state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "PushTagCombination [id=" + id + ", tagId=" + tagId + ", tagType=" + tagType + ", tagsop=" + tagsop
				+ ", cvtagsop=" + cvtagsop + ", tags=" + tags + ", ctags="
				+ ctags + ", vtags=" + vtags + ", updateTime=" + updateTime
				+ "]";
	}

}
