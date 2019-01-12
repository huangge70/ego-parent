package com.ego.manage.service;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.pojo.TbContent;

public interface TbContentService {
	//分页显示
	EasyUIDataGrid showContent(long categoryId,int page,int rows);
	
	//新增
	int save(TbContent content);
}
