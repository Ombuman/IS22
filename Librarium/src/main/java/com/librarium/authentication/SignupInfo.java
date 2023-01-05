package com.librarium.authentication;

import com.librarium.database.security.EncryptionUtility;

public class SignupInfo {
	
	private String nome;
	private String cognome;
	private String email;
	private String password;
	private String confermaPassword;
	
	public SignupInfo() {
		this("", "", "", "");
	}
	
	public SignupInfo(String nome, String cognome, String email, String password) {
		this.nome = nome;
		this.cognome = cognome;
		this.email = email;
		this.password = password;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}
	
	public String getEncryptedPassword() {
		return EncryptionUtility.encrpyt(password);
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getConfermaPassword() {
		return confermaPassword;
	}

	public void setConfermaPassword(String confermaPassword) {
		this.confermaPassword = confermaPassword;
	}

	@Override
	public String toString() {
		return "SignupInfo [nome=" + nome + ", cognome=" + cognome + ", email=" + email + ", password=" + password
				+ ", confermaPassword=" + confermaPassword + "]";
	}
	
}
