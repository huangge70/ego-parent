package com.ego.item.service;

import org.springframework.stereotype.Service;

import com.ego.commons.pojo.TbItemChild;
import com.ego.pojo.TbItem;

public interface TbItemService {
	//��ʾ��Ʒ����
	TbItemChild show(long id);
}
