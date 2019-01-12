package com.ego.dubbo.service;

import java.util.List;

import com.ego.pojo.TbItemCat;

public interface TbItemCatDubboService {
	//根据父类目id查询子类目
	List<TbItemCat> show(long pid);
	
	//根据id查询类目
	TbItemCat selById(long id);
	
}
