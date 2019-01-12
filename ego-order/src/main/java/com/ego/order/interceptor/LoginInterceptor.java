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
		//�ж��û��Ƿ��Ѿ���½
		//��ȡ�û�cookie�е�token��Ϣ
		String token=CookieUtils.getCookieValue(request, "TT_TOKEN");
		//ʹ��httpclient��������ego-passport�еĿ�������ȡ���û�����Ϣ
		if(token!=null&&!token.equals("")){
			String json=HttpClientUtil.doPost("http://localhost:8084/user/token/"+token);
			EgoResult er=JsonUtils.jsonToPojo(json, EgoResult.class);
			if(er.getStatus()==200){
				return true;
			}
		}
		String num=request.getParameter("num");
		if(num!=null&&!num.equals("")){
			//�û�δ��½����ת����½ҳ��
			//request.getRequestURL():ȡ�������ص�url
			//%3F:?�ŵ�ת���ַ�
			response.sendRedirect("http://localhost:8084/user/showLogin?interurl="+request.getRequestURL()+"%3Fnum="+num);
		}else{
			response.sendRedirect("http://localhost:8084/user/showLogin");
		}
		
		return false;
	}

}
