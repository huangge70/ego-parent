package com.ego.passport.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ego.commons.pojo.EgoResult;
import com.ego.pojo.TbUser;

public interface TbUserService {
	EgoResult login(TbUser user,HttpServletRequest request,HttpServletResponse response);

	//����token��redis�в�ѯ�û���Ϣ
	EgoResult getUserInfoByTocken(String token);
	
	EgoResult logout(String token,HttpServletRequest request,HttpServletResponse response);
}
