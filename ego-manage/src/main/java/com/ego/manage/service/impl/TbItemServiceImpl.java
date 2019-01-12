package com.ego.manage.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.commons.utils.HttpClientUtil;
import com.ego.commons.utils.IDUtils;
import com.ego.commons.utils.JsonUtils;
import com.ego.dubbo.service.TbItemDescDubboService;
import com.ego.dubbo.service.TbItemDubboService;
import com.ego.manage.service.TbItemService;
import com.ego.pojo.TbItem;
import com.ego.pojo.TbItemDesc;
import com.ego.pojo.TbItemParamItem;
import com.ego.redis.dao.JedisDao;

@Service
public class TbItemServiceImpl implements TbItemService{
	//调用dubbo中提供的服务
	@Reference
	private TbItemDubboService tbItemDubboServiceImpl;
	@Reference
	private TbItemDescDubboService tbItemDescDubboService;
	@Value("${search.url}")
	private String url;
	@Resource
	private JedisDao jedisDaoImpl;
	@Value("${redis.item.key}")
	private String itemKey;
	@Override
	public EasyUIDataGrid show(int page, int rows) {
		return tbItemDubboServiceImpl.show(page, rows);
	}
	@Override
	public int update(String ids, byte status) {
		int index=0;
		TbItem item=new TbItem();
		String[] idsStr=ids.split(",");
		for(String id:idsStr){
			item.setId(Long.parseLong(id));
			item.setStatus(status);
			index+=tbItemDubboServiceImpl.updItem(item);
			
			//当商品下架或者删除的时候，我们要在redis缓存中删除相应的数据
			if(status==2||status==3){
				jedisDaoImpl.del(itemKey+id);
			}
		}
		if(index==idsStr.length)
			return 1;
		
		
		
		return 0;
	}
	@Override
	public int save(TbItem item, String desc,String itemParams) {
		//不考虑事务回滚
//		long id=IDUtils.genItemId();
//		item.setId(id);
//		Date date=new Date();
//		item.setCreated(date);
//		item.setUpdated(date);
//		item.setStatus((byte)1);
//		int index=tbItemDubboServiceImpl.insTbItem(item);
//		if(index>0){
//			TbItemDesc itemDesc=new TbItemDesc();
//			itemDesc.setItemDesc(desc);
//			itemDesc.setItemId(id);
//			itemDesc.setCreated(date);
//			itemDesc.setUpdated(date);
//			index+=tbItemDescDubboService.insDesc(itemDesc);
//		}
//		if(index==2){
//			return 1;
//		}
		//考虑事务回滚
		long id=IDUtils.genItemId();
		item.setId(id);
		Date date=new Date();
		item.setCreated(date);
		item.setUpdated(date);
		item.setStatus((byte)1);
		
		TbItemDesc itemDesc=new TbItemDesc();
		itemDesc.setItemDesc(desc);
		itemDesc.setItemId(id);
		itemDesc.setCreated(date);
		itemDesc.setUpdated(date);
		
		TbItemParamItem paramItem=new TbItemParamItem();
		paramItem.setCreated(date);
		paramItem.setUpdated(date);
		paramItem.setId(id);
		paramItem.setParamData(itemParams);
		
		int index=tbItemDubboServiceImpl.insTbItemDesc(item, itemDesc,paramItem);
		
		//使用java代码调用其他项目的控制器,借助于httpclient实现
		Map<String, Object> map=new HashMap<>();
		map.put("item", item);
		map.put("desc", desc);
		HttpClientUtil.doPostJson(url, JsonUtils.objectToJson(item));
		
		
		return index;
	}
	
}
