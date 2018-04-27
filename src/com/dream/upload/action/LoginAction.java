package com.dream.upload.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dream.common.DataInit;
import com.dream.common.SuperController;
import com.dream.model.System_User;
import com.dream.upload.service.SystemService;

@RequestMapping("/loginAction/loginAction.do")
@Controller
@SuppressWarnings("all")
public class LoginAction extends SuperController{

	@Autowired
	private SystemService systemServiceImpl;
	
	@RequestMapping(params = "method=loginSystem")
	@ResponseBody
	public void loginSystem(System_User model,HttpServletRequest request,HttpServletResponse response) throws IOException{
		System_User user = systemServiceImpl.selectOne(model);
		JSONObject json = new JSONObject();
		if(user == null){
			json.put("errMsg", "404");
			/*List<System_User> adminList =  DataInit.listAdmin;
			System_User admin = null;
			for(System_User user:adminList){
				if(user.getUserName().equals(model.getUserName()) 
						&& user.getUserPwd().equals(model.getUserPwd())){
					admin = user;
				}
			}
			if(admin != null){
				request.getSession().setAttribute("systemUser", admin);
				DataInit.setLoginUser(admin);
				json.put("success", "成功！");
			}else{
				json.put("errMsg", "404");
			}*/
		}else{
			request.getSession().setAttribute("systemUserTable", user.getTable_id().trim());
			HttpSession session = request.getSession();
			session.setAttribute("systemUser", user);
			DataInit.setLoginUser(session.getId(),user);
			DataInit.setLoginUser(user);
			json.put("success", "成功！");
		}
		
		outData(response, json);
	}
	
}
