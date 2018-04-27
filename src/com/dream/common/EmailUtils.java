/**
 * 
 */
package com.dream.common;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * 
 * 简单的邮件发送
 * 
 * @author jianghong
 * 
 * @createDate 2014年7月21日 下午2:02:22
 * 
 * @file EmailUtils.java
 * 
 * @pakage EmailUtils
 * 
 * @email jiangjianhua123@qq.com
 * 
 * @version 0.1
 * 
 */
@SuppressWarnings("all")
public class EmailUtils {

	public static void main(String[] args) throws Exception {
		// System.out.println(3&1);
		// System.out.println(4&1);
		EmailUtils.sendMail("zhuixun_hebe@qq.com", "通过课程预定消息2",
				"你好！<br>1我们真诚的提醒你，你的课程已经完全提交，请注意及时回复我们。 你预定的课程通过了！");
	}

	/**
	 * 发送邮件　
	 * 
	 * @param fromEmail
	 *            　发送邮箱
	 * @param toEmail
	 *            　接收邮箱
	 * @param emailName
	 *            　邮箱登录名
	 * @param emailPassword
	 *            　密码
	 * @param title
	 *            　发送主题
	 * @param centent
	 *            　发送内容
	 * @throws Exception
	 */
	public static void sendMail(String fromEmail, String cemail,
			String toEmail, String emailName, String emailPassword,
			String title, String centent) throws Exception {
		Properties properties = new Properties();// 创建Properties对象
		properties.setProperty("mail.transport.protocol", "smtp");// 设置传输协议
		properties.put("mail.smtp.host", PropertiesUtils.getProperties()
				.getProperty("mail.smtp.host"));// 设置发信邮箱的smtp地址
		properties.setProperty("mail.smtp.auth", "true"); // 验证

		Authenticator auth = new AjavaAuthenticator(emailName, emailPassword); // 使用验证，创建一个Authenticator
		Session session = Session.getDefaultInstance(properties, auth);// 根据Properties，Authenticator创建Session
		Message message = new MimeMessage(session);// Message存储发送的电子邮件信息
		message.setFrom(new InternetAddress(fromEmail));
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(
				toEmail));// 设置收信邮箱
		// 指定邮箱内容及ContentType和编码方式
		message.setContent(centent, "text/html;charset=utf-8");
		message.setSubject(title);// 设置主题
		message.setSentDate(new Date());// 设置发信时间

		Transport.send(message);// 发送
	}

	private static String getMailList(String[] mailArray) {

		StringBuffer toList = new StringBuffer();
		int length = mailArray.length;
		if (mailArray != null && length < 2) {
			toList.append(mailArray[0]);
		} else {
			for (int i = 0; i < length; i++) {
				toList.append(mailArray[i]);
				if (i != (length - 1)) {
					toList.append(",");

				}
			}
		}
		return toList.toString();
	}

	/**
	 * 默认邮箱发送邮件
	 * 
	 * @param toEmail
	 *            　接收邮箱
	 * @param title
	 *            　发送主题
	 * @param centent
	 *            　发送内容
	 * @throws Exception
	 */
	public static void sendMail(String toEmail, String title, String centent)
			throws Exception {

		Properties properties = new Properties();// 创建Properties对象
		properties.setProperty("mail.transport.protocol", "smtp");// 设置传输协议
		properties.put("mail.smtp.host", PropertiesUtils.getProperties()
				.getProperty("mail.smtp.host"));// 设置发信邮箱的smtp地址
		properties.setProperty("mail.smtp.auth", "true"); // 验证
		Authenticator auth = new AjavaAuthenticator(PropertiesUtils
				.getProperties().getProperty("emailName"), PropertiesUtils
				.getProperties().getProperty("emailPassword")); // 使用验证，创建一个Authenticator
		Session session = Session.getInstance(properties, auth);// 根据Properties，Authenticator创建Session
		// 抄送
		// 抄送
		// String[] cs = new String[] { toEmail };
		// InternetAddress[] iaToListcs = new
		// InternetAddress().parse(getMailList(cs));
		Message message = new MimeMessage(session);// Message存储发送的电子邮件信息
		message.setFrom(new InternetAddress(PropertiesUtils.getProperties()
				.getProperty("fromEmail")));
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(
				toEmail));// 设置收信邮箱
		// 指定邮箱内容及ContentType和编码方式
		message.setContent(centent, "text/html;charset=utf-8");
		message.setSubject(title);// 设置主题
		message.setSentDate(new Date());// 设置发信时间
		// message.setRecipients(Message.RecipientType.CC, iaToListcs); // 抄送人
		Transport.send(message);// 发送

	}
}

// 创建传入身份验证信息的 Authenticator类
class AjavaAuthenticator extends Authenticator {
	private String user;
	private String pwd;

	public AjavaAuthenticator(String user, String pwd) {
		this.user = user;
		this.pwd = pwd;
	}

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(user, pwd);
	}
}
