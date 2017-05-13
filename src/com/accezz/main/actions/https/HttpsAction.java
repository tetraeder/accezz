package com.accezz.main.actions.https;

import com.accezz.main.actions.AbstractHttpAction;
import com.accezz.main.actions.ActionKey;

public class HttpsAction extends AbstractHttpAction {

	@Override
	public String getCommandKey() {
		return ActionKey.HTTPS.command;
	}

	@Override
	public String getName() {
		return ActionKey.HTTPS.testName;
	}

}
