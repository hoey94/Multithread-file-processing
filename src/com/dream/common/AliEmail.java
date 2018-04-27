package com.dream.common;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.dm.model.v20151123.SingleSendMailResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

@SuppressWarnings("all")
public class AliEmail {

	public void sample() {
		// IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou",
		// "LTAIfRSlZtT11yrs", "iU5pCzG6y88DCaJW5wEJa5BMFlUFs2");
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou",
				"LTAIFpIxFH50kK71", "EtJ8a4pgFfDvUzIuevaev4xh0JIvDV");

		IAcsClient client = new DefaultAcsClient(profile);
		SingleSendMailRequest request = new SingleSendMailRequest();
		try {
			request.setAccountName("postmaster@we-speak.cn");
			request.setFromAlias("we-speak Admin");
			request.setAddressType(1);
			request.setTagName("控制台创建的标签");
			request.setReplyToAddress(true);
			request.setToAddress("zhuixun_hebe@qq.com");
			request.setSubject("欢迎你来全友之家");
			request.setHtmlBody("热诚的希望你来的全友之前，非常的喜欢你的到来");
			SingleSendMailResponse httpResponse = client
					.getAcsResponse(request);
		} catch (ServerException e) {
			System.out.println("--------------邮件发送错误------------------");
		} catch (ClientException e) {
			System.out.println("--------------邮件发送错误------------------");
		}
		System.out.println("-----------发送成功-------");
	}

	public static IClientProfile profile = DefaultProfile
			.getProfile("cn-hangzhou", "LTAIFpIxFH50kK71",
					"EtJ8a4pgFfDvUzIuevaev4xh0JIvDV");

	/**
	 * 
	 * @param toEmail
	 *            发送的邮件
	 * @param title
	 *            标题
	 * @param content
	 * @param htmlBody
	 */

	public static boolean send(String toEmail, String title, String htmlBody) {

		IAcsClient client = new DefaultAcsClient(profile);
		SingleSendMailRequest request = new SingleSendMailRequest();
		try {
			request.setAccountName("postmaster@we-speak.cn");
			request.setFromAlias("we-speak Admin");
			request.setAddressType(1);
			request.setTagName("控制台创建的标签");
			request.setReplyToAddress(true);
			request.setToAddress(toEmail);
			request.setSubject(title);
			request.setHtmlBody(htmlBody);
			SingleSendMailResponse httpResponse = client
					.getAcsResponse(request);
		} catch (ServerException e) {
			System.out.println("--------------邮件发送错误------------------");
			e.printStackTrace();
			return false;
		} catch (ClientException e) {
			System.out.println("--------------邮件发送错误------------------");
			e.printStackTrace();
			return false;
		}
		System.out.println("--------------邮件发送成功------------------");
		return true;
	}

	public static void main(String[] args) {
		AliEmail ae = new AliEmail();
		ae.sample();
	}
}
