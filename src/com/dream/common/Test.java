package com.dream.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

@SuppressWarnings("all")
public class Test {

	public static void main(String[] args) throws Exception {
		// String url =
		// "http://restapi.amap.com/v3/geocode/regeo?output=json&key=53af259f31386633b25ae8c1c97d8361&radius=500&extensions=base&batch=true";
		// List list = new ArrayList<NameValuePair>();
		// list.add(new
		// BasicNameValuePair("location","116.66556,31.11154|116.310009,39.991988"));
		// list.add(new BasicNameValuePair("batch","true"));
		// String str = Common.httpPost(url, list);
		// System.out.println(str);

		String url = "http://restapi.amap.com/v3/assistant/coordinate/convert";
		List list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("locations",
				"121.401005,31.1668183|121.401005,31.1668183"));
		list.add(new BasicNameValuePair("coordsys", "gps"));
		list.add(new BasicNameValuePair("output", "json"));
		list.add(new BasicNameValuePair("key",
				"53af259f31386633b25ae8c1c97d8361"));
		String str = Common.httpPost(url, list);
		System.out.println(str);
	}
}
