package com.ego.cart.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ego.commons.pojo.EgoResult;
import com.ego.commons.pojo.TbItemChild;

public interface CartService {
	//���빺�ﳵ
	void addCart(long id,int num,HttpServletRequest request);
	
	//��ʾ���ﳵ
	List<TbItemChild> showCart(HttpServletRequest request);
	
	//������Ʒid�޸���Ʒ�ڻ����е�����
	EgoResult update(long id,int num,HttpServletRequest request);
	
	//ɾ�����ﳵ�е�ĳ����Ʒ
	EgoResult delete(long id,HttpServletRequest request);
}
