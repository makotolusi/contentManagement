package com.cyou.video.mobile.server.cms.service.sys;

import java.util.List;

import com.cyou.video.mobile.server.cms.model.sys.SysProperty;

/**
 * CMS系统参数业务接口
 * @author zs
 */
public interface SysPropertyService {
	
	/**
	   * 创建令牌
	   * @param sysPro 系统参数
	   * @return 参数id
	   * @throws Exception
	   */
	  public int createSysProperty(SysProperty sysPro) throws Exception;

	  /**
	   * 获取系统参数
	   * @param sysPro 系统参数
	   * @return 系统参数列表
	   * @throws Exception
	   */
	  public List<SysProperty> listSysProperty(SysProperty sysPro) throws Exception;
	  
	  /**
	   * 更新系统参数
	   * @param sysPro 系统参数
	   * @throws Exception
	   */
	  public void updateSysProperty(SysProperty sysPro) throws Exception;

	  /**
	   * 删除参数
	   * @param id 参数id
	   * @param plat 使用平台
     * @param version 项目版本
	   * @throws Exception
	   */
	  public void deleteSysProperty(int id, int plat, String version, String channel) throws Exception;
	  
	  /**
	   * 更新参数状态
	   * @param id 参数id
	   * @param status 参数状态
	   * @param plat 使用平台
	   * @param version 项目版本
	   * @throws exception
	   */
	  public void updateStatus(int id, int status, int plat, String version, String channel) throws Exception;
	  
	  /**
     * 获取系统参数
     * @param key key
     * @return 系统参数列表
     * @throws Exception
     */
    public List<SysProperty> listSysPropertyByKey(String key, int type, int plat, String version, int appType) throws Exception;
    
    /**
     * 为rest接口提供获取系统参数
     * @param sysPro 系统参数
     * @return 系统参数列表
     * @throws Exception
     */
    public List<SysProperty> restListSysProperty(SysProperty sysPro) throws Exception;
}
