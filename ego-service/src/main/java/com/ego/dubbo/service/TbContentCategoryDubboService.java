package com.ego.dubbo.service;

import java.util.List;

import com.ego.pojo.TbContentCategory;

public interface TbContentCategoryDubboService {
	//���ݸ�id��ѯ��������Ŀ
	List<TbContentCategory> selByPid(long id);
	
	int insTbContentCategory(TbContentCategory cate);
	
	//�޸���Ŀ��isparent����
	int updIsParentById(TbContentCategory cate);
	
	//ͨ��id��ѯ��Ŀ����ϸ��Ϣ
	TbContentCategory selById(long id);
}
