package com.dream.common;

import java.math.BigDecimal;

public class GPSConver {
	private static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

	/**
	 * 对double类型数据保留小数点后多少位 高德地图转码返回的就是 小数点后6位，为了统一封装一下
	 * 
	 * @param digit
	 *            位数
	 * @param in
	 *            输入
	 * @return 保留小数位后的数
	 */
	static double dataDigit(int digit, double in) {
		return new BigDecimal(in).setScale(6, BigDecimal.ROUND_HALF_UP)
				.doubleValue();

	}

	/**
	 * 将火星坐标转变成百度坐标
	 * 
	 * @param lngLat_gd
	 *            火星坐标（高德、腾讯地图坐标等）
	 * @return 百度坐标
	 */

	public static LngLat bd_encrypt(LngLat lngLat_gd) {
		double x = lngLat_gd.getLongitude(), y = lngLat_gd.getLantitude();
		double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
		return new LngLat(dataDigit(6, z * Math.cos(theta) + 0.0065),
				dataDigit(6, z * Math.sin(theta) + 0.006));

	}

	/**
	 * 将百度坐标转变成火星坐标
	 * 
	 * @param lngLat_bd
	 *            百度坐标（百度地图坐标）
	 * @return 火星坐标(高德、腾讯地图等)
	 */
	static LngLat bd_decrypt(LngLat lngLat_bd) {
		double x = lngLat_bd.getLongitude() - 0.0065, y = lngLat_bd
				.getLantitude() - 0.006;
		double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
		return new LngLat(dataDigit(6, z * Math.cos(theta)), dataDigit(6, z
				* Math.sin(theta)));

	}

	/*
	 * GPS坐标转换为高德地图坐标 输入GPS坐标，单位度，数据类型double，参数一为Lat,参数二为Lng
	 * 输出高德地图坐标，单位度，数据类型double[]，参数一为Lat,参数二为Lng
	 */
	private static double pi = Math.PI;
	private static double a = 6378245.0;
	private static double ee = 0.00669342162296594323;

	public static double[] transLatLng(double wgLat, double wgLng) {
		double[] ds = new double[2];
		double dLat = transLat(wgLng - 105.0, wgLat - 35.0, pi);
		double dLng = transLng(wgLng - 105.0, wgLat - 35.0, pi);
		double radLat = wgLat / 180.0 * pi;
		double magic = Math.sin(radLat);
		magic = 1 - ee * magic * magic;
		double sqrtMagic = Math.sqrt(magic);
		dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
		dLng = (dLng * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
		ds[0] = wgLat + dLat;
		ds[1] = wgLng + dLng;
		return ds;
	}

	private static double transLat(double x, double y, double pi) {
		double ret;
		ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2
				* Math.sqrt(Math.abs(x));
		ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
		ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
		return ret;
	}

	private static double transLng(double x, double y, double pi) {
		double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1
				* Math.sqrt(Math.abs(x));
		ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
		ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0
				* pi)) * 2.0 / 3.0;
		return ret;
	}

	// 测试代码
	public static void main(String[] args) {
		LngLat lngLat_bd = new LngLat(120.153192, 30.25897);
		System.out.println(bd_decrypt(lngLat_bd));
	}
}
