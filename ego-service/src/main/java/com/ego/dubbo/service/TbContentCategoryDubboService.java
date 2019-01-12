package com.ego.dubbo.service;

import java.util.List;

import com.ego.pojo.TbContentCategory;

public interface TbContentCategoryDubboService {
	//根据父id查询所有子类目
	List<TbContentCategory> selByPid(long id);
	
	int insTbContentCategory(TbContentCategory cate);
	
	//修改类目的isparent属性
	int updIsParentById(TbContentCategory cate);
	
	//通过id查询类目的详细信息
	TbContentCategory selById(long id);
}
