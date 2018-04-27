package com.dream.upload.service;

import java.util.List;
import java.util.Map;

import com.dream.model.System_User;
import com.dream.model.UploadFile;

public interface SystemService {

	boolean checkUser(System_User model);
	
	void insertUploadFileInfo(UploadFile model);
	
	Map<String, Object> getUploadFileMap(UploadFile model);
	
	System_User selectOne(System_User model);
	
	void deleteModel(UploadFile model);
	
	void insertSystemUser(System_User model);
	
	List<System_User> getUserInfo();
}
