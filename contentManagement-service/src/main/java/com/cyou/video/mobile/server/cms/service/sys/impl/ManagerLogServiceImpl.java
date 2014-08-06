package com.cyou.video.mobile.server.cms.service.sys.impl;

import org.springframework.stereotype.Service;

/**
 * 用户日志业务实现
 * 
 * @author zs
 */
@Service("userlogInfoService")
public class ManagerLogServiceImpl {//implements ManagerLogService {

//  private ManagerLogDao userLogInfoDao;
//
//  @Override
//  public int createUserLog(ManagerLog log) throws Exception {
//      if(log == null) {
//        throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_log");
//      }
//      if(StringUtils.isBlank(log.getName())) {
//        throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_log.name");
//      }
//      if(log.getId() > 0) {
//        throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.toString() + "_log.id");
//      }
//      if(log.getType() != 1 && log.getType() != 2 && log.getType() != 3) {
//        throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.toString() + "_log.type");
//      }
//      int id = userLogInfoDao.createUserLog(log);
//        if(id == 1 && log.getId() > 0){
//          id = log.getId();
//        }
//    return id;
//  }
//
//  @Override
//  public Pagination listLogInfo(int curPage, int pageSize, int type, String userName,
//      String modelName, Date from, Date to) throws Exception {
//    Pagination pagination = null;
//      pagination = new Pagination();
//      pagination.setCurPage(curPage);
//      pagination.setPageSize(pageSize);
//      pagination.setRowCount(userLogInfoDao.countUserLog(type, userName, modelName, from, to));
//      curPage =  (curPage-1)*pageSize;
//      pagination.setContent(userLogInfoDao.ListUserLog(curPage, pageSize, type, userName, modelName, from, to));
//    return pagination;
//  }

}
