package com.ego.manage.service;

import java.util.List;

import com.ego.commons.pojo.EasyUiTree;
import com.ego.commons.pojo.EgoResult;
import com.ego.pojo.TbContentCategory;

public interface TbContentCategoryService {
	//��ѯ������Ŀ������tree����ʽչʾ
	List<EasyUiTree> showCategory(long id);
	
	//��Ŀ����
	EgoResult create(TbContentCategory cate);
	
	//��Ŀ������
	EgoResult update(TbContentCategory cate);
	
	//ɾ����Ŀ
	EgoResult delete(TbContentCategory cate);
}
