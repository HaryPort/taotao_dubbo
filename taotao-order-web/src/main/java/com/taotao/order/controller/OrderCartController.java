package com.taotao.order.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbItem;

/**
 * 订单处理页面
 * @author Administrator
 *
 */
@Controller
public class OrderCartController {
	
	@Value("${TT_CART}")
	private String TT_CART;
	
	/*
	 *  展示订单确认页面
	 */
	@RequestMapping("/order/order-cart")
	public String showOderCart(HttpServletRequest request) {
		//用户必须是登录状态
		
		//取用户id
		
		//根据用户信息取收货地址列表, 使用静态数据模拟
		
		//把收货地址列表取出传给页面
		
		//从cookie 去购物车商品列表, 展示到页面
		List<TbItem> cartList = getCartItemList(request);
		request.setAttribute("cartList", cartList);
		
		//返回逻辑视图
		return "order-cart";
	}
	
	private List<TbItem> getCartItemList(HttpServletRequest request){
		//从cookie中取购物车商品列表
		String json = CookieUtils.getCookieValue(request, TT_CART, true);
		if(StringUtils.isNotBlank(json)) {
			return JsonUtils.jsonToList(json, TbItem.class);
		}
		
		return new ArrayList<TbItem>();
	}
	

}
