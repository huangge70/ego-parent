package com.ego.manage.service;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.commons.pojo.EgoResult;
import com.ego.pojo.TbItemParam;

public interface TbItemParamService {
	EasyUIDataGrid showPage(int page,int rows);
	
	//����ɾ��
	int delete(String ids) throws Exception;
	
	//������Ŀid��ѯģ����Ϣ
	EgoResult showParam(long catid);
	
	EgoResult save(TbItemParam param);
}
