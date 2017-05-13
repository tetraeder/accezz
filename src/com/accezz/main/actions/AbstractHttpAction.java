package com.accezz.main.actions;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.pmw.tinylog.Logger;

import com.accezz.main.consts.AccezzConsts;
import com.accezz.main.utils.ConfigurationLoader;
import com.accezz.main.utils.DBUtils;
import com.accezz.main.utils.MailUtils;

public abstract class AbstractHttpAction implements IAction {
	protected final long LATENCY_THRESHOLD;
	protected final long BANDWIDTH_THRESHOLD;

	public AbstractHttpAction() {
		JSONObject serviceDetails = ConfigurationLoader.getServiceConfigurations().get(getCommandKey());
		LATENCY_THRESHOLD = (long) serviceDetails.get(AccezzConsts.ACTION_PARAM_LATENCY);
		BANDWIDTH_THRESHOLD = (long) serviceDetails.get(AccezzConsts.ACTION_PARAM_BANDWITH);
	}

	@Override
	public void perform(String line) {
		CommandDetails command = new CommandDetails(line);
		try {
			JSONObject previousAction = DBUtils.selectLatestUrlData(getCommandKey(),
					command.getParam(AccezzConsts.PARAM_URL));
			JSONObject responseDetails = execute(command);
			saveResultsToDatabase(command, responseDetails);
			saveResultsToFile(command, responseDetails, previousAction);
			logProccessing("Done Proccessing: ", line);
		} catch (SQLException | ParseException e) {
			logProccessing("Error processing: ", line);
		}
	}

	private void logProccessing(String msg, String line) {
		if (AccezzConsts.COMMAND_FILE_COMPLETED.equals(line)) {
			return;
		}
		System.out.println(msg + line);
	}

	private boolean validateResults(JSONObject responseDetails, JSONObject previousAction) {
		if (previousAction.isEmpty()) {
			return true;
		}
		long currentLatency = (long) responseDetails.get(AccezzConsts.ACTION_PARAM_LATENCY);
		long previousLatency = (long) previousAction.get(AccezzConsts.ACTION_PARAM_LATENCY);
		boolean isLatencyValid = (currentLatency - LATENCY_THRESHOLD) < previousLatency;

		long currentBandwidth = (long) responseDetails.get(AccezzConsts.ACTION_PARAM_BANDWITH);
		long previousBandwidth = (long) previousAction.get(AccezzConsts.ACTION_PARAM_BANDWITH);
		boolean isBandwidthValid = (currentBandwidth * ((100 - BANDWIDTH_THRESHOLD) / 100)) < previousBandwidth;

		return isLatencyValid && isBandwidthValid;
	}

	private void saveResultsToFile(CommandDetails command, JSONObject responseDetails, JSONObject previousAction) {
		boolean result = validateResults(responseDetails, previousAction);
		if (result) {
			Logger.info(getName() + " test to " + command.getParam(AccezzConsts.PARAM_URL) + ": SUCCESS. Latency: "
					+ responseDetails.get(AccezzConsts.ACTION_PARAM_LATENCY) + "ms, Bandwidth: "
					+ responseDetails.get(AccezzConsts.ACTION_PARAM_BANDWITH) + "MBPs");
		} else {
			String msg = getName() + " test to " + command.getParam(AccezzConsts.PARAM_URL) + ": FAILURE. Latency: "
					+ responseDetails.get(AccezzConsts.ACTION_PARAM_LATENCY) + "ms, Bandwidth: "
					+ responseDetails.get(AccezzConsts.ACTION_PARAM_BANDWITH) + "BPs";
			Logger.error(msg);
			MailUtils.sendMail(msg);
		}
	}

	private void saveResultsToDatabase(CommandDetails commandDetails, JSONObject responseDetails) {
		String url = commandDetails.getParam(AccezzConsts.PARAM_URL);
		DBUtils.insertUrlData(getCommandKey(), responseDetails, url);
	}

	protected JSONObject execute(CommandDetails command) {
		String url = command.getParam(AccezzConsts.PARAM_URL);
		JSONObject responseDetails = testLatency(url);
		testBandwidth(responseDetails, url);
		return responseDetails;
	}

	private void testBandwidth(JSONObject responseDetails, String url) {
		OutputStream out = null;
		URLConnection conn = null;
		InputStream in = null;
		responseDetails.put(AccezzConsts.ACTION_PARAM_BANDWITH, 0L);

		long currentNano = System.nanoTime();
		String tempFileName = currentNano + ".tmp";
		String tempFileFullPath = ConfigurationLoader.getTempDir() + tempFileName;
		try {
			URL connectionUrl = new URL(url);
			out = new BufferedOutputStream(new FileOutputStream(tempFileFullPath));
			conn = connectionUrl.openConnection();
			in = conn.getInputStream();
			byte[] buffer = new byte[1024];
			int numRead;
			long numWritten = 0;
			long start = System.currentTimeMillis();
			while ((numRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, numRead);
				numWritten += numRead;
				if (checkBandwidthTestTimeout(start)) {
					break;
				}
			}
			long end = System.currentTimeMillis();
			long speed = numWritten / ((end - start) * 1000);
			responseDetails.put(AccezzConsts.ACTION_PARAM_BANDWITH, speed);
		} catch (Exception ex) {
			// TODO
			System.out.println(ex.getMessage());
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
					new File(tempFileFullPath).delete();
				}
			} catch (IOException ex) {
				// TODO
				System.out.println(ex.getMessage());
			}
		}
	}

	private static boolean checkBandwidthTestTimeout(long start) {
		return (System.currentTimeMillis() - start) > 5000;
	}

	private JSONObject testLatency(String url) {
		JSONObject responseDetails = new JSONObject();
		int timeOut = 30000;
		long s = System.currentTimeMillis();
		try {
			url = trimPrefixIfNeeded(url);
			InetAddress.getByName(url).isReachable(timeOut);
		} catch (IOException e) {
			e.printStackTrace();
		}
		responseDetails.put(AccezzConsts.ACTION_PARAM_LATENCY, System.currentTimeMillis() - s);
		return responseDetails;
	}

	protected String trimPrefixIfNeeded(String url) {
		if (url.indexOf("://") > -1) {
			url = url.substring(url.indexOf("://") + 3);
		}
		return url;
	}

}
