package bigupload;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UploadInit extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		String uploadBasePath = config.getInitParameter("uploadBasePath");
		String downHost = config.getInitParameter("downHost");
		String downPort = config.getInitParameter("downPort");
		String downProject = config.getInitParameter("downProject");
		
		FileUploadUtil.setBasePath(uploadBasePath);
		FileUploadUtil.setDownloadbaseUrl(downHost, downPort, downProject);
		
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println(FileUploadUtil.getUploadFileSavePath());
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}
	
}
