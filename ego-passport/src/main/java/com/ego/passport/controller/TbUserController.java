package com.ego.passport.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ego.commons.pojo.EgoResult;
import com.ego.passport.service.TbUserService;
import com.ego.pojo.TbUser;

@Controller
public class TbUserController {
	@Resource
	private TbUserService tbUserServiceImpl;
	
	@RequestMapping("user/showLogin")
	//@RequestHeader("Referer"):获取请求头中Referer中的数据，即来源url
	public String showLogin(@RequestHeader(value="Referer",defaultValue="") String url,Model model,String interurl){
		if(url.equals("")){
			model.addAttribute("redirect", interurl);
		}
		else if(!url.equals("")){
			model.addAttribute("redirect", url);
		}
		return "login";
	}
	
	@RequestMapping("user/login")
	@ResponseBody
	public EgoResult login(TbUser user,HttpServletRequest request,HttpServletResponse response){
		return tbUserServiceImpl.login(user,request,response);
	}
	
	//通过token获取用户信息
	@RequestMapping("user/tocken/{token}")
	@ResponseBody
	public Object getUserInfo(@PathVariable String token,String callback){
		EgoResult er=tbUserServiceImpl.getUserInfoByTocken(token);
		//callback不为空的时候，说明前端是以jsonp的形式请求的
		if(callback!=null&&!callback.equals("")){
			MappingJacksonValue mjv=new MappingJacksonValue(er);
			mjv.setJsonpFunction(callback);
			return mjv;
		}else{
			return er;
		}
	}
	
	@RequestMapping("user/logout/{token}")
	@ResponseBody
	public Object logout(@PathVariable String token,String callback,HttpServletRequest request,HttpServletResponse response){
		EgoResult er=tbUserServiceImpl.logout(token,request,response);
		//callback不为空的时候，说明前端是以jsonp的形式请求的
		if(callback!=null&&!callback.equals("")){
			MappingJacksonValue mjv=new MappingJacksonValue(er);
			mjv.setJsonpFunction(callback);
			return mjv;
		}else{
			return er;
		}
	}
}
