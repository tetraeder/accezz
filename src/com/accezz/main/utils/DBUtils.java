package com.accezz.main.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DBUtils {

	private static Connection dbConnection;

	public static void initDatabase(String url) throws SQLException {
		String sql = "CREATE TABLE IF NOT EXISTS log (\n" + "	ID integer PRIMARY KEY,\n"
				+ "	action_type text NOT NULL,\n" + "	data text,\n" + "url text,\n" + "timestamp integer NOT NULL \n"
				+ ");";

		dbConnection = DriverManager.getConnection(url);
		try (Statement stmt = dbConnection.createStatement()) {
			stmt.execute(sql);
		}
	}

	public static void initDatabase() throws SQLException {
		String url = "jdbc:sqlite:" + ConfigurationLoader.getDatabasePath();
		initDatabase(url);
	}

	public static void dropTable() throws SQLException, ParseException {
		String sql = "drop table log";
		try (Statement stmt = getDbConnection().createStatement()) {
			stmt.execute(sql);
		}
	}

	public static JSONObject selectLatestUrlData(String actionType, String url) throws SQLException, ParseException {

		String sql = "select data from log a where url = \'%s\' and action_type = \'%s\' and timestamp ="
				+ " (select max(timestamp) from log b where b.url = a.url and b.action_type = a.action_type)";

		sql = String.format(sql, url, actionType);
		JSONObject dataObject = null;
		try (Statement stmt = getDbConnection().createStatement()) {
			ResultSet rs = stmt.executeQuery(sql);

			if (!rs.next()) {
				dataObject = new JSONObject();
			} else {
				String data = rs.getString("data");
				if (data != null) {
					JSONParser parser = new JSONParser();
					dataObject = (JSONObject) parser.parse(rs.getString("data"));
				} else {
					dataObject = new JSONObject();
				}
			}

		}
		return dataObject;

	}

	public static void insertUrlData(String actionType, JSONObject data, String url) {
		String sql = "INSERT INTO log (action_type,data,url,timestamp) VALUES(?,?,?,?)";
		try {
			update(sql, actionType, data.toJSONString(), url, String.valueOf(System.currentTimeMillis()));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void update(String sql, String... params) throws SQLException {
		PreparedStatement pstmt = getDbConnection().prepareStatement(sql);
		for (int i = 0; i < params.length; i++) {
			pstmt.setString(i + 1, params[i]);
		}
		pstmt.executeUpdate();
	}

	public static Connection getDbConnection() {
		return dbConnection;
	}

	/**
	 * @param args
	 *            the command line arguments
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		// curl -s -w "%{time_total}\n" -o /dev/null http://server:3000
		ProcessBuilder pb = new ProcessBuilder("C:/Program Files (x86)/Git/bin/curl.exe", "-s", "-w",
				"\"%{time_total}\"", "-o", "/dev/null/", "http://www.ynet.co.il");

		// ProcessBuilder pb = new
		// ProcessBuilder("C:/Program Files (x86)/Git/bin/curl.exe", "-s",
		// "http://static.tumblr.com/cszmzik/RUTlyrplz/the-simpsons-season-22-episode-13-the-blue-and-the-gray.jpg");

		pb.directory(new File("c:/test/help/"));
		pb.redirectErrorStream(true);
		Process p = pb.start();
		InputStream is = p.getInputStream();

		FileOutputStream outputStream = new FileOutputStream("c:/test/help/temp.tmp");
		long start = System.currentTimeMillis();
		String line;
		BufferedInputStream bis = new BufferedInputStream(is);
		byte[] bytes = new byte[100];
		int numberByteReaded;
		while ((numberByteReaded = bis.read(bytes, 0, 100)) != -1) {

			outputStream.write(bytes, 0, numberByteReaded);
			Arrays.fill(bytes, (byte) 0);

		}

		System.out.println(System.currentTimeMillis() - start);
		outputStream.flush();
		outputStream.close();
	}
}
