package com.ego.manage.service;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.pojo.TbContent;

public interface TbContentService {
	//��ҳ��ʾ
	EasyUIDataGrid showContent(long categoryId,int page,int rows);
	
	//����
	int save(TbContent content);
}
