package com.dream.common;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Base64;
//import org.apache.commons.codec.digest.DigestUtils;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import com.slspeedup.slframe.common.utils.http.HttpErrorException;
//import com.slspeedup.slframe.common.utils.http.HttpRequestProxy;
import org.apache.commons.codec.digest.DigestUtils;

//@Service
@SuppressWarnings("all")
public class UcpaasClientService {

	private String server;

	private String version;

	private String accountSid;

	private String authToken;

	//
	// public String findAccountInfo() throws Exception {
	//
	// HttpRequestProxy httpRequestProxy = new HttpRequestProxy();
	// String timestamp = new SimpleDateFormat("yyyyMMddHHmmss")
	// .format(new Date());
	// String signature = getSignature(accountSid, authToken, timestamp);
	// String url = getStringBuffer().append("/").append(version)
	// .append("/Accounts/").append(accountSid).append("")
	// .append("?sig=").append(signature).toString();
	// return httpRequestProxy.doRequest(url, null,
	// getHeader(timestamp),"utf-8");
	// }

	// /**
	// * @throws HttpErrorException
	// *
	// * @Title: voiceCode
	// * @Description: TODO(鍙戦�璇煶鐭俊)
	// * @param @param accountSid
	// * @param @param authToken
	// * @param @param appId
	// * @param @param to
	// * @param @param verifyCode
	// * @param @return 璁惧畾鏂囦欢
	// * @return String 杩斿洖绫诲瀷
	// * @throws
	// */
	// public String voiceCode(String appId, String to, String verifyCode)
	// throws HttpErrorException {
	// HttpRequestProxy httpRequestProxy = new HttpRequestProxy();
	// String timestamp = new SimpleDateFormat("yyyyMMddHHmmss")
	// .format(new Date());
	// String signature = getSignature(accountSid, authToken, timestamp);
	// String url = getStringBuffer().append("/").append(version)
	// .append("/Accounts/").append(accountSid)
	// .append("/Calls/voiceCode").append("?sig=").append(signature)
	// .toString();
	// String body = "{\"voiceCode\":{\"appId\":\"" + appId + "\",\"to\":\""
	// + to + "\",\"verifyCode\":\"" + verifyCode + "\"}}";
	// return httpRequestProxy.doRequestForStr(url, body,
	// getHeader(timestamp), "utf-8");
	// }

	/**
	 * @throws HttpErrorException
	 * @throws HttpErrorException
	 * 
	 * @Title: templateSMS @Description: TODO(鍙戦�妯℃澘鐭俊) @param @param
	 *         accountSid @param @param authToken @param @param appId @param @param
	 *         templateId @param @param to @param @param param @param @return
	 *         璁惧畾鏂囦欢 @return String 杩斿洖绫诲瀷 @throws
	 */
	public String templateSMS(String appId, String templateId, String to,
			String param) throws HttpErrorException {

		appId = "db6a58cedd5342cea2040964524b39ce";
		templateId = "14749";
		to = "13548192078";
		param = "0000,2";
		accountSid = "dec661a8ef154aa538d072c2089410b1";
		authToken = "b365b7b045c2958eeccc4d29e4d00194";
		version = "2014-06-30";
		HttpRequestProxy httpRequestProxy = new HttpRequestProxy();
		String timestamp = new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new Date());
		String signature = getSignature(accountSid, authToken, timestamp);
		String url = getStringBuffer().append("/").append(version)
				.append("/Accounts/").append(accountSid)
				.append("/Messages/templateSMS").append("?sig=")
				.append(signature).toString();
		String body = "{\"templateSMS\":{\"appId\":\"" + appId + "\",\"to\":\""
				+ to + "\",\"templateId\":\"" + templateId + "\",\"param\":\""
				+ param + "\"}}";
		return httpRequestProxy.doRequestForStr(url, body,
				getHeader(timestamp), "utf-8");
	}

	/**
	 * 
	 * @Title: getSignature @Description: TODO(鑾峰彇Signature) @param @param
	 *         accountSid @param @param authToken @param @param timestamp @param @return
	 *         璁惧畾鏂囦欢 @return String 杩斿洖绫诲瀷 @throws
	 */
	private String getSignature(String accountSid, String authToken,
			String timestamp) {
		String sig = accountSid + authToken + timestamp;
		return DigestUtils.md5Hex(sig).toUpperCase();
	}

	public StringBuffer getStringBuffer() {
		StringBuffer sb = new StringBuffer("https://api.ucpaas.com");
		return sb;
	}

	public Map<String, String> getHeader(String timestamp) {
		Map<String, String> header = new HashMap<String, String>();
		header.put("Accept", "application/json");
		header.put("Content-Type", "application/json;charset=utf-8");
		String src = accountSid + ":" + timestamp;
		String auth = new String(Base64.encodeBase64(src.getBytes()));
		header.put("Authorization", auth);
		return header;
	}

	public String sendSMS(String phone, String vaildateCode)
			throws HttpErrorException {
		/*
		 * if (!ValidateUtil.isMobileNO(phone)) { throw new
		 * RuntimeException("电话号码错误：" + phone); }
		 */
		if (!isAllowSend(phone)) {
			throw new RuntimeException("不能发送，请稍后再试：" + phone);
		}
		String appId = "dd4121df9b4c4770b0278fa0fed2feb5";
		String templateId = "34598";
		String to = phone.trim();
		String param = vaildateCode + "";
		accountSid = "ac21be992aab7d79fa0bef9fd5b09286";
		authToken = "1feb15491850baf334b8e28861f28511";
		version = "2014-06-30";
		HttpRequestProxy httpRequestProxy = new HttpRequestProxy();
		String timestamp = new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new Date());
		String signature = getSignature(accountSid, authToken, timestamp);
		String url = getStringBuffer().append("/").append(version)
				.append("/Accounts/").append(accountSid)
				.append("/Messages/templateSMS").append("?sig=")
				.append(signature).toString();
		String body = "{\"templateSMS\":{\"appId\":\"" + appId + "\",\"to\":\""
				+ to + "\",\"templateId\":\"" + templateId + "\",\"param\":\""
				+ param + "\"}}";
		return httpRequestProxy.doRequestForStr(url, body,
				getHeader(timestamp), "utf-8");
	}

	public String sendPhone(String phone) throws HttpErrorException {
		if (!isAllowSend(phone)) {
			throw new RuntimeException("不能发送，请稍后再试：" + phone);
		}
		String appId = "dd4121df9b4c4770b0278fa0fed2feb5";
		String templateId = "36175";
		String to = phone.trim();
		// String param = vaildateCode + "";
		accountSid = "ac21be992aab7d79fa0bef9fd5b09286";
		authToken = "1feb15491850baf334b8e28861f28511";
		version = "2014-06-30";
		HttpRequestProxy httpRequestProxy = new HttpRequestProxy();
		String timestamp = new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new Date());
		String signature = getSignature(accountSid, authToken, timestamp);
		String url = getStringBuffer().append("/").append(version)
				.append("/Accounts/").append(accountSid)
				.append("/Messages/templateSMS").append("?sig=")
				.append(signature).toString();
		String body = "{\"templateSMS\":{\"appId\":\"" + appId + "\",\"to\":\""
				+ to + "\",\"templateId\":\"" + templateId + "\"}}";
		return httpRequestProxy.doRequestForStr(url, body,
				getHeader(timestamp), "utf-8");
	}

	public String sendPhone_en(String phone) throws HttpErrorException {
		if (!isAllowSend(phone)) {
			throw new RuntimeException("不能发送，请稍后再试：" + phone);
		}
		String appId = "dd4121df9b4c4770b0278fa0fed2feb5";
		String templateId = "36176";
		String to = phone.trim();
		// String param = vaildateCode + "";
		accountSid = "ac21be992aab7d79fa0bef9fd5b09286";
		authToken = "1feb15491850baf334b8e28861f28511";
		version = "2014-06-30";
		HttpRequestProxy httpRequestProxy = new HttpRequestProxy();
		String timestamp = new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new Date());
		String signature = getSignature(accountSid, authToken, timestamp);
		String url = getStringBuffer().append("/").append(version)
				.append("/Accounts/").append(accountSid)
				.append("/Messages/templateSMS").append("?sig=")
				.append(signature).toString();
		String body = "{\"templateSMS\":{\"appId\":\"" + appId + "\",\"to\":\""
				+ to + "\",\"templateId\":\"" + templateId + "\"}}";
		return httpRequestProxy.doRequestForStr(url, body,
				getHeader(timestamp), "utf-8");
	}

	public String sendSMS_en(String phone, String vaildateCode)
			throws HttpErrorException {
		/*
		 * if (!ValidateUtil.isMobileNO(phone)) { throw new
		 * RuntimeException("电话号码错误：" + phone); }
		 */
		if (!isAllowSend(phone)) {
			throw new RuntimeException("不能发送，请稍后再试：" + phone);
		}
		String appId = "dd4121df9b4c4770b0278fa0fed2feb5";
		String templateId = "35010";
		String to = phone.trim();
		String param = vaildateCode + "";
		accountSid = "ac21be992aab7d79fa0bef9fd5b09286";
		authToken = "1feb15491850baf334b8e28861f28511";
		version = "2014-06-30";
		HttpRequestProxy httpRequestProxy = new HttpRequestProxy();
		String timestamp = new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new Date());
		String signature = getSignature(accountSid, authToken, timestamp);
		String url = getStringBuffer().append("/").append(version)
				.append("/Accounts/").append(accountSid)
				.append("/Messages/templateSMS").append("?sig=")
				.append(signature).toString();
		String body = "{\"templateSMS\":{\"appId\":\"" + appId + "\",\"to\":\""
				+ to + "\",\"templateId\":\"" + templateId + "\",\"param\":\""
				+ param + "\"}}";
		return httpRequestProxy.doRequestForStr(url, body,
				getHeader(timestamp), "utf-8");
	}

	public static void main(String[] args) {
		try {
			// System.out.println(new
			// UcpaasClientService().sendSMS("0084933996010", "0001") + "---");
			System.out.println(new UcpaasClientService().sendSMS(
					"0084933562786", "0002") + "---");
			// System.out.println(new
			// UcpaasClientService().sendSMS("0084963865144", "0001") + "---");
			// System.out.println(new
			// UcpaasClientService().sendSMS("00639054123359", "0001") + "---");
			// System.out.println(new
			// UcpaasClientService().sendSMS("008613600337646", "0001") +
			// "---");
		} catch (HttpErrorException e) {
			e.printStackTrace();
		}
	}

	public String sendPWD(String phone, String pwd) throws HttpErrorException {
		if (!ValidateUtil.isMobileNO(phone)) {
			throw new RuntimeException("电话号码错误：" + phone);
		}
		if (!isAllowSend(phone)) {
			throw new RuntimeException("不能发送，请稍后再试：" + phone);
		}
		String appId = "934383db9ded4d708b97315b3dc9256b";
		String templateId = "23486";
		String to = phone.trim();
		String param = pwd + "";
		accountSid = "dec661a8ef154aa538d072c2089410b1";
		authToken = "b365b7b045c2958eeccc4d29e4d00194";
		version = "2014-06-30";
		HttpRequestProxy httpRequestProxy = new HttpRequestProxy();
		String timestamp = new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new Date());
		String signature = getSignature(accountSid, authToken, timestamp);
		String url = getStringBuffer().append("/").append(version)
				.append("/Accounts/").append(accountSid)
				.append("/Messages/templateSMS").append("?sig=")
				.append(signature).toString();
		String body = "{\"templateSMS\":{\"appId\":\"" + appId + "\",\"to\":\""
				+ to + "\",\"templateId\":\"" + templateId + "\",\"param\":\""
				+ param + "\"}}";
		return httpRequestProxy.doRequestForStr(url, body,
				getHeader(timestamp), "utf-8");
	}

	/**
	 * 是否允许发生短信
	 * 
	 * @param phone
	 * @return
	 */
	private static boolean isAllowSend(String phone) {

		return true;
	}

	/**
	 * 短信发送记录
	 * 
	 * @author yangmin
	 *
	 */
	static class SendMsgRecord implements Serializable {
		private static final long serialVersionUID = -133223621231841283L;
		private int sendTimes; // 发送次数
		private Date lastSendTime;// 上次发送时间

		public int getSendTimes() {
			return sendTimes;
		}

		public void setSendTimes(int sendTimes) {
			this.sendTimes = sendTimes;
		}

		public Date getLastSendTime() {
			return lastSendTime;
		}

		public void setLastSendTime(Date lastSendTime) {
			this.lastSendTime = lastSendTime;
		}

	}
}