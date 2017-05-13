package com.accezz.main.actions;

public interface IAction {
	String getCommandKey();

	String getName();

	void perform(String command);
}
