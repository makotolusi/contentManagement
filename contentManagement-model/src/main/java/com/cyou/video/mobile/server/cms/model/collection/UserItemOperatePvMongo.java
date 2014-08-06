package com.cyou.video.mobile.server.cms.model.collection;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.cyou.video.mobile.server.cms.model.BaseModel;
import com.tencent.xinge.TagTokenPair;

public class UserItemOperatePvMongo extends BaseModel {

  List<TagTokenPair> iosTags = new ArrayList<TagTokenPair>();

  List<TagTokenPair> androidTags = new ArrayList<TagTokenPair>();

  List<TagTokenPair> iosDelTags = new ArrayList<TagTokenPair>();

  List<TagTokenPair> androidDelTags = new ArrayList<TagTokenPair>();

  public void addIosTag(TagTokenPair tag) {
    iosTags.add(tag);
  }

  public void addIosDelTag(TagTokenPair tag) {
    iosDelTags.add(tag);
  }

  public void addAndroidTag(TagTokenPair tag) {
    androidTags.add(tag);
  }

  public void addAndroidDelTag(TagTokenPair tag) {
    androidDelTags.add(tag);
  }

  
  public List<TagTokenPair> getIosTags() {
    return iosTags;
  }

  public void setIosTags(List<TagTokenPair> iosTags) {
    this.iosTags = iosTags;
  }

  public List<TagTokenPair> getAndroidTags() {
    return androidTags;
  }

  public void setAndroidTags(List<TagTokenPair> androidTags) {
    this.androidTags = androidTags;
  }

  public List<TagTokenPair> getIosDelTags() {
    return iosDelTags;
  }

  public void setIosDelTags(List<TagTokenPair> iosDelTags) {
    this.iosDelTags = iosDelTags;
  }

  public List<TagTokenPair> getAndroidDelTags() {
    return androidDelTags;
  }

  public void setAndroidDelTags(List<TagTokenPair> androidDelTags) {
    this.androidDelTags = androidDelTags;
  }

  @Override
  public void writeExternal(ObjectOutput out) throws IOException {
    // out.writeObject(appId);
    // out.writeObject(value);

  }

  @Override
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    // appId = (ClientLogBestWalkthroughCollection) in.readObject();

  }

  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE);
    // builder.append("appId", id);
    // builder.append("value", value);
    return builder.toString();
  }

  @Override
  public boolean equals(Object other) {
    boolean equals = false;
    if(other instanceof UserItemOperatePvMongo) {
      if(this == other) {
        equals = true;
      }
      else {
        UserItemOperatePvMongo cast = (UserItemOperatePvMongo) other;
        EqualsBuilder builder = new EqualsBuilder();
        // builder.append("appId", appId);
        // builder.append("value", value);
        equals = builder.isEquals();
      }
    }
    return equals;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    // builder.append(appId);
    // builder.append(value);
    return builder.toHashCode();
  }

}
