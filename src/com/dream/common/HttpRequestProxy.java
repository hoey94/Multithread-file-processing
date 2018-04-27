package com.dream.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Http应用库
 * 
 * @author cnlyml
 * @date 2014-8-1
 */
@SuppressWarnings("all")
public class HttpRequestProxy implements Cloneable {
	// 超时间隔
	private static int connectTimeOut = 60000;
	// 让connectionmanager管理httpclientconnection时是否关闭连接
	private static boolean alwaysClose = true;
	// 返回数据编码格式
	private String encoding = "UTF-8";

	private HttpClient client = new HttpClient(new SimpleHttpConnectionManager(
			alwaysClose));

	public HttpClient getHttpClient() {
		return client;
	}

	@SuppressWarnings("deprecation")
	public String doRequestForStr(String url, String postData,
			Map<String, String> header, String encoding)
			throws HttpErrorException {
		String responseString = null;
		// 头部请求信息
		Header[] headers = null;
		if (header != null) {
			Set entrySet = header.entrySet();
			int dataLength = entrySet.size();
			headers = new Header[dataLength];
			int i = 0;
			for (Iterator itor = entrySet.iterator(); itor.hasNext();) {
				Map.Entry entry = (Map.Entry) itor.next();
				headers[i++] = new Header(entry.getKey().toString(), entry
						.getValue().toString());
			}
		}
		// post方式
		if (postData != null) {
			PostMethod postRequest = new PostMethod(url.trim());
			postRequest.getParams().setParameter(
					HttpMethodParams.RETRY_HANDLER,
					new DefaultHttpMethodRetryHandler());
			postRequest.getParams().setParameter("http.protocol.cookie-policy",
					CookiePolicy.BROWSER_COMPATIBILITY);
			if (headers != null) {
				for (int i = 0; i < headers.length; i++) {
					postRequest.setRequestHeader(headers[i]);
				}
			}
			postRequest.setRequestBody(postData);
			try {
				responseString = this.executeMethod(postRequest, encoding);
			} catch (HttpErrorException e) {
				throw e;
			} finally {
				postRequest.releaseConnection();
			}
		}
		// get方式
		if (postData == null) {
			GetMethod getRequest = new GetMethod(url.trim());
			getRequest.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
					new DefaultHttpMethodRetryHandler());
			getRequest.getParams().setParameter("http.protocol.cookie-policy",
					CookiePolicy.BROWSER_COMPATIBILITY);
			if (headers != null) {
				for (int i = 0; i < headers.length; i++) {
					getRequest.setRequestHeader(headers[i]);
				}
			}
			try {
				responseString = this.executeMethod(getRequest, encoding);
			} catch (HttpErrorException e) {
				e.printStackTrace();
				throw e;
			} finally {
				getRequest.releaseConnection();
			}
		}
		return responseString;
	}

	/**
	 * 用法： HttpRequestProxy hrp = new HttpRequestProxy();
	 * hrp.doRequest("http://www.163.com",null,null,"gbk");
	 * 
	 * @param url
	 *            请求的资源ＵＲＬ
	 * @param postData
	 *            POST请求时form表单封装的数据 没有时传null
	 * @param header
	 *            request请求时附带的头信息(header) 没有时传null
	 * @param encoding
	 *            response返回的信息编码格式 没有时传null
	 * @return response返回的文本数据
	 * @throws CustomException
	 */
	@SuppressWarnings("rawtypes")
	public String doRequest(String url, Map postData, Map header,
			String encoding) throws HttpErrorException {
		String responseString = null;
		// 头部请求信息
		Header[] headers = null;
		if (header != null) {
			Set entrySet = header.entrySet();
			int dataLength = entrySet.size();
			headers = new Header[dataLength];
			int i = 0;
			for (Iterator itor = entrySet.iterator(); itor.hasNext();) {
				Map.Entry entry = (Map.Entry) itor.next();
				headers[i++] = new Header(entry.getKey().toString(), entry
						.getValue().toString());
			}
		}
		// post方式
		if (postData != null) {
			PostMethod postRequest = new PostMethod(url.trim());
			postRequest.getParams().setParameter(
					HttpMethodParams.RETRY_HANDLER,
					new DefaultHttpMethodRetryHandler());
			postRequest.getParams().setParameter("http.protocol.cookie-policy",
					CookiePolicy.BROWSER_COMPATIBILITY);
			if (headers != null) {
				for (int i = 0; i < headers.length; i++) {
					postRequest.setRequestHeader(headers[i]);
				}
			}
			Set entrySet = postData.entrySet();
			int dataLength = entrySet.size();
			NameValuePair[] params = new NameValuePair[dataLength];
			int i = 0;
			for (Iterator itor = entrySet.iterator(); itor.hasNext();) {
				Map.Entry entry = (Map.Entry) itor.next();
				params[i++] = new NameValuePair(entry.getKey().toString(),
						entry.getValue().toString());
			}
			postRequest.setRequestBody(params);
			try {
				responseString = this.executeMethod(postRequest, encoding);
			} catch (HttpErrorException e) {
				throw e;
			} finally {
				postRequest.releaseConnection();
			}
		}
		// get方式
		if (postData == null) {
			GetMethod getRequest = new GetMethod(url.trim());
			getRequest.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
					new DefaultHttpMethodRetryHandler());
			getRequest.getParams().setParameter("http.protocol.cookie-policy",
					CookiePolicy.BROWSER_COMPATIBILITY);
			if (headers != null) {
				for (int i = 0; i < headers.length; i++) {
					getRequest.setRequestHeader(headers[i]);
				}
			}
			try {
				responseString = this.executeMethod(getRequest, encoding);
			} catch (HttpErrorException e) {
				e.printStackTrace();
				throw e;
			} finally {
				getRequest.releaseConnection();
			}
		}
		return responseString;
	}

	private String executeMethod(HttpMethod request, String encoding)
			throws HttpErrorException {
		String responseContent = null;
		InputStream responseStream = null;
		BufferedReader rd = null;
		try {
			request.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
					new DefaultHttpMethodRetryHandler());
			request.getParams().setParameter("http.protocol.cookie-policy",
					CookiePolicy.BROWSER_COMPATIBILITY);
			this.getHttpClient().executeMethod(request);
			if (encoding != null) {
				responseStream = request.getResponseBodyAsStream();
				rd = new BufferedReader(new InputStreamReader(responseStream,
						encoding));
				String tempLine = rd.readLine();
				StringBuffer tempStr = new StringBuffer();
				String crlf = System.getProperty("line.separator");
				while (tempLine != null) {
					tempStr.append(tempLine);
					tempStr.append(crlf);
					tempLine = rd.readLine();
				}
				responseContent = tempStr.toString();
			} else
				responseContent = request.getResponseBodyAsString();
			Header locationHeader = request.getResponseHeader("location");
			// 返回代码为302,301时，表示页面己经重定向，则重新请求location的url，这在
			// 一些登录授权取cookie时很重要
			if (locationHeader != null) {
				String redirectUrl = locationHeader.getValue();
				this.doRequest(redirectUrl, null, null, null);
			}
		} catch (HttpException e) {
			throw new HttpErrorException(e.getMessage());
		} catch (IOException e) {
			throw new HttpErrorException(e.getMessage());
		} finally {
			if (rd != null)
				try {
					rd.close();
				} catch (IOException e) {
					throw new HttpErrorException(e.getMessage());
				}
			if (responseStream != null)
				try {
					responseStream.close();
				} catch (IOException e) {
					throw new HttpErrorException(e.getMessage());
				}
		}
		return responseContent;
	}

	/**
	 * 特殊请求数据,这样的请求往往会出现redirect本身而出现递归死循环重定向 所以单独写成一个请求方法
	 * 比如现在请求的url为：http://localhost:8080/demo/index.jsp 返回代码为302
	 * 头部信息中location值为:http://localhost:8083/demo/index.jsp
	 * 这时httpclient认为进入递归死循环重定向，抛出CircularRedirectException异常
	 * 
	 * @param url
	 * @return
	 * @throws CustomException
	 */
	public String doSpecialRequest(String url, int count, String encoding)
			throws HttpErrorException {
		String str = null;
		InputStream responseStream = null;
		BufferedReader rd = null;
		GetMethod getRequest = new GetMethod(url);
		// 关闭httpclient自动重定向动能
		getRequest.setFollowRedirects(false);
		getRequest.getParams().setParameter("http.protocol.cookie-policy",
				CookiePolicy.BROWSER_COMPATIBILITY);
		try {
			client.executeMethod(getRequest);
			Header header = getRequest.getResponseHeader("location");
			if (header != null) {
				// 请求重定向后的ＵＲＬ，count同时加1
				this.doSpecialRequest(header.getValue(), count + 1, encoding);
			}
			// 这里用count作为标志位，当count为0时才返回请求的ＵＲＬ文本,
			// 这样就可以忽略所有的递归重定向时返回文本流操作，提高性能
			if (count == 0) {
				getRequest = new GetMethod(url);
				getRequest.setFollowRedirects(false);
				client.executeMethod(getRequest);
				responseStream = getRequest.getResponseBodyAsStream();
				rd = new BufferedReader(new InputStreamReader(responseStream,
						encoding));
				String tempLine = rd.readLine();
				StringBuffer tempStr = new StringBuffer();
				String crlf = System.getProperty("line.separator");
				while (tempLine != null) {
					tempStr.append(tempLine);
					tempStr.append(crlf);
					tempLine = rd.readLine();
				}
				str = tempStr.toString();
			}
		} catch (HttpException e) {
			throw new HttpErrorException(e.getMessage());
		} catch (IOException e) {
			throw new HttpErrorException(e.getMessage());
		} finally {
			getRequest.releaseConnection();
			if (rd != null)
				try {
					rd.close();
				} catch (IOException e) {
					throw new HttpErrorException(e.getMessage());
				}
			if (responseStream != null)
				try {
					responseStream.close();
				} catch (IOException e) {
					throw new HttpErrorException(e.getMessage());
				}
		}
		return str;
	}

	public void downloadFile(OutputStream os, String url) throws IOException {
		InputStream is = null;
		try {
			GetMethod getRequest = new GetMethod(url.trim());
			getRequest.getParams().setParameter("http.protocol.cookie-policy",
					CookiePolicy.BROWSER_COMPATIBILITY);
			client.executeMethod(getRequest);
			is = getRequest.getResponseBodyAsStream();
			IOUtils.copy(is, os);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			is.close();
		}
	}

	public void downloadFile(File path, String url) throws IOException {
		if (!path.exists()) {
			path.getParentFile().mkdirs();
			path.createNewFile();
		}
		InputStream is = null;
		try {
			GetMethod getRequest = new GetMethod(url.trim());
			getRequest.getParams().setParameter("http.protocol.cookie-policy",
					CookiePolicy.BROWSER_COMPATIBILITY);
			client.executeMethod(getRequest);
			is = getRequest.getResponseBodyAsStream();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			is.close();
		}
	}

	/**
	 * 获取Cookie
	 * 
	 * @Title: getCookie
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param name
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getCookie(String name) {
		if (StringUtils.isEmpty(name)) {
			return "";
		}
		Cookie[] cookies = getHttpClient().getState().getCookies();
		for (int i = 0; i < cookies.length; i++) {
			if (name.equals(cookies[i].getName())) {
				return cookies[i].getValue();
			}
		}
		return "";
	}

	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		HttpRequestProxy httpRequestProxy = null;
		httpRequestProxy = (HttpRequestProxy) super.clone();
		httpRequestProxy.client = new HttpClient(
				new SimpleHttpConnectionManager(alwaysClose));
		Cookie[] cookies = client.getState().getCookies();
		httpRequestProxy.client.getState().addCookies(cookies);
		return httpRequestProxy;
	}
}