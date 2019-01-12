package com.ego.manage.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface PicService {
	//文件上传
	Map<String, Object> upload(MultipartFile file);
}
