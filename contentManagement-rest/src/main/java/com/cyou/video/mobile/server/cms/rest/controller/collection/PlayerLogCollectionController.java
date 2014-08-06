package com.cyou.video.mobile.server.cms.rest.controller.collection;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cyou.video.mobile.server.cms.model.collection.PlayerLogCollection;
import com.cyou.video.mobile.server.cms.service.collection.PlayerLogCollectionService;
import com.cyou.video.mobile.server.cms.service.push.PushTagService;

/**
 * 数据收集 and pv
 * 
 * @author duxiaona
 * 
 */
@Controller
@RequestMapping("/rest/playerlog")
public class PlayerLogCollectionController {

	private Logger LOGGER = LoggerFactory
			.getLogger(PlayerLogCollectionController.class);

	@Autowired
	PlayerLogCollectionService playerLogCollectionService;

	@Autowired
	PushTagService pushTagService;

	// private String AdvCode=playerLogCollectionService.getAdvCode();

	/**
	 * 数据收集接口直接存入mongo
	 * 
	 * @param params
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/loginfo", method = RequestMethod.POST)
	@ResponseBody
	public ModelMap logInfo(
			@RequestBody Map<String, ArrayList<LinkedHashMap<String, String>>> params,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		if (params == null) {
			LOGGER.error("params is null ");
			response.setStatus(HttpServletResponse.SC_OK);
			return model;
		}
		List<PlayerLogCollection> result = new ArrayList<PlayerLogCollection>();

		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);

		ArrayList<LinkedHashMap<String, String>> playerLogCollections = params
				.get("logs");
		for (int i = 0; i < playerLogCollections.size(); i++) {
			try {
				PlayerLogCollection collection = new PlayerLogCollection();
				LinkedHashMap<String, String> obj = playerLogCollections.get(i);
				// 关键数据验证
				if (obj.get("appid") == null)
					continue;
				if (obj.get("type") == null
						|| (!"pv".equals(obj.get("type")) && !"av".equals(obj
								.get("type"))))
					continue;
				try {
					Long.parseLong(obj.get("playtime"));
				} catch (Exception e) {
					continue;
				}
				// 收集数据
				collection.setAppid(obj.get("appid"));
				collection.setAppname(obj.get("appname") == null ? "" : obj
						.get("appname"));
				collection.setType(obj.get("type") == null ? "" : obj
						.get("type"));
				collection.setUid(obj.get("uid") == null ? "" : obj.get("uid"));
				collection.setUa(obj.get("ua") == null ? "" : obj.get("ua"));
				collection.setPlaytime(obj.get("playtime") == null ? "" : obj
						.get("playtime"));
				collection.setDuration(obj.get("duration") == null ? "" : obj
						.get("duration"));
				collection.setSrc(obj.get("src") == null ? "" : obj.get("src"));
				collection.setBr(obj.get("br") == null ? "" : obj.get("br"));
				collection.setRes(obj.get("res") == null ? "" : obj.get("res"));
				collection.setFmt(obj.get("fmt") == null ? "" : obj.get("fmt"));
				collection.setVfmt(obj.get("vfmt") == null ? "" : obj
						.get("vfmt"));
				collection.setVpixfmt(obj.get("vpixfmt") == null ? "" : obj
						.get("vpixfmt"));
				collection.setVrefs(obj.get("vrefs") == null ? "" : obj
						.get("vrefs"));
				collection.setVprofile(obj.get("vprofile") == null ? "" : obj
						.get("vprofile"));
				collection.setLastUpdate(dateString);

				result.add(collection);
			} catch (Exception e) {
				// e.printStackTrace();
				LOGGER.error("PlayerLogCollection exception is "
						+ e.getMessage());
			}
		}
		try {
			if (result.size() == 0) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				return model;
			}
			playerLogCollectionService.collectLogInfo(result);
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (Exception e) {
			// e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_OK);
			LOGGER.error("collectLogInfo exception is " + e.getMessage());
		}
		return model;
	}

	/**
	 * 获取广告代码(待优化)
	 * 
	 * @param params
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getadv", method = RequestMethod.GET)
	@ResponseBody
	public String getAdv(@RequestParam("appid") String appid,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		// 获取广告代码
		String AdvCode = playerLogCollectionService.getAdvCode();
		// 根据appid查询广告开关是否打开，如果打开则获取对应广告代码，否则返回0,appid不存在返回2
		String advStatus = playerLogCollectionService.getAdvState(appid);
		if (!"1".equals(advStatus)) {
			AdvCode = advStatus;
		}
		return AdvCode;
	}

}
