package com.accezz.main.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.accezz.main.actions.IAction;
import com.accezz.main.actions.dns.DnsAction;
import com.accezz.main.actions.http.HttpAction;
import com.accezz.main.actions.https.HttpsAction;

public class ActionFactory {
	private static Map<String, IAction> actionMap = new ConcurrentHashMap<>();

	static {
		initActionsConfigurations();
	}

	public static void initActionsConfigurations() {
		HttpAction httpAction = new HttpAction();
		HttpsAction httpsAction = new HttpsAction();
		DnsAction dnsAction = new DnsAction();

		actionMap.put(httpAction.getCommandKey(), httpAction);
		actionMap.put(httpsAction.getCommandKey(), httpsAction);
		actionMap.put(dnsAction.getCommandKey(), dnsAction);
	}

	public static IAction getAction(String line) {
		String command = getCommandName(line);
		IAction action = actionMap.get(command);
		if (action == null) {
			System.out.println("no action found for: " + line);
		}
		return action;
	}

	private static String getCommandName(String line) {
		if (line == null) {
			return null;
		}

		return line.split(" ")[0];
	}

}
