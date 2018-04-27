package bigupload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet(name = "MerageFile", urlPatterns = { "/merageFile" })
@SuppressWarnings("all")
@MultipartConfig
public class MerageFile extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5494760614480001296L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		/*response.setContentType("text/html;charset=UTF-8");*/
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
				System.out.println(e.getAbsolutePath());
				fc.transferFrom(tempFc, fc.size(), tempFc.size());
				tempFc.close();
			}
			
			fc.close();
		
			JSONObject resultObject = new JSONObject();
			resultObject.put("path", resultFile.getAbsolutePath().substring(FileUploadUtil.getBasePath().length(), resultFile.getAbsolutePath().length()));
			System.out.println(resultObject);
			resultList.add(resultObject);
			
			System.out.println("开始删除临时文件！");
			deleteTempFiles(tempDir);
		}
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(JSONArray.fromObject(resultList).toString());
		response.getWriter().close();
		response.getWriter().flush();
		
	}
	private void deleteTempFiles(String path){
		File f = new File(path);
		if(!f.exists()){
			return;
		}
		File[] fs = f.listFiles();
		for(File e:fs){
			System.out.println(e.delete());
		}
		f.delete();
	}
}
 