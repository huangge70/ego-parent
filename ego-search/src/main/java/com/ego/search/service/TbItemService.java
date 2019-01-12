package com.ego.search.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServerException;

import com.ego.pojo.TbItem;

public interface TbItemService {
	//��ʼ��solr�е�����
	void init() throws SolrServerException, IOException;
	
	//��ҳ��ѯ
	Map<String, Object> selByQuery(String query,int page,int rows) throws SolrServerException, IOException;

	//����
	int add(Map<String, Object> map,String desc) throws SolrServerException, IOException;
}
