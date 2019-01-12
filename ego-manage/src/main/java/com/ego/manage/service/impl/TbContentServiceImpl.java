package com.ego.manage.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.commons.utils.JsonUtils;
import com.ego.dubbo.service.TbContentDubboService;
import com.ego.manage.service.TbContentService;
import com.ego.pojo.TbContent;
import com.ego.redis.dao.JedisDao;

@Service
public class TbContentServiceImpl implements TbContentService{

	@Reference
	private TbContentDubboService tbContentDubboServiceImpl;
	@Resource
	private JedisDao jedisDaoImpl;
	@Value("${redis.bigpic.key}")
	private String key;
	@Override
	public EasyUIDataGrid showContent(long categoryId, int page, int rows) {
		return tbContentDubboServiceImpl.selContentByPage(categoryId, page, rows);
	}
	@Override
	public int save(TbContent content) {
		Date date=new Date();
		content.setCreated(date);
		content.setUpdated(date);
		int index=tbContentDubboServiceImpl.insContent(content);
		//�ж�redis���Ƿ��л�������
		if(jedisDaoImpl.exists(key)){
			//��redis�е�����ȡ��
			String value=jedisDaoImpl.get(key);
			if(value!=null&&!value.equals("")){
				//��redis�д洢���ַ�������ת����ԭ���Ķ�������
				List<HashMap> list=JsonUtils.jsonToList(value, HashMap.class);
				
                HashMap<String,Object> map = new HashMap<>();
				map.put("srcB", content.getPic2());
				map.put("height", 240);
				map.put("alt", "����ͼƬʧ��");
				map.put("width", 670);
				map.put("src", content.getPic());
				map.put("widthB", 550);
				map.put("href", content.getUrl() );
				map.put("heightB", 240);
				
				//list�������6������
				if(list.size()==6){
					list.remove(5);
				}
				//�������ӵĶ�����뼯����,���ҷ�����ǰ��
				list.add(0,map);
				
				//������ת�����ַ����ٴ��redis��
				jedisDaoImpl.set(key, JsonUtils.objectToJson(list));
			}
		}
		return 1;
	}

}
