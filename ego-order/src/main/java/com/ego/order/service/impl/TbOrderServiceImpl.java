package com.ego.order.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.commons.pojo.EgoResult;
import com.ego.commons.pojo.TbItemChild;
import com.ego.commons.utils.CookieUtils;
import com.ego.commons.utils.HttpClientUtil;
import com.ego.commons.utils.IDUtils;
import com.ego.commons.utils.JsonUtils;
import com.ego.dubbo.service.TbItemDubboService;
import com.ego.dubbo.service.TbOrderDubboService;
import com.ego.order.pojo.MyOrderParam;
import com.ego.order.service.TbOrderService;
import com.ego.pojo.TbItem;
import com.ego.pojo.TbOrder;
import com.ego.pojo.TbOrderItem;
import com.ego.pojo.TbOrderShipping;
import com.ego.pojo.TbUser;
import com.ego.redis.dao.JedisDao;
@Service
public class TbOrderServiceImpl implements TbOrderService{
	@Resource
	private JedisDao jedisDaoImpl;
	@Value("${cart.key}")
	private String cartKey;
	@Value("${passport.url}")
	private String passportUrl;
	@Reference
	private TbItemDubboService tbItemDubboServiceImpl;
	@Reference
	private TbOrderDubboService tbOrderDubboServiceImpl;
	
	@Override
	public List<TbItemChild> showOrderCart(List<Long> ids, HttpServletRequest request) {
		String token=CookieUtils.getCookieValue(request, "TT_TOKEN");
		String result=HttpClientUtil.doPost(passportUrl+token);
		EgoResult er=JsonUtils.jsonToPojo(result, EgoResult.class);
		String key=cartKey+((LinkedHashMap)er.getData()).get("username");
		String json=jedisDaoImpl.get(key);
		List<TbItemChild> list=JsonUtils.jsonToList(json, TbItemChild.class);
		List<TbItemChild> listNew=new ArrayList<TbItemChild>();
		for(TbItemChild child:list){
			for(Long id:ids){
				if((long)child.getId()==(long)id){
					//判断购买量是否大于库存量
					TbItem tbItem=tbItemDubboServiceImpl.selById(id);
					if(tbItem.getNum()>=child.getNum()){
						child.setEnough(true);
					}else {
						child.setEnough(false);
					}
					listNew.add(child);
				}
			}
		}
		return listNew;
	}

	@Override
	public EgoResult create(MyOrderParam param,HttpServletRequest request) {
		//组装订单数据
		TbOrder order=new TbOrder();
		order.setPayment(param.getPayment());
		order.setPaymentType(param.getPaymentType());
		//生成一个id
		long id=IDUtils.genItemId();
		order.setOrderId(id+"");
		Date date=new Date();
		order.setCreateTime(date);
		order.setUpdateTime(date);
		//取出用户id
		String token=CookieUtils.getCookieValue(request, "TT_TOKEN");
		String result=HttpClientUtil.doPost(passportUrl+token);
		EgoResult er=JsonUtils.jsonToPojo(result, EgoResult.class);
		TbUser user=(TbUser) er.getData();
		order.setUserId(user.getId());
		order.setBuyerNick(user.getUsername());
		order.setBuyerRate(0);
		
		//设置订单详情表
		for(TbOrderItem item:param.getOrderItems()){
			item.setId(IDUtils.genItemId()+"");
			item.setOrderId(id+"");
		}
		
		//设置收货人信息
		TbOrderShipping shipping=param.getOrderShipping();
		shipping.setOrderId(id+"");
		shipping.setCreated(date);
		shipping.setUpdated(date);
		
		EgoResult erResult=new EgoResult();
		try {
			int index=tbOrderDubboServiceImpl.insOrder(order, param.getOrderItems(), shipping);
			if(index>0){
				erResult.setStatus(200);
				
				//用户购买成功之后，删除redis中存储的用户的购物车信息
				String json=jedisDaoImpl.get(cartKey+user.getUsername());
				//从redis中取出用户的购物车信息
				List<TbItemChild> listCart=JsonUtils.jsonToList(json, TbItemChild.class);
				
				List<TbItemChild> listNew=new ArrayList<TbItemChild>();
				
				//遍历购物车，把用户已经结账的商品删除
				for(TbItemChild child:listCart){
					for(TbOrderItem item:param.getOrderItems()){
						if((long)child.getId()==Long.parseLong(item.getItemId())){
							listNew.add(child);
						}
					}
				}
				for(TbItemChild mynew:listNew){
					listCart.remove(mynew);
				}
				//删除完已经结账的商品将购物车再存回redis中
				jedisDaoImpl.set(cartKey+user.getUsername(), JsonUtils.objectToJson(listCart));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return erResult;
	}

}
