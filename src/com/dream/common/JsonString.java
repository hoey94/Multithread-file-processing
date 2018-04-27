package com.dream.common;

public class JsonString {

	public static String[] system_car = new String[] { "listgps",
			"system_user", "listsc", "system_group_car" };
	public static String[] system_user = new String[] { "listcar" };;
	public static String[] system_company = new String[] { "listsc" };;
	public static String[] system_device = new String[] { "system_car" };
	public static String system_device_sql1 = "t.id,t.datetime, t.status, t.localstatus, t.power, t.longitude, t.latitude, t.high, t.speed, t.temp1, t.temp2, t.temp3, t.temp4, t.temp5, t.temp6, t.temp7, t.temp8, t.temp9, t.temp10, t.oil, t.mile, t.gpsmile, t.gpsnum, t.direction, t.exceptionmsg, t.alarmmsg,t1.carzdbianhao,t1.carcard";
	public static String[] system_option = new String[] { "system_User" };
	public static String[] system_group_car = new String[] { "System_Company",
			"listsc" };

}
