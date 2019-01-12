package com.ego.cart.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.cart.service.CartService;
import com.ego.commons.pojo.EgoResult;
import com.ego.commons.pojo.TbItemChild;
import com.ego.commons.utils.CookieUtils;
import com.ego.commons.utils.HttpClientUtil;
import com.ego.commons.utils.JsonUtils;
import com.ego.dubbo.service.TbItemDubboService;
import com.ego.pojo.TbItem;
import com.ego.pojo.TbUser;
import com.ego.redis.dao.JedisDao;

@Service
public class CartServiceImpl implements CartService{

	@Resource
	private JedisDao jedisDaoImpl;
	@Reference
	private TbItemDubboService tbItemDubboServiceImpl;
	@Value("${passport.url}")
	private String passportUrl;
	@Value("${cart.key}")
	private String cartKey;
	
	@Override
	public void addCart(long id, int num,HttpServletRequest request) {
		List<TbItemChild> list=new ArrayList<TbItemChild>();;
		
		//拿到当前登陆到系统中的用户的token
		String token=CookieUtils.getCookieValue(request, "TT_TOKEN");
		//根据用户token，去ego-passport项目中的控制器取出用户的信息(要使用httpclient技术)
		String jsonUser=HttpClientUtil.doPost(passportUrl+token);
		//将从redis中取出的用户json数据转换为对象
		EgoResult er=JsonUtils.jsonToPojo(jsonUser, EgoResult.class);
		//redis中存储的用户购物车信息的键值为: cart:用户名
		//设置redis中存储的key的值
		String key=cartKey+((LinkedHashMap)er.getData()).get("username");
		
		boolean isExists=false;
		
		//如果redis中存在用户的购物车缓存
		if(jedisDaoImpl.exists(key)){
			String json=jedisDaoImpl.get(key);
			if(json!=null&&!json.equals("")){
				//取出用户购物车中的缓存数据
				list=JsonUtils.jsonToList(json, TbItemChild.class);
				//判断用户购物车缓存中有没有当前商品，有的话直接增加数量就可以
				for(TbItemChild tbItemChild:list){
					if((long)tbItemChild.getId()==id){
						//用户购物车中存在该商品
						isExists=true;
						tbItemChild.setNum(tbItemChild.getNum()+num);
						//把用户购物车重新添加到redis中
						jedisDaoImpl.set(key, JsonUtils.objectToJson(list));
						return;
					}
				}
			}
		}
		//redis缓存中没有用户的购物车信息
		
		//购物车中没有该商品
		TbItem item=tbItemDubboServiceImpl.selById(id);
		TbItemChild child=new TbItemChild();
		
		child.setId(item.getId());
		child.setTitle(item.getTitle());
		child.setImages(item.getImage()==null||item.getImage().equals("")?new String[1]:item.getImage().split(","));
		child.setNum(num);
		child.setPrice(item.getPrice());
		list.add(child);
		
		jedisDaoImpl.set(key, JsonUtils.objectToJson(list));
	}

	@Override
	public List<TbItemChild> showCart(HttpServletRequest request) {
		//拿到当前登陆到系统中的用户的token
		String token=CookieUtils.getCookieValue(request, "TT_TOKEN");
		//根据用户token，去ego-passport项目中的控制器取出用户的信息(要使用httpclient技术)
		String jsonUser=HttpClientUtil.doPost(passportUrl+token);
		//将从redis中取出的用户json数据转换为对象
		EgoResult er=JsonUtils.jsonToPojo(jsonUser, EgoResult.class);
		//redis中存储的用户购物车信息的键值为: cart:用户名
		//设置redis中存储的key的值
		String key=cartKey+((LinkedHashMap)er.getData()).get("username");
		String json=jedisDaoImpl.get(key);
		return JsonUtils.jsonToList(json, TbItemChild.class);
	}

	@Override
	public EgoResult update(long id, int num,HttpServletRequest request) {
		//拿到当前登陆到系统中的用户的token
		String token=CookieUtils.getCookieValue(request, "TT_TOKEN");
		//根据用户token，去ego-passport项目中的控制器取出用户的信息(要使用httpclient技术)
		String jsonUser=HttpClientUtil.doPost(passportUrl+token);
		//将从redis中取出的用户json数据转换为对象
		EgoResult er=JsonUtils.jsonToPojo(jsonUser, EgoResult.class);
		//redis中存储的用户购物车信息的键值为: cart:用户名
		//设置redis中存储的key的值
		String key=cartKey+((LinkedHashMap)er.getData()).get("username");
		String json=jedisDaoImpl.get(key);
		List<TbItemChild> list=JsonUtils.jsonToList(json, TbItemChild.class);
		for(TbItemChild child:list){
			if((long)child.getId()==id){
				child.setNum(num);
			}
		}
		String ok=jedisDaoImpl.set(key, JsonUtils.objectToJson(list));
		EgoResult egoResult=new EgoResult();
		if(ok.equals("OK")){
			egoResult.setStatus(200);
		}
		return egoResult;
	}

	@Override
	public EgoResult delete(long id, HttpServletRequest request) {
		//拿到当前登陆到系统中的用户的token
		String token=CookieUtils.getCookieValue(request, "TT_TOKEN");
		//根据用户token，去ego-passport项目中的控制器取出用户的信息(要使用httpclient技术)
		String jsonUser=HttpClientUtil.doPost(passportUrl+token);
		//将从redis中取出的用户json数据转换为对象
		EgoResult er=JsonUtils.jsonToPojo(jsonUser, EgoResult.class);
		//redis中存储的用户购物车信息的键值为: cart:用户名
		//设置redis中存储的key的值
		String key=cartKey+((LinkedHashMap)er.getData()).get("username");
		String json=jedisDaoImpl.get(key);
		List<TbItemChild> list=JsonUtils.jsonToList(json, TbItemChild.class);
		TbItemChild tbItemChild=null;
		for(TbItemChild child:list){
			if((long)child.getId()==id){
				tbItemChild=child;
			}
		}
		list.remove(tbItemChild);
		String ok=jedisDaoImpl.set(key, JsonUtils.objectToJson(list));
		EgoResult egoResult=new EgoResult();
		if(ok.equals("OK")){
			egoResult.setStatus(200);
		}
		return egoResult;
	}
	
}
