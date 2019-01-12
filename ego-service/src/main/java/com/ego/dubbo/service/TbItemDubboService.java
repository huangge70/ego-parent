package com.ego.dubbo.service;

import java.util.List;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.pojo.TbItem;
import com.ego.pojo.TbItemDesc;
import com.ego.pojo.TbItemParamItem;

public interface TbItemDubboService {
	//��Ʒ�ķ�ҳ��ѯ
	EasyUIDataGrid show(int page,int rows);
	
	//����id�޸���Ʒ��״̬
	//int updItemStatus(TbItem tbItem);
	
	int updItem(TbItem tbItem);
	
	//��Ʒ����
	int insTbItem(TbItem tbItem);
	
	//��������Ʒ�����Ʒ�����������
	int insTbItemDesc(TbItem tbItem,TbItemDesc tbItemDesc,TbItemParamItem paramItem);
	
	//����״̬��ѯȫ����������
	List<TbItem> selAllByStatus(byte status);
	
	//����������ѯ
	TbItem selById(long id);
}
