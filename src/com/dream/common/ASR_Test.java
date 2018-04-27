package com.dream.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;

public class ASR_Test {

	private static String URL = "http://test.api.hcicloud.com:8880/asr/recognise";
	private static String filepath;
	private static int filelen;
	private static String app_key = "c85d54f1";
	private static String dev_key = "712ddd892cf9163e6383aa169e0454e3";
	private static String cap_key = "asr.cloud.freetalk.english";

	public static void main(String[] args) {

	}

	public static String getVoiceResult(String path) throws IOException {
		HttpClient client = new HttpClient();
		PostMethod myPost = new PostMethod(URL);
		FileInputStream rd = null;
		try {

			myPost.setRequestHeader("Content-Type", "text/xml");
			myPost.setRequestHeader("charset", "utf-8");
			myPost.setRequestHeader("x-app-key", app_key);
			myPost.setRequestHeader("x-sdk-version", "3.6");
			String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
					.format(new Date());
			myPost.setRequestHeader("x-request-date", date);
			myPost.setRequestHeader("x-task-config", "capkey=" + cap_key
					+ ",audioformat=amr");
			String str = date + dev_key;
			System.out.println(str);
			myPost.setRequestHeader("x-session-key", MD5.getMD5(str.getBytes()));
			myPost.setRequestHeader("x-udid", "101:123456789");

			// filepath = new String(System.getProperty("user.dir") +
			// "\\wav\\e2.amr");
			filepath = new String(path);
			System.out.println(filepath);
			File fileSrc = new File(filepath);
			if (!fileSrc.exists()) {
				System.out.println("---文件不存在---");
				return "";
			}
			System.out.println("1");

			byte[] content = null;

			rd = new FileInputStream(fileSrc);
			filelen = rd.available();
			content = new byte[filelen];

			rd.read(content);

			RequestEntity entity = new StringRequestEntity(new String(content,
					"iso-8859-1"), "application/octet-stream", "iso-8859-1");
			myPost.setRequestEntity(entity);
			int statusCode = client.executeMethod(myPost);
			String.format("%d", statusCode);
			System.out.println(statusCode);

			if (statusCode == HttpStatus.SC_OK) {
				InputStream txtis = myPost.getResponseBodyAsStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						txtis));

				String tempbf;
				StringBuffer html = new StringBuffer(256);
				while ((tempbf = br.readLine()) != null) {
					html.append(tempbf);
				}
				String value = html.toString();
				System.out.println(html.toString());
				return value.substring(value.indexOf("<Text>") + 6,
						value.indexOf("</Text>"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rd != null) {
				rd.close();
			}
		}
		return "";
	}
}
