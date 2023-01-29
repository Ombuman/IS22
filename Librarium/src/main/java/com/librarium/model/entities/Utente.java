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
		if(nome != null && !nome.isBlank())
			datiUtente.setNome(nome);
	}
	
	public void setCognome(String cognome) {
		if(cognome != null && !cognome.isBlank())
			datiUtente.setCognome(cognome);
	}
	
	public void setEmail(String email) {
		if(email != null && !email.isBlank())
			datiUtente.setEmail(email);
	}
	
	public void setPassword(String password) {
		if(password != null && !password.isBlank())
			datiUtente.setPassword(password);
	}
	
	public void setStato(String stato) {
		if(stato != null && !stato.isBlank())
			datiUtente.setStato(stato);
	}
	
	public void setStato(StatoAccountUtente stato) {
		if(stato != null)
			datiUtente.setStato(stato.name());
	}
	
	public void setRuolo(String ruolo) {
		if(ruolo != null && !ruolo.isBlank())
			datiUtente.setRuolo(ruolo);
	}
	
	public void setRuolo(RuoloAccount ruolo) {
		if(ruolo != null)
			datiUtente.setRuolo(ruolo.name());
	}
	
	public void setNumeroPrestiti(Integer numeroPrestiti) {
		if(numeroPrestiti != null)
			this.numeroPrestiti = numeroPrestiti;
	}
	
	public void setNumeroSolleciti(Integer numeroSolleciti) {
		if(numeroSolleciti != null)
			this.numeroSolleciti = numeroSolleciti;
	}
	
}
