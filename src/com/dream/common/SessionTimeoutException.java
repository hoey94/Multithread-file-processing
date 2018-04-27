package com.dream.common;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 处理session超时的拦截器
 */
@Component
@SuppressWarnings("all")
public class SessionTimeoutException implements HandlerInterceptor, HandlerExceptionResolver {

	public String[] allowUrls;

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse arg1, Object arg2, Exception arg3) throws Exception {
		String requestUrl = request.getRequestURI().replace(request.getContextPath(), "");
		request.setCharacterEncoding("UTF-8");
		arg1.setCharacterEncoding("UTF-8");
		arg1.setContentType("text/html;charset=UTF-8");
		//System.out.println("------SessionTimeoutException------------" + requestUrl + "------------------");
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub
		arg0.setCharacterEncoding("UTF-8");
		arg1.setCharacterEncoding("UTF-8");
		arg1.setContentType("text/html;charset=UTF-8");
		//System.out.println("----SessionTimeoutException--------------postHandle------------------");
	}

	@Override
	public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2) throws Exception {
		// TODO Auto-generated method stub
		arg0.setCharacterEncoding("UTF-8");
		arg1.setCharacterEncoding("UTF-8");
		arg1.setContentType("text/html;charset=UTF-8");
		//System.out.println("-------------" + arg0.getRequestURI() + "----------------");
		//System.out.println("------------------preHandle------------------");
		try{
			Cookie[] cookes = arg0.getCookies();
			String sessionId = "";
			for(Cookie c:cookes){
				if(c.getName().equalsIgnoreCase("jsessionid")){
					sessionId = c.getValue().toString();
				}
			}
			/*if (arg0.getRequestURI().indexOf("/loginAction/loginAction.do") < 0) { 
				if (null == arg0.getSession().getAttribute("systemUser")) {
					System.out.println("------------------preHandle---------false---------");
					DataInit.removeUser(sessionId);
					return false;
				} 
			}*/
		//	 System.out.println("------------------preHandle-------true-----------");
		}catch(Exception e){
			
		}
		return true;
	}

	public String[] getAllowUrls() {
		return allowUrls;
	}

	public void setAllowUrls(String[] allowUrls) {
		this.allowUrls = allowUrls;
	}

	@Override
	public ModelAndView resolveException(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) {
		// TODO Auto-generated method stub
		arg3.printStackTrace();
		//System.out.println("---------SessionTimeoutException---------resolveException------------------");
		return new ModelAndView("/error");
	}

}
