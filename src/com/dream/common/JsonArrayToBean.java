package com.dream.common;

import net.sf.json.JSONArray;

public class JsonArrayToBean {

	public JSONArray jsonArray = new JSONArray();;

	public JsonArrayToBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public JsonArrayToBean(Object... args) {

		for (int i = 0; i < args.length; i++) {
			jsonArray.add(args[i]);
		}
	}

}
