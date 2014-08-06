package com.cyou.video.mobile.server.cms.service.push;

import java.util.Map;

import com.cyou.video.mobile.server.cms.model.Pagination;
import com.cyou.video.mobile.server.cms.model.push.PushApp;

/**
 * 意见反馈业务接口
 * 
 * @author jyz
 */
public interface AppSelectService {


	public Pagination listApp(Map<String, Object> params);

	/**
	 * 同步应用列表
	 * @param pushApp
	 */
  void syncApp();

  /**
   * 取app
   * @param id
   * @return
   */
  PushApp getAppById(Integer id);

}
