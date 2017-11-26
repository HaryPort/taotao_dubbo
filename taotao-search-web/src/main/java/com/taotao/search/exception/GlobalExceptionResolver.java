package com.taotao.search.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;


public class GlobalExceptionResolver implements HandlerExceptionResolver {
	
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionResolver.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		//控制台打印异常
		ex.printStackTrace();
		
		//向日志文件写入异常
		logger.error("系统发生异常", ex);
		
		//展示错误页面
		ModelAndView mav = new ModelAndView();
		mav.addObject("message", "系统出现问题, 请稍候重试");
		mav.setViewName("error/exception");
		return mav;
	}

}
