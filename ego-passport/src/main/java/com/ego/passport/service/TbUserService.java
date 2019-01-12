package com.ego.passport.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ego.commons.pojo.EgoResult;
import com.ego.pojo.TbUser;

public interface TbUserService {
	EgoResult login(TbUser user,HttpServletRequest request,HttpServletResponse response);

	//根据token在redis中查询用户信息
	EgoResult getUserInfoByTocken(String token);
	
	EgoResult logout(String token,HttpServletRequest request,HttpServletResponse response);
}
