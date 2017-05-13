package com.accezz.main.actions;

import java.util.HashMap;
import java.util.Map;

public class CommandDetails {
	private String name;
	private Map<String, String> params = new HashMap<>();
	
	public CommandDetails(String text) {
		String[] args = text.split(" ");
		name = args[0];
		
		for (int i = 1; i < args.length; i+=2) {
			params.put(args[i], args[i + 1]);
		}
	}

	public String getName() {
		return name;
	}

	public String getParam(String key) {
		return params.get(key);
	}
}
