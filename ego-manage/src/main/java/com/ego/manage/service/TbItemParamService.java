package com.ego.manage.service;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.commons.pojo.EgoResult;
import com.ego.pojo.TbItemParam;

public interface TbItemParamService {
	EasyUIDataGrid showPage(int page,int rows);
	
	//批量删除
	int delete(String ids) throws Exception;
	
	//根据类目id查询模板信息
	EgoResult showParam(long catid);
	
	EgoResult save(TbItemParam param);
}
