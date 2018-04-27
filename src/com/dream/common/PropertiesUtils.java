package com.dream.common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * 对属性文件操作的工具类 获取，新增，修改
 * 
 * @author jianghong
 * 
 * @createDate 2014年7月21日 下午3:56:48
 * 
 * @file PropertiesUtils.java
 * 
 * @pakage com.dtm.common.utils
 * 
 * @email jiangjianhua123@qq.com
 * 
 * @version 0.1
 * 
 */
public class PropertiesUtils {
	/**
	 * 获取属性文件的数据 根据key获取值
	 * 
	 * @param fileName
	 *            文件名　(注意：加载的是src下的文件,如果在某个包下．请把包名加上)
	 * @param key
	 * @return
	 */
	public static String findPropertiesKey(String key) {
		try {
			Properties prop = getProperties();
			return prop.getProperty(key);
		} catch (Exception e) {
			return "";
		}

	}

	/**
	 * 返回　Properties
	 * 
	 * @param fileName
	 *            文件名　(注意：加载的是src下的文件,如果在某个包下．请把包名加上)
	 * @param
	 * @return
	 */
	public static Properties getProperties() {
		Properties prop = new Properties();
		String rootPath = PropertiesUtils.class.getResource("/").getPath();
		rootPath = rootPath.substring(1, rootPath.indexOf("WEB-INF"));// 从路径字符串中取出工程路径
		String path = rootPath + File.separator + "config" + File.separator
				+ "config.properties";
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(path));
			prop.load(in);
		} catch (Exception e) {
			return null;
		}
		return prop;
	}

	/**
	 * 写入properties信息
	 * 
	 * @param key
	 *            名称
	 * @param value
	 *            值
	 */
	public static void modifyProperties(String key, String value) {
		try {
			// 从输入流中读取属性列表（键和元素对）
			Properties prop = getProperties();
			prop.setProperty(key, value);
			String rootPath = PropertiesUtils.class.getResource("/").getPath();
			rootPath = rootPath.substring(1, rootPath.indexOf("WEB-INF"));// 从路径字符串中取出工程路径
			String path = rootPath + File.separator + "config" + File.separator
					+ "config.properties";
			FileOutputStream outputFile = new FileOutputStream(path);
			prop.store(outputFile, "modify");
			outputFile.close();
			outputFile.flush();
		} catch (Exception e) {
		}
	}

	public static void main(String[] arg) {
		Properties properties = PropertiesUtils.getProperties();
		System.out.println(properties.keySet());
	}
}
