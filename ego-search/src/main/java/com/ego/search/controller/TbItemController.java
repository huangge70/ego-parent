package com.ego.search.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ego.pojo.TbItem;
import com.ego.search.service.TbItemService;

@Controller
public class TbItemController {
	@Resource
	private TbItemService tbItemServiceImpl;
	
	@RequestMapping(value="solr/init",produces="text/html;charset=utf-8")
	@ResponseBody
	public String init(){
		long start=System.currentTimeMillis();
		try {
			tbItemServiceImpl.init();
		} catch (Exception e) {
			e.printStackTrace();
			return "初始化失败";
		} 
		long end=System.currentTimeMillis();
		return "初始化花费时间："+(end-start)/1000+"秒";
	}
	
	@RequestMapping("search.html")
	public String search(Model model,String q,@RequestParam(defaultValue="1") int page,@RequestParam(defaultValue="12") int rows){
		//解决中文乱码问题
		try {
			q=new String(q.getBytes("iso-8859-1"),"utf-8");
			Map<String, Object> map=tbItemServiceImpl.selByQuery(q, page, rows);
			model.addAttribute("query",q);
			model.addAttribute("itemList",map.get("itemList"));
			model.addAttribute("totlePages",map.get("totlePages"));
			model.addAttribute("page", page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "search";
	}
	
	@RequestMapping("solr/add")
	@ResponseBody
	//@ResponseBody:把响应数据设置为json放入响应体中，@RequestBody:把请求体中的json数据转换为对象
	public int add(@RequestBody Map<String,Object> map){
		try {
			return tbItemServiceImpl.add((LinkedHashMap<String, Object>)map.get("item"),map.get("desc").toString());
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return 0;
	}
}
