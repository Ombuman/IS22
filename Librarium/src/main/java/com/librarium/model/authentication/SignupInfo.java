package com.librarium.model.authentication;

import com.librarium.controller.utility.EncryptionUtility;

/**
*
*La classe SignupInfo rappresenta le informazioni necessarie per l'iscrizione di un utente.
*Contiene i campi per il nome, cognome, email, password e conferma password dell'utente.
*Offre metodi per accedere e modificare queste informazioni.
*Inoltre, fornisce un metodo per crittografare la password.
*
*/
public class SignupInfo {
	
	private String nome;
	private String cognome;
	private String email;
	private String password;
	private String confermaPassword;
	
	/**
	*Costruttore di default che imposta tutti i campi a stringhe vuote.
	*/
	public SignupInfo() {
		this("", "", "", "");
	}
	
	/**
	*
	*Costruttore che accetta come argomenti i valori per nome, cognome, email e password.
	*@param nome il nome dell'utente
	*@param cognome il cognome dell'utente
	*@param email l'email dell'utente
	*@param password la password scelta dall'utente
	*/
	public SignupInfo(String nome, String cognome, String email, String password) {
		this.nome = nome;
		this.cognome = cognome;
		this.email = email;
		this.password = password;
	}

	/**
	*
	*Restituisce il nome dell'utente.
	*@return il nome dell'utente
	*/
	public String getNome() {
		return nome;
	}

	/**
	*
	*Imposta il nome dell'utente.
	*@param nome il nome dell'utente
	*/
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	*
	*Restituisce il cognome dell'utente.
	*@return il cognome dell'utente
	*/
	public String getCognome() {
		return cognome;
	}
	/**
	*
	*Imposta il cognome dell'utente.
	*@param cognome il cognome dell'utente
	*/
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	
	/**
	*
	*Imposta il cognome dell'utente.
	*@param cognome il cognome dell'utente
	*/
	public String getEmail() {
		return email;
	}

	/**
	*
	*Imposta l'email per l'oggetto corrente della classe SignupInfo
	*@param email stringa che rappresenta l'email dell'utente
	*/
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	*
	*Restituisce la password impostata per l'oggetto corrente della classe SignupInfo
	*@return la password come stringa
	*/
	public String getPassword() {
		return password;
	}
	
	/**
	*
	*Restituisce la password cifrata per l'oggetto corrente della classe SignupInfo
	*@return la password cifrata come stringa
	*/
	public String getEncryptedPassword() {
		return EncryptionUtility.encrpyt(password);
	}

	/**
	*
	*Imposta la password per l'oggetto corrente della classe SignupInfo
	*@param password stringa che rappresenta la password dell'utente
	*/
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	*
	*Restituisce la conferma password impostata per l'oggetto corrente della classe SignupInfo
	*@return la conferma password come stringa
	*/
	public String getConfermaPassword() {
		return confermaPassword;
	}

	/**
	*
	*Imposta la conferma password per l'oggetto corrente della classe SignupInfo
	*@param confermaPassword stringa che rappresenta la conferma password dell'utente
	*/
	public void setConfermaPassword(String confermaPassword) {
		this.confermaPassword = confermaPassword;
	}

	/**
	*
	*Restituisce la rappresentazione testuale dell'oggetto corrente della classe SignupInfo
	*@return la rappresentazione testuale dell'oggetto come stringa
	*/
	@Override
	public String toString() {
		return "SignupInfo [nome=" + nome + ", cognome=" + cognome + ", email=" + email + ", password=" + password
				+ ", confermaPassword=" + confermaPassword + "]";
	}
	
}
