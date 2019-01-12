package com.ego.passport.service.impl;

import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.commons.pojo.EgoResult;
import com.ego.commons.utils.CookieUtils;
import com.ego.commons.utils.JsonUtils;
import com.ego.dubbo.service.TbUserDubboService;
import com.ego.passport.service.TbUserService;
import com.ego.pojo.TbUser;
import com.ego.redis.dao.JedisDao;

@Service
public class TbUserServiceImpl implements TbUserService{
	@Reference
	private TbUserDubboService tbUserDubboServiceImpl;
	@Resource
	private JedisDao jedisDaoImpl;
	@Override
	public EgoResult login(TbUser user,HttpServletRequest request,HttpServletResponse response) {
		EgoResult er=new EgoResult();
		TbUser userSelect=tbUserDubboServiceImpl.selByUser(user);
		if(userSelect!=null){
			er.setStatus(200);
			//当用户登陆成功后把用户信息放入到redis中
			//产生一个uuid值，作为redis中存储对象的key
			String key=UUID.randomUUID().toString();
			jedisDaoImpl.set(key, JsonUtils.objectToJson(userSelect));
			//设置redis中存储数据的有效时间
			jedisDaoImpl.expire(key, 60*60*24*7);
			//产生cookie,并把redis中存储用户信息的key放入到cookie中
			CookieUtils.setCookie(request, response, "TT_TOKEN", key, 60*60*24*7);
		}else {
			er.setMsg("用户名或密码错误!");
		}
		return er;
	}
	@Override
	public EgoResult getUserInfoByTocken(String token) {
		EgoResult er=new EgoResult();
		//在redis中根据id取出用户信息
		String json=jedisDaoImpl.get(token);
		if(json!=null&&!json.equals("")){
			TbUser user=JsonUtils.jsonToPojo(json, TbUser.class);
			//为了安全性考虑，返回的用户信息可以清除密码
			user.setPassword(null);
			er.setStatus(200);
			er.setMsg("OK");
			er.setData(user);
		}else{
			er.setMsg("获取失败");
		}
		
		return er;
	}
	@Override
	public EgoResult logout(String token, HttpServletRequest request, HttpServletResponse response) {
		jedisDaoImpl.del(token);
		CookieUtils.deleteCookie(request, response, "TT_TOKEN");
		EgoResult er=new EgoResult();
		er.setStatus(200);
		er.setMsg("OK");
		return er;
	}

}
