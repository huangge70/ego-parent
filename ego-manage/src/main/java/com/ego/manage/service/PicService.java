package com.ego.manage.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface PicService {
	//�ļ��ϴ�
	Map<String, Object> upload(MultipartFile file);
}
