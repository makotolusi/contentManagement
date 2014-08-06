package com.cyou.video.mobile.server.cms.service.sys.impl;

import org.springframework.stereotype.Service;

/**
 * 系统参数业务实现
 * @author zs
 */
@Service("sysPropertyService")
public class SysPropertyServiceImpl {//implements SysPropertyService {
//  
//  private final String LISTSYSPROPERTY = "listSysProperty_";
//  
//  private final String LISTSYSPROPETYBYKEY = "listSysPropetyByKey_";
//  
//	private SysPropertyDao sysPropertyDao;
//	
//  private MemcacheTemplate memcacheTemplate;
//
//	@Override
//	public int createSysProperty(SysProperty sysPro)
//			throws Exception {
//			if (sysPro == null) {
//			  throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_sysProperty");
//			}
//			if (StringUtils.isBlank(sysPro.getDesc())) {
//			  throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_sysProperty.desc");
//			}
//			if (StringUtils.isBlank(sysPro.getKey())) {
//			  throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_sysProperty.key");
//			}
//			if (StringUtils.isBlank(sysPro.getValue())) {
//			  throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_sysProperty.value");
//			}
//			if (StringUtils.isBlank(sysPro.getVersion())) {
//			  throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_sysProperty.version");
//			}
//			if (sysPro.getType() < 0) {
//			  throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.toString() + "_sysProperty.type");
//			}
//			if (sysPro.getPlat() < 0) {
//			  throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.toString() + "_sysProperty.plat");
//			}
//			if (sysPro.getAppType() < 0) {
//        throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.toString() + "_sysProperty.appType");
//      }
//			if (StringUtils.isBlank(sysPro.getChannel())) {
//        throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_sysProperty.channel");
//      }
//			List<SysProperty> sys = sysPropertyDao.getSysProperty(sysPro);
//			if(sys != null && sys.size() > 0){
//			  if(sysPro.getAppType() == Consts.APP_TYPE.GAMETOPLINE.getValue()){
//			    throw new VerifyException(Constants.CUSTOM_ERROR_CODE.UNIQUENESS_CONSTRAINT.getValue(), Constants.CUSTOM_ERROR_CODE.UNIQUENESS_CONSTRAINT.toString() + "_sysProperty");
//	      }else if(sysPro.getAppType() == Consts.APP_TYPE.ZQSTRATEGY.getValue()){
//	        for(SysProperty sysp : sys) {
//            List<String> kclist = new ArrayList<String>(Arrays.asList(sysp.getAppSx().split(",")));
//            kclist.retainAll(sysPro.getAppSxList());
//            if(!kclist.isEmpty()){
//              throw new VerifyException(Constants.CUSTOM_ERROR_CODE.UNIQUENESS_CONSTRAINT.getValue(), Constants.CUSTOM_ERROR_CODE.UNIQUENESS_CONSTRAINT.toString() + "_sysProperty");
//            }
//          }
//	      }
//      }
//    sysPro.setStatus(Constants.STATUS.ON.getValue());
//    sysPro.setAppSx(sysPro.getAppSxList().toString().replace(" ", "").replace("[", "").replace("]", ""));
//    int id = sysPropertyDao.createSysProperty(sysPro);
//    if(id == 1 && sysPro.getId() > 0) {
//      id = sysPro.getId();
//    }
//    memcacheTemplate.delete(LISTSYSPROPETYBYKEY+sysPro.getKey()+"_"+sysPro.getPlat()+"_"+sysPro.getType()+"_"+sysPro.getVersion()+"_"+sysPro.getAppType());
//    memcacheTemplate.delete(LISTSYSPROPERTY+sysPro.getPlat()+"_"+sysPro.getStatus()+"_"+sysPro.getType()+"_"+sysPro.getVersion()+"_"+sysPro.getAppType());
//		return id;
//	}
//
//	@Override
//  public List<SysProperty> listSysProperty(SysProperty sysPro) throws Exception {
//    if(sysPro == null) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_sysProperty");
//    }
//    return sysPropertyDao.listSysProperty(sysPro);
//  }
//
//  @Override
//	public void updateSysProperty(SysProperty sysPro)
//			throws Exception {
//    if(sysPro == null) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(),Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_sysProperty");
//    }
//    if(sysPro.getId() < 1) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.getValue(),Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.toString() + "_sysProperty.id");
//    }
//    if(StringUtils.isBlank(sysPro.getDesc())) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(),Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_sysProperty.desc");
//    }
//    if(StringUtils.isBlank(sysPro.getKey())) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(),Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_sysProperty.key");
//    }
//    if(StringUtils.isBlank(sysPro.getValue())) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(),Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_sysProperty.value");
//    }
//    if(StringUtils.isBlank(sysPro.getVersion())) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(),Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_sysProperty.version");
//    }
//    if(sysPro.getType() < 0) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.getValue(),Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.toString() + "_sysProperty.type");
//    }
//    if(sysPro.getPlat() < 0) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.getValue(),Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.toString() + "_sysProperty.plat");
//    }
//    if (StringUtils.isBlank(sysPro.getChannel())) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_sysProperty.channel");
//    }
//    SysProperty syspro = sysPropertyDao.getSysPropertyById(sysPro.getId());
//    if(syspro == null) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.OBJECT_NOT_FOUND.getValue(), Constants.CUSTOM_ERROR_CODE.OBJECT_NOT_FOUND.toString() + "_sysProperty");
//    }
//    sysPro.setAppSx(sysPro.getAppSxList().toString().replace(" ", "").replace("[", "").replace("]", ""));
//    List<String> myself = new ArrayList<String>(Arrays.asList(syspro.getAppSx().split(",")));
//    if(!(sysPro.getVersion().equals(syspro.getVersion()) && sysPro.getPlat() == syspro.getPlat()
//        && sysPro.getType() == syspro.getType() && sysPro.getKey().equals(syspro.getKey()) && sysPro.getChannel().equals(syspro.getChannel()) 
//            && (sysPro.getAppSxList().containsAll(myself) && myself.containsAll(sysPro.getAppSxList())) && sysPro.getAppType() == syspro.getAppType() )) {
//      // 修改时，要保证与数据库中出了本身之外的其他参数的plat、key、version、type不同
//      List<SysProperty> sys = sysPropertyDao.getSysProperty(sysPro);
//      if(sys != null && sys.size() > 0){
//        if(sysPro.getAppType() == Consts.APP_TYPE.GAMETOPLINE.getValue()){
//          throw new VerifyException(Constants.CUSTOM_ERROR_CODE.UNIQUENESS_CONSTRAINT.getValue(), Constants.CUSTOM_ERROR_CODE.UNIQUENESS_CONSTRAINT.toString() + "_sysProperty");
//        }else if(sysPro.getAppType() == Consts.APP_TYPE.ZQSTRATEGY.getValue()){
//          for(SysProperty sysp : sys) {
//            if(sysPro.getId() == sysp.getId()){
//              continue;
//            }
//            List<String> kclist = new ArrayList<String>(Arrays.asList(sysp.getAppSx().split(",")));
//            kclist.retainAll(sysPro.getAppSxList());
//            if(!kclist.isEmpty()){
//              throw new VerifyException(Constants.CUSTOM_ERROR_CODE.UNIQUENESS_CONSTRAINT.getValue(), Constants.CUSTOM_ERROR_CODE.UNIQUENESS_CONSTRAINT.toString() + "_sysProperty");
//            }
//          }
//        }
//      }
//    }
//    sysPropertyDao.updateSysProperty(sysPro);
//    memcacheTemplate.delete(LISTSYSPROPETYBYKEY+sysPro.getKey()+"_"+sysPro.getPlat()+"_"+sysPro.getType()+"_"+sysPro.getVersion()+"_"+sysPro.getAppType());
//    memcacheTemplate.delete(LISTSYSPROPERTY+sysPro.getPlat()+"_"+sysPro.getStatus()+"_"+sysPro.getType()+"_"+sysPro.getVersion()+"_"+sysPro.getAppType());
//	}
//
//	@Override
//	//@CacheEvict(value = "videoMobileCMSCache", allEntries = true)
//  public void deleteSysProperty(int id, int plat, String version, String channel) throws Exception {
//    if(id < 1) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.toString() + "_sysProperty.id");
//    }
//    SysProperty syspro = sysPropertyDao.getSysPropertyById(id);
//    if(syspro == null) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.OBJECT_NOT_FOUND.getValue(), Constants.CUSTOM_ERROR_CODE.OBJECT_NOT_FOUND.toString() + "_sysProperty");
//    }
//    sysPropertyDao.deleteSysProperty(id);
//    memcacheTemplate.delete(LISTSYSPROPETYBYKEY+syspro.getKey()+"_"+syspro.getPlat()+"_"+syspro.getType()+"_"+syspro.getVersion()+"_"+syspro.getAppType());
//    memcacheTemplate.delete(LISTSYSPROPERTY+syspro.getPlat()+"_"+syspro.getStatus()+"_"+syspro.getType()+"_"+syspro.getVersion()+"_"+syspro.getAppType());
//  }
//
//	@Override
//  public void updateStatus(int id, int status, int plat, String version, String channel) throws Exception {
//    if(id < 1) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_INVALID.toString() + "_sysProperty.id");
//    }
//    SysProperty syspro = sysPropertyDao.getSysPropertyById(id);
//    if(syspro == null) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.OBJECT_NOT_FOUND.getValue(), Constants.CUSTOM_ERROR_CODE.OBJECT_NOT_FOUND.toString() + "_sysProperty");
//    }
//    syspro.setStatus(status);
//    sysPropertyDao.updateSysProperty(syspro);
//    memcacheTemplate.delete(LISTSYSPROPETYBYKEY+syspro.getKey()+"_"+syspro.getPlat()+"_"+syspro.getType()+"_"+syspro.getVersion()+"_"+syspro.getAppType());
//    memcacheTemplate.delete(LISTSYSPROPERTY+syspro.getPlat()+"_"+Constants.STATUS.TRUE.getValue()+"_"+syspro.getType()+"_"+syspro.getVersion()+"_"+syspro.getAppType());
//  }
//
//  @Override
//  @SuppressWarnings("unchecked")
//  public List<SysProperty> listSysPropertyByKey(String key, int type, int plat, String version, int appType) throws Exception {
//    List<SysProperty> sysPros = null;
//    if(key == null) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_sysProperty.key");
//    }
//    SysProperty sysPro = new SysProperty();
//    sysPro.setKey(key);
//    sysPro.setType(type);
//    sysPro.setStatus(1);// 返回可用的列表
//    sysPro.setPlat(plat);
//    sysPro.setVersion(version);
//    sysPro.setAppType(appType);
//    sysPros = (List<SysProperty>) memcacheTemplate.get(LISTSYSPROPETYBYKEY+sysPro.getKey()+"_"+sysPro.getPlat()+"_"+sysPro.getType()+"_"+sysPro.getVersion()+"_"+sysPro.getAppType());
//    if(sysPros == null) {
//      sysPros = sysPropertyDao.listSysProperty(sysPro);
//      if(sysPros != null){
//        memcacheTemplate.set(LISTSYSPROPETYBYKEY+sysPro.getKey()+"_"+sysPro.getPlat()+"_"+sysPro.getType()+"_"+sysPro.getVersion()+"_"+sysPro.getAppType(), sysPros, 0);
//      }
//    }
//    return sysPros;
//  }
//
//  @SuppressWarnings("unchecked")
//  @Override
//  public List<SysProperty> restListSysProperty(SysProperty sysPro) throws Exception {
//    if(sysPro == null) {
//      throw new VerifyException(Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.getValue(), Constants.CUSTOM_ERROR_CODE.PARAMATER_MISSING.toString() + "_sysProperty");
//    }
//    List<SysProperty> info = null;
//    if(StringUtils.isBlank(sysPro.getKey()) && StringUtils.isBlank(sysPro.getChannel())){
//      info = (List<SysProperty>) memcacheTemplate.get(LISTSYSPROPERTY+sysPro.getPlat()+"_"+sysPro.getStatus()+"_"+sysPro.getType()+"_"+sysPro.getVersion()+"_"+sysPro.getAppType());
//      if(info == null) {
//        info = sysPropertyDao.listSysProperty(sysPro);
//        if(info != null){
//          memcacheTemplate.set(LISTSYSPROPERTY+sysPro.getPlat()+"_"+sysPro.getStatus()+"_"+sysPro.getType()+"_"+sysPro.getVersion()+"_"+sysPro.getAppType(), info, 0);
//        }
//      }
//    }else{
//      info = sysPropertyDao.listSysProperty(sysPro);
//    }
//    return info;
//  }
}
