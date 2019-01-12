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
		//�����÷�ҳ����
		PageHelper.startPage(page, rows);
		//��ѯȫ��
		//xxxExample(),����������ʲô�����൱����sql�е�where�Ӿ������������
		List<TbItemParam> list= tbItemParamMapper.selectByExampleWithBLOBs(new TbItemParamExample());
		//������ҳ���
		PageInfo<TbItemParam> pi=new PageInfo<>(list);
		//���÷��ؽ��
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
			throw new Exception("ɾ��ʧ�ܣ�");
		}
	}
	@Override
	public TbItemParam selByCatID(long catid) {
		TbItemParamExample example=new TbItemParamExample();
		example.createCriteria().andItemCatIdEqualTo(catid);
		List<TbItemParam> list=tbItemParamMapper.selectByExampleWithBLOBs(example);
		if(list!=null&&list.size()>0){
			//Ҫ��ÿ����Ŀֻ����һ��ģ��
			return list.get(0);
		}
		return null;
	}
	@Override
	public int insParam(TbItemParam param) {
		return tbItemParamMapper.insertSelective(param);
	}

}
