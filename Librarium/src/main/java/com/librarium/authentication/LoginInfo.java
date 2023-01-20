package com.librarium.authentication;

import com.librarium.application.utility.EncryptionUtility;

public class LoginInfo {
	
	private String email;
	private String password;
	
	public LoginInfo() {
		this("", "");
	}
	
	public LoginInfo(String email, String password) {
		this.email = email;
		this.password = password;
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

	@Override
	public String toString() {
		return "LoginInfo [email=" + email + ", password=" + password + "]";
	}
	
}
