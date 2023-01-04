package com.librarium.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import com.vaadin.base.devserver.DevServerOutputTracker.Result;

public abstract class DatabaseConnection {
	private static final String DATABASE_PATH = "/data/librarium.db";
	
	protected static Connection connect() {
		String url = "jdbc:sqlite:" + System.getProperty("user.dir") + DATABASE_PATH;
		Connection conn = null;

		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return conn;
	}
}
