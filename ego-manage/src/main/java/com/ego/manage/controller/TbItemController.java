package com.ego.manage.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.commons.pojo.EgoResult;
import com.ego.manage.service.TbItemService;
import com.ego.pojo.TbItem;
import com.ego.pojo.TbItemDesc;

@Controller
public class TbItemController {
	@Resource
	private TbItemService tbItemServiceImpl;
	
	//分页显示商品
	@RequestMapping("item/list")
	@ResponseBody
	public EasyUIDataGrid show(int page,int rows){
		return tbItemServiceImpl.show(page, rows);
	}
	
	@RequestMapping("rest/page/item-edit")
	public String showItemEdit(){
		return "item-edit";
	}
	
	@RequestMapping("rest/item/delete")
	public EgoResult delete(String ids){
		EgoResult egoResult=new EgoResult();
		int index=tbItemServiceImpl.update(ids, (byte)3);
		if(index==1){
			egoResult.setStatus(200);
		}
		return egoResult;
	}
	
	//商品上架
	@RequestMapping("rest/item/reshelf")
	public EgoResult reshelf(String ids){
		EgoResult egoResult=new EgoResult();
		int index=tbItemServiceImpl.update(ids, (byte)1);
		if(index==1){
			egoResult.setStatus(200);
		}
		return egoResult;
	}
	
	//商品下架
	@RequestMapping("rest/item/instock")
	public EgoResult instock(String ids){
		EgoResult egoResult=new EgoResult();
		int index=tbItemServiceImpl.update(ids, (byte)2);
		if(index==1){
			egoResult.setStatus(200);
		}
		return egoResult;
	}
	
	//商品新增
	@RequestMapping("item/save")
	@ResponseBody
	public EgoResult insert(TbItem item,String desc,String itemParams){
		EgoResult er=new EgoResult();
		int index=tbItemServiceImpl.save(item, desc,itemParams);
		if(index==1){
			er.setStatus(200);
		}
		return er;
	}
}
