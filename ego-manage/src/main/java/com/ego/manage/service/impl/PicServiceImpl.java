package com.ego.manage.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ego.commons.utils.FtpUtil;
import com.ego.commons.utils.IDUtils;
import com.ego.manage.service.PicService;
@Service
public class PicServiceImpl implements PicService{

	@Value("${ftpclient.host}")
	private String host;
	@Value("${ftpclient.port}")
	private int port;
	@Value("${ftpclient.username}")
	private String username;
	@Value("${ftpclient.password}")
	private String password;
	@Value("${ftpclient.basepath}")
	private String basepath;
	@Value("${ftpclient.filepath}")
	private String filepath;
	
	@Override
	public Map<String,Object> upload(MultipartFile file) {
		String filename= IDUtils.genImageName()+file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		boolean result=false;
		try {
			result=FtpUtil.uploadFile(host, port, username, password, basepath, filepath, filename, file.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		Map<String, Object> map=new HashMap<String, Object>();
		if(result){
			map.put("error", 0);
			map.put("url","http://"+host+"/"+filename);
		}else{
			map.put("error", 1);
			map.put("message","Í¼Æ¬ÉÏ´«Ê§°Ü");
		}
		return map;
	}

}
