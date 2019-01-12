package com.ego.dubbo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.dubbo.service.TbItemDubboService;
import com.ego.mapper.TbItemDescMapper;
import com.ego.mapper.TbItemMapper;
import com.ego.mapper.TbItemParamItemMapper;
import com.ego.pojo.TbItem;
import com.ego.pojo.TbItemDesc;
import com.ego.pojo.TbItemExample;
import com.ego.pojo.TbItemParamItem;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

public class TbItemDubboServiceImpl implements TbItemDubboService{

	@Resource
	private TbItemMapper tbItemMapper;
	@Resource
	private TbItemDescMapper tbItemDescMapper;
	@Resource
	private TbItemParamItemMapper tbItemParamItemMapper;
	
	@Override
	public EasyUIDataGrid show(int page, int rows) {
		PageHelper.startPage(page, rows);
		
		//查询全部商品
		List<TbItem> list=tbItemMapper.selectByExample(new TbItemExample());
		
		//分页代码
		//设置分页条件
		PageInfo<TbItem> pi=new PageInfo<TbItem>(list);
		
		//将结果集放入到返回对象实体类中
		EasyUIDataGrid datagrid=new EasyUIDataGrid();
		datagrid.setRows(pi.getList());
		datagrid.setTotle(pi.getTotal());
		
		return datagrid;
	}

	@Override
	public int updItem(TbItem tbItem) {
		return tbItemMapper.updateByPrimaryKeySelective(tbItem);
	}

	@Override
	public int insTbItem(TbItem tbItem) {
		return tbItemMapper.insert(tbItem);
	}

	@Override
	public int insTbItemDesc(TbItem tbItem, TbItemDesc tbItemDesc,TbItemParamItem paramItem)  {
		int index=0;
		try {
			index=tbItemMapper.insert(tbItem);
			index+=tbItemDescMapper.insert(tbItemDesc);
			index+=tbItemParamItemMapper.insertSelective(paramItem);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(index==3){
			return 1;
		}else{
			try {
				throw new Exception("新增失败");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	@Override
	public List<TbItem> selAllByStatus(byte status) {
		TbItemExample example=new TbItemExample();
		example.createCriteria().andStatusEqualTo(status);
		return tbItemMapper.selectByExample(example);
	}

	@Override
	public TbItem selById(long id) {
		return tbItemMapper.selectByPrimaryKey(id);
	}

	
}
