package com.ego.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ego.commons.pojo.EgoResult;
import com.ego.commons.utils.CookieUtils;
import com.ego.commons.utils.HttpClientUtil;
import com.ego.commons.utils.JsonUtils;

public class LoginInterceptor implements HandlerInterceptor{

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//判断用户是否已经登陆
		//获取用户cookie中的token信息
		String token=CookieUtils.getCookieValue(request, "TT_TOKEN");
		//使用httpclient技术调用ego-passport中的控制器，取出用户的信息
		if(token!=null&&!token.equals("")){
			String json=HttpClientUtil.doPost("http://localhost:8084/user/token/"+token);
			EgoResult er=JsonUtils.jsonToPojo(json, EgoResult.class);
			if(er.getStatus()==200){
				return true;
			}
		}
		String num=request.getParameter("num");
		if(num!=null&&!num.equals("")){
			//用户未登陆，跳转到登陆页面
			//request.getRequestURL():取出被拦截的url
			//%3F:?号的转义字符
			response.sendRedirect("http://localhost:8084/user/showLogin?interurl="+request.getRequestURL()+"%3Fnum="+num);
		}else{
			response.sendRedirect("http://localhost:8084/user/showLogin");
		}
		
		return false;
	}

}
