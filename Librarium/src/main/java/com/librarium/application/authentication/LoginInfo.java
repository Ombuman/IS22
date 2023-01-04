package com.librarium.application.authentication;

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

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "LoginInfo [email=" + email + ", password=" + password + "]";
	}
	
}
