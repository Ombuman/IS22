package com.librarium.model.entities;

import com.librarium.database.generated.org.jooq.tables.records.UtentiRecord;
import com.librarium.model.enums.RuoloAccount;
import com.librarium.model.enums.StatoAccountUtente;

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
	
	public String getPassword() {
		return datiUtente.getPassword();
	}
	
	public String getStato() {
		return datiUtente.getStato();
	}
	
	public String getRuolo() {
		return datiUtente.getRuolo();
	}
	
	public Integer getNumeroPrestiti() {
		return numeroPrestiti;
	}
	
	public Integer getNumeroSolleciti() {
		return numeroSolleciti;
	}
	
	public void setId(Integer nuovoId) {
		if(nuovoId != null)
			datiUtente.setId(nuovoId);
	}
	
	public void setNome(String nome) {
		datiUtente.setNome(nome);
	}
	
	public void setCognome(String cognome) {
		datiUtente.setCognome(cognome);
	}
	
	public void setEmail(String email) {
		datiUtente.setEmail(email);
	}
	
	public void setPassword(String password) {
		datiUtente.setPassword(password);
	}
	
	public void setStato(String stato) {
		datiUtente.setStato(stato);
	}
	
	public void setStato(StatoAccountUtente stato) {
		datiUtente.setStato(stato.name());
	}
	
	public void setRuolo(String ruolo) {
		datiUtente.setRuolo(ruolo);
	}
	
	public void setRuolo(RuoloAccount ruolo) {
		datiUtente.setRuolo(ruolo.name());
	}
	
	public void setNumeroPrestiti(Integer numeroPrestiti) {
		this.numeroPrestiti = numeroPrestiti;
	}
	
	public void setNumeroSolleciti(Integer numeroSolleciti) {
		this.numeroSolleciti = numeroSolleciti;
	}
	
}
