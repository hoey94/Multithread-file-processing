package com.dream.common;

import org.apache.commons.lang.StringUtils;

public class ValidateUtil {

	/**
	 * 验证手机号码
	 * 
	 * @param mobiles
	 *            手机号码
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		if (StringUtils.isEmpty(mobiles)) {
			return false;
		}
		return mobiles.matches("^((13[0-9])|(15[0-9])|(18[0-9]))\\d{8}$");
	}

	/**
	 * 验证邮编
	 * 
	 * @param post
	 * @return
	 */
	public static boolean isPost(String post) {
		return post.matches("[1-9]\\d{5}(?!\\d)");
	}

	/**
	 * 验证邮箱
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		return email
				.matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	}
}