package com.dream.common;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传
 * 
 * @author yongw
 *
 */
public class FileUpload {

	// 文件上传
	public static String uploadFile(MultipartFile file,
			HttpServletRequest request) throws IOException {
		String fileName = file.getOriginalFilename();
		String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
		String uuid = UUID.randomUUID().toString().replaceAll("\\-", "");
		String path = request.getSession().getServletContext()
				.getRealPath("upload/");
		File tempFile = new File(path, String.valueOf(uuid + "." + prefix));
		if (!tempFile.getParentFile().exists()) {
			tempFile.getParentFile().mkdir();
		}
		if (!tempFile.exists()) {
			tempFile.createNewFile();
		}
		file.transferTo(tempFile);
		return "upload/" + tempFile.getName();
	}

}
