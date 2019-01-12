package com.ego.dubbo.service;

import java.util.List;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.pojo.TbItem;
import com.ego.pojo.TbItemDesc;
import com.ego.pojo.TbItemParamItem;

public interface TbItemDubboService {
	//商品的分页查询
	EasyUIDataGrid show(int page,int rows);
	
	//根据id修改商品的状态
	//int updItemStatus(TbItem tbItem);
	
	int updItem(TbItem tbItem);
	
	//商品新增
	int insTbItem(TbItem tbItem);
	
	//包含对商品表和商品描述表的新增
	int insTbItemDesc(TbItem tbItem,TbItemDesc tbItemDesc,TbItemParamItem paramItem);
	
	//根据状态查询全部可用数据
	List<TbItem> selAllByStatus(byte status);
	
	//根据主键查询
	TbItem selById(long id);
}
