package com.dream.common;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.web.multipart.MultipartFile;

import com.dream.model.System_User;

@SuppressWarnings("all")
public class SuperController {

	private static SecureRandom random = new SecureRandom();

	public Msg getMsg(String code, String message, String data) {
		Msg msg = new Msg();
		msg.setCode(code);
		if (null != data) {
			msg.setData(data);
		}
		msg.setMessage(message);
		return msg;
	}

	public Map<String, Object> uploadFile(MultipartFile file,
			HttpServletResponse response) throws IOException {
		Map<String, Object> obj = new HashMap<String, Object>();

		String filename = "";
		String realAttName = "";
		String newFileName = "";

		try {
			// 得到文件名

			File pathfile = new File(DataInit.UPLOADURL);
			if (!pathfile.exists() && !pathfile.isDirectory()) {
				pathfile.mkdir();
			}

			realAttName = file.getOriginalFilename();

			if (realAttName.lastIndexOf('.') != -1) {
				filename = UUID.randomUUID() + "_" + randomString(7) + "_"
						+ realAttName.substring(realAttName.lastIndexOf('.'));
			}
			try {
				file.transferTo(new File(DataInit.UPLOADURL + filename));
				newFileName = UUID.randomUUID() + "_" + randomString(7) + "_"
						+ realAttName.substring(realAttName.lastIndexOf('.'));
				ImgCompress compress = new ImgCompress(DataInit.UPLOADURL
						+ filename, DataInit.UPLOADURL + newFileName);
				compress.resizeByWidth(300);
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		obj.put("fileName", filename);
		obj.put("newFileName", newFileName);
		obj.put("realName", realAttName);
		return obj;
	}

	public static String randomString(int length) {
		String str = new BigInteger(130, random).toString(32);
		return str.substring(0, length);
	}

	public boolean checkFileSize(HttpServletResponse response,
			MultipartFile... files) throws IOException {
		for (MultipartFile file : files) {
			if (null != file && file.getSize() > 10000000) {
				return false;
			}
		}
		return true;
	}

	public void respWriter(HttpServletResponse resp, String msg)
			throws IOException {
		resp.getWriter().write(msg);
		resp.getWriter().flush();
		resp.getWriter().close();
	}

	public void respReturn(HttpServletResponse resp, String errMsg)
			throws IOException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("success", false);
		jsonObject.put("errMsg", errMsg);
		respWriter(resp, jsonObject.toString());
	}

	public void respReturn(HttpServletResponse resp) throws IOException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("success", true);
		respWriter(resp, jsonObject.toString());
	}

	public String getUserTableId(HttpServletRequest req) throws IOException {
		JSONObject json = (JSONObject) req.getSession().getAttribute(
				"systemUser");
		System.out.println(json.toString() + "----");
		if (json.isNullObject()) {
			return "";
		}
		return json.get("table_id").toString();
	}

	public String getUserName(HttpServletRequest req) throws IOException {
		JSONObject json = JSONObject.fromObject(req.getSession().getAttribute(
				"systemUser"));
		if (json.isNullObject()) {
			return "";
		}
		return json.get("userName").toString();
	}

	/**
	 * 得到系统当前的sessionUser
	 * 
	 * @param req
	 * @return
	 * @throws IOException
	 */
	public System_User getSessionUser(HttpServletRequest req)
			throws IOException {
		JSONObject ju1 = (JSONObject) req.getSession().getAttribute(
				"systemUser");
		if (ju1 == null) {
			return null;
		}
		System_User ju = (System_User) JSONObject
				.toBean(ju1, System_User.class);
		return ju;
	}

	/**
	 * 
	 * 验证是不是超级管理员
	 * 
	 * @param ju
	 * @return
	 */
	public boolean isAdmin1(System_User ju) {

		for (int i = 0; i < DataInit.listAdmin.size(); i++) {

			System_User ju2 = DataInit.listAdmin.get(i);

		}
		return false;

	}

	/**
	 * 
	 * 验证是不是超级管理员
	 * 
	 * @param ju
	 * @return
	 */
	public System_User isAdmin(System_User ju) {

		for (int i = 0; i < DataInit.listAdmin.size(); i++) {
			System_User ju2 = DataInit.listAdmin.get(i);
			if (ju2.getUserName().equals(ju.getUserName())
					&& ju2.getUserPwd().equals(ju.getUserPwd())) {
				return ju2;
			}
		}
		return null;

	}

	public void outData(HttpServletResponse response, Object object,
			JsonConfig jsonConfig) throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(
				JSONObject.fromObject(object, jsonConfig).toString());
		response.getWriter().close();
		response.getWriter().flush();
	}

	public void outData(HttpServletResponse response, Object object)
			throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(JSONObject.fromObject(object).toString());
		response.getWriter().close();
		response.getWriter().flush();
	}

	public void outData(HttpServletResponse response, JSONArray object)
			throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(JSONArray.fromObject(object).toString());
		response.getWriter().close();
		response.getWriter().flush();
	}
}
