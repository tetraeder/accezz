package com.accezz.mock;

import org.json.simple.JSONObject;

import com.accezz.main.consts.AccezzConsts;

public class Web {

	private static JSONObject mockWebAction() {
		JSONObject responseDetails = new JSONObject();
		responseDetails.put(AccezzConsts.ACTION_PARAM_LATENCY, (long) (Math.random() * 15000));
		responseDetails.put(AccezzConsts.ACTION_PARAM_BANDWITH, (long) (Math.random() * 15000));
		responseDetails.put(AccezzConsts.ACTION_PARAM_DNS_LOOKUP, true);

		try {
			Thread.sleep((long) responseDetails.get(AccezzConsts.ACTION_PARAM_LATENCY));
		} catch (InterruptedException e) {

		}

		return responseDetails;
	}

	public static JSONObject doHttpStuff() {
		return mockWebAction();
	}

	public static JSONObject doHttpsStuff() {
		return mockWebAction();
	}

	public static JSONObject doDnsStuff() {
		return mockWebAction();
	}
}
