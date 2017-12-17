package com.taotao.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
/**
 * 判断用户登录拦截器
 * @author Administrator
 *
 */
public class LoginInterceptor implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		//执行handler先执行此方法
		
		//1. 从cookie 中取token 信息
		//2. 如果取不到token, 跳转sso登录页面. 需要把当前请求的url作为参数传递给sso, sso登录成功后跳转回请求的页面
		
		//3. 取到token, 调用sso 服务, 判断用户是否登录. 
		//4. 用户未登录, 即没取到用户信息, 跳转到sso登录页面
		//5. 取到用户信息, 放行
		
		//返回值 返回true, 放行. 返回false, 拦截
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		//handler执行之后, modeAndView 返回之前
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
		//ModeAndView 返回之后
		
	}

}
