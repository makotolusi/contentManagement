package com.cyou.video.mobile.server.cms.service.push.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baidu.yun.channel.exception.ChannelClientException;
import com.baidu.yun.channel.exception.ChannelServerException;
import com.baidu.yun.channel.model.TagInfo;
import com.cyou.video.mobile.server.cms.common.Consts.PUSH_SEND_STATE;
import com.cyou.video.mobile.server.cms.model.push.Push;
import com.cyou.video.mobile.server.cms.model.push.PushApp;
import com.cyou.video.mobile.server.cms.model.push.PushTagCollection;
import com.cyou.video.mobile.server.cms.service.push.AppSelectService;
import com.cyou.video.mobile.server.cms.service.push.PushInterface;
import com.tencent.xinge.ClickAction;
import com.tencent.xinge.Message;
import com.tencent.xinge.MessageIOS;
import com.tencent.xinge.Style;
import com.tencent.xinge.TagTokenPair;
import com.tencent.xinge.TimeInterval;
import com.tencent.xinge.XingeApp;

@Service("xingePush")
public class XinGePush implements PushInterface {

  private Logger logger = LoggerFactory.getLogger(XinGePush.class);

  @Value("${ios.deployStatus}")
  private int ios_deployStatus;

  @Autowired
  AppSelectService appSelectService;

  public XingeApp initChannel(Push push) {
    PushApp app = appSelectService.getAppById(push.getAppId());
    switch(push.getClientType()) {
      case IOS :
        return new XingeApp(Long.parseLong(app.getAppKey_ios()), app.getSecretKey_ios());
      case ANDROID :
        return new XingeApp(Long.parseLong(app.getAppKey()), app.getSecretKey());
      default :
        return null;
    }

  }

  public Push pushAll(Push push) {
    try {
      XingeApp xinge = this.initChannel(push);
      switch(push.getClientType()) {
        case ANDROID :
          push = androidPush(push, xinge);
          break;
        case IOS :
          push = iosPush(push, xinge);
          break;
        default :
          break;
      }
      return push;
    }
    catch(Exception e) {
      // e.printStackTrace();
      logger.error("push all exception ------- {} ", e.getMessage());
      push.setSentLogs(e.getMessage());
      push.setSendState(PUSH_SEND_STATE.FAIL);
      return push;
    }
  }

  private Push androidPush(Push push, XingeApp xinge) throws JSONException {
    Message message = androidMessage(push);
    JSONObject respose = xinge.pushAllDevice(XingeApp.DEVICE_ANDROID, message);
    setSuccess(push, respose);
    return push;
  }

  private void setSuccess(Push push, JSONObject respose) throws JSONException {
    logger.info(respose.toString());
    if(respose.getInt("ret_code") == 0)
      push.setSendState(PUSH_SEND_STATE.SEND);
    else
      push.setSendState(PUSH_SEND_STATE.FAIL);
    push.setSentLogs(respose.toString());
  }

  private Push androidPushTag(Push push, XingeApp xinge) throws JSONException {
    Message message = androidMessage(push);
    List<String> tagList = new ArrayList<String>();
    setTags(push, tagList);
    JSONObject respose = xinge.pushTags(XingeApp.DEVICE_ANDROID, tagList, push.getTagRelation(), message);
    setSuccess(push, respose);
    return push;
  }

  private void setTags(Push push, List<String> tagList) {
    List<PushTagCollection> tags = push.getTags();
    for(Iterator iterator = tags.iterator(); iterator.hasNext();) {
      PushTagCollection s = (PushTagCollection) iterator.next();
      tagList.add(s.getTagId());
    }
  }

  private Message androidMessage(Push push) throws JSONException {
    Message message = new Message();
    message.setType(Message.TYPE_NOTIFICATION);
    Style style = new Style(1);
    style = new Style(3, 1, 0, 1, 0);
    ClickAction action = new ClickAction();
    action.setActionType(ClickAction.TYPE_ACTIVITY);
    action.setActivity("XGExclusive");
    message.setStyle(style);
    message.setAction(action);
    if(push.getInterval() != null) {
      TimeInterval acceptTime1 = setAcceptTime(push.getInterval());
      message.addAcceptTime(acceptTime1);
    }
    message.setTitle(push.getTitle());
    message.setContent(push.getContent());
    message.setType(Message.TYPE_NOTIFICATION);
    if(push.getKeyValue() != null && !"".equals(push.getKeyValue())) {
      Map map = new HashMap();
      map = push.getKeyValue();
      message.setCustom((HashMap) ((HashMap) map).clone());
    }
    return message;
  }

  private TimeInterval setAcceptTime(Map<String, Integer> interval) {
    TimeInterval acceptTime1 = new TimeInterval(interval.get("sh"), interval.get("sm"), interval.get("eh"),
        interval.get("em"));
    return acceptTime1;
  }

  private Push iosPush(Push push, XingeApp xinge) throws JSONException {
    MessageIOS message = iosMessage(push);
    int state = 0;
    if(ios_deployStatus == 1)
      state = XingeApp.IOSENV_DEV;
    else
      state = XingeApp.IOSENV_PROD;
    JSONObject respose = xinge.pushAllDevice(XingeApp.DEVICE_IOS, message, state);// 1
    setSuccess(push, respose);
    return push;
  }

  private MessageIOS iosMessage(Push push) throws JSONException {
    MessageIOS message = new MessageIOS();
    message.setAlert(push.getContent());
    message.setBadge(1);
    message.setSound("beep.wav");
    if(push.getInterval() != null) {
      TimeInterval acceptTime1 = setAcceptTime(push.getInterval());
      message.addAcceptTime(acceptTime1);
    }
    if(push.getKeyValue() != null && !"".equals(push.getKeyValue())) {
      Map map = new HashMap();
      map = push.getKeyValue();
      HashMap xingeKV = (HashMap) ((HashMap) map).clone();
      xingeKV.put("isXG", "1");
      message.setCustom((HashMap) ((HashMap) map).clone());
    }
    return message;
  }

  private Push iosPushTag(Push push, XingeApp xinge) throws JSONException {
    MessageIOS message = iosMessage(push);
    List<String> tagList = new ArrayList<String>();
    setTags(push, tagList);
    JSONObject respose = xinge.pushTags(XingeApp.DEVICE_ANDROID, tagList, push.getTagRelation(), message,
        ios_deployStatus);
    setSuccess(push, respose);
    return push;
  }

  @Override
  public Push pushTag(Push push) {
    try {
      XingeApp xinge = this.initChannel(push);
      switch(push.getClientType()) {
        case ANDROID :
          push = androidPushTag(push, xinge);
          break;
        case IOS :
          push = iosPushTag(push, xinge);
          break;
        default :
          break;
      }
      return push;
    }
    catch(Exception e) {
      e.printStackTrace();
      logger.error("push all exception ------- {} ", e.getMessage());
      push.setSentLogs(e.getMessage());
      push.setSendState(PUSH_SEND_STATE.FAIL);
      return push;
    }
  }

  @Override
  public void setTagByXinge(List<TagTokenPair> pairs, Push p) {
    try {
      XingeApp xinge = this.initChannel(p);
      JSONObject ret = xinge.BatchSetTag(pairs);
      logger.debug(ret.toString());
    }
    catch(Exception e) {
      logger.error("xinge set tag exception may be could not find appKey by id");
    }
    logger.debug("set tag success");
  }

  @Override
  public void delTagByXinge(List<TagTokenPair> pairs, Push p) {
    try {
      XingeApp xinge = this.initChannel(p);
      JSONObject ret = xinge.BatchDelTag(pairs);
      logger.debug(ret.toString());
    }
    catch(Exception e) {
      logger.error("xinge set tag exception may be could not find appKey by id");
    }
    logger.debug("delete tag success");
  }

  @Override
  public void deleteTag(String uid, String tag) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public void deleteUserTag(String uid) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public List<TagInfo> queryUserTag(String uid) throws ChannelClientException, ChannelServerException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Push pushOne(Push push) {
    try {
      XingeApp xinge = this.initChannel(push);
      JSONObject respose = null;
      switch(push.getClientType()) {
        case ANDROID :
          Message message = androidMessage(push);
          respose = xinge.pushSingleDevice(push.getUserId(), message);
          break;
        case IOS :
          MessageIOS messageios = iosMessage(push);
          respose = xinge.pushSingleDevice(push.getUserId(), messageios, ios_deployStatus);
          break;
        default :
          break;
      }
      System.out.println(respose.toString());
      logger.info(respose.toString());
      setSuccess(push, respose);
      return push;
    }
    catch(JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return push;
    }
  }

  @Override
  public void setTag(String uid, String tag) throws Exception {
    // TODO Auto-generated method stub

  }

}
