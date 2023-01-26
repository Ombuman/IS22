package com.librarium.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DatabaseConnection {
	private final String DB_PATH = "jdbc:sqlite:" + System.getProperty("user.dir") + "/data/librarium.db";
	protected Connection connection;
	
	protected DatabaseConnection() {
		try {
			connection = DriverManager.getConnection(DB_PATH);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void setAutoCommit(boolean active) throws SQLException {
		connection.setAutoCommit(active);
	}
	
	public void rollback() throws SQLException {
		connection.rollback();
	}
}
