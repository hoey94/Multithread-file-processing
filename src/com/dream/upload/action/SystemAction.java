package com.dream.upload.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import bigupload.FileUploadUtil;

import com.dream.common.DataInit;
import com.dream.common.DateTools;
import com.dream.common.SuperController;
import com.dream.model.System_User;
import com.dream.model.UploadFile;
import com.dream.upload.service.SystemService;
import com.hxtt.b.co;
import com.hxtt.c.o;

@RequestMapping("/systemAction/systemAction.do")
@Controller
@SuppressWarnings("all")
public class SystemAction extends SuperController{

	@Autowired
	private SystemService systemServiceImpl;
	
	@RequestMapping(params = "method=initMainPage")
	public String initMainPage(HttpServletRequest request,HttpServletResponse response){
	   System_User user = (System_User) DataInit.getLoginUser();
		if(user  == null || "".equals(user.getTable_id())){
			return "../index";
		}
		List<System_User> list = systemServiceImpl.getUserInfo();
		request.getSession().setAttribute("userList", list);
		return "main";
	}
	@RequestMapping(params = "method=uploadPartFile")
	@ResponseBody
	public void uploadPartFile(@RequestParam("file") MultipartFile file ,HttpServletRequest request,
			HttpServletResponse response) throws IllegalStateException, IOException, ServletException{
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		String path = FileUploadUtil.getBasePath();
		String name = request.getParameter("name");
		String index = request.getParameter("index");
		String tempFileName = name.substring(0,name.lastIndexOf(".")) + "_" + index + name.substring(name.lastIndexOf("."), name.length());
		
		BufferedInputStream buff = null;
		FileOutputStream out = null;
		try{
			buff = new BufferedInputStream(file.getInputStream());
			File f = new File(path+File.separator+name+"_temp"+File.separator+tempFileName);
			if(!f.getParentFile().exists()){
				f.getParentFile().mkdirs();
			}
			if(f.exists()){ 
				f.delete();
			}
			out = new FileOutputStream(f);
			byte[] read = new byte[1024];
			int mark = 0;
			while((mark = buff.read(read)) != -1 ){
				out.write(read,0,mark);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(buff != null){
				try {
					buff.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	@RequestMapping(params = "method=meragePartFile")
	@ResponseBody
	public void meragePartFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		response.setHeader("Access-Control-Allow-Origin", "*");
		String jsonStr = request.getParameter("jsonStr");
		String path = FileUploadUtil.getBasePath();
		JSONArray ja = JSONArray.fromObject(jsonStr);
		List<JSONObject> resultList = new ArrayList<JSONObject>();
		
		for(int j = 0;j < ja.size();j++){
			String message = "";
			String name = ja.getJSONObject(j).getString("name");
			
			String total = ja.getJSONObject(j).getString("total");
			String uuid = UUID.randomUUID().toString().replaceAll("-", "");
			
			String resultFilePath = FileUploadUtil.getUploadFileSavePath() + File.separator+ uuid  + name.substring(name.lastIndexOf("."), name.length());
			File resultFile = new File(resultFilePath);
			FileChannel fc = new FileOutputStream(resultFile, true).getChannel();
			String tempDir = path + File.separator + name + "_temp";
			
			
			for (int i = 1;i <= Integer.parseInt(total);i++) {
				String tempFileName = name.substring(0,name.lastIndexOf(".")) + "_" + i + name.substring(name.lastIndexOf("."), name.length());
				File e = new File(tempDir+File.separator+tempFileName);
				if(!e.exists()){
					message = "临时文件不存在!";
					System.out.println(message);
					break;
				}
				FileChannel tempFc = new FileInputStream(e).getChannel();
				fc.transferFrom(tempFc, fc.size(), tempFc.size());
				tempFc.close();
			}
			
			fc.close();
		
			JSONObject resultObject = new JSONObject();
			
			resultObject.put("path", resultFile.getAbsolutePath().substring(FileUploadUtil.getBasePath().length(), 
										resultFile.getAbsolutePath().length()).replaceAll("\\\\", "/"));
			resultObject.put("uuidFileName", resultFile.getName());
			resultObject.put("fileSize", reckonFileSize(resultFile.length()));
			resultObject.put("fileType", resultFile.getName().subSequence(resultFile.getName().lastIndexOf(".")+1, resultFile.getName().length()));
			
			System.out.println(resultObject);
			resultList.add(resultObject);
			
			System.out.println("开始删除临时文件！");
			deleteTempFiles(tempDir);
		}
		outData(response, JSONArray.fromObject(resultList));
		
	}
	@RequestMapping(params = "method=saveUploadFileInfo")
	@ResponseBody
	public void saveUploadFileInfo(HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		JSONObject j = new JSONObject();
		response.setHeader("Access-Control-Allow-Origin", "*");
		try{
			String jaStr = request.getParameter("jaStr");
			String userData = request.getParameter("userData");
			
			JSONArray jsonArray = JSONArray.fromObject(jaStr);
			List<UploadFile> list = new ArrayList<UploadFile>();
			JSONObject userDataJSON = JSONObject.fromObject(userData);
			for(int i = 0;i < jsonArray.size();i++){
				
				JSONObject json = jsonArray.getJSONObject(i);
				UploadFile file = (UploadFile) JSONObject.toBean(json, UploadFile.class);

				file.setCreateTime(DateTools.getCurrentDateLong());
				file.setShowTime(DateTools.getCurrentDate());
				
				if("".equals(userData) || userData == null || userDataJSON.size() <= 0){
					file.setUploadUserName(((System_User)DataInit.getLoginUser()).getUserName());
					file.setUser((System_User)DataInit.getLoginUser());
					file.setInputUserId(((System_User)DataInit.getLoginUser()).getTable_id());
				}else{
					
					file.setUploadUserName(userDataJSON.getString("userName"));
					file.setInputUserId(userDataJSON.getString("userId"));
					file.setInputUserName(userDataJSON.getString("userName"));
				}
				
				list.add(file);
			}
			
			for(UploadFile u:list){
				systemServiceImpl.insertUploadFileInfo(u);
			}
			
			j.put("status", "1");
			j.put("msg", "成功！");
		}catch(Exception e){
			e.printStackTrace();
			j.put("status", "0");
			j.put("msg", "失败！");
		}
		outData(response, j);
	}
	@RequestMapping(params = "method=getUploadFileInfo")
	@ResponseBody
	public void getUploadFileInfo(UploadFile model,HttpServletRequest request,
									HttpServletResponse response) throws IOException{
		Map<String, Object> map = systemServiceImpl.getUploadFileMap(model);
		JsonConfig config = new JsonConfig();
		config.setExcludes(new String[]{"user"});
		outData(response, JSONObject.fromObject(map, config));
	}
	@RequestMapping(params = "method=deleteModel")
	@ResponseBody
	public void deleteModel(UploadFile model,HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		JSONObject json = new JSONObject();
		try{
			systemServiceImpl.deleteModel(model);
			json.put("status", "1");
			json.put("msg", "成功！");
		}catch(Exception e){
			e.printStackTrace();
			json.put("status", "0");
			json.put("msg", "失败！");
		}
		
		outData(response, json);
	}
	
	@RequestMapping(params = "method=downLoadFile")
	public void downLoadFile(UploadFile model,HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		
	}
	
	@RequestMapping(params = "method=addSystemUser")
	public void addSystemUser(System_User model,HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		JSONObject json = new JSONObject();
		try{
			
			model.setCreateTime(DateTools.getCurrentDateLong());
			model.setShowTime(DateTools.getCurrentDate());
			
			systemServiceImpl.insertSystemUser(model);
			json.put("status", "1");
			json.put("msg", "成功！");
		}catch(Exception e){
			e.printStackTrace();
			json.put("status", "0");
			json.put("msg", "失败！");
		}
		
		outData(response, json);
	}
	@RequestMapping("method=addUserUploadInfo")
	public void addUserUploadInfo(@RequestParam("uploadFileName") String uploadFileName,
										@RequestParam("userInfoJosnStr") String jsonStr,HttpServletRequest request,
										HttpServletResponse response) throws IOException{
		JSONObject json = new JSONObject();
		try{
			
			String result = findFilePath(uploadFileName, FileUploadUtil.getBasePath());
			if("".equals(result) || result == null){
				json.put("status", "0");
				json.put("msg", "没有找到指定文件: "+uploadFileName);
				outData(response, json);
				return;
			}
			System_User user = (System_User) JSONObject.toBean(JSONObject.fromObject(jsonStr), System_User.class);
			String path = result.substring(FileUploadUtil.getBasePath().length(), 
									result.length()).replaceAll("\\\\", "/");
			UploadFile uploadFile = new UploadFile();
			
			uploadFile.setPath(path);
			uploadFile.setCreateTime(DateTools.getCurrentDateLong());
			uploadFile.setShowTime(DateTools.getCurrentDate());
			uploadFile.setInputUserId(user.getTable_id());
			uploadFile.setUuidFileName(uploadFileName);
			uploadFile.setFileName(uploadFileName);
			uploadFile.setFileSize(reckonFileSize(new File(result).length()));
			uploadFile.setFileType(result.substring(result.lastIndexOf("."), result.length()));
			uploadFile.setInputUserName(user.getUserName());
			
			systemServiceImpl.insertUploadFileInfo(uploadFile);
			
			json.put("status", "1");
			json.put("msg", "成功！");
			outData(response, json);
			return;
		}catch(Exception e){
			e.printStackTrace();
			json.put("status", "0");
			json.put("msg", "程序异常: "+e.getMessage());
			outData(response, json);
			return;
		}
	}
	public String findFilePath(String fileName,String path){
		File[] fs = (new File(path)).listFiles();
		String s = "";
		for(File e:fs){
			if(e.isDirectory()){
				s = findFilePath(fileName, e.getAbsolutePath());
				if(!"".equals(s)){
					break;
				}
			}else{
				if(fileName.equals(e.getName()) && e.isFile()){
					s =  e.getAbsolutePath();
					break;
				}
			}
		}
		return s;
	}
	public String reckonFileSize(long size){
		DecimalFormat format = new DecimalFormat("0.00");
		String[] s = {"B","KB","M","G"};
		boolean mark = true;
		String res = "";
		int i = 0;
		Double douSize = Double.valueOf(size);
		while(mark){
			Double temp = douSize/Double.valueOf(1024);
			i+= 1;
			if(temp < 1 || i > 3){
				res = String.valueOf(format.format(temp * Double.valueOf(1024))) + s[i - 1];
				mark = false;
			}
			douSize = temp;
		}
		
		return res;
	}
	private void deleteTempFiles(String path){
		File f = new File(path);
		if(!f.exists()){
			return;
		}
		File[] fs = f.listFiles();
		for(File e:fs){
			boolean mark = e.delete();
			if(!mark) {
				System.gc();
				e.delete();
			}
		}
		f.delete();
	}
	public static void main(String[] args) {
		SystemAction sa = new SystemAction();
		String re = "";
		re = sa.findFilePath("fe1c8c737ca64cedb854bbf9fce38b4a.rar","D:\\uploadImg");
		System.out.println("----------------------------------");
		System.out.println(re);
	}
}
