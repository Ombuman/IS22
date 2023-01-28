package com.librarium.model.entities;

import com.librarium.database.generated.org.jooq.tables.records.UtentiRecord;

public class Utente {
	
	private UtentiRecord datiUtente;
	private Integer numeroPrestiti;
	private Integer numeroSolleciti;
	
	public Utente(UtentiRecord datiUtente, Object numeroPrestiti, Object numeroSolleciti) {
		this.datiUtente = datiUtente;
		this.numeroPrestiti = (Integer)numeroPrestiti;
		this.numeroSolleciti = (Integer)numeroSolleciti;
	}
	
	public UtentiRecord getDatiUtente() {
		return datiUtente;
	}
	
	public Integer getId() {
		return datiUtente.getId();
	}
	
	public String getNome() {
		return datiUtente.getNome();
	}
	
	public String getCognome() {
		return datiUtente.getCognome();
	}
	
	public String getEmail() {
		return datiUtente.getEmail();
	}
	
	public String getStato() {
		return datiUtente.getStato();
	}
	
	public Integer getNumeroPrestiti() {
		return numeroPrestiti;
	}
	
	public Integer getNumeroSolleciti() {
		return numeroSolleciti;
	}
	
}
