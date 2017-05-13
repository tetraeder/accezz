package com.accezz.main.actions.http;

import com.accezz.main.actions.AbstractHttpAction;
import com.accezz.main.actions.ActionKey;

public class HttpAction extends AbstractHttpAction {

	@Override
	public String getCommandKey() {
		return ActionKey.HTTP.command;
	}

	@Override
	public String getName() {
		return ActionKey.HTTP.testName;
	}

}
