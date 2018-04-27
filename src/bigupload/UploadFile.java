package bigupload;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@SuppressWarnings("all")
@MultipartConfig
public class UploadFile extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3175195356819934255L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		String path = FileUploadUtil.getBasePath();
		Part part = request.getPart("file");
		String name = request.getParameter("name");
		String index = request.getParameter("index");
		String tempFileName = name.substring(0,name.lastIndexOf(".")) + "_" + index + name.substring(name.lastIndexOf("."), name.length());
		System.out.println(tempFileName);
		
		BufferedInputStream buff = null;
		FileOutputStream out = null;
		try{
			buff = new BufferedInputStream(part.getInputStream());
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
			
		}finally{
			if(out != null){
				out.close();
			}
			if(buff != null){
				buff.close();
			}
		}
	}
}
