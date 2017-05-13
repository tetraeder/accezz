package com.accezz.main.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
	 */
	public static void main(String[] args) {
		// String test = "http://www.ynet.co.il";
		// String test2 = "http://www.ynet.co.il/gad";
		try {
			initDatabase("jdbc:sqlite:" + "C:/accezz/db/test.db");
			JSONObject data = selectLatestUrlData("dns", "http://www.ynet.co.il");
			initDatabase("jdbc:sqlite:" + "C:/accezz/db/database.db");
			// JSONObject json = new JSONObject();
			// json.put("test", "no");
			// insertUrlData("dns",json, "http://www.ynet.co.il");
		} catch (SQLException | ParseException e) {
			e.printStackTrace();
		}
	}
}
