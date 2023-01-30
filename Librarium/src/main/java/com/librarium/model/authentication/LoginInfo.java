package com.librarium.model.authentication;

import com.librarium.controller.utility.EncryptionUtility;

/**
*
*La classe LoginInfo rappresenta le informazioni di accesso per un utente.
*@author Autore sconosciuto
*/
public class LoginInfo {
	
	private String email;
	private String password;
	
	/**
	 * Costruttore di default che inizializza i campi a valori vuoti.
	 */
	public LoginInfo() {
		this("", "");
	}
	
	/**
	 * Costruttore che inizializza i campi con le informazioni di accesso fornite.
	 * 
	 * @param email l'email dell'utente
	 * @param password la password dell'utente
	 */
	public LoginInfo(String email, String password) {
		this.email = email;
		this.password = password;
	}

	/**
	 * Restituisce l'email dell'utente.
	 * 
	 * @return l'email dell'utente
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Imposta l'email dell'utente.
	 * 
	 * @param email l'email dell'utente
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Restituisce la password dell'utente.
	 * 
	 * @return la password dell'utente
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Restituisce la password criptata dell'utente.
	 * 
	 * @return la password criptata dell'utente
	 */
	public String getEncryptedPassword() {
		return EncryptionUtility.encrpyt(password);
	}

	/**
	 * Imposta la password dell'utente.
	 * 
	 * @param password la password dell'utente
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Restituisce una stringa che rappresenta le informazioni di accesso dell'utente.
	 * 
	 * @return una stringa che rappresenta le informazioni di accesso dell'utente
	 */
	@Override
	public String toString() {
		return "LoginInfo [email=" + email + ", password=" + password + "]";
	}
	
}
