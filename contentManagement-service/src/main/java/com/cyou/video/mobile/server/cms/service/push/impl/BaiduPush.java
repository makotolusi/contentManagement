package com.cyou.video.mobile.server.cms.service.push.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baidu.yun.channel.auth.ChannelKeyPair;
import com.baidu.yun.channel.client.BaiduChannelClient;
import com.baidu.yun.channel.exception.ChannelClientException;
import com.baidu.yun.channel.exception.ChannelServerException;
import com.baidu.yun.channel.model.DeleteTagRequest;
import com.baidu.yun.channel.model.PushBroadcastMessageRequest;
import com.baidu.yun.channel.model.PushBroadcastMessageResponse;
import com.baidu.yun.channel.model.PushTagMessageRequest;
import com.baidu.yun.channel.model.PushTagMessageResponse;
import com.baidu.yun.channel.model.PushUnicastMessageRequest;
import com.baidu.yun.channel.model.PushUnicastMessageResponse;
import com.baidu.yun.channel.model.QueryUserTagsRequest;
import com.baidu.yun.channel.model.QueryUserTagsResponse;
import com.baidu.yun.channel.model.SetTagRequest;
import com.baidu.yun.channel.model.TagInfo;
import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;
import com.cyou.video.mobile.server.cms.common.Consts.CLIENT_TYPE;
import com.cyou.video.mobile.server.cms.common.Consts.PUSH_SEND_STATE;
import com.cyou.video.mobile.server.cms.model.push.Push;
import com.cyou.video.mobile.server.cms.model.push.PushApp;
import com.cyou.video.mobile.server.cms.model.push.PushTagCollection;
import com.cyou.video.mobile.server.cms.service.push.AppSelectService;
import com.cyou.video.mobile.server.cms.service.push.PushInterface;
import com.tencent.xinge.TagTokenPair;
import com.tencent.xinge.XingeApp;

@Service("baiduPush")
public class BaiduPush implements PushInterface {

  private Logger logger = LoggerFactory.getLogger(BaiduPush.class);

  @Autowired
  AppSelectService appSelectService;

  // 1.申请ApiKey/SecretKey
  @Value("${baidu.apiKey}")
  private String baidu_apiKey;// = "ehRkun5rx6BRH2ALXGiegNYA";

  @Value("${baidu.secretKey}")
  private String baidu_secretKey;// = "fzylqg0E6A5jZ4jnSoGw9yNsje59dftD";

  @Value("${ios.deployStatus}")
  private int ios_deployStatus;// = "fzylqg0E6A5jZ4jnSoGw9yNsje59dftD";

  public BaiduChannelClient initChannel(final Push push) {
    PushApp app = appSelectService.getAppById(push.getAppId());
    ChannelKeyPair pair = new ChannelKeyPair(app.getAppKey(),  app.getSecretKey());
    // 2.
    BaiduChannelClient channelClient = new BaiduChannelClient(pair);
    // 3.
    channelClient.setChannelLogHandler(new YunLogHandler() {
      @Override
      public void onHandle(YunLogEvent event) {
        push.setSentLogs(event.getMessage());
        System.out.println(event.getMessage());
      }
    });
    return channelClient;
  }

  public BaiduChannelClient initChannel() {
    ChannelKeyPair pair = new ChannelKeyPair(baidu_apiKey, baidu_secretKey);
    // 2.
    BaiduChannelClient channelClient = new BaiduChannelClient(pair);
    // 3.
    channelClient.setChannelLogHandler(new YunLogHandler() {
      @Override
      public void onHandle(YunLogEvent event) {
        logger.debug(event.getMessage());
      }
    });
    return channelClient;
  }

  public Push pushAll(Push push) {
    BaiduChannelClient channelClient = this.initChannel(push);
    try {
      PushBroadcastMessageRequest request = new PushBroadcastMessageRequest();
      StringBuffer keyValue = new StringBuffer();
      String iosOnly = "";
      switch(push.getClientType()) {
        case ANDROID :
          request.setDeviceType(3); // device_type
          break;
        case IOS :
          request.setDeviceType(4); // device_type
          request.setDeployStatus(ios_deployStatus);
          iosOnly = ",\"aps\": {\"alert\":\"" + push.getContent() + "\",\"sound\":\"\",\"badge\":1}";
          break;
        default :
          break;
      }
      keyValue = keyValue(push, keyValue);
      request.setMessageType(1);// notification
      request.setMessage("{\"title\":\"" + push.getTitle() + "\",\"description\":\"" + push.getContent() + "\""
          + keyValue.toString() + iosOnly + "}");
      PushBroadcastMessageResponse response = channelClient.pushBroadcastMessage(request);
      push.setSendState(PUSH_SEND_STATE.SEND);
      return push;
    }
    catch(Exception e) {
      e.printStackTrace();
      push.setSentLogs(e.getMessage());
      push.setSendState(PUSH_SEND_STATE.FAIL);
      return push;
    }
  }

  private StringBuffer keyValue(Push push, StringBuffer keyValue) {
    Set<String> keyV = push.getKeyValue().keySet();
    keyValue.append(",\"custom_content\":{");
    for(Iterator it = keyV.iterator(); it.hasNext();) {
      String k = (String) it.next();
      String v = push.getKeyValue().get(k);
      keyValue.append("\"").append(k).append("\":\"").append(v).append("\",");

    }
    if(keyValue.length() != 0) {
      keyValue = keyValue.delete(keyValue.length() - 1, keyValue.length());
    }
    keyValue.append("}");
    return keyValue;
  }

  public Push pushOne(Push push) {
    BaiduChannelClient channelClient = this.initChannel(push);
    try {
      // 手机端的ChannelId， 手机端的UserId， 先用1111111111111代替，用户需替换为自己的
      PushUnicastMessageRequest request = new PushUnicastMessageRequest();
      String iosOnly = "";
      StringBuffer keyValue = new StringBuffer();
      keyValue = keyValue(push, keyValue);
      request.setChannelId(new Long(push.getChannelId()));
      request.setUserId(push.getUserId());
      switch(push.getClientType()) {
        case ANDROID :
          request.setDeviceType(3); // device_type
          request.setMessage("{"+ keyValue.toString().substring(1,keyValue.length())+"}");
          PushUnicastMessageResponse response = channelClient.pushUnicastMessage(request);
          int result = response.getSuccessAmount();//第一次消息
          request.setMessageType(1);//第二次通知
          request.setMessage("{\"title\":\"" +  push.getContent() + "\",\"description\":\"" + push.getContent() + "\""
              + keyValue.toString()+"}");
          break;
        case IOS :
          request.setDeviceType(4); // device_type
          request.setDeployStatus(ios_deployStatus);
          iosOnly = ",\"aps\": {\"alert\":\"" + push.getContent() + "\",\"sound\":\"\",\"badge\":1}";
          request.setMessageType(1);
          request.setMessage("{\"title\":\"" + push.getTitle() + "\",\"description\":\"" + push.getContent() + "\""
              + keyValue.toString() + iosOnly + "}");//message不能为空必须要
          break;
        default :
          break;
      }
      // 5. 调用pushMessage接口
      PushUnicastMessageResponse response = channelClient.pushUnicastMessage(request);
      int result = response.getSuccessAmount();
      // 6. 认证推送成功
      if(result > 0) {
        push.setSendState(PUSH_SEND_STATE.SEND);
      }
      else {
        push.setSendState(PUSH_SEND_STATE.FAIL);
      }

    }
    catch(Exception e) {
      // 处理客户端错误异常
      e.printStackTrace();
      push.setSentLogs(e.getMessage());
      push.setSendState(PUSH_SEND_STATE.FAIL);
    }
    return push;
  }

  public Push pushTag(Push push) {
    StringBuffer sentLog = new StringBuffer();
    PushTagMessageResponse response = null;
    try {
      BaiduChannelClient channelClient = this.initChannel(push);
      PushTagMessageRequest request = new PushTagMessageRequest();
      StringBuffer keyValue = new StringBuffer();
      String iosOnly = "";
      switch(push.getClientType()) {
        case ANDROID :
          request.setDeviceType(3); // device_type
          break;
        case IOS :
          request.setDeviceType(4); // device_type
          request.setDeployStatus(ios_deployStatus);
          iosOnly = ",\"aps\": {\"alert\":\"" +  push.getContent() + "\",\"sound\":\"\",\"badge\":1}";
          break;
        default :
          break;
      }
      keyValue = keyValue(push, keyValue);
      request.setMessageType(1);
      request.setMessage("{\"title\":\"" + push.getTitle() + "\",\"description\":\"" + push.getContent() + "\""
          + keyValue.toString() + iosOnly + "}");
      List<PushTagCollection> tags = push.getTags();
      if(!tags.isEmpty()) {
        PushTagCollection tag = tags.get(0);
        request.setTagName(tag.getTagId());
        response = channelClient.pushTagMessage(request);
      }
    }
    catch(Exception e) {
    }
    if(sentLog.length() == 0) {
      push.setSendState(PUSH_SEND_STATE.SEND);
    }
    else {
      push.setSendState(PUSH_SEND_STATE.FAIL);
    }
    return push;

  }

  public void setTag(String uid, String tag) throws Exception {
    if(tag.length() != 0) {
      BaiduChannelClient channelClient = initChannel();
      SetTagRequest request = new SetTagRequest();
      request.setTag(tag);
      request.setUserId(uid);
      channelClient.setTag(request);
    }

  }

  /**
   * 删除tag
   * 
   * @param tag
   * @param clientType
   */
  public void deleteTag(String uid, String tag) throws Exception {
    BaiduChannelClient channelClient = initChannel();
    DeleteTagRequest request = new DeleteTagRequest();
    request.setTag(tag);
    request.setUserId(uid);
    channelClient.deleteTag(request);

  }

  /**
   * 删除tag
   * 
   * @param tag
   * @param clientType
   */
  public void deleteUserTag(String uid) throws Exception {
    BaiduChannelClient channelClient = initChannel();
    // query
    QueryUserTagsRequest request = new QueryUserTagsRequest();
    request.setUserId(uid);
    QueryUserTagsResponse response = channelClient.queryUserTags(request);
    List<TagInfo> l = response.getTags();
    for(Iterator iterator = l.iterator(); iterator.hasNext();) {
      TagInfo tagInfo = (TagInfo) iterator.next();
      DeleteTagRequest r = new DeleteTagRequest();
      r.setTag(tagInfo.getName());
      r.setUserId(uid);
      channelClient.deleteTag(r);
    }
  }

  /**
   * query tag
   * 
   * @param tag
   * @param clientType
   * @throws ChannelServerException
   * @throws ChannelClientException
   */
  public List<TagInfo> queryUserTag(String uid) throws ChannelClientException, ChannelServerException {
    BaiduChannelClient channelClient = initChannel();
    QueryUserTagsRequest request = new QueryUserTagsRequest();
    request.setUserId(uid);
    QueryUserTagsResponse response = channelClient.queryUserTags(request);
    return response.getTags();
  }


  @Override
  public void setTagByXinge(List<TagTokenPair> pairs, Push p){
    // TODO Auto-generated method stub
    
  }

  @Override
  public void delTagByXinge(List<TagTokenPair> pairs, Push p){
    // TODO Auto-generated method stub
    
  }

}
