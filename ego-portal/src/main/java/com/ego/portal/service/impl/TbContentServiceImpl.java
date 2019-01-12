package com.ego.portal.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.commons.utils.JsonUtils;
import com.ego.dubbo.service.TbContentDubboService;
import com.ego.pojo.TbContent;
import com.ego.portal.service.TbContentService;
import com.ego.redis.dao.JedisDao;
@Service
public class TbContentServiceImpl implements TbContentService{

	@Reference
	private TbContentDubboService tbContentDubboServiceImpl;
	@Resource
	private JedisDao JedisDaoImpl;
	@Value("${redis.bigpic.key}")
	private String key;
	@Override
	public String showBigPic() {
		//先判断在redis中是否存在
		//如果存在
		if(JedisDaoImpl.exists(key)){
			String value=JedisDaoImpl.get(key);
			if(value!=null&&!value.equals("")){
				return value;
			}
		}
		
		//如果不存在,从数据库中取出数据
		List<TbContent> list=tbContentDubboServiceImpl.selByCount(6, true);
		List<Map<String, Object>> listResult=new ArrayList<Map<String,Object>>();
		for(TbContent tc:list){
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("srcB", tc.getPic2());
			map.put("height", 240);
			map.put("alt", "加载图片失败");
			map.put("width", 670);
			map.put("src", tc.getPic());
			map.put("widthB", 550);
			map.put("href", tc.getUrl());
			map.put("heightB", 240);
			listResult.add(map);
		}
		String listJson=JsonUtils.objectToJson(listResult);
		//把从数据库中查出的数据存入redis中
		JedisDaoImpl.set(key, listJson);
		return listJson;
	}

}
