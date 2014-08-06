package com.cyou.video.mobile.server.cms.service.push;

import org.springframework.data.mongodb.core.query.Query;


/**
 * 意见反馈业务接口
 * 
 * @author jyz
 */
public interface PushTagXinGeService{

  /**
   * 最强攻略安装游戏
   * @param s
   * @param end
   * @param query
   * @param name
   * @return
   * @throws Exception
   */
  int sendBestWalkThroughInstalledGameTags(int s, int end, Query query, String name) throws Exception;

 

}
