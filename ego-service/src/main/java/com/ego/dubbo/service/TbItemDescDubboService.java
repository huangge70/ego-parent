package com.ego.dubbo.service;

import com.ego.pojo.TbItemDesc;

public interface TbItemDescDubboService {
	int insDesc(TbItemDesc itemDesc);
	
	//����������ѯ��Ʒ��������
	TbItemDesc selByItemid(long itemid);
}
