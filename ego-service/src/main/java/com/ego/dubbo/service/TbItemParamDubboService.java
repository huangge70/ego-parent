package com.ego.dubbo.service;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.pojo.TbItemParam;

public interface TbItemParamDubboService {
	EasyUIDataGrid showPage(int page,int rows);
	//批量删除
	int delByIds(String ids) throws Exception;
	
	//根据类目id查询参数模板  
	TbItemParam selByCatID(long catid);
	
	int insParam(TbItemParam param);
}
