package com.ego.item.service;

import org.springframework.stereotype.Service;

import com.ego.commons.pojo.TbItemChild;
import com.ego.pojo.TbItem;

public interface TbItemService {
	//显示商品详情
	TbItemChild show(long id);
}
