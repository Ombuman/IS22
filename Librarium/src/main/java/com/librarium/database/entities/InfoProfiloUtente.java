package com.librarium.database.entities;

import com.librarium.database.generated.org.jooq.tables.records.UtentiRecord;

public class InfoProfiloUtente implements Comparable<UtentiRecord>{
	
	private String email;
	private String nome;
	private String cognome;
	
	public InfoProfiloUtente() {}
	
	public InfoProfiloUtente(String email, String nome, String cognome) {
		this.email = email;
		this.nome = nome;
		this.cognome = cognome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	@Override
	public String toString() {
		return "InfoUtente [email=" + email + ", nome=" + nome + ", cognome=" + cognome + "]";
	}

	@Override
	public int compareTo(UtentiRecord o) {
		if(o.getNome().equals(nome) && o.getCognome().equals(cognome))
			return 1;
		return 0;
	}	
}
