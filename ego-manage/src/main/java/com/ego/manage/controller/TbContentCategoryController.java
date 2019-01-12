package com.ego.manage.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ego.commons.pojo.EasyUiTree;
import com.ego.commons.pojo.EgoResult;
import com.ego.manage.service.TbContentCategoryService;
import com.ego.pojo.TbContentCategory;

@Controller
public class TbContentCategoryController {
	@Resource
	private TbContentCategoryService tbContentCategoryServiceImpl;
	
	@RequestMapping("content/category/list")
	@ResponseBody
	public List<EasyUiTree> showCategory(@RequestParam(defaultValue="0") long id){
		return tbContentCategoryServiceImpl.showCategory(id);
	}
	
	//������Ŀ
	@RequestMapping("content/category/create")
	@ResponseBody
	public EgoResult create(TbContentCategory cate){
		return tbContentCategoryServiceImpl.create(cate);
	}
	
	//��Ŀ������
	@RequestMapping("content/category/update")
	@ResponseBody
	public EgoResult update(TbContentCategory cate){
		return tbContentCategoryServiceImpl.update(cate);
	}
	
	@RequestMapping("content/category/delete")
	@ResponseBody
	public EgoResult delete(TbContentCategory cate){
		return tbContentCategoryServiceImpl.delete(cate);
	}
}
