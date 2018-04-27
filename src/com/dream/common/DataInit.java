package com.dream.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.dream.model.System_User;

 
public class DataInit {
	public static String WEB_APP_REAL_PATH="";//系统当前相对路径
	public static String UPLOADURL="/";//上传文件路径
	public static String FILEDELETE="0";//0表示不删除，1表示删除
	public static String MANAGERID = "1"; //管理员ID
	public static List<System_User> listAdmin = new ArrayList<System_User>();//超级管理员用户组
//	public static List<Map<String, Object>> jiaoyuType = new ArrayList<Map<String, Object>>(); // 获取教材类型
	private static Map<String, Object> loginUserObject = new HashMap<String, Object>();
	
	public final static String APIKEY = "sk_test_5KWjHGLGCaf1mLe9q154GSSS";
	
	public final static String APILIVEKEY = "sk_live_qrDib5Kqnn5S5efDqH58aLi9";
	
	public final static String PRIVATEKEY = "-----BEGIN RSA PRIVATE KEY-----\nMIICXAIBAAKBgQCWpyJbbRaIOLXSPN5azCcx8j5drluEfKEd4S19ByvdeDo3z2Ao\nioPbiNPfOYPCA4QELMtE1bRNassClGT8HAjMS+O5Kjq7wdZ9mOWgggQTtvAeP3GT\nrlWDaPxrk5rwP8yiJ9FAsn9twmXvYhYBiwUxHLMw9Y43QU48wYbq5y3yewIDAQAB\nAoGAGLRhqvJxnncGQeqfX+5YgxVBauVbtpclaJvzdKxHYjMEAQDtuNg2FTsT22g4\nFgmzjWe3EZulzIg1sD+ba8hBHNbveuigpnm6QYQL2E9lmy34Bx9PQyc51yrcBJJV\nSU4hB7kxk9dP2mn38/Q4SkUcH6qOLWiWppO2INs2i/Sv22kCQQDGa4vDRWkkgqDx\nnQOkRXwKHm7mrRrA4Obbgbm1TBmTOQwMS/NGhpHrZHWkKFy2L4+0A10Fn05ADm/B\npkdYIU7lAkEAwl8AsDeW0+gv4BxqxAUu2cmVchp6P/hgstlxYK52jFC/z+IVNTEn\nQpg87H3jx5rMiChWUB6SPdsY+qa0A2TF3wJAX2GzfdsQAA0LcRoZdg4w4mxew2TT\nR4TVdQ+XRh+e67awo/ieqXhnDNDt/Euqxal3WwGC5gD+jKxj7KMSCLt6QQJBALVo\nUY4l88FHHRe/re3RII7nU3GxQJPfzvBdfTHM4pvG6Z6t1TlV7Nx8la/rNuIemfIi\nI5yXS8oraL7XQ38/WGUCQFYGINS4iDHL36T+xsjksbHIgx5xv0PDtIUdtsI/yqO8\nkzSQUd5Q4Y8m2+JVLCcbfCfmB+u5UR51ORFFoynd3KE=\n-----END RSA PRIVATE KEY-----\n";
	
//	public final static String PRIVATEKEY = "-----BEGIN RSA PRIVATE KEY-----\nMIICXgIBAAKBgQC4kohOR471o76SUvhmaX5qDve17/ufpz7aaPOiaHR3usC+bJ9H\nvLnElcYpFQjV8M4oDOmNbWY08lmgpSvMYBb3cV923RuhxnDTqRG7Xizrm+Q9nQr3\nVAuPxPw0Q/YNXms/SiYYnKVUNPwWLt3UVad0llA2gBuJ3pTDv9d6apUBpwIDAQAB\nAoGBAISd/MRKFEUZB0f5x7NGH2ezI3cdXK2lLLsf59aMuQqCLBVm1LdluhCvEPfZ\nvkSCRo5uYK2WqnZmn9GWfDbX4AdoGCvpLkLQYzeMNQaCur4q9lYpKslAH5/E34jO\npv5MfeWtQbVBxt9balRXtqEWgtRN+XT+Pxp3KoBT+7/95FPhAkEA5ewoxeehHx5F\n5xo0Cf5ToY3dak4HdhxgyO8/D5mGFGfnUIKDko+UvCn5C/FarRF5y1J5JX9btZem\nzQAtrU6n9wJBAM2Bn/JBcnPfAwmL3vHtMAGTe0TwlOMqOB7i2pCJ4rT265jwCHhi\nu9KOvCd6zH5yqN2g+2cJlLJdOluFSkMi59ECQQCgn5L8bw0IiEWdZZjlQl96hXkh\nQtHDmZ+jwD/1zfL1XoEipYr1YIkvysLJrRsDvSmw+TTZFVgkTIWiwXUnlcORAkAc\ncOof7urWGnEjKxZpyPex4CVZPyp73eXycFbrVgnoHZwxiFbDwnZYB6UGmbofbeLs\n+VCRzAOmQ7zF2caTUAthAkEAiL+kCKtTSO16GMFMBwrROF+hlEHqnfou06aJAL/o\nKhrGz+qAm80PxiKwPuv34zAQ4y3O+Fc7s3dcTUhq/9Y8LQ==\n-----END RSA PRIVATE KEY-----\n";
	
	public final static String CLIENT_IP = "125.65.42.126";
	
    /**
     * Pingpp 管理平台对应的应用 ID，app_id 获取方式：登录 [Dashboard](https://dashboard.pingxx.com)->点击你创建的应用->应用首页->应用 ID(App ID)
     */
	public final static String APPID = "app_K8yXjTzHSOq9y5qj";
	
	public static JSONArray menuJSONArray = new JSONArray();
	
	private static Object loginUser;

	public static Object getLoginUser() {
		return loginUser;
	}
	public static Object getLoginUser(String sessionId) {
		return loginUserObject.get(sessionId);
	}
	public static void setLoginUser(String seeesionId,Object loginUser) {
		loginUserObject.put(seeesionId, loginUser);
	}
	public static void setLoginUser(Object loginUser) {
		DataInit.loginUser = loginUser;
	}
	
	public static void removeUser(String sessionId){
		loginUserObject.remove(sessionId);
	}
}
