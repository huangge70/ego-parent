package com.ego.item.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.dubbo.service.TbItemCatDubboService;
import com.ego.item.pojo.PortalMenu;
import com.ego.item.pojo.PortalMenuNode;
import com.ego.item.service.TbItemCatService;
import com.ego.pojo.TbItemCat;

@Service
public class TbItemCatServiceImpl implements TbItemCatService{
	@Reference
	private TbItemCatDubboService tbItemCatDubboServiceImpl;
	@Override
	public PortalMenu show() {
		//查询出所有的一级菜单
		List<TbItemCat> list=tbItemCatDubboServiceImpl.show(0);
		PortalMenu portalMenu=new PortalMenu();
		portalMenu.setData(selAllMenu(list));
		return portalMenu;
	}
	public List<Object> selAllMenu(List<TbItemCat> list){
		List<Object> lisNode=new ArrayList<>();
		for(TbItemCat tbItemCat:list){
			if(tbItemCat.getIsParent()){//如果当前菜单为父菜单的时候
				PortalMenuNode pmd=new PortalMenuNode();
				pmd.setU("/products/"+tbItemCat.getId()+".html");
				pmd.setN("<a href='/products/"+tbItemCat.getId()+".html'>"+tbItemCat.getName()+"</a>");
				pmd.setI(selAllMenu(tbItemCatDubboServiceImpl.show(tbItemCat.getId())));
				lisNode.add(pmd);
			}else{
				lisNode.add("/products/"+tbItemCat.getId()+".html|"+tbItemCat.getName());
			}
		}
		return lisNode;
	}
}
