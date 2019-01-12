package com.ego.dubbo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.dubbo.service.TbItemParamDubboService;
import com.ego.mapper.TbItemParamMapper;
import com.ego.pojo.TbItemParam;
import com.ego.pojo.TbItemParamExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

public class TbItemParamDubboServiceImpl implements TbItemParamDubboService{
	@Resource
	private TbItemParamMapper tbItemParamMapper;
	@Override
	public EasyUIDataGrid showPage(int page, int rows) {
		//先设置分页条件
		PageHelper.startPage(page, rows);
		//查询全部
		//xxxExample(),其中设置了什么，就相当与在sql中的where从句中添加了条件
		List<TbItemParam> list= tbItemParamMapper.selectByExampleWithBLOBs(new TbItemParamExample());
		//产生分页结果
		PageInfo<TbItemParam> pi=new PageInfo<>(list);
		//设置返回结果
		EasyUIDataGrid dataGrid=new EasyUIDataGrid();
		dataGrid.setRows(pi.getList());
		dataGrid.setTotle(pi.getTotal());
		return dataGrid;
	}
	@Override
	public int delByIds(String ids) throws Exception {
		String[] idStr=ids.split(",");
		int index=0;
		for(String id:idStr){
			index+=tbItemParamMapper.deleteByPrimaryKey(Long.parseLong(id));
		}
		if(index==idStr.length){
			return 1;
		}else{
			throw new Exception("删除失败！");
		}
	}
	@Override
	public TbItemParam selByCatID(long catid) {
		TbItemParamExample example=new TbItemParamExample();
		example.createCriteria().andItemCatIdEqualTo(catid);
		List<TbItemParam> list=tbItemParamMapper.selectByExampleWithBLOBs(example);
		if(list!=null&&list.size()>0){
			//要求每个类目只能有一个模板
			return list.get(0);
		}
		return null;
	}
	@Override
	public int insParam(TbItemParam param) {
		return tbItemParamMapper.insertSelective(param);
	}

}
