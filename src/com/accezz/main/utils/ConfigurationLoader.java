package com.accezz.main.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.accezz.main.controller.ActionFactory;
import com.accezz.main.entity.AccezzException;

public class ConfigurationLoader {
	private static Map<String, JSONObject> serviceConfigurations = new HashMap<>();
	private static String databasePath;
	private static String logPath;
	private static String tempDir;
	private static long rolloutAfter;
	private static String curlDir;

	/**
	 * 
	 * @param file
	 *            configuration file
	 * @throws FileNotFoundException
	 *             if configuration file doesn't exist
	 * @throws IOException
	 *             Exception while reading the file, parsing it, or while
	 *             reading/parsing services configuration
	 * 
	 * @throws ParseException
	 *             Exception while parsing the file, parsing it, or while
	 *             parsing services configuration
	 */
	public static void loadConfigurationFile(String file) throws IOException, ParseException {
		try (FileReader fileReader = new FileReader(file)) {
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(fileReader);
			JSONObject jsonObject = (JSONObject) obj;

			databasePath = ((String) jsonObject.get("database"));
			tempDir = ((String) jsonObject.get("tempDir"));
			logPath = ((String) jsonObject.get("logFile"));
			rolloutAfter = ((long) jsonObject.get("rolloutAfter"));
			curlDir = ((String) jsonObject.get("curlDir"));
			handleDirectories();
			String servicesFile = (String) jsonObject.get("servicesFile");

			loadServices(servicesFile);
		}
	}

	private static void handleDirectories() {
		File dir = new File(tempDir);

		if (!dir.exists()) {
			boolean result = false;

			try {
				dir.mkdir();
				result = true;
			} catch (SecurityException se) {
				throw new AccezzException(se);
			}
			if (result) {
				dir = new File(tempDir + "db");

				if (!dir.exists()) {
					result = false;

					try {
						dir.mkdir();
						result = true;
					} catch (SecurityException se) {
						throw new AccezzException(se);
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param file
	 *            service configuration file
	 * @throws FileNotFoundException
	 *             if configuration file doesn't exist
	 * @throws IOException
	 *             Exception while reading/parsing the file
	 * 
	 * @throws ParseException
	 *             Exception while parsing the file/parsing it
	 */
	private static void loadServices(String file) throws IOException, ParseException {
		try (FileReader fileReader = new FileReader(file)) {
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(fileReader);
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray services = (JSONArray) jsonObject.get("services");

			for (int i = 0; i < services.size(); i++) {
				JSONObject service = (JSONObject) services.get(i);
				serviceConfigurations.put((String) service.get("syntax"), service);
			}
			ActionFactory.initActionsConfigurations();
		}
	}

	public static Map<String, JSONObject> getServiceConfigurations() {
		return serviceConfigurations;
	}

	public static String getDatabasePath() {
		return databasePath;
	}

	public static String getLogPath() {
		return logPath;
	}

	public static long getRolloutAfter() {
		return rolloutAfter;
	}

	public static String getTempDir() {
		return tempDir;
	}

	public static void setTempDir(String tempDir) {
		ConfigurationLoader.tempDir = tempDir;
	}

	public static void main(String[] args) {
		handleDirectories();
	}

	public static String getCurlDir() {
		return curlDir;
	}
}
