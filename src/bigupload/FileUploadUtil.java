package bigupload;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUploadUtil {

	private static String basePath = "";

	private static String downloadbaseUrl = "";

	static {
		basePath = System.getProperty("user.dir") + File.separator + "..";
		downloadbaseUrl = "http://";
	}

	public static String getUploadFileSavePath() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dirName = dateFormat.format(new Date());
		String resultDirPath = basePath + File.separator + "uploadFiles"
				+ File.separator + dirName;
		File f = new File(resultDirPath);
		if (!f.exists()) {
			f.mkdirs();
		}
		return f.getAbsolutePath();
	}

	public static String getBasePath() {
		return basePath;
	}

	public static void setBasePath(String basePath) {
		FileUploadUtil.basePath = basePath;
	}

	public static String getDownloadbaseUrl() {
		return downloadbaseUrl;
	}

	public static void setDownloadbaseUrl(String host, String port,
			String projectName) {
		FileUploadUtil.downloadbaseUrl = FileUploadUtil.downloadbaseUrl + host
				+ ":" + port + "/" + projectName;
	}

}
