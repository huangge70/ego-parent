package com.ego.manage.service;

import java.util.List;

import com.ego.commons.pojo.EasyUiTree;
import com.ego.commons.pojo.EgoResult;
import com.ego.pojo.TbContentCategory;

public interface TbContentCategoryService {
	//查询所有类目，并以tree的形式展示
	List<EasyUiTree> showCategory(long id);
	
	//类目新增
	EgoResult create(TbContentCategory cate);
	
	//类目重命名
	EgoResult update(TbContentCategory cate);
	
	//删除类目
	EgoResult delete(TbContentCategory cate);
}
