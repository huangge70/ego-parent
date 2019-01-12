package com.ego.cart.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ego.commons.pojo.EgoResult;
import com.ego.commons.pojo.TbItemChild;

public interface CartService {
	//加入购物车
	void addCart(long id,int num,HttpServletRequest request);
	
	//显示购物车
	List<TbItemChild> showCart(HttpServletRequest request);
	
	//根据商品id修改商品在缓存中的数量
	EgoResult update(long id,int num,HttpServletRequest request);
	
	//删除购物车中的某样商品
	EgoResult delete(long id,HttpServletRequest request);
}
