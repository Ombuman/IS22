package com.librarium.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Questa classe rappresenta una connessione al database.
 */
public abstract class DatabaseConnection {
	/**
	 * La stringa che rappresenta il percorso del database.
	 */
	private final String DB_PATH = "jdbc:sqlite:" + System.getProperty("user.dir") + "/data/librarium.db";
	
	/**
	 * La connessione al database.
	 */
	protected Connection connection;
	
	/**
	 * Costruttore protetto che stabilisce la connessione al database.
	 * In caso di errore, viene stampato un messaggio di errore.
	 */
	protected DatabaseConnection() {
		try {
			connection = DriverManager.getConnection(DB_PATH);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Imposta l'autocommit per la connessione al database.
	 * 
	 * @param active Il valore booleano che indica se l'autocommit è attivo o disattivo.
	 * @throws SQLException In caso di errore durante l'impostazione dell'autocommit.
	 */
	public void setAutoCommit(boolean active) throws SQLException {
		connection.setAutoCommit(active);
	}
	
	/**
	 * Effettua un rollback sulla connessione al database.
	 * @throws SQLException In caso di errore durante il rollback.
	 */
	public void rollback() throws SQLException {
		connection.rollback();
	}
}
