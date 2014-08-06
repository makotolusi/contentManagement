package com.cyou.video.mobile.server.cms.model.collection;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 信息收集
 * 
 * @author lu si
 */
@Document(collection = "ClientLogBestWalkthroughCollection")
public class ClientLogBestWalkthroughCollection extends ClientLogCollection {


  private String appId; // 区分app

  public String getAppId() {
    return appId;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

 
}
