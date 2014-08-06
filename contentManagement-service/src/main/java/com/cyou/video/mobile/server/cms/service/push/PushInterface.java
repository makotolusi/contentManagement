package com.cyou.video.mobile.server.cms.service.push;

import java.util.List;

import com.baidu.yun.channel.exception.ChannelClientException;
import com.baidu.yun.channel.exception.ChannelServerException;
import com.baidu.yun.channel.model.TagInfo;
import com.cyou.video.mobile.server.cms.model.push.Push;
import com.tencent.xinge.TagTokenPair;

public interface PushInterface {
  public Push pushAll(Push push);

  public Push pushTag(Push push);

  public void setTag(String uid, String tag) throws Exception;

  public void deleteTag(String uid, String tag) throws Exception;

  public void deleteUserTag(String uid) throws Exception;

  public List<TagInfo> queryUserTag(String uid) throws ChannelClientException, ChannelServerException;

  public Push pushOne(Push push);

  /**
   * xinge set tag
   * 
   * @param pairs
   * @param cType
   * @throws Exception
   */
  void setTagByXinge(List<TagTokenPair> pairs, Push p);

  /**
   * 信鸽批量删除
   * 
   * @param pairs
   * @param cType
   * @throws Exception
   */
  void delTagByXinge(List<TagTokenPair> pairs, Push p);
}
