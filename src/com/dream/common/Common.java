package com.dream.common;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.InputFormatException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.hibernate.dialect.MySQLInnoDBDialect;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.google.gson.JsonArray;

@SuppressWarnings("all")
public final class Common {

	/**
	 * 两位小数
	 */
	public static DecimalFormat df1 = new DecimalFormat("######0.00");

	public static String getTime() {
		DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		Date date = Calendar.getInstance().getTime();
		TimeZone srcTimeZone = TimeZone.getTimeZone("EST");
		TimeZone destTimeZone = TimeZone.getTimeZone("GMT+8");
		// return VeDate.dateTransformBetweenTimeZone(date, formatter,
		// srcTimeZone, destTimeZone).toString();
		return VeDate.dateToStr(Calendar.getInstance().getTime());
	}

	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static String dateTransformBetweenTimeZone(Date sourceDate,
			DateFormat formatter, TimeZone sourceTimeZone,
			TimeZone targetTimeZone) {
		Long targetTime = sourceDate.getTime() - sourceTimeZone.getRawOffset()
				+ targetTimeZone.getRawOffset();
		return Common.getTime(new Date(targetTime), formatter);
	}

	public static String getTime(Date date, DateFormat formatter) {
		return formatter.format(date);
	}

	public static String getTime(Long l) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date(l));
	}

	/**
	 * 得到当前系统日期的格式化
	 * 
	 * @return
	 */
	public static Long getTime1() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date beginTime2 = null;
		try {
			beginTime2 = Calendar.getInstance().getTime();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return beginTime2.getTime();
	}

	/**
	 * 得到当前时间的前一周，本周，和下周的所有日期
	 * 
	 * 
	 */
	public static List<String> getWeek123() {
		List<Date> list = DateTools.dateToWeek(new Date());
		List<Date> d1 = DateTools.dateToWeek(DateTools.getDateAdd(list.get(0),
				-3));
		List<Date> d2 = DateTools.dateToWeek(DateTools.getDateAdd(list.get(6),
				1));
		List<Date> d3 = DateTools.dateToWeek(DateTools.getDateAdd(list.get(6),
				9));
		Long beginTime2 = null;

		List<Date> all = new ArrayList<Date>();

		for (int i = 0; i < d1.size(); i++) {
			all.add(d1.get(i));

		}
		for (int i = 0; i < list.size(); i++) {
			all.add(list.get(i));
		}
		for (int i = 0; i < d2.size(); i++) {
			all.add(d2.get(i));
		}
		for (int i = 0; i < d3.size(); i++) {
			all.add(d3.get(i));
		}
		List<String> lists = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (int i = 0; i < all.size(); i++) {
			lists.add(sdf.format(all.get(i)));
		}
		return lists;
	}

	/**
	 * 功能:压缩多个文件成一个zip文件
	 * <p>
	 * 作者 陈亚标 Jul 16, 2010 10:59:40 AM
	 * 
	 * @param srcfile
	 *            ：源文件列表
	 * @param zipfile
	 *            ：压缩后的文件
	 */
	public static File zipFiles(File[] srcfile, File zipfile) {
		byte[] buf = new byte[1024];
		try {
			// ZipOutputStream类：完成文件或文件夹的压缩
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
					zipfile));
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < srcfile.length; i++) {
				FileInputStream in = new FileInputStream(srcfile[i]);
				String zipName = srcfile[i].getName().substring(37,
						srcfile[i].getName().length());
				if (list.contains(zipName)) {
					zipName = i + zipName;
					list.add(zipName);
				} else {
					list.add(zipName);
				}
				out.putNextEntry(new ZipEntry(zipName));
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				out.closeEntry();
				in.close();
			}
			out.close();
			// System.out.println("压缩完成.");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return zipfile;
	}

	/**
	 * 加载以下配置文件
	 */
	public void loadFileConfig() {

	}

	/**
	 * 得到这周几具体在本周哪里上课显示时间
	 * 
	 * @param week
	 * @return
	 */
	public static String getClassTimeShow(String week, String start1) {
		// scp.setClassTime();//设置上课时间
		List<String> timelist = DateTools.getCutWeek();
		String day = "";
		if ("1".equals(week)) {
			day = timelist.get(0) + " " + start1 + ":00";
		} else if ("2".equals(week)) {
			day = timelist.get(1) + " " + start1 + ":00";
		} else if ("3".equals(week)) {
			day = timelist.get(2) + " " + start1 + ":00";
		} else if ("4".equals(week)) {
			day = timelist.get(3) + " " + start1 + ":00";
		} else if ("5".equals(week)) {
			day = timelist.get(4) + " " + start1 + ":00";
		} else if ("6".equals(week)) {
			day = timelist.get(5) + " " + start1 + ":00";
		} else if ("7".equals(week)) {
			day = timelist.get(6) + " " + start1 + ":00";
		}
		return day;

	}

	/**
	 * 得到这周几具体在本周哪里上课显示时间
	 * 
	 * @param week
	 * @return
	 * @throws ParseException
	 */
	public static Long getClassTime(String week, String start1)
			throws ParseException {
		// scp.setClassTime();//设置上课时间
		List<String> timelist = DateTools.getCutWeek();
		String day = "";
		if ("1".equals(week)) {
			day = timelist.get(0) + " " + start1 + ":00";
		} else if ("2".equals(week)) {
			day = timelist.get(1) + " " + start1 + ":00";
		} else if ("3".equals(week)) {
			day = timelist.get(2) + " " + start1 + ":00";
		} else if ("4".equals(week)) {
			day = timelist.get(3) + " " + start1 + ":00";
		} else if ("5".equals(week)) {
			day = timelist.get(4) + " " + start1 + ":00";
		} else if ("6".equals(week)) {
			day = timelist.get(5) + " " + start1 + ":00";
		} else if ("7".equals(week)) {
			day = timelist.get(6) + " " + start1 + ":00";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		return sdf.parse(day).getTime();

	}

	/**
	 * 字符转时间的Long
	 * 
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Long getTimeLong(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return sdf.parse(str).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("-------------------时间转换格式出错------------------");
			return new Long(0);
		}
	}

	/**
	 * 创建指定数量的随机字符串
	 * 
	 * @param length
	 * @return
	 */
	public static String createRandom(int length) {
		String retStr = "";
		String strTable = "1234567890";
		int len = strTable.length();
		boolean bDone = true;
		do {
			retStr = "";
			int count = 0;
			for (int i = 0; i < length; i++) {
				double dblR = Math.random() * len;
				int intR = (int) Math.floor(dblR);
				char c = strTable.charAt(intR);
				if (('0' <= c) && (c <= '9')) {
					count++;
				}
				retStr += strTable.charAt(intR);
			}
			if (count >= 2) {
				bDone = false;
			}
		} while (bDone);

		return retStr;
	}

	/**
	 * java下载
	 * 
	 * @param remoteFilePath
	 * @param localFilePath
	 */
	public static void downloadFile(String remoteFilePath, String localFilePath) {
		URL urlfile = null;
		HttpURLConnection httpUrl = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		File f = new File(localFilePath);
		try {
			if (!f.isFile()) {
				f.createNewFile();
			}
			urlfile = new URL(remoteFilePath);
			httpUrl = (HttpURLConnection) urlfile.openConnection();
			httpUrl.connect();
			bis = new BufferedInputStream(httpUrl.getInputStream());
			bos = new BufferedOutputStream(new FileOutputStream(f));
			int len = 2048;
			byte[] b = new byte[len];
			while ((len = bis.read(b)) != -1) {
				bos.write(b, 0, len);
			}
			bos.flush();
			bis.close();
			httpUrl.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bis.close();
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 增加图片到qq服务器
	 * 
	 * @param urlString
	 * @return
	 * @throws IOException
	 */
	public String addImageForQQ(String urlString) throws IOException {
		URL urlfile = null;
		HttpURLConnection httpUrl = null;
		BufferedOutputStream bos = null;
		urlfile = new URL(urlString);
		httpUrl = (HttpURLConnection) urlfile.openConnection();
		httpUrl.connect();
		InputStream bis = httpUrl.getInputStream();
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		int n = 0;
		while (-1 != (n = bis.read(buffer))) {
			output.write(buffer, 0, n);
		}
		byte[] bbb = output.toByteArray();
		String str = "";
		try {
			// str = QQImage.QQupload(bbb);
			System.out.println(str);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 从网络Url中下载文件,增加到qq服务器
	 * 
	 * @param urlStr
	 * @param fileName
	 * @param savePath
	 * @throws IOException
	 */
	public static boolean downLoadFromUrl(String urlStr, String fileName,
			String savePath) throws IOException {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 设置超时间为3秒
		conn.setConnectTimeout(3 * 1000);
		// 防止屏蔽程序抓取而返回403错误
		conn.setRequestProperty("User-Agent",
				"Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

		// 得到输入流
		InputStream inputStream = conn.getInputStream();
		// 获取自己数组
		byte[] getData = readInputStream(inputStream);

		// 文件保存位置
		File saveDir = new File(savePath);
		if (!saveDir.exists()) {
			saveDir.mkdir();
		}
		File file = new File(saveDir + File.separator + fileName);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(getData);
		if (fos != null) {
			fos.close();
		}
		if (inputStream != null) {
			inputStream.close();
		}

		return true;

	}

	/**
	 * 从输入流中获取字节数组
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static byte[] readInputStream(InputStream inputStream)
			throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		return bos.toByteArray();
	}

	public static String httpGet(String url) throws ClientProtocolException,
			IOException, URISyntaxException, ParseException {
		HttpClient httpclient = new DefaultHttpClient();
		String result = "";
		try {
			// 连接超时
			httpclient.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
			// 读取超时
			httpclient.getParams().setParameter(
					CoreConnectionPNames.SO_TIMEOUT, 5000);

			HttpGet hg = new HttpGet(url);
			// 模拟浏览器
			hg.addHeader(
					"User-Agent",
					"Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31");
			String charset = "UTF-8";
			hg.setURI(new java.net.URI(url));
			HttpResponse response = httpclient.execute(hg);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				charset = getContentCharSet(entity);
				// 使用EntityUtils的toString方法，传递编码，默认编码是ISO-8859-1
				result = EntityUtils.toString(entity, charset);
			}

		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		return result;
	}

	/**
	 * 默认编码utf -8 Obtains character set of the entity, if known.
	 * 
	 * @param entity
	 *            must not be null
	 * @return the character set, or null if not found
	 * @throws ParseException
	 *             if header elements cannot be parsed
	 * @throws IllegalArgumentException
	 *             if entity is null
	 */
	public static String getContentCharSet(final HttpEntity entity)
			throws ParseException {

		if (entity == null) {
			throw new IllegalArgumentException("HTTP entity may not be null");
		}
		String charset = null;
		if (entity.getContentType() != null) {
			HeaderElement values[] = entity.getContentType().getElements();
			if (values.length > 0) {
				NameValuePair param = values[0].getParameterByName("charset");
				if (param != null) {
					charset = param.getValue();
				}
			}
		}

		if (StringUtils.isEmpty(charset)) {
			charset = "UTF-8";
		}
		return charset;
	}

	public static String sendGet1(String url) {
		HttpGet request = new HttpGet(url);
		String result = "";
		try {
			HttpResponse response = HttpClients.createDefault()
					.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e) {

		}
		return result;
	}

	public static String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// // 遍历所有的响应头字段
			// for (String key : map.keySet()) {
			// System.out.println(key + "--->" + map.get(key));
			// }
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	// public static void main(String[] args) {
	// changeToMp3("C:\\Users\\liuyang\\Desktop/-dkjckStYBoIUf-Mqy799-IRfrvXRrVcncrb8_v7L20pPZNBDbiPnXmQb9iwCBrb.amr",
	// "C:\\Users\\liuyang\\Desktop/aa.mp3");
	// }

	public static void changeToMp3(String sourcePath, String targetPath) {
		File source = new File(sourcePath);
		File target = new File(targetPath);
		AudioAttributes audio = new AudioAttributes();
		Encoder encoder = new Encoder();

		audio.setCodec("libmp3lame");
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setFormat("mp3");
		attrs.setAudioAttributes(audio);

		try {
			encoder.encode(source, target, attrs);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InputFormatException e) {
			e.printStackTrace();
		} catch (EncoderException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解决中英文 长度的问题
	 * 
	 * @param name
	 * @param endcoding
	 * @return
	 * @throws Exception
	 */
	public static int getChineseLength(String name, String endcoding)
			throws Exception {
		int len = 0; // 定义返回的字符串长度
		int j = 0;
		// 按照指定编码得到byte[]
		byte[] b_name = name.getBytes(endcoding);
		while (true) {
			short tmpst = (short) (b_name[j] & 0xF0);
			if (tmpst >= 0xB0) {
				if (tmpst < 0xC0) {
					j += 2;
					len += 2;
				} else if ((tmpst == 0xC0) || (tmpst == 0xD0)) {
					j += 2;
					len += 2;
				} else if (tmpst == 0xE0) {
					j += 3;
					len += 2;
				} else if (tmpst == 0xF0) {
					short tmpst0 = (short) (((short) b_name[j]) & 0x0F);
					if (tmpst0 == 0) {
						j += 4;
						len += 2;
					} else if ((tmpst0 > 0) && (tmpst0 < 12)) {
						j += 5;
						len += 2;
					} else if (tmpst0 > 11) {
						j += 6;
						len += 2;
					}
				}
			} else {
				j += 1;
				len += 1;
			}
			if (j > b_name.length - 1) {
				break;
			}
		}
		return len;
	}

	public static String getNull(Object obj) {
		if (obj == null) {
			return "";
		} else {
			return obj.toString();
		}
	}

	/**
	 * 请求服务，并传参数
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String httpPost(String url, List<NameValuePair> params)
			throws Exception {
		String result = "";
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			HttpResponse response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instreams = entity.getContent();
				result = convertStreamToString(instreams);

			}
		} catch (Exception e) {
			throw e;
		}
		return result;
	}

	/**
	 * 
	 * 远程返回的string
	 * 
	 * @param is
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String convertStreamToString(InputStream is)
			throws UnsupportedEncodingException {
		// BufferedReader reader = new BufferedReader(new
		// InputStreamReader(is));
		BufferedReader reader = new BufferedReader(new InputStreamReader(is,
				"UTF-8"));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/**
	 * 
	 * 根据坐标得名字
	 * 
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public static String getNameAddressList(List<String> list) throws Exception {
		String url = "http://restapi.amap.com/v3/geocode/regeo?output=json&key=53af259f31386633b25ae8c1c97d8361&radius=500&extensions=base";
		List list1 = new ArrayList<NameValuePair>();
		String str = "";
		for (int i = 0; i < list.size(); i++) {
			if (i == list.size() - 1) {
				str += list.get(i);
			} else {
				str += list.get(i) + "|";
			}
		}
		list1.add(new BasicNameValuePair("location", str));
		list1.add(new BasicNameValuePair("batch", "true"));
		return Common.httpPost(url, list1);
	}

	/**
	 * 转换坐标为高德的坐标
	 * 
	 * @param list1
	 * @return
	 * @throws Exception
	 */
	public static List<String> getAllGDxy(List<String> list1) throws Exception {

		List<String> list = new ArrayList<String>();

		for (int i = 0; i < list1.size(); i++) {
			String[] ll = list1.get(i).split(",");
			String longitude = ll[0];
			String latitude = ll[1];
			// LngLat lngLat_bd = new LngLat(Double.parseDouble(longitude),
			// Double.parseDouble(latitude));
			double[] lt = GPSConver.transLatLng(Double.parseDouble(latitude),
					Double.parseDouble(longitude));
			String str = lt[1] + "," + lt[0];
			list.add(str);
		}
		return list;
		/*
		 * String url =
		 * "http://restapi.amap.com/v3/assistant/coordinate/convert"; List list
		 * = new ArrayList<NameValuePair>(); list.add(new
		 * BasicNameValuePair("coordsys","gps")); list.add(new
		 * BasicNameValuePair("output","json")); list.add(new
		 * BasicNameValuePair("key","53af259f31386633b25ae8c1c97d8361")); String
		 * str=""; for (int i = 0; i < list1.size(); i++) { if (i ==
		 * list1.size() - 1) { str += list1.get(i); } else { str += list1.get(i)
		 * + ";"; } } list.add(new BasicNameValuePair("locations",str)); String
		 * str1 =Common.httpPost(url, list); JSONObject jsonObject =
		 * JSONObject.fromObject(str1); String ok =jsonObject.getString("info");
		 * if("ok".equals(ok)){ String locations
		 * =jsonObject.getString("locations"); String[] listxy =
		 * locations.split(";"); List<String> listxystring = new
		 * ArrayList<String>(); for (int i = 0; i < listxy.length; i++) {
		 * listxystring.add(listxy[i]); } return listxystring; } return new
		 * ArrayList<String>();
		 */
	}

	/**
	 * 
	 * 长字符串转IP
	 * 
	 * @param phoneNo
	 * @return
	 */
	public static String getPretendip(String phoneNo) {
		String ip = "";
		byte[] ip_byte = new byte[6];
		if (phoneNo.length() == 11) {
			ip_byte[0] = Byte.valueOf(phoneNo.substring(0, 1));
			ip_byte[1] = Byte.valueOf(phoneNo.substring(1, 3));
			ip_byte[2] = Byte.valueOf(phoneNo.substring(3, 5));
			ip_byte[3] = Byte.valueOf(phoneNo.substring(5, 7));
			ip_byte[4] = Byte.valueOf(phoneNo.substring(7, 9));
			ip_byte[5] = Byte.valueOf(phoneNo.substring(9, 11));

			if ((ip_byte[1] >= 30) && (ip_byte[1] < 46)) {
				ip_byte[1] = (byte) (ip_byte[1] - 30);
				if ((ip_byte[1] & 0x08) == 0x08) {
					ip += (ip_byte[2] + 0x80) + ".";

				} else {
					ip += (ip_byte[2]) + ".";
				}
				if ((ip_byte[1] & 0x04) == 0x04) {
					ip += (ip_byte[3] + 0x80) + ".";

				} else {
					ip += (ip_byte[3]) + ".";
				}
				if ((ip_byte[1] & 0x02) == 0x02) {
					ip += (ip_byte[4] + 0x80) + ".";

				} else {
					ip += (ip_byte[4]) + ".";
				}
				if ((ip_byte[1] & 0x01) == 0x01) {
					ip += (ip_byte[5] + 0x80);

				} else {
					ip += (ip_byte[5]);
				}

			} else if ((ip_byte[1] >= 50) && (ip_byte[1] < 60)) {
				ip_byte[1] = (byte) ((ip_byte[1] - 30) & 0x0f);
				if ((ip_byte[1] & 0x08) == 0x08) {

					ip += (ip_byte[2] + 0x80) + ".";
				} else {
					ip += (ip_byte[2]) + ".";
				}
				if ((ip_byte[1] & 0x04) == 0x04) {

					ip += (ip_byte[3] + 0x80) + ".";
				} else {
					ip += (ip_byte[3]) + ".";
				}
				if ((ip_byte[1] & 0x02) == 0x02) {

					ip += (ip_byte[4] + 0x80) + ".";
				} else {
					ip += (ip_byte[4]) + ".";
				}
				if ((ip_byte[1] & 0x01) == 0x01) {

					ip += (ip_byte[5] + 0x80);
				} else {
					ip += (ip_byte[5]);
				}

			}

		}
		return ip;
	}

	public static String getImageStr(String imgFile) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		// String imgFile = "d:\\111.jpg";// 待处理的图片
		InputStream in = null;
		byte[] data = null;
		// 读取图片字节数组
		try {
			in = new FileInputStream(imgFile);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);// 返回Base64编码过的字节数组字符串
	}

	/**
	 * 将字符串转为图片
	 * 
	 * @param imgStr
	 * @return
	 */
	public static boolean generateImage(String imgStr, String imgFile)
			throws Exception {// 对字节数组字符串进行Base64解码并生成图片
		if (imgStr == null) // 图像数据为空
			return false;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			// Base64解码
			byte[] b = decoder.decodeBuffer(imgStr);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {// 调整异常数据
					b[i] += 256;
				}
			}
			// 生成jpeg图片
			String imgFilePath = imgFile;// 新生成的图片
			OutputStream out = new FileOutputStream(imgFilePath);
			out.write(b);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			throw e;
		}
	}

	public static void main(String[] args) {
		String str = "\u0000\u0010JFIF\u0000\u0001\u0001\u0001\u0000`\u0000`\u0000\u0000\u0010Exif\u0000\u0000MM\u0000*\u0000\u0000\u0000\b\u0000\u0003i\u0000\u0004\u0000\u0000\u0000\u0001\u0000\u0000\b>\u0000\u0001\u0000\u0000\u0000\u0014\u0000\u0000\u0010|\u001c\u0000\u0007\u0000\u0000\b\f\u0000\u0000\u00002\u0000\u0000\u0000\u0000\u001c\u0000\u0000\u0000\b\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0002\u0003\u0000\u0002\u0000\u0000\u0000\u0014\u0000\u0000\u0010h\u001c\u0000\u0007\u0000\u0000\b\f\u0000\u0000\b\\\u0000\u0000\u0000\u0000\u001c\u0000\u0000\u0000\b\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u00002016:08:21 04:54:31\u0000E\u0000L\u0000I\u0000T\u0000E\u0000 \u00007\u00008\u00009\u0000\u0000\u0000\u000b]http://ns.adobe.com/xap/1.0/\u0000<?xpacket begin='?' id='W5M0MpCehiHzreSzNTczkc9d'?>\r\n<x:xmpmeta xmlns:x=\"adobe:ns:meta/\"><rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"><rdf:Description rdf:about=\"uuid:faf5bdd5-ba3d-11da-ad31-d33d75182f1b\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\"><dc:creator><rdf:Seq xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"><rdf:li>ELITE 789</rdf:li></rdf:Seq>\r\n\t\t\t</dc:creator></rdf:Description><rdf:Description rdf:about=\"uuid:faf5bdd5-ba3d-11da-ad31-d33d75182f1b\" xmlns:tiff=\"http://ns.adobe.com/tiff/1.0/\"><tiff:artist>ELITE 789</tiff:artist></rdf:Description><rdf:Description rdf:about=\"uuid:faf5bdd5-ba3d-11da-ad31-d33d75182f1b\" xmlns:exif=\"http://ns.adobe.com/exif/1.0/\"><exif:DateTimeOriginal>2016-08-21T11:54:31Z</exif:DateTimeOriginal></rdf:Description></rdf:RDF></x:xmpmeta>\r\n<?xpacket end='w'?>                                                                                                    \n                                                                                                    \n                                                                                                    \n                                                                                                    \n                                                                                                    \n                                                                                                    \n                                                                                                    \n                                                                                                    \n                                                                                                    \n                                                                                                    \n                                                                                                    \n                                                                                                    \n                                                                                                    \n                                                                                                    \n                                                                                                    \n                                                                                                    \n                                                                                                    \n                                                                                                    \n                                                                                                    \n                                                                                                    \n                            \u0000C\u0000\u0006\u0004\u0005\u0006\u0005\u0004\u0006\u0006\u0005\u0006\u0007\u0007\u0006\b\n\u0010\n\n\t\t\n\u0014\u000e\u000f\f\u0010\u0017\u0014\u0018\u0018\u0017\u0014\u0016\u0016\u001a\u001d%\u001f\u001a\u001b#\u001c\u0016\u0016 , #&')*)\u0019\u001f-0-(0%()(\u0000C\u0001\u0007\u0007\u0007\n\b\n\u0013\n\n\u0013(\u001a\u0016\u001a((((((((((((((((((((((((((((((((((((((((((((((((((\u0000\u0011\b\u0002q\u0001\u0003\u0001\"\u0000\u0002\u0011\u0001\u0003\u0011\u0001\u0000\u001f\u0000\u0000\u0001\u0005\u0001\u0001\u0001\u0001\u0001\u0001\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007\b\t\n\u000b\u0000\u0010\u0000\u0002\u0001\u0003\u0003\u0002\u0004\u0003\u0005\u0005\u0004\u0004\u0000\u0000\u0001}\u0001\u0002\u0003\u0000\u0004\u0011\u0005\u0012!1A\u0006\u0013Qa\u0007\"q\u00142\b#B\u0015R$3br\t\n\u0016\u0017\u0018\u0019\u001a%&'()*456789:CDEFGHIJSTUVWXYZcdefghijstuvwxyz\u0000\u001f\u0001\u0000\u0003\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007\b\t\n\u000b\u0000\u0011\u0000\u0002\u0001\u0002\u0004\u0004\u0003\u0004\u0007\u0005\u0004\u0004\u0000\u0001\u0002w\u0000\u0001\u0002\u0003\u0011\u0004\u0005!1\u0006\u0012AQ\u0007aq\u0013\"2\b\u0014B\t#3R\u0015br\n\u0016$4%\u0017\u0018\u0019\u001a&'()*56789:CDEFGHIJSTUVWXYZcdefghijstuvwxyz\u0000\f\u0003\u0001\u0000\u0002\u0011\u0003\u0011\u0000?\u0000F\b^F1.K\u001cr[I\u0016?0\u0019=:VFD>\u0019W\u000fp\u001e.]\u0004falp\u0000\u0003\u001eo\u000fh\u0018#,\u0016?hV\u0005\b[\u0018@\u0018?jQ???X=ΟZMmmXHF0\u0007\u0015-\u0014\u0005/oh\u000bu9hUIE\u0002!\u0016\u0006??fa=*Z(F[\u0000@?{\u001b)\u0000!g\\\u001e?\u0010\u000f*\u0014\n?\u001e#\\?rsp<1\u0000{9Sp(\u001c\u0001S@?H-?*(-B(\u0016Dqn-\nh\u0003\u0002bR=*j)\u0005\u0013?TzR\u0016T\b\u0014\u0011?P\u0016Dbc+@Zd\u0016\"[#Q0\u0000,B\u0016$DzS?\u0002?\u001fo\u0017w\u0011n[RQ@Y\u0010h\u0003\u0002bR\u0007jW+\nhb6?\u0002:ZcK\u0004Ok\u0014\u00048#\u001dz(\u000b\"$[-\n6\u0016?@b9 b6?\u000b%\u0014\u0005\u001cp[w@Zj@\u0018\u0013\u0012;TP\u0016DOo\u00031+\n\u0014\u0005U\u0002\u0005\u0004u>%\u0014\u0005\u001an\u0010\u0002\u0012z\u001fJD[-\nh,QM,C\u001br\u0001oS\u0005\u001c(\u001fo\u0017w\u0011n\n6}jJ(\u000b\"\u00150&%>Ikl!\u0000@J,\u0016#x-?\u0010(#An\u0010\nTP+\u000el\u001fLg/\u0018\r?\u001dM\u0002cii\\.NpSfwwae\u0014I\u0018P\u0004-\u0016\u0015A\u0003$v B\u00121EqS?\fg4F\u0010n\u0012\u000e8\u0019E*\u0001K??mG}l6{*X%о_?\u0016\u0015QO(?\u0006)\u0019\u000ea?QO!\u0002F(>_13<a\\e\u0014\rPs?n]4Xw\u0012s`:iq_j\u0016s?\u0016\u000b(\u001bV5\u001d\u000e/'\u001c\u001e`\u0000\\g5e3L\u0016HuECF\u0015 ~NMW$?K\u000b\u000f>\u0011XX\u0016cV~l\u0000k\u0012C8\u000fh$dpr\rM\u001d?y@p\u001c,+\u0000?hp76vwQO!\n#\u0004sB~SnE2|a\u0003\u00101Mpv\u000eQO)\u0002F(o/]?\u000be\u0014\u00038@x;M\u0016\u001dJ\u0016;\u0001N!\u0002N9qSǘ\u001c~x {1EqR3\u000e\u0014\"\u000ehBlj,+\u0002]hp71@vu\u001d,fN@i@`\u001a.\b3>юybZ\u00047sTd%\u001d.\t\r\tG\u0004\u0012y$e\u0002$?\u0005W?;iZBG\u0006p\t\u0003\u0002r\u000b\u0012\u0003?)BWR8怰o`.1qn\u0010I\u0014?F?=hbd\u0004S\u000bn\u001b\n#Jv\u001avf$\f\ncp:BAH,oi\u0002G\u0000C0AzF)B\f:\u0010F?iv%\u001c\u0010I\u0014L\u00128=)\u000e\u000b\fAc`g-\u0002dk\u00000)'3D\t\u001eX#2Q\u001bN\u0016\u001cw[\u0004MU\u0016\\(c\u0002i#+.\u001avv$\u0002\u0005s.xQ3\u0015Ct=q{\u000f?\"=\u00021k*>:\u0015XWz0Af;RkeI瑲Hr?9!#K?\u0013<C\u0007?36\u0019R}h?Cu4Yd5\u0000gp1[N\r7G9\u0000~\u0015В8m1^W\u00020A\u001dzzuz=\u0014\u0006?32Q?r\tq\"+2F\u00179dm9oft\u000bKB7\u001a%?!}\u001eV{\u00108\u0000Tdtk(cI+L%;Y\u0005K\u00001\u0000l\u0001~W\u0003.1Uz~\u001buIIv\t\u000b\u0000v=\u0014\u001ettlk\u0019_.\u0003\u0014\u0016h\b r\u0014*é\u0019\u00020`K\u001e.Z\u000e@Q\u0004I4)\u000e\u000bF#qdk\u00000)?3D\t\u001eX b~z\u000b\u000e[\u0000H\bQ\u0016\\)\"d\u00048.7\r &\u001arH\u0018\u0014\u0006iG\u0000T(0P\u0004\u00040i,\u0007\u0010A@HA\u001b\u0006R\\4\u000e\u000b\u0002fF?8\u0016\u0004Q1\u0004\u000f4J\u0016\u001eZ1J`\u0004\\c4Z?\u000eE4Z\"(\t\u00148\u0016\u0017aRL(L]\u0003\u0014 02\u0015.J\f/ 1H\u0018R\u0017h=i\u001cD\n\baХ\u0004l\u0018\u00120M\u0000?ΙOk\u00006R(C\n(\n(\n(\n(\n(\n(\nHt]\"D\u000b<\u0001Vxǎl:Do?H9UMF<\\uI]Z{5c\u0003\u0000\u000fJP\u0015Qtc??7\u0016\\zwTm:/.3\"p:f\u0015$\u0013~\u0015&Qr\u000fE\u0007?S\u0000:?e\u0011\u0000\u0002\u0001b?#?-W\u000f\u0011\u0018_?\u001epq,\u0011&K\u0014\u000e,\u0006?Zdfi\u001c\u0016\u0001B;@jL!\nG\fE\u0017FUI<{z\u001ao?rwDssM7\u0000xSms@q\u0000Zo\u000e6=?s\u001f9Y\u0014CT*H\u0006$msB{[/}`V7fG^\b>\u0000\n\u001by\u00183g Y(\u0000\u001aB*GGD\u001dQE\u0015aE\u0014P\u0001E\u0014P\u0001E\u0014P\u0001E\u0014P\u0001E\u0014P\u0001E\u0014P\u0001E\u0014P\u0001E\u0014P\u0004?{h\u0001`\t=YUM|#T\u001bj3Ld+NUQJI\u001cJPL[?\u0012hUh;( V\u001d\u0000*\u0018t\u0014*h*\u001a@.iXB(\u0004\n,\u0017B3\u0007Z|F1FFc7g9s\u0003\t\"\u0005?g\u0011\u0003\u0002h?V\r;\"@_b\u0000\\\u00068\u001eH\u0002\nr=iHFGz\u0003yA?\u0012{`\u0012EUU*'ULL?àUhY\u001d\u0010z\nB\n\u0002\u0005?U\u000eG4v3z?L\u0002(\u0004\nM??nZ,lF|Q\u0017\u001f\u00105V% \u001f\u0011s?\u001c@r+o\u0016ul\fGi/x=\fyn;\n\u001bFy!\u0007VQ9\u0019\u0002|\u0018)?2+\u0019?\u001b_\u0006hSβ\u001f|O&L\u001a\u0015y\u0011<?\u0007C\u0000kzVs\u0006O?d`>m\n0rN}T<CZC^Z\u0010}Dk>:5g\rmr\u001aC3\u0016#RZ?_\u0012r\u0000k\u0011(?rZe_\"\bk?3hXkE*~2\u0003\t\u001exg\n=lDW~?x}7R4?=[\u0004\u001awilo{m\u001d\\`\u0005rT}\u001b#*h^TZ? {*\u000f;Bc?*?+WWL,F?,[0\u00030ZJ{1Yy5?p\r\u0000?\u001f1>jI#\u001d|\u0000V\bSv}l2-K3$\\H3r\u000f\u0005}aw\r&\u000bh\u0018;X\u0002?C_\u001b\u0013,k??q1?\u000f\u0010\u000b}\u0015\u0000]-\r!sUBmULL?àPв (? P\u00175*hZo\u001b3?l\"\u0000@+`?Y\u0005UQz?0lN\u0019u\u0004M\b\u0016Bh\\#U`i\u0003l`3\u0000iT\u0000:\u000by\u0011P\u0002;`\fp\u0003 wJv6iwyj\u0002?\u0000`egPA\u001d(_ U\u0016gI\u00126\u001du(\u001a@.i?l@0(_D1\u0019=i?\u001bp?{\u0006.`f\u000e(]eQ\u0012l(T\u0016l\u00108# Y\u0019r!feP\u0000Ec#\u0001\u00068\u0007\u000e\u0000b\u0014z2E@\bF-^6PIE';f}y;\u0002)F;M\u00004\u001bih\u0013c\u000721?\bfQ#\u001dE+c?v\u0010Nj:(X~\u0013'q3??\u00133\u001e(XPrB;3Ns1E2.\u0016\u001e\u0002)F;Л\u0019\\wje\u0014\u0016\u001c3C\u001c\u0016Tc4)\\,I& ?4O(34)9I#LzW?\u0017-?mm\u0000O\u0006\u0000?gh> O9\u001f, \u0000v\u0006\u00115pO\u0004?p\fЯ\u001dpqu\u0003?R?M1\u001bs\u0000u\u0014%-d?`_?UИ=B_^\u001eb%?$O-qhv\u001b~\u0005B`YVXCBcu>\u001c{\u0011W2\u000f\u00127\u0013\u0000g=Fv?8\u0004r\u0014\u001cgo d\u001f\t\u001f<Cai6#iu'-$mT\u0005m'\u000f$5*f49g\u0000\u0004M~[\u0005WC=\u001dguφq3\u000b\u0004f\na\u0003B3W[h:>[.S??Z\u000e/\u0013.E!f,Hi9]!f?{}^m&kGjO-d\u0011$`瞃\u0017t}_?z+kg\u001enm\u001909\u0005\u000e<\u00018\\\u001c~\u0015\u001f\u001b|\u000f}xBQEEU\u0001\u0010|??\u001d\"?\u000f/izk(8kw\u000e\u001b\u0007\u0019\\p\u0005hhRw9l磐z1PA##?\n?\u0002\u0011v=9nf?Ca\n\tq3?\u0007?,\n5Q\b\u000b~\"f?V?B~|\u0003n\bbrC(1???L\b\u0000\u0012In$'*s|\u001fτtN\u0007QfgpF\u0010\u0004vk\u001c\u0012ni({XMc?L'34)9I#*LzqLS\u0015%\u0014\u0016\u001e+0ClUC\u001b\u001dSXz(B\u0010\u0004vL±*v&a12.\u0016\u001f\f|61c+\u001ce\u0014\u0016\u0015NJ\\f\u0011iR=*(C\u001b\u0017cGKEa?2iy\u0003\u0004j$\u001b( \u0013MMXJ)d]W ElR\u001d)\u0019h\u0000\u0007jH}E]\r0g3O\fl\u0001 (]\fc\" \u001cE\u001f\u0004\f\fE]\f\u00143\"r\u0007\u0014y\tE>H*wP \u0001?1s-QN<\b\u0014a\u001ec@x/7d]\u0016cIag\u0000wi\u0003\u0000\u001ejSl\u0012\u000f\u0015?\u000b\u0000\b6p@,=1K\n??N'\u0011mn\u0001\u0006}-Z1w?2\ff3?XDRu\u000e=A\u001f?WS5!Q\u0019h\u0000>Q[U~\u000f\u0015*/64\u001aM?yu,\u000e+w[-\u001bv@88zO\u001e}*P/J?52;\u0013qZ\u0005NA\u0011?\u0018\u000eMrW%c#M?\u0014.\rc>?=;Jy\u0019\u000e?0=RE\u0017%B73<CimiX\u0002c|\u0010T?FS\u0000\u001d\rq6>\u0001;.5]f`m\bQ\u0012gq{\u001d+\u0012[5^eHaY]R8?;\u001c\u0005\u0003IK\u000eJ?kU+=>\"&\b\u0000>?tG7\u0005s]o?uxP\f3\u0007\u0016qh\u001f#fbFy\\NvQqy\u0000w?\u0013?llj\u0012Aee\u00126\u0012'sebY=\u0017=tX\u001a\u001c?''\u00023)%faI$\u0015\u00165TF\u0005T\u0000\b1\u0016|\nK\u0003vc-\u0011|\u0003V\u0016{\u0015?\u0018(?c\u0010;qc#3GA?SO-\r+FDA\u0003Ye\u0014.\u0018\r<cR\u0017 f1\tE++,~X\\9\u00190\u0019E<FLE\f\u0003?$26@<f\u0017Ch\u001f62:4S}qS2+\u0007=2 m\u0014Y(wPq\u001dv\\-/Вqg?4(\u0014Os>?b#?S?\u0011vq\"\u0010mei\u0014?8\u000f7{GHU\u001dqfE;3N}\"R\u000e)\u000b\u0014?\\?y,7\u0004?\u001b;?1\u001bIMd7g9s;@\u0018?sLJq@f F[9\bcIFDTS\u000b\u0015?w?Qc?<\u0003\u000f?\bc)IS0?NTc+*;aE#3&?\u000f[d`!CR\u0002QRZs~偍$Rm\u00063&=)Q?yG#\u0011oq8?\u0000\u0001qk\u0017|\u0003\u0002\u001eoA}\u00119OcK\u000e\u0007?~\u001exT)\u0013atg#\u001b\"no*bTX\u0010O~\u0007JbQ\f3\u001b\u001c\u0015=+QRV\"\u0012pjHf(4\u0012\u001b\r\u0007[T|\u0015\tkfK\u001f D-?_o?\u00004\u0010Z\u000b\u001a\u001bnT\u0011W\u0014*н<m\u000b35,\u0000\u001dMSu7W2.w\u0014\u001e\u000b8e\u001e<?yi=Z\u0019qF\rdh\u0018/-ie\b\u0015f\n2T3MIJ7]CVNofo>E78_Jo|Ia[2|?;{?K5GJQ\nX\rq\u00128c\u0002Ei\u001c?|Z$o\t\u0007en~j9'?pJd~zz{S$\u0014pXBz{a?t\u0015\\t\u000b(=0vp\u001aE.^hR@G_\u0018c\u0010v}o?I\u001a@Y-#Ab\u001c|?\u0000GS*sN?U\u0000R\u0016d\r\u001aW5\u0015DqHJy[v3=i\u00067\u0007\"hvzP$\t\u0018)ck2?\u001c\f\nhf`恃%;E\fQ\u0015\u0014J?\u0007\u0004ya\u001d\u001cn=(\u0010+?\u001b\u001c\u0019T\u0000Hw\nL?#`\u0001@\f*NU;3O}S?(h|\u001e[?\u0018>fz?$J8n#\u0012\u0007\u001b?f28\u0004\n\u00041W\u001bY\u000e\u0003\u0018\u0014[\u000bV&&eF?m?QQiH:8,J\u0002Gn!S\u0000Z\u0000\u001fM{\u0012Z)\fJ)h\u0004\u0000JZ(\u0002(\u0004\u0000(L\u0003\u0015\u001f\u0006?\u0001\u0000r\u001f^+,>WE@J]^4{\"/&5Pz?ly7\u000e+\u001a\u0011\u001d?=p/?Z+9\u0019H\u00170Oq>5hV<\u000f&\u0017\u0000\u0001G\f?#?;\u001a3\\#?\fc\u0012i#\r+\u0015' -[?Q{\\e??j\u001aC\\Ok4r\u0018Gl8u?z\f{?\u001f\u0000,4~\u001f3^?l\u001c\u0016S\u001d\t?&\u001fi?fM\u0000\tq=oD75fv H =]}湭^6$6P\u0007\u001b?\u001f/?q\u0000\b5svm/s?sm\u0000\u001ey?OJ\\\u001e(s6\u0015*fF7,S\u0003??\u0018o=xF?{\r\u0019F2ktK\rh$+\u0014kH8\u001cuQi\u001e\u0003cd^]\u000fv3f\u0000Zt}\"MFт\u001f\u001b1?tW?Na5\u001dF\u0011\u001fs?+\u000eaS\rft\u0019? ;\u000b\u0011\u0000|z?SQ\u001bOx/5S<O\f\u00122K r@G?|\rk\tKg#\u0002w\f;o~*%\u0017%P\u001a\u0011T\u0005\u001fjxC&S)?cd\u0007XNWm\u0017\u0015k3\u0014\nZCIJhAIKE!(\"A!(?`%\u0014R\u0001(\u0012Z(\u0002VivPY\u001dMI9?O4?\"'zMkF*zVUR\u001cwjD!Dw`!`>QDhW#\u001c\u0016\bN?j,\u0016\u000b\u0010?\"4d\u0006\u001cipQ$D{Y?\u0016\u000b巖\u001fh\u0019tH\u000f@I?s|v-j\u0015z\u001dJ1V)`D(N.?d'j-/GUpzP?E\t%'e\fpX!;3F\u001a\u0019\u001b\u000b?A\r*I6/?sII>fhI\u00118~,3M\u0019\u0011XD`?]\\\n3?\u001es\u0013?\u0011E&saO\\8k\u000fG\r\u001a\r])?k\u001cP(ew??\u0002?a}a\u001ft\u0013>pXLb8$;8\u0003]\\\f\u0001J\u0016??;[gK\u0012\u0003G\u0017\u001cW?i?qiS\u001btf@I~hK1-\u0015\u001a]\u000f?gHcVTs\u001dqch#\u0015h6wvCp\u00182\u0012\u001cs~~\u0000j\u0015UG\u001f2?\u0006)i,jXZ,KSuW\u0005\u0013C\u0016\u0003b\u000f<\u0015s?\u00051 \u0016\u00134r`aI?4{\\qw!\u000e?'\u001eZ\u0003\u001c\u0002I\u001d赵\"R\u0003/\u000fj\u0000<3h#n\u001d*UU 9\u001ck?auA4L,\u0017w\u001cdH\u0004?\bd\u0012*\u0016\f\u0007\u001e?\u0006B\u0001\u0007\u000fz9\u000e\u000bSo2h\u0015nC\b#\n[?$ij\u0016kx\\VSF\u001c)A\u001a?[c\f0\u0000@sG\u000fj.?d'j[\bh\\0w\u000b\u0001?\u001b\u0018?T!|v1`\u0016[\u001a\u0019\u001b\u000b?o|}f&#ft\u0015\"4g\r?\u001b\b?rmf>s\u001erT1;3E#g\fW\u001c*`\u00179\u0001'm+l\n2w?\u0018Qd\u0000J\n9rw\bg_4v \u0017(\u001aF1\u0014?Xy,9wc0wm\u001dsY\u0010n'\u000eOT?9\u0014XW\u001a?9\u001c\u001a\u00126pG\u0003\nrT9;3C\u0019Dq\")v\nM\f\nR9\u0014\u0015\flwv\u0014bE\u0005$c\fc\u0007u3cF'\u0007'Ms\u0000i\r-\t6?ni'\u0003\u001d*:(I\u000b4jry['\u0004P4\\9Gb^1NQvj\u0007\u001d(YofA\u0016\u0007Z\\2r*\u001aSZ.\u001c\u001a ps?kMN\u0006*/?.\u0016{i'\u0003\u001d)\u000b4j`u\u001cQE×Bl5$SJ1C/\u0018Ja'\u0017\u00155?\u00024GR-y?@\u0000\u0000*f\u0011;\u001c*Y\u001d-\u0007wO5s{\u0015cK\u0014i@bWPEQeq\u0010u Rv\u0010@EDOI@25]6\u000b輛7n\u0005\u0018\u0007?}j\u0011[\u0004\b\u0012(\"(\u0000\u0018\u0002sI\u001fZ\u000787\u0018jCL[_2\"D:H\b`OZZ]p1?o]\u001dF\u0017<gj/?n2?N\u0006)7+E?e%\u0017'Bl5$Sv67g04v\u0017\u0015k'M?\u0016\u0007Ze%;).Z\u0006tP\u0015'\u00075\u0017ZZ.\u001c?b-;*0*.tH]Z,\u000e5pI\u0015\r\u001dzW\u001f)&\b%\u0019Krx\u0018\u0015\u00178\u001d(Y\u0007i\u0015bK.\u001cTuА+CN\u000ez\n\n\u0015\u001d\u0014\\,:ot?E-,A<d\u0001Oq\u0018\u0010pF\r)V\u0000\u0012\b\u0007\u0000%\u0014X \u001c\u000e(,p&\u00129\u0007\u001dYJ0 \u0002QKB6v\u0012\u0006I\u00194\n\b\u0004\u0000\t\u0004\u0003?T\u000e\u0007z\u0002wT?N@\u00155hwc*??3S[!8\u0014~\u0002j+|`:P\u0017j?:)\n+?O\u001cVdy9#8\u0015?0>fKoRp\u0015B\u000f\u0001[\u001f\\UpWg]?)K\u0002=z\rq^\u0015y\b?vKF\u0015\u0015\u0014\u001e(2*4?S\u00117/?S\u0003DB\rHO\u0014Hhy\u0016o\u000bZ'\u000f\u001c8?:n\u0016tL0hfS\u0018\u001e0*H#\u0007??\u0019JX\u0011\u0005bp;M?\u0015K\u001c(&\u001c\u001cP\u0002C)S\u00184m!C\u0010@.\u0014P?\u0002q?\t (?\u0000RA \u001a\u0019Yq\u0011\u0005?\u0002v*8Q\u0000CA\u001d?8a@\tE)V\n\u0018?V`p\t\u0003\u0000%\u0014\u0000I\u0001FM\u0004\u0010pF\r &sI\u0015\bwHG49'\u0015\u001aM?VBowp\bi\u0014Y\u0011\n?J\u0019L?\u0014\u0002\"&\bh\u000fB7\"TV)HH\bDtd?A3\u000fM\\?\u00037i ?9|\u0000L`t?A\u00022L?]oGZM#\tg*?$P\u0001\"?\u0000$\u000e0i\u0015Jmv\\e2\u0016t\\(\u0001I\u001dM?jRD\u0015\u0010\u0006\u001f\u001b# ?\u0007V\u0003\u001bH\fe?J?S\n]F\r\u0000Qf{`\n\u0016\u0010O\u0017??\u0013[5\u0018Sе~VF'?\u0010XE\u001bHTul\u000e\u0005q?4V;5b\u0000Q]\r:on0yI?NS~4\u001aVr!e=\b ?;3X$H\r\u0013/?N?z5<\t\"\u001c\u00191M\u0004F##\u001akes\u001c7wE?:K/η\u0010r\r2+ā0A\u0012\u0018U\u0018\u0015\u000ff\u0012C*)b u\f\b?\u000bm?4\u0011~ii\r4\u001c3\u001f6q7eI9A\u001dp\u0018d}:\u001fo \u00047zz;kq2K\u0004lJ\u001d\u0005t%L\u0017\n)*E+4ReeW|\u0001?@?es?UcL\u0010(\u0015\u0019\u0019>le\u0015r)6/7*\u000bDGJn?3o?\u0000r@M\fX,e03910:Rn\r\u001aO=h\u000f\u00187= F\u0011?O]d@I\u0014S\u0019Wu?\u0003\u0014(]oJ\u0014VG\u001d\u0019Ku\\-9UcL\u0010(C\u001aXd\r\u0000pcpr:Mcd)g\n\f/\u00139\u001d(\u000f6\u001foJU\u001es1 w8\u0012m3J?\u0000t\u0004\f\\,l\\?13*>AA`*|B+ze\u0000\u0018GG\u0005oJ\u0002\u0011?nU1w\\U\u00019&g?\u0014T\u0002I\u0003k\u00006?B2\u0014\u000fZE%H (;\u0001$I4I?%\u0014\u0005n?+2\u001c RQE\u0006A<X'?(\u0001K1P\u000b\u0012\u0007j\u0015A\nH\u0006]J\u0014I'& @\n??U-jW]2XPb\u0015\u0003s?sU?>uKh6B3zP-E\u001a\u001a`*(\n\u0000\u0005AN-e\\\u000fX\u000f\u0016\b<?\u0015I\u001aE]\u001b\u0013K%\n\u0013\u000eXā\u0000\u0001\t3\u00059`GZ\u001fk]\u001a9\u0017l73|~d54g\u0000@f\u000fWM<4'_>e\t5&QJ9SOq]\r\u0010\u0006*u6uPn?圃\u0004j?U`\u0010yn`[\u0004!85-+?W?j\b}3?I?C|\u00144\u0013d\u0002b?[D?\u0011\f\u0005e<\fQqswz2\u0017eN@H~5?eR\u0001 \u001en!Np\u00002\u001f?\u0019J??\u001a\t$?(VfoI?IE\u0017ad*.v)\u0001 \u001eh?XY\u0000I t\u0014.\u0016B`\u0002@=Rr\u0007?p\u0003rz?3\u001c'Fi3EmrvP?RQE\u0001 \u000e\r\fK\u001c?\u0000R\u0002I\u0003\u0001)PH\u0007%\u0014\\,OT;C4L?I'MT#4TZb4\u0006iGJ,>b>\u000fH]WOZ\\5pI\u0014X9b^:?k\u0011JE8?\u001d\u0004\\ue?Nr*:T\u001a ps?68\u0018GEH\u0019(\fQW,\u0017#\u001aEY?K\f{\u001e[\u0005O\u0011\u001b\u0011k\u001c9?{\f\n\fi\fI\u0014j\u0015\u0011B\u001dE?lGZKd9\u000f*\u001f{\r]|\bay\u001b??r\u001e\u0014Cw)?{3\u0003\u0006H;**\\2r)64A$+\u0019s\u001cDdn\n[\\!???\n\"Z&RR\b?? h7Rn(\u001a\u0014KKD\\]M0zi\u0014\u0015r\u0015M\u000f?8\\ph\u001ag-iiM0rd\f\u0004+]R$:n7+)#W\u0000\u0013x?o}*\u0000u\u0016?&?\u0012\u001c!o_Oyq.QRk\nMq?V9GEJC[8?)$Rlmo\u001dhs\u0011=\\5ó\nMq?\u0005r3\u0007\"+EN\u000ehs\u0011RmiC.ZvU`Q`\"rxdFQ`\"Rl`^:\\9<\f\nV\u000eb.\u0015&V.:,\u0017N\u000ei.D(6;$捍*\fRs\u000emi|sF\n\u0007fin\u0018??F\u00027v\u00073y`}PF\u000e#\"^\"1F\fE\u0017\u000b1?F?<1\u0012zt4?u \u001dI \u0014cy{?h+?\u0014\u0000MJ\u001b8\u0004:4;H?\u0017\u001d0T;(p\u000b1\u001d4\u0014\u0014$?\u000fZ4\u0015\u0010F\u000e.\u001b\u0006\u0007jllQ/QLG\u001cJp\u000b\u0003\u0017\u001dYG\u0019g_C~\u0014\u000f?f+!\u0011=\u001b\u0001\u0012?\u0006\u0007Z4\u00163C7\\\u001c(Mf\u00068I\n|?EG\tj/\u001c]\u0014\u00189|u\u0007[E[d\u0000y(\u0007\u001ds??}6.(l)\u001dH#nn<c\u0001?l/]K\u0017@r\b?\u0003;!\u0000\u001d+\"[F\u0007}N\u0003\u0014\u0000 Wgg\u001e\u0002:-\u0014 qJiE!f!\u0013c?c!q@\u0012wPKh*'\u0003\u0018\r\u0016?]?′4TWCe`A\u0004d\u0010ES\u001e? =\u0000Ж2bu\u0004&c?0J|$\u0002`r~~56\b0>fx[|W1?2:V\u001d\r\\?ML?1?vooc\u001e?,/&\u00068fY\u001aB7vo/f~Z43Y6\u001cbP9\u001a2v%X09f=6\rPwv@\u0003)\u001d9v,iZFeU=\u0007J4\u000b0q0vSa\"\u00078暲2Px4#n^]\u0005PDf61L$$<? j.\u0016b\u0001H;3N\f\u0007\u0018\u001b4G#G\u000b1IO-B\t\u0007\u00121IV\u0004u\u0014Y.;=@\u0003)\u001d0bfUSzP2@x4h+=ott_M)l\u0014QE!\u0014Q@\u0005\u0014Q@\u0005\u0014Q@\u0005\u0014Q@\u0005gkf??{\u001ak\u0016P| y\n?@-PiAf\u0016?\u001b\u00144?\u0000i\u0017?>a&0?\u000f]us~\noH>ij\u001fU^?-P?\nDl\ta-*Y?[\u0012?DHQ\u0001fc\u0001N\u001f<Bt\u0014\u0000\fc}ɡ\n%m.U\u0015\b?\u001a|U\\E\u0014H8Td])k*0>Wo\u00001w l3!qV?k\rQ\u0001U@\u0000R+-WjrjM\u0005DEn\\\u0005q\u0014-|)?l\u001d~$a{Y\u0001\u0007Zt3\\[)k>^?3H?f\b3U.KYηтB\u0019\u001f\u001d3H\u0012?9\u001bO@,R]\u000bH\"+\u0006F\u0019\u0004t\"X%?9\u0000C\u0002Iwz=+b\u0006\u0014QE \n(\n(\n(\n(\n(\n(&v\u0011.\u0007$4R]*OO`4c\u0011qQU]Ul\nAp\u0007jR|\u001f#\u001d\u001b\u001d?\u001a7;P\u0001b:2|J?H\u0002laB\u0011#+b\u0010\u0010d?\u0000v\u0016<c4o'M!\u0018a>lS+$L(0sg*mrW b\u0010)kM\fR?;R)U]ZW;\u00196;\r-,=o)>nD\u0000?\u0010\u00023\u00003V?@f0\u0011K1\u0000sYDo\u001e\u000f9L\u0000}IO?k+8ywUQGJ$`\n\u0006I5x\u001dg9a\r\u001d\u000b \u000fs\u0000}\nHhmm0\u000e>???\u0000rri\bA\u0010`\u0000>zU\u001c\u001fs$ZZ\u000f|<g\b\u000fO? \u0015E\u00153j\u001ae7q\u001c(?5R6JQ9\u0016qFh\u0006IHM74\u0015?\u0012\u0016P]'\u0014Mln>i>T\u000b)\u0018)`\ry&h(W>Z>j9Wu\"\u000bo\u0010i3[.\u0001\u001f\n\u0019LWS^\t\f?<S.\t3]S\u001d6;)?9_N6R\u001b\u0018xdv-2n\r\\\u000eqVM'%swgHf\u0000\\$#Yz\u0000gvOp5'eXc\u00004\u0017Mgv\t5\u000ed3\u001f?%x\u001cj\u001dN-n?Qл\u000e\u0015>?m\\`frcL`t\u001a5'?k}nJ?\u00137>=h\u0010\u001a?FV|\t\"TQrv+?w2B\u0002W\u000bNc\u0015T@\b\u0014;K*1N}19$h\u0010?\u001b?s\u00033\u0000&fo=)P\u001f@\u0001n\u0000o?s\u00033\u001a`\u0001?fiDnDNpG4?\u0003oW@I)\bEY_vs#?\u0005\u0006242u\\-\"#\u0010m\u0018zun`⑤\u001a1?ot\u001b?%d>96+\ri\r%\u0014;!]18{\nty@L?tSh??wQE\u0003aCS1`\u000f6\u0002{?5]c\u0011?\u0019v(UP?1?[\u0004X^\u0000*UO,zl\u0013\u000bN\u00074\u001a#\u0003Z<1\u000f\u0003\u0015\u0000t(\u0006F3Lu\u001fs \u001f\u0011x5S-8f\u0005?:(O-\u000eGz=\u001ee?\u00001]j<Y#6?m\r$;\u0014^DF7#sWj+q\u0010{\nGb\r)5\u0005To^,R\u0007@E\u0001bBi\tL-\u0003H4i\t\u00033M$*Ч<\u0006gPr\u0006Ah\\d}\rn~}RWu\\?\u0018~t\u0000\u000e}\u0007OfL\tQr*+N'6\u00014w18\u001c#;kskT\u0011s\bQmv\u001f?`xDf\u0019\"]B\b\u0018eH#Sf9T?J=?e\u0001\"\u000e9#?Zp?\u0012X?\u001c0#EB*+\u0010e$m~?r%\u001e`\u000f+?\u0015v2.gd=$\u001b&s?'6\u00014(Y\u0003\u001c8s|y\u000e0;S(\u001ed!6\u0011IALl*6\u0007\u0019j$}[\u0000g?<mn>I7.6d\u001b&s?E\u0017bV?jf&\u0007D\u0005Km\u0002z\tE9fc\u001a2N\u0017\u0019E=J\b0;Q\u0018Uq(\\e\u0014u`fimg*zQae\u0014S\u0000\u0003g\u0011]U9\u0014XW\u0019Q\\ΖM&vF:R\u000f2!c\u00035+Z/!3C#\u001c\tXrGu\u0007\u001dyt&bx\u00141< cg:}\u0000~#&&\u000e\u0019\u001fzja`\u0018w#\u0007c8\u001fLX:jM\r0iH.nnX6A\f\n?\u0015\u001fqn9\u0001?\u0019]\\\u0000e\u001c[Kv\u0012mg?b\u0000\u0014\u001e\"'{ZɑGNZv?nGK\u0001)\u0015%Cls\u0004J5%\f20zVd?e,>:%6V)\u0011$\"\u0006Si\"fc0=3^[\u00163J\\\"c*\u0007S{w\u0011\u001cg\u001d}A]VvAV\u0018t{!\u0004M[C#\u0019?]*GER\"!4\u0018-?'>{\u0017\"S)A\u0013\u0006\u0007~xW9\fm$C+y\u0017CW70]KgzyV\"PHU<O\u0000#??\u0016JaGWG\u0011`tF\u001f\u001fnw:*Fs\u001cov35)\u0002,\rF\u000f|~DUKci2]?\u0015?9 L\u0016LvD\u000e>f\b\\C7[\f?bSgc\u0014;Z\u0002\u0001hNp\u000b1E;E96\u00121\u001d\u0014PD3?￡N1E(;vf Vs)\u0014Xw\u0019E<1U;Л\u00148NX\\( \nd\u0007i,?vQm\u0007}lHe\\\u001bIL\fl\u0005\u0017\u0004\u0019\u0003\u001c3!B\u0018u5Or\u0012\u000e,ЗE`A0b'RF?$U\\\u0016\u0019\u001eaL3\f?\u00163\u0001Q\u0005Q{Sdb6.\n.\u0016\u0002\u0014\b?[l\u001a;227v#Z]Y/\n\u001f-5?R:6VS;y?u?nQ\u001ed,'r~OZ:d\"?w?Vo o\u001e??\u0005п\u001e)\u0003nTz\\b\rt\"Z\n\u0019\u0000gJ\u000b?Y4tPkT`\t>0kHLL\u001e\t\u000f\n.\u001bTC#{??\fF\u001e\u0016oa\f\u0019B\u0006>c\u001fqRek?\u000b\\8\u000e`SwS\u0016=h?xeo!u\t\"\u0000\u0019/zn\bρq\u00079]t,z\u0015DvE\u0003\u0014RH\u0019\u000f)\\]I\u001d\u0014\u001cD?\u0015\rnT&\\QE\u00028O\u0018qe5m+<\u0017\u0006!p?\u001c\u001ai?b]G?\u001d\u0012L~dběXd\u000bc3#\u0011vLqHK\u0010]=Z/%\u0002'\u001e\u001cv?\u0007wr~ff<o\u0012ig`]G\u001c7)oB\u000f·(_??5sQF\u0006\u0007Ju\u0002\"\u000b_?@\u0016FFL\u0000Uxa\u0011M?h\tM\u0019 U$Rl.!aU\\\u0017\\JF  `g\u00172\\H-/1I\u00013>T.\u0000fh$\u0006)\u0003\f\u001cq;\u0015hYMeu\u0002e)J\u0014U<35?bN*A\u0014\u0018u#\u001c)Zw6RK?&n6<I6\u0000??/4),x\"id\u0012\u0011\u0010\u0001>v\u0000\u001c=?24\u001aze]Ax\u0019dzuv\u0016D?S9%1$9j=\b\t\u0014q(UE\u0018\u0000\u000efl\u0001?)VrP`zR!E\n#aaK4[V\u0005iH0\u0015e`I\u0014d\u0011e\u001e&Up]w\u000fJ.\u0016\u001dm!a\u0005\u001f`0*&  `g>VFavP\u0016\rTEm9@Q?O(.?=hI??Lh\u0006\u0019'\u0000\u00190\u0004\n\b\f\u000b\fJY\n\u0003?4\u0011\u0000)\u000b4a\f\n\u0010*?d\u00110e\u001e[I?i6R(C\n()j\u0017?@^\\$?{s\u0007s7:?\\ZHN\u0017NUW,;\u001c_<Q\u0013$A2-AKo,L\u0010\u0001)4?jP0,p\u000elX\u0004\u0012(i\u0007\u0011<zVyI\u000e2\u0006L\u0006\u0017>?`>y!m'K6\u000f}\u0012da?$u{I6\u00000\u000fw\u001bp<K\u0016\u0000i`]7\u0001Pn#\u001fU\fK}:]I(I{saCs3c8kV\u000bXV\fj0\u00125\n\u0007+/L\u0003K0L\u001cToc^\"\u0006\u0012k\u001e\"\u0019n\u0011(G1l\u001d?n$_6ar,ZLL(B\n??G\u000b85T?_\u0019&A\t\u0011\u0016?_\\!@J?[E)\u0003X\u001f^?\fWM\\ai/\fWQ@nO{?\u001f\u000f6=X~*b&F8q4_]\u0015?Je\"LR\u0014?WE;YXcúh??KcR(+\u001dC\u0000A\u0000;?\u0002E\u0014\u0000f\u0001E\u0014P\u0001E\u0014P\u0001E\u0014P\u0001E\u0014P\u0001E\u0014R\u0002fT3KD\u0016\u0001\u0007sR?4`\u000f?\u0018P\bh1\u0005\u000eB\u001c:@WkeQJ\u0019?\u0002O\u0014\u0005hJH@ a\"1fv\u0014\u0004/8\u0014Z@`\u0001i?\u0000\u0002\u0005\u0016\u000b\u0000]7\u001c\u001e3?(@?\nv\u0010?g=iFwE\u0016\u000b'\bo=(!608#*p6CN\u0000?ZyR\b$RDqr}qHWM<?m8\u001dBu|G\u0015J\u0005?gtR\n*Yi2NmPO/\u000f+\u0012G&\u0014b\u0002%QB11f\u0014\tG \u0010z\nB i\u0000\u0000f?bq\u0000R\f\u0000U\u0018\u0014??g=iX.$\u0015\u0019C|lO\u0019\u0018c5`nT)[\u0004\u0012\u00106?g-iDu\u0003?\u001f\u0004\u0012mMR5\\\u000e\u0018T3LS\u001c\u0015&*\u0011)Xa=?d1]\\A\u000bb=Dbk\u001f{!]-p* U$EWxG?g\t8\u0007_V\u0000FY^,\u001fzfG2G\u001fZ\u0017ReE\u0000H\"G=\u0015\u000b\u001f?\u0005]\u000fO\t\u0000\"xiW:\u0000\u001d5f;RELav&obI\u0002\u0000\u0018G4(O)c\u00002\u0000搣:\u00003Gcb\b_\u0003\u0014n~\\N\u0000*\u00057ym\u0019Z,J\u0014>#9\u0018\u0011)\r)?\u0010I\"\u0011V\\\fg;\u0005?!\r@0?8\u0000bP\u0000X/v#p\u0003\u0010+N \u000b;q^2?\u001a\u0006V`\u000eh_5ByD\n\"\b[\u0003\u0014\u0014gV\u0003\u0019\u0012\u000e\u0002\u0018\u0014\u0005\u0000x\u001b=ie\n\u001f\u0011S\u0018hz?C\u0005$,\u0017cX Jz#T!\u0011?#\"\fg\u0005Zr\u0000\u0018(_daK\u000bC\fvcOf\u0018P\bF\u001aeA'+\u00053摶\u0004RI7\u0000x\u0000:e\r4D\t?\b.a;iRXV \u0016\bN?(LD(a\u0000_34FQIL\u0004d\u0002NT9M`E(N&?d'wjf(qXteKNF#s\u0005'ni(q?]HO'e.)V\u001f\u0019FcF8\u00022\u0001'fh\u0018,q\u001e\u0015¤}\u001c]p+??N\u001b\u0000\"v\f}\u0002\\\fm?H94\u0012]I\u000f{d(\b1\u0013?5\u0018[V5R3Wf\u001fB\u000b}9??O-\u0000V{Ucj7(\u0007??uM?\u0014\\\u0006O\b6+\u0010\u0003&?~?n?\\lmQK\u0013\u0000\"/WMN^*\\y?\u0000&)h\"\u0018\u0018'fib(\u0012RO\u001cS\u0014\u0014bvi_`T(NQ.\u0016\u001c\n1|DeK4Rbb2B4\n\u0006\u001eQ cf(p\u0000#f\u0012RzqQ?XU 'm,C)\\v\u001e\u0002_,?2w\u0015M\u0000?ΛS\u0018.X.\tQ g\u0019MXJ)d]WvqS\u0002JwC(\u0003\u001b61?$\u0007?0\u001bE\u0004|gZ|#`\u0003\u0006\u0005)\u0000>(w\\q\u0017s\u0001g,?\u000e;;aS0p;Pp\u0004vg(?G>ldui[?\u0019?6`PP\b\u0019=?ny?`\u0005?\u0000Z\u0001\u0003\u0002\u001b\u0013\u001f4h?o{\rB?Bi3A6)+KKQ;\u0017\u001eElVij6q\u001f5jDc`^)X4?\u0017(:#\tbaVL(\u001fw,e.?N?\u0016\u0000*L%?JLQB\f\u0003\u0019\u0006\u0001\u0000$_=2&o\u0010s\u0019lw]T\u001c;ir&4\u0010 S\u001c,`?\u0002;S?n34f\u0012\t\u001f\u0005?\u0019\u001dq5w&h>T\u0011CP\u0016 '\u0016bC3Fi\u001c1,\u0006\u0007zlk?\u0006*\u000eq?$a\u0002dS\u000bSy\bI\u0012yx4m\u0014c{4Sc6h]l6{F\u0016%}?\u000ew\u0001?\u0016{\u00072e\u0014\u000b3?\u001bX\u001dP$L>y?:f1&\u0011QOJ\u000bd*:eOjE\u001b\u0019ZD%OJH\u001d|7ozE&FUv??[ssS\u0012\u0007\u0014b[)sb&\u0006'8\u0004?\u0014?1\u00133JM/*?\u001fQH\u0001q11&\u0000\fH\b[9#|;R\u0001\tVDTO?J\u001ddLojB\u0002*:?J\u0000pt?D\u001e[+HUId|(V\u0007>%`\"LqI|>|lD8曵||_\u00054)\u0018r&i`b@B&h\u0019G\u001fdv\u001c_?\u0015|?f?@U\u000eK[#g5e'k\u001b{btGX&Y\u0019)\u0002kV`\f9X\u0000?\u001f\u0012\u0011&\u000e+x\u0004s5?\"`b|sM\u0018s7??\u0019Ю\u001f|?)i\u001fMa?<E\u001e\u0000\u000ejE\u001b2/T<5(GPc.?&k\u0014j=0\t\u0010\ns\u0003\"*MR\u001e\u001ccU&|\rJ\u0001=\u0002??#aP%r!a?t{P^%1lr?\u0003U\u0013*?yn\"\u0012m?)YVG?ib[)Ξ29+\u0001\u0012c7/g?\u00189\u001cm_/~\u0000=)\u0013\u00111\u0012I\u001cSUJv_4?L\n`bQ\t\u0006+1?L(C\u0015uEDwc,o>\u0010#\b\u00007j\u0005@U\u0011A岙S \u0002?)]D\u0005\u00004m?9+)14\u00017?Μ\u0000q>x\u0006&T\u0013g?)6\u0018p\u0000>zR%f2>\b\u0014\tU+ra34i\u001c1m?+\u0013\u001b2SHz\u0018TTO\u0000#J@##R\u0005\u000e\u000071~BM\u0000?ΙOs\u00006Z))h0(\u0000)QH\u0004\u0000(\u0000(\u0000(`q~1'*)jK7\u0007:j?v\u001017>'a6>4H&\u0015m9)\u0001\r:\u001b\u0010\u0014lU\u0000y-#\u0013\u0016+r23vi\u001e'\u001f,s\u0011mOtc\u0014A\u0011f\u00112aF\b[C?j\u0017\u001aK7b\u0011uU-?\u001b4Lì\u0006\u001cgdc\u001fPT\u001e5Mps:?q+4*Zj3mdRFz+S1a+)F\u0007Z\u001elG{nv\u0003?'X\u0016LH\u0002ti_?3\u001c3g\u001dKn_T\"?4S9\u0014b4f\fRb4f\u0013\u0014b4f\u0013\u0014b4P\u0002QE\u0014(\u0000JZ(\u0002(\tZ6Yvj5\u0005\u0001\u0000\\3H6&T?&5?\u0015ndUc7JT??R&7Y\u000e\u00166h0(\fqF[fvf&W?Qd\u0017c\b!8I\u001bF6=iw\u001c\u0011|(\\C\u001byah6vqH;gv??[d\u0017cQKQQ6&Blfw~<lqcfc\u00023\u001a\u0005?F?7\u0007vI&)?ww3ED\fm?)A6X>vv?Wg_\u0013%y2u9P?%O\u0015&QXfU`'c&\u0018?:8#??\u0017?\u0014QZd\u0016?^%\u0018 p\u000fv5q\r?\u0015x\u00075f[m\u0019N=\u000fPm?=\u0014?BI[6/\nY\n\u001b8=IbO??l.3?|,\u001d\u000781\u0006???$?y;Q?#?qTCg$U);fP*\u0000fiSft\u0005?/u\f?\u001aE^1Sy>A\u0002s>STt[\u001d\\}&\u000fJ\u0013no?)S@ksI\u0011\u000b8;2G:\u0011U7)lsB\u0019q\n\u0013\u001b?3F{\u0004q\u001b\u000bi0wmS?y;sI+,\u0017$cl7^RF\u001ciM8?Wvvf\u0002Xg\fW\u001cSQKUi?nCl?^]u(OQN6.sB\u0011xs\u0014X.F2\u0018\u0014??46\u001eNd\u0017c0wmR?6/\t-n8q\u001a6Xü`\u001clq/%wfi_\u001b?7\u001d\"`?\u00158?6??_\u001d@\u001d?5I29=)2\u0012Z\u0012i c\u00034\u00101$.?ǐFz?kw\u0004\u0010I\u0015\r\u0014\\9I6\u0012\\)u?0**(XxvdXi?\f\b9\u0015\r\u0014\\9IvIs?6\t\u0003\u0015\u0015-\u0017\u000b=\u0006i! bF\u001eF3?IE×&!\u0010A$Rl&3.G^\u0015-\u0017\u000b2QH\u0018\u0015\u00147^>0\u0001\u0000Y?)R\u001cZi\u0018(]us\u001a\\[Z\"_+Ι\n+c;?W9\\m|?? P\u001fU\u001a\u0004\u0012EWb5\u001eSIr:\u0000\u000eI `T\u0014_YGL3%\u000fq??\u0005m\u001c\\YN1??B3r&,6\u001cV?|`1\u000b\u001fS-[OM_c/t:\t?? b\"|\u0011\u001e(+q1\rn\t\"r:\u0017\u000b2l5òH\u0018\u0014qtb))-\u0003:\u0002\u000e{?\t  ?\u0018bMP\u0012\u0006)r? `TX\u0014\\9I\u000bE3?q\t\"\u0018I\u0012uKfb@ TTQp\"|c4\u0002\b9QQE?kC@A'QWhYM\u0000?ΛRH?g\u0004\u00002M'E\f\n\u0018`?H=(\u000b(\nK\u0000p:?p&QqiYY\u000e\u0018\u0010h\u0001(7`Q;A8@]\tE\u0000\u0012p\u0006M+\u0002C\f\u0011@\tE+#(\u0005\u0001@F*X\u0003w.\u0015FM&\b8@\u0001Fm\u0003T\u0006\u0018lG{\u00179\u000f`nGO-ob\u0018n\" \u000f+t|\f\u000f+{\u0014?|C3\u0001;W WGоnO0'Pu:J)B1R|\b\u0014d1QcWVC\u00184\u0005?\u0012:SH\u0001&'I?\u0000?k\u001aDú;wK)?2\u000f\u0004W3\u0004i|cEb=2)?\u001aWG?_b\"?v\u0007\u000b\f?R3I'MFiY\u0019@,0\u000fJ\u00021R|\u00075\u0014*(?;R\u0001h\u000e\u000e\u0018``Ozaq3E\n2\u0007Z\u0014\u0016`\u0014dAp\u0005I\f0iY\u0019@1\u0005?T\u001f(B9?\u0002\u001a(1JWVF\f\u001a\u0000J)J0@|\n\u0004?\u0012U\u0005\n2M!\u0005N\b\tfbI\t@DGG=)TPwg\u0019\u0005d\t#(+B\u0014\t\u0003B1]??)V\u0007Z\tv\"\u0004Qz5V6\"e1>I\u0014#7g*\u000b\u000eGJ?@D?Qr\u0000>zR%re|`wLF?3 \u0018\u0014\u000f\u0018\u0018!c4|a\u001b\r\u001b\u0018 ?K+$\u0000B\u0015\u0015\u001d\u001fP\u001d]\nvPT?iK\u0000u\u0014D>c*QR\u0016+\u0015tL\u0011\u0010\u0002(\r\u001e6Q\u001f\u001101>x?V\u0000>zR08.t(LF\u0002%re|`SC\u0013-f\u0000\"M?5(z#\u00015I\u0003\u001bje\u001a\u000b27:\u0007lP?\rc\u0015Mg\u001b1PX\u0010\u0000k\u0015y,@\u0014\u0016\u0016r\r?w\")ΜH\u0014n?\u0019AP`p]A?1F_+~\u0000=)S\u001220)663\u0003ni\fJ(\u0018\u0014\u000701m[qыt\u001cy0svGEv[F\u0019Z\u0010?\u0004lp##\u0019\u0004]\u001dq34omM)FZ^\u001d#Y4_?2?3I\u001fw$c>\u0013j!\b\u0010[3\u0011m\u0003[\u001fjJg\u0015?!\tB3kXQmKBV;_\nk',(\u0019\u0019n?S ,Wr+e3?? Yo\u000e?Vg\u000f\u0000?n?\u0019A[F\u000bQ_\u001181>r9?V\u0000>zRh$\u0005\u001cnm\u001bsH\u0007&%f2\b\u0014?7?{\u0003;\u0006\u0005!m豄\u0019Z\u0003\u0006>[0\f\u0002*2?{zRa.)\u0015Z\"\b=\u0003\u0000\u0015\u000f$|\u0000(ei7\u000bJI@\u000fab2fiψyO:7m(7g(\u0006\u0007?9\u0014\u00077jAJ qI6039(`P\u0003\u0015Fl.*V>[2SKz$j9@&\u001d\t4#\u0001\u001a#P\u0002;Z\"\b=)\n\n\u0002\u0001'g\u0000xI$\u0000INk\u00006kd\u0000\b\u0004h\u0004phhb['E\u0014\u0000dN=(\u0004BG?(X\u0007_z\t$E\u0000\u0004\u0000$t\u0012\u0001\u0000\r\u0014Qp\u0000H9\u0007\u0006?\u0000\u0018ě2v'\u001eQE$Qu\u0000??&$#?\u0000V\u0019{Z\u001b5g\u000fn0\u0000?$>U\u0015B\u0015\u0000G\u001c<\rfAv[b6\u001fICHrz?$E\"C'n?zPBG?(\u000b\u0015u;g]\\\fM ?@My;F\u0015vr\u001dIvyq?\u0014WK=U\"+Xe\u001f.?N2p\u0006\t5\u001a'Z5]1NQ\f\"\"%6t\u0004^\u0019\u0014l\u0012\u0007_Zegj\u001f\u0016\\\u0000gh\u0013]\u0005mO\b?\u0017?\u001a>-?P[-<\"G%\u0000\u001b?\u001b??%??\t\u0018\u0007\"\u0001\u0004\u0002\u001ce@kRG\u0013\rR}#\r\u000e\u001cnGVWm?&5S?&*X\u0005K'???E$3\u001dC)\u0007r\u0016?,~bM\u0019;qJ(\r,\u0005\"d\u001c\brNI?$\u0001$E\u0014\u0005\u0012\u0001\u0000\u001e\u0002A84Q@XNOZRK\u001fE\u0014\u0000dN=(\u0004pH@XAI9'&)\u0000\u0012H\u0000?H\u0004\u0002p{QE\u0001a&\u0006~cLvi6Sv$\u0004s?Zg\u001c?\u00003orb1H\u0000\u000e\u0005\u0016\u001f1\u0018\u0007\u0019\u0003$\u000ei0\b'8p$Q`\":~1K\u000e)u?0(s\u0011`88\u0002z\u0002i?E?h\u0019\u0010r9r/\u0007I\u0012@A'\u0001Zm\u0016\u0003\u001c`# \u001cg\u0007\u0015 f\u0012\u0000\u001c\nM\u001a`\u0013?\u0005\u0019!\u0010A?%\fs?a\u0004\u001eF(pqR\u000f\u0003\u0002\u001dV,f=E\"O.\u0007\u0000eSx\u0000ex| \u001f`?S\u0016N\u0000tUψ[1\u0000ip4f\u0006\"s\u0000\u00034j \u001c\n\u0000'2i5h\u0018'8pARH\\\b1Rl;<9J7\\9$E!iW1\u0006{w\u0015`8qJzt\u0006*h?0]>\u0000^I\u00128*m B`g]_Mb_\u001c\u0012-\fB8.H\u00148^gs]i\u0017b\tu\u0019\"bKT\u0019S\u0010o)#9;Y\u0016\rKC\u001f\u0002i/A\u0007\u001bO\\y/\u0016Ag&\u0015?u\u001fU\u0005)\f@q^NG?)\u001aK\t.!~eH};)b%x\u0019E\u0005k?#=^\u0005\u0010GD|\u00007'\u000e\u0007J=HgGK\u0000Bp`HOGk&4;i\u000e\u0014t$(\u0015?\u0007tI\u0012\u00072\tfa7d!@k+D\bk*;#X\t8\u00034T5\b$MG\u0019Js\u0011Ab\u001cg\u0007\u0015(p\u0006\u00054;:Y\u0000f\u0005\u0000Np3@Rh\u0019\u0010sI\u0012@A'Qas\u0011:A\u001dA6U,8\u0005eR@\"88\u0002O\u0003&]Z0F3N;`Hr*\b#I\u0012g=)@kbX\f\n,\u001cX8\u000e(\u0000\u001axvR<3JY.\r\u0016\u000b1ZkC@A'Q?!a9?NIFiVu;3KL=\t\u0014ve\nO\u0003\u000fq$+\r5;i[k\u0004XMWe\u0004)=hF(vcEFVS?\u0001\u001c\u0019T\u001c\nc\u0012ē?\u0014\\Vb\u0010NX)?s?\u0014f*ph\u001d?(S=iSj\u0016\u00128 :i],rhXU\u0018*Χe\u000e7\u0018\u00143(Rx\u001d(WeR\u0014z?Y+m`\u001a\u001d\u0005Dl~*j1F\f\u0004R1,I=M\u0017\u0016=\u0000G\u001ejcI\u0014\u000es7?h,\u001c;N1!(b\n\u0014ZDvBJf\t\u0007#Z.\u0016fgbS6;6_\u000fSG6Ig?F?3\u00003(l\u001e\u001frpKI]?U%3\u001fWg\u0015\u000eE?h\u0007\nYNJ#5I\u0018S\t,??8va4I%F\u0001iw?k?\"\u000bQ#\\5Aw}\u001a\u001d<GY?ou_Rn_:o2JP%\u001c\u0002\u000e\u0014O \u0011Qi)iVgd\u001ab7\u0001%~9\u001d`M\u000bXl.Z/ZXT-hpF\u0007\u0000\u001eix{\u001a|nZv\u001bKb;\u0000(\u000f\b\u0011]|VF-\u000e}k\u001b^=fHt#\u00162|\u0016(\u001a5C*T\u0011k5iZIXr\u001bz\u001e\u001b\u000b\u0006麥Z{5?qY~\u0000\u001c\u0018\u0000\u000eF;?i-kV\u0001\"gh'\u000ein\u0007_\n,\u0017\\\u0016\u000f\f`\nPFwax1\u001a\u001d\u0012\u0019\u0007efq$+hI'Il.\u0002?T\u00057om;3I#X\u0014f7Cs8!(cUU;?\u001d;N3H\b(Y]Jv8)ZFbK\u001cVve\u0000\u0007JwB\u0006\u001b2)\t??\u001cCPx=i\u0011\u001b*pi]\u0005PDTZT\u0001\u001fHJI'9?4\\v`\u0007Fvfw>Rb{l??vL8Z.gUTh]\u001cHwojb\u0011R\u0017b[hT\u001bYZE;\u000fC\r?\u0014gfU\u0004:ReBz?gM\u0000?ΛOs\u0000i[\u0005\u0014QHaE\u0014P\u0001E\u0014P\u0001E\u0014P\u0001E\u0014P\u0001E\u0014P\u0001E\u0014P\u0001E\u0014S\u0002]><g|LT>sA#5\u0017/2dx\u0000\u001c^Z\u000b\u0018X~Q\t\"8\u001c?j\u0007%t\u001ch??f\u0000\u0000Zy  \u001b\u000bqZ<~a<)-5qE7~lX(\u001c2\n\u001cZWhnI#v\u0014T\b+?i6z\\\t\u00168w\u0015Y\u001cc$zxWgX+\",\u0016\u0019S?\u00128\u000fc r\u0005T%i&?\u000f\u000fxSB\u00068m9c#\u001bEcVO\u0015\u001a?[?\nIm\bOF\u000fjK\u0018i#}Ι?cmv9V\u001c}3Vo/?z\r\u0017>d??\u0001]W9\u0012t\u001aL\t5OorFue>yj_^\u001cTmnH\u0019\u0000cSǚ3q\u0001??zUτ?\u0014\\XRd\u001e(8u\u001e\u0016i$\u000b?b.R\u0011J4noYp[?5}\u0000ME\u0015n\u0014QE\u0000\u0006(\u0000RQ.\u0002QE\u0000\u0014QE \n(\n(\n('vrj0\u001c*i\u0010/HS?f?uP\u0000.U\u0000k\u0019dm!7\u0014Gz?08.t\u001b\flXB\u0019ǚ\u0018\u000106wO'?`Td\u0000I_,4\u001bF\u0014g=i\u0019\u0012E4\u00046c+\u001c \n?\u0015*?uP\u0000T9;(r\u00030\u000f[,e?\u0011?<eFs?(C\u0019;6\u0018?;0*u\u0007\"c\u00152`m\u0011y\u0014?A\u000en$\u0014\f\u000eX?+W84`\u0010Ob8\u0015j%?\u0003k=?J_x\u0000'\u001e^YN-.a;U*K\u000e?\u001dsQ7\u0005\u0012Db\u000e\u0001\u0001_\u0004j>#ci6V?!oS$\t\u0007\u001e=Q,t2OX8!\u0012*F;\u0005\u0003{).m\u0015.!0\fK\u0012\u000f#52%TP?<<\u000epA\u0007\u0007-\"\u000f\u000fh6W^I}ax\n\"$\u0012\u0005MO\"W\n3rjQ&\u0019ZQ\u0007\u0005\u001cu+)\f\t$\n:I\u0004\u001c\fd2#m?yq663\u0003ni??P0*<?tU\u0000rN1!\u0005I\u0014JA?=#N.?73\u0015t\u0011R|Л[yqLDv\"`8P\u00015)V|\u0000\u00055\u000eJ\tC\u001c\u0016\u0011A1?*\u0004\n7lFz>bc\u001c6\u0018?;1i\u001c\u0001u\u0007\"\u001bsBmg\u001cb;3@0i?P0)?\u0014g=hr'\u0018\u0011R|$(&\u0012\t\"\u0006\u000fj\u0013ie'wjD \tHbi7H\u0000\u000bN$UQ@ S\u001cBvRbc0\u0017~29J7@??\u0002ylI>fx61@c\u0005\u0012`m8E\u0003\u0002\u0007'fiυr\"'n)\f]#U\u0019Z\u00010FPI\u0018R\u001d5*z\u0017i\u000edc1~B\u0014vPA\u0014YH\u0000\u000bD \u0018c\tK-3#HZ5L\f-\u0013?ihG!Ee\u0000|y$lQ\u0001?d\u000e?id@\u0018EwDE00{\u00146$\u0000x4(\u000b \u0007\f\u000f:G29cShnB?\u00100(I\n+(\u0003QE?U,nQ\u0001f,?4QGK\u000e|\u0019\b\u0003\u0003\u001ea`bE\u0017{Zā\u000e}k7^{M\u001a1\u0012&+\\qW/\u0001OP>/i÷S\u0018X\u001bh#b#\f\u0015`pAk^X-/5\u000bm>Mi\u001e\u0007\u001c\u000b.T\u001c2Hv\u0015\u00172Hk\u0016+\u0000?F?)q_Q\\\u0005#\u001d}+C\u001e[C@\f?}\u0005?\"p?? @\t:\f5?i*\u001d[\\pL8\u001ao\fv'VK+Y\u001ee\fFTu\u0019\u001ej\u0016\u0016'/Κ[;{x?2nd8\nw=\u000b?\u0019t?U\r\u0013(q\u000bw铂?d \u0006\u0007jL:]Y]O.48\u000b*\u0005!6:\u0000OU\u0000k\u0016m?VW5c\u001aEv^a\fR!\u00009\u0018EcrP)!9JG,G4E\u001eC\u001e\u0016ER\u0006\u001620ivqrXtncp\u000f?Y9<mn>Y\f\u0012\u0000\u0014y1Lr!\u00009+m`?<E\u0017\u001d\\8y\u000b\u0003\u000bLl\f\u001aH?\u0007M1,?4d2\u0010H\u0003\u0003\u001cShaClsK\u0014<\u0003?E\u0017ae+m`GoZY\u001c#IE\u0017\u0016B@в\u0015\u0001M?\u001f7\u0000x\u0000:eLVi|'$Qv:m\u0002z\r{1NJ\\\"?wzV\u000b\nl~zP#5N1?\u0005QJA傝>\u001d\u0000tqEqS0N6a*\u001c,+iVu;OC\r?v?\u000ehW\b?}\u0005V6VS=}(2z\u0000\fpGJn\u000fvf\u000eS]Uёea\u000fB*g?8!*b\n\u0014Z,+\u000f!:&\u0018H\\\u0013}#\u0006t]oI\u000bGrQ/He\u000e?'>?M\\Hq\\g4?J\n\"G\u0019R:+c?tROct{h#i\b*O5x~\\\u001bm\u0005g\u0010k\u0004j?i\u0018u\u0018\u0007\u0003?s??uo-EfO?Y]]\u0014v\u0010-gf\u0014?,\u0001??u}4/>KsqE=Ρ#\u000bta\u00111t+?\u0015??K[\u0017b0\u001f-C\u0001?\u0004\u001e4\u000f\tj^\"\u0005\u0012\u0011?o}I+|-;CW\u0014]\u00138W\u0001?{\u0019\u0000?![\\\u0001nz{zp&wm#N[\u0014\u0010;\u0001U$6vz?\u001c3\u001e\r繍aT\u0011@\rs?mQ'sJ\u0018UNv\"\u0012qeb2rv\u001f?\u001b\u0014\u0014Xw\u001bEH_h\u000eq? *EqT\u0004o$c3\u001f?\u0016\u001d?p$s!)\tS\u001a?M\u0016\u0015QO]Hv8QN`)7\u0016dS~?\u001c`)A\u001b+)?&#?#\u001d(\\e\u0014 nv;)\u000e1EqSC\u001aлP8IlqEqSmegS0YN*,;K801}?\u001e\u001b\u0005āR\t\u0007?mjGS6[\"vc\u0019dF?y{\u001d\u001c\u0016U]/X\u0012\t=\u000e:ScW\u0004 v?gnwg\u001b?I\u0000j[$8\u00004o?.\u0007\u0017@LY\r?\u0000v*?\u001bi\"F%ou\u000b\u001bq\u0018pvzRK4\u0001\u0003z/ \f\u000eFz`?w%X(\u0017AbbQ\n?J\u000b\u0018\"6TT\u000f}lQT\u0010\bq?T`H$8<Q0,\u0011ldpXv\n\u0019\u0019?\u0018\u000e4y\u0001bC\tE[R?3#\u0001?sP}lm!p=\r\u001fndn\u0016\u001b0#|u\u0014B\u001c8?\u0004W?\r\u001ab@\u0018\u0012,[Q\u0016i\u001f\u0004\u000e9Qn6\u0018h]J2B\b\u0015\u0014\u0005\u001e4}l?\b\u0004u8<u?C,%n|?\u0018!/r{0\u001d:\t\u001a\n\u001dZ60&?}:d\u001fQ_GGH\t\"\f\u001fdgmd\u000f]?gB~?U{<\u00077z.lnHg3\\\u000bf/41)\u001c\u0000+\u001f\u001czu?r~\u0016?oo\u0015q=-G\u0006~Yt?\u0018-\"\f\u0017??Wr9\u001fc?5m?x\u0018?\u0018\u0005<?>^\t\u0001;\tii^Pv\f7x?\u0000?M\bUC4iPK(UC\u0014\u00003_[!%lCQ*qB?\u0007\u001b?\u000f4H\u0010;y\u0000`K\u0001N?G\rEv,+\u0019\n6\u0014Pc?SP=\" \u0011\t}l\t=h|\u0004y%Y\u001c\u0016\"(dgg\u001b?{n\u000b0`;`Z?!\u0004,[Rfq>\u0000\u001dM7sc桖*Bc[[27g`u,Xbar#|u\u0014Bq==*\bВ6G1o \f\tp@EX\u0007 wHU\u001dV?`{vD\u0000GS\u0017],\u0016)\u0015$#HUFH$8<Q\u0015?,\r\u0017]4lRf8\u0018\u001dMTkr?\u0000\u0013\u0006-*B=(\u0002p\u0002=.iL.DoGQU\u0017#vz\u0015q\rh\u0004v\"?y\u0002={E\f\tp@K%9!L\u001aWAb?!Uv?^?nEl\u001e?@ \u001098<з6RA'u\f\u0000?\u000b`=iM\u0014P0H(\n(\u000e?Q@\tG\u0014P\u0001E\u0014P\u0002\n(\u0003\u0006(\u0001h\u0000J(\u0003KE\u0014\u0000Q@\u0001\u0000)(\n\r\u0014P\u0001?Q@\u0007z(\n=(\u000fZ(\nZ(\u0018QE\u0001\r-\u0014P!\u0007JQ?)\u000f";
		try {
			Common.generateImage(str, "C:/API/1.gif");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println(str);

	}

	public static String gettxt2String(String path) {
		File file = new File(path);
		StringBuilder result = new StringBuilder();

		try {
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader ifs = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(ifs);// 构造一个BufferedReader类来读取文件
			String s = null;
			while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
				result.append(System.lineSeparator() + s);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

}
