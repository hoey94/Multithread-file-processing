package bigupload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@WebServlet(name = "ManyThreadMerageFile", urlPatterns = { "/manyThreadMerageFile" })
@SuppressWarnings("all")
@MultipartConfig
public class ManyThreadMerageFile extends HttpServlet{


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
		
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		Map<String, List<String>> resultMap = new HashMap<String, List<String>>();
		
		ExecutorService pool = Executors.newFixedThreadPool(5);
		
		String jsonStr = request.getParameter("jsonStr");
		String path = FileUploadUtil.getBasePath();
		
		JSONArray ja = JSONArray.fromObject(jsonStr);
		List<JSONObject> resultList = new ArrayList<JSONObject>();
		List<Future<Integer>> futureList = new ArrayList<Future<Integer>>();
		
		for(int j = 0;j < ja.size();j++){
			String message = "";
			String name = ja.getJSONObject(j).getString("name");
			
			String total = ja.getJSONObject(j).getString("total");
			String uuid = UUID.randomUUID().toString().replaceAll("-", "");
			
			String resultFilePath = FileUploadUtil.getUploadFileSavePath() + File.separator+ uuid  + name.substring(name.lastIndexOf("."), name.length());
			File resultFile = new File(resultFilePath);
		
			String tempDir = path + File.separator + name + "_temp";
			int radomStartSeek = 0;
			
			for (int i = 1;i <= Integer.parseInt(total);i++) {
				String tempFileName = name.substring(0,name.lastIndexOf(".")) + "_" + i + name.substring(name.lastIndexOf("."), name.length());
				File e = new File(tempDir+File.separator+tempFileName);
				if(!e.exists()){
					message = "临时文件不存在!";
					System.out.println(message);
					break;
				}
				Future<Integer> future =  pool.submit(new MerageFileThread(radomStartSeek, e,resultFile));
				radomStartSeek += (int)e.length();
				futureList.add(future);
			}
		
			boolean mark = true;
			for(Future<Integer> f:futureList){
				try {
					Integer res = f.get();
					if(res == 0){
						mark = false;
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(mark){
				JSONObject resultObject = new JSONObject();
				resultObject.put("path", resultFile.getAbsolutePath().substring(FileUploadUtil.getBasePath().length(), resultFile.getAbsolutePath().length()));	
				resultList.add(resultObject);
			}
			new Thread(new DeleteTmpeFiles(tempDir)).start();
		}
		pool.shutdown();
		System.out.println("=================ManyThreadMerageFile=======================");
		
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
	class DeleteTmpeFiles implements Runnable{

		private String path;
		
		public DeleteTmpeFiles(String path) {
			// TODO Auto-generated constructor stub
			this.path = path;
		}
		@Override
		public void run()  {
			// TODO Auto-generated method stub
			try{
				File f = new File(path);
				if(!f.exists()){
					return;
				}
				File[] fs = f.listFiles();
				for(File e:fs){
					System.out.println(e.delete());
				}
				f.delete();
				return;
			}catch(Exception e){
				e.printStackTrace();
			}
			
			return;
		}
	}
	class MerageFileThread implements Callable<Integer>{

		private File targetFile;
		private long start;
		private File resultFile;
		public MerageFileThread(long start,File targetFile,File  resultFile) {
			// TODO Auto-generated constructor stub
			this.resultFile = resultFile;
			this.targetFile = targetFile;
			this.start = start;
			
		}
		@Override
		public Integer call() {
			// TODO Auto-generated method stub
			FileInputStream fileIn = null;
			FileChannel fileChannel = null;
			RandomAccessFile radomFile = null;
			try{
				radomFile = new RandomAccessFile(resultFile, "rw");
				fileIn = new FileInputStream(targetFile);
				fileChannel = fileIn.getChannel();
				ByteBuffer byteBuffer = ByteBuffer.allocate((int)fileChannel.size());
				while ((fileChannel.read(byteBuffer)) > 0) {  
	            } 
				radomFile.seek(start);
				radomFile.write(byteBuffer.array());
				return 1;
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				if(fileChannel != null){
					try {
						fileChannel.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(fileIn != null){
					try {
						fileIn.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(radomFile != null){
					try {
						radomFile.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			return 0;
		}
		
	}
}