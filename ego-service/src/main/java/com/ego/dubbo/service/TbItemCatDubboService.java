package com.ego.dubbo.service;

import java.util.List;

import com.ego.pojo.TbItemCat;

public interface TbItemCatDubboService {
	//���ݸ���Ŀid��ѯ����Ŀ
	List<TbItemCat> show(long pid);
	
	//����id��ѯ��Ŀ
	TbItemCat selById(long id);
	
}
