package com.accezz.main.actions;

public enum ActionKey {
	HTTP("http", "HTTP"), HTTPS("https", "HTTPS"), DNS("dns", "DNS");
	public final String command;
	public final String testName;

	private ActionKey(String command, String testName) {
		this.command = command;
		this.testName = testName;
	}
}
