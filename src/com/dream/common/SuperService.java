package com.dream.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("superService")
public class SuperService {

	@Autowired
	private HttpSession session;
	@Autowired
	private HttpServletRequest request;

	public String getList(String table_name) {

		// 当前權限，循環session的权限，得到他们的权限条件表中的条件，然后把所有条件进行拼装
		// ${userId}${departmentId}，

		return " where 1=1 ";
	}
}
