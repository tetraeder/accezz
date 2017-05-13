package com.accezz.main.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.SQLException;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.accezz.main.controller.AccezzController;
import com.accezz.main.entity.AccezzException;

public class AccezzCLI {
	private static final String DEFAULT_CONFIG_FILE = "c:/accezz/config.json";

	public static void main(String[] args) {
		AccezzCLI accezz = new AccezzCLI();
		accezz.execute(args);
	}

	// TODO document
	/**
	 *
	 * @param args
	 */
	public void execute(String[] args) {
		if (args.length == 0) {
			System.out.println("Missing required argument: command file");
			return;
		}

		try {
			ConfigurationLoader.loadConfigurationFile(handleConfigFileArg(args));

			DBUtils.initDatabase();
			FileUtils.initLogFile();

			AccezzController controller = new AccezzController();
			controller.request(retreiveCommandFile(args));
		} catch (IOException | ParseException e) {
			System.out.println("Error loading configuration files");
		} catch (SQLException e) {
			System.out.println("Error initializing database");
		} catch (AccezzException e) {
			System.out.println("Error occured " + e.getMessage());
		}
	}

	private File retreiveCommandFile(String[] args) {
		return new File(args[0]);
	}

	@Deprecated
	private void executeCommands(String[] args) {
		String commandFile = args[0];
		try (BufferedReader br = new BufferedReader(new FileReader(commandFile))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (!validateCommand(line)) {
					continue;
				}
				String command = extractServiceName(line);
				JSONObject service = ConfigurationLoader.getServiceConfigurations().get(command);
				callService(service, line);
			}
		} catch (IOException e) {
			System.out.println("Error loading command file");
		}
	}

	@Deprecated
	private void callService(JSONObject service, String line) {
		String charset = "UTF-8";
		StringBuffer urlString;
		try {
			urlString = new StringBuffer("http://localhost:8080/").append(service.get("serviceUrl"))
					.append("?command=").append(URLEncoder.encode(line, charset));

			URL url = new URL(urlString.toString());
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			int responseCode = con.getResponseCode();
			handleResponseCode(responseCode);
			con.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Deprecated
	private void handleResponseCode(int responseCode) {
		//
	}

	private String extractServiceName(String line) {
		return line.split(" ")[0];
	}

	private boolean validateCommand(String line) {
		if (line.isEmpty()) {
			return false;
		}

		if (!ConfigurationLoader.getServiceConfigurations().containsKey(extractServiceName(line))) {
			return false;
		}

		return true;
	}

	private static String handleConfigFileArg(String[] args) {
		String configFile = args.length == 2 ? args[1] : DEFAULT_CONFIG_FILE;
		return configFile;
	}

}
