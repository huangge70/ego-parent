package com.ego.cart.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.cart.service.CartService;
import com.ego.commons.pojo.EgoResult;
import com.ego.commons.pojo.TbItemChild;
import com.ego.commons.utils.CookieUtils;
import com.ego.commons.utils.HttpClientUtil;
import com.ego.commons.utils.JsonUtils;
import com.ego.dubbo.service.TbItemDubboService;
import com.ego.pojo.TbItem;
import com.ego.pojo.TbUser;
import com.ego.redis.dao.JedisDao;

@Service
public class CartServiceImpl implements CartService{

	@Resource
	private JedisDao jedisDaoImpl;
	@Reference
	private TbItemDubboService tbItemDubboServiceImpl;
	@Value("${passport.url}")
	private String passportUrl;
	@Value("${cart.key}")
	private String cartKey;
	
	@Override
	public void addCart(long id, int num,HttpServletRequest request) {
		List<TbItemChild> list=new ArrayList<TbItemChild>();;
		
		//�õ���ǰ��½��ϵͳ�е��û���token
		String token=CookieUtils.getCookieValue(request, "TT_TOKEN");
		//�����û�token��ȥego-passport��Ŀ�еĿ�����ȡ���û�����Ϣ(Ҫʹ��httpclient����)
		String jsonUser=HttpClientUtil.doPost(passportUrl+token);
		//����redis��ȡ�����û�json����ת��Ϊ����
		EgoResult er=JsonUtils.jsonToPojo(jsonUser, EgoResult.class);
		//redis�д洢���û����ﳵ��Ϣ�ļ�ֵΪ: cart:�û���
		//����redis�д洢��key��ֵ
		String key=cartKey+((LinkedHashMap)er.getData()).get("username");
		
		boolean isExists=false;
		
		//���redis�д����û��Ĺ��ﳵ����
		if(jedisDaoImpl.exists(key)){
			String json=jedisDaoImpl.get(key);
			if(json!=null&&!json.equals("")){
				//ȡ���û����ﳵ�еĻ�������
				list=JsonUtils.jsonToList(json, TbItemChild.class);
				//�ж��û����ﳵ��������û�е�ǰ��Ʒ���еĻ�ֱ�����������Ϳ���
				for(TbItemChild tbItemChild:list){
					if((long)tbItemChild.getId()==id){
						//�û����ﳵ�д��ڸ���Ʒ
						isExists=true;
						tbItemChild.setNum(tbItemChild.getNum()+num);
						//���û����ﳵ������ӵ�redis��
						jedisDaoImpl.set(key, JsonUtils.objectToJson(list));
						return;
					}
				}
			}
		}
		//redis������û���û��Ĺ��ﳵ��Ϣ
		
		//���ﳵ��û�и���Ʒ
		TbItem item=tbItemDubboServiceImpl.selById(id);
		TbItemChild child=new TbItemChild();
		
		child.setId(item.getId());
		child.setTitle(item.getTitle());
		child.setImages(item.getImage()==null||item.getImage().equals("")?new String[1]:item.getImage().split(","));
		child.setNum(num);
		child.setPrice(item.getPrice());
		list.add(child);
		
		jedisDaoImpl.set(key, JsonUtils.objectToJson(list));
	}

	@Override
	public List<TbItemChild> showCart(HttpServletRequest request) {
		//�õ���ǰ��½��ϵͳ�е��û���token
		String token=CookieUtils.getCookieValue(request, "TT_TOKEN");
		//�����û�token��ȥego-passport��Ŀ�еĿ�����ȡ���û�����Ϣ(Ҫʹ��httpclient����)
		String jsonUser=HttpClientUtil.doPost(passportUrl+token);
		//����redis��ȡ�����û�json����ת��Ϊ����
		EgoResult er=JsonUtils.jsonToPojo(jsonUser, EgoResult.class);
		//redis�д洢���û����ﳵ��Ϣ�ļ�ֵΪ: cart:�û���
		//����redis�д洢��key��ֵ
		String key=cartKey+((LinkedHashMap)er.getData()).get("username");
		String json=jedisDaoImpl.get(key);
		return JsonUtils.jsonToList(json, TbItemChild.class);
	}

	@Override
	public EgoResult update(long id, int num,HttpServletRequest request) {
		//�õ���ǰ��½��ϵͳ�е��û���token
		String token=CookieUtils.getCookieValue(request, "TT_TOKEN");
		//�����û�token��ȥego-passport��Ŀ�еĿ�����ȡ���û�����Ϣ(Ҫʹ��httpclient����)
		String jsonUser=HttpClientUtil.doPost(passportUrl+token);
		//����redis��ȡ�����û�json����ת��Ϊ����
		EgoResult er=JsonUtils.jsonToPojo(jsonUser, EgoResult.class);
		//redis�д洢���û����ﳵ��Ϣ�ļ�ֵΪ: cart:�û���
		//����redis�д洢��key��ֵ
		String key=cartKey+((LinkedHashMap)er.getData()).get("username");
		String json=jedisDaoImpl.get(key);
		List<TbItemChild> list=JsonUtils.jsonToList(json, TbItemChild.class);
		for(TbItemChild child:list){
			if((long)child.getId()==id){
				child.setNum(num);
			}
		}
		String ok=jedisDaoImpl.set(key, JsonUtils.objectToJson(list));
		EgoResult egoResult=new EgoResult();
		if(ok.equals("OK")){
			egoResult.setStatus(200);
		}
		return egoResult;
	}

	@Override
	public EgoResult delete(long id, HttpServletRequest request) {
		//�õ���ǰ��½��ϵͳ�е��û���token
		String token=CookieUtils.getCookieValue(request, "TT_TOKEN");
		//�����û�token��ȥego-passport��Ŀ�еĿ�����ȡ���û�����Ϣ(Ҫʹ��httpclient����)
		String jsonUser=HttpClientUtil.doPost(passportUrl+token);
		//����redis��ȡ�����û�json����ת��Ϊ����
		EgoResult er=JsonUtils.jsonToPojo(jsonUser, EgoResult.class);
		//redis�д洢���û����ﳵ��Ϣ�ļ�ֵΪ: cart:�û���
		//����redis�д洢��key��ֵ
		String key=cartKey+((LinkedHashMap)er.getData()).get("username");
		String json=jedisDaoImpl.get(key);
		List<TbItemChild> list=JsonUtils.jsonToList(json, TbItemChild.class);
		TbItemChild tbItemChild=null;
		for(TbItemChild child:list){
			if((long)child.getId()==id){
				tbItemChild=child;
			}
		}
		list.remove(tbItemChild);
		String ok=jedisDaoImpl.set(key, JsonUtils.objectToJson(list));
		EgoResult egoResult=new EgoResult();
		if(ok.equals("OK")){
			egoResult.setStatus(200);
		}
		return egoResult;
	}
	
}
