package com.accezz.main.actions.dns;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.json.simple.JSONObject;
import org.pmw.tinylog.Logger;

import com.accezz.main.actions.ActionKey;
import com.accezz.main.actions.CommandDetails;
import com.accezz.main.actions.IAction;
import com.accezz.main.consts.AccezzConsts;
import com.accezz.main.utils.DBUtils;

public class DnsAction implements IAction {

	@Override
	public void perform(String line) {
		CommandDetails command = new CommandDetails(line);
		JSONObject responseDetails = execute(command);
		saveResultsToDatabase(command, responseDetails);
		saveResultsToFile(command, responseDetails);
		System.out.println("Done processing: " + line);
	}

	private JSONObject execute(CommandDetails command) {
		JSONObject responseDetails = new JSONObject();
		boolean found = true;
		try {
			InetAddress.getAllByName(command.getParam(AccezzConsts.PARAM_URL));
		} catch (UnknownHostException e) {
			found = false;
		}

		responseDetails.put(AccezzConsts.ACTION_PARAM_DNS_LOOKUP, found);
		return responseDetails;
	}

	private boolean validateResults(JSONObject responseDetails) {
		return (boolean) responseDetails.get(AccezzConsts.ACTION_PARAM_DNS_LOOKUP);
	}

	private void saveResultsToFile(CommandDetails command, JSONObject responseDetails) {
		boolean result = validateResults(responseDetails);
		if (result) {
			Logger.info(getName() + " test to " + command.getParam(AccezzConsts.PARAM_URL) + ": SUCCESS.");
		} else {
			Logger.error(getName() + " test to " + command.getParam(AccezzConsts.PARAM_URL) + ": FAILURE");
		}
	}

	private void saveResultsToDatabase(CommandDetails commandDetails, JSONObject responseDetails) {
		String url = commandDetails.getParam(AccezzConsts.PARAM_URL);
		DBUtils.insertUrlData(getCommandKey(), responseDetails, url);
	}

	@Override
	public String getCommandKey() {
		return ActionKey.DNS.command;
	}

	@Override
	public String getName() {
		return ActionKey.DNS.testName;
	}
}
