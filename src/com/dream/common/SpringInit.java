package com.dream.common;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.dream.model.System_User;

@Component("springInit")
@SuppressWarnings("all")
public class SpringInit extends HttpServlet {

	@Autowired
	private static long serialVersionUID = 1L;
	public static String ImageUrl = "http://127.0.0.1:80/dreamOA/";
	public static String IP = "";
	public static String DK = ":80/dreamOA/";

	// private static final String ImageUrl =
	// "http://125.65.42.48:8082/dreamOA/";

	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		ServletContext servletContext = config.getServletContext();
		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		AutowireCapableBeanFactory autowireCapableBeanFactory = webApplicationContext.getAutowireCapableBeanFactory();
		DataInit.WEB_APP_REAL_PATH = config.getServletContext().getRealPath(File.separator);
		ServletContext application = getServletContext();
		// 加载系统默认的数据
		// autowireCapableBeanFactory.configureBean(this,
		// "systemUserServiceImpl");

		try {
			getListUploadUrl(DataInit.WEB_APP_REAL_PATH + File.separator + "config" + File.separator + "upload.xml");
			System.out.println("上传文件路径为:" + DataInit.UPLOADURL);
		} catch (DocumentException e) {
			e.printStackTrace();
			System.out.println("------------加载上传配置文件失败!-------------------");
		}
		application.setAttribute("imageUrl", ImageUrl);

		// JSONArray list = systemServiceImpl.getSysTypeAll();
		// JSONArray listFirst = systemServiceImpl.getSysTypeFirst();
		// JSONArray rate = systemServiceImpl.getSystemRate(new
		// Jiaoyu_Rate()).getJSONArray("rows");

		// application.setAttribute("systemType", list);
		// application.setAttribute("systemTypeFirst", listFirst);
		// application.setAttribute("rateType", rate);

		System.out.println("------------------加载配置全局类型菜单成功------------------ ");
		JSONArray json;
		try {
			DataInit.listAdmin = getListTerm(DataInit.WEB_APP_REAL_PATH + "/" + "config" + File.separator + "admin.xml");
			System.out.println("超级管理员路径 为:" + DataInit.WEB_APP_REAL_PATH + "/" + "config" + File.separator + "admin.xml");
			DataInit.menuJSONArray = getMenu(DataInit.WEB_APP_REAL_PATH + "/" + "config" + File.separator + "menu.xml");
			System.out.println("加载全局菜单: " + DataInit.WEB_APP_REAL_PATH + "/"+ "config" + File.separator + "menu.xml");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 加载上传的路径
	 * 
	 * @param path
	 * @throws DocumentException
	 */
	public static void getListUploadUrl(String path) throws DocumentException {
		File f = new File(path);
		SAXReader reader = new SAXReader();
		Document doc = reader.read(f);
		Element root = doc.getRootElement();
		String str = System.getProperties().getProperty("os.name");
		boolean b = StringUtils.containsIgnoreCase(str, "windows");

		for (Iterator i = root.elementIterator("system"); i.hasNext();) {
			Element foo = (Element) i.next();

			if (b) {
				DataInit.UPLOADURL = foo.elementText("windows");
				// SpringInit.ImageUrl = "http://127.0.0.1:8080/dreamOA/";
			} else {
				DataInit.UPLOADURL = foo.elementText("linux");
				// SpringInit.ImageUrl = "http://125.65.42.126:8080/dreamOA/";
			}
		}
	}

	/**
	 * 加载语言定义
	 * 
	 * @param path
	 * @throws DocumentException
	 */
	public static JSONArray getLanguage(String path) throws DocumentException {
		JSONArray json = new JSONArray();
		File f = new File(path);
		SAXReader reader = new SAXReader();
		Document doc;
		doc = reader.read(f);
		Element root = doc.getRootElement();
		root.getDocument();
		for (@SuppressWarnings("rawtypes")
		Iterator i = root.elementIterator("language"); i.hasNext();) {
			Element e = (Element) i.next();
			json.add(e.elementText("value"));
		}
		return json;
	}

	/**
	 * 加载学历定位
	 * 
	 * @param path
	 * @throws DocumentException
	 */
	public static JSONArray getEduAction(String path) throws DocumentException {
		JSONArray json = new JSONArray();
		File f = new File(path);
		SAXReader reader = new SAXReader();
		Document doc;
		doc = reader.read(f);
		Element root = doc.getRootElement();
		Element foo;
		for (@SuppressWarnings("rawtypes")
		Iterator i = root.elementIterator("edu"); i.hasNext();) {
			Element e = (Element) i.next();
			json.add(e.elementText("value"));
		}
		return json;
	}

	/**
	 * 加载超级管理员
	 * 
	 * @param path
	 * @return
	 * @throws DocumentException
	 */
	public static List<System_User> getListTerm(String path) throws DocumentException {
		List listAdmin = new ArrayList();
		File f = new File(path);
		SAXReader reader = new SAXReader();

		Document doc = reader.read(f);
		Element root = doc.getRootElement();
		for (Iterator i = root.elementIterator("user"); i.hasNext();) {
			Element foo = (Element) i.next();
			System_User sys_user = new System_User();
			sys_user.setTable_id(foo.elementText("table_id"));
			sys_user.setUserName(foo.elementText("userName"));
			sys_user.setUserPwd(foo.elementText("userPwd"));
			// sys_user.setUserShowName(foo.elementText("userShowName"));
			listAdmin.add(sys_user);
		}
		return listAdmin;
	}

	/**
	 * 加载教材类型
	 * 
	 * @param path
	 * @return
	 * @throws DocumentException
	 */
	public static JSONArray getJiaoyuType(String path) throws DocumentException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		File f = new File(path);
		SAXReader reader = new SAXReader();

		JSONArray json = new JSONArray();

		Document doc = reader.read(f);
		Element root = doc.getRootElement();

		for (Iterator i = root.elementIterator("power"); i.hasNext();) {
			Element foo = (Element) i.next();
			JSONObject js = new JSONObject();
			js.put("id", foo.elementText("id"));
			js.put("name", foo.elementText("name"));
			js.put("pid", foo.elementText("pid"));
			json.add(js);
		}
		return json;
	}

	/**
	 * 加载菜单
	 * 
	 * **/
	public static JSONArray getMenu(String path) throws Exception {
		File f = new File(path);
		SAXReader reader = new SAXReader();

		JSONArray json = new JSONArray();

		Document doc = reader.read(f);
		Element root = doc.getRootElement();
		for (Iterator i = root.elementIterator("menu"); i.hasNext();) {

			Element foo = (Element) i.next();
			JSONObject js = new JSONObject();
			js.put("id", foo.elementText("id"));
			js.put("name", foo.elementText("name"));
			String pid = foo.elementText("pId");
			js.put("pId", pid);

			if ("0".equals(pid)) {
				js.put("open", "true");
			}

			Element btn = foo.element("bottons");
			if (btn != null) {

				for (Iterator bi = btn.elementIterator("botton"); bi.hasNext();) {
					Element bfoo = (Element) bi.next();
					JSONObject j = new JSONObject();
					j.put("id", bfoo.elementText("id"));
					j.put("name", bfoo.elementText("name"));
					j.put("pId", foo.elementText("id"));
					j.put("open", "true");
					json.add(j);
				}
			}

			json.add(js);

		}
		return json;
	}
}
