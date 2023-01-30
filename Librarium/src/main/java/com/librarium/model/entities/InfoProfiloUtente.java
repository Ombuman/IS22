package com.librarium.model.entities;

import com.librarium.database.generated.org.jooq.tables.records.UtentiRecord;

/**
 * Classe che rappresenta le informazioni di un utente
 */
public class InfoProfiloUtente implements Comparable<UtentiRecord>{
	
	private String email;
	private String nome;
	private String cognome;
	
	/**
	 * Costruttore di default
	 */
	public InfoProfiloUtente() {}
	
	/**
	 * Costruttore che inizializza le informazioni di un utente
	 * @param email l'indirizzo email dell'utente
	 * @param nome il nome dell'utente
	 * @param cognome il cognome dell'utente
	 */

	public InfoProfiloUtente(String email, String nome, String cognome) {
		this.email = email;
		this.nome = nome;
		this.cognome = cognome;
	}

	/**
	 * Restituisce l'indirizzo email dell'utente
	 * @return l'indirizzo email dell'utente
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Imposta l'indirizzo email dell'utente
	 * @param email l'indirizzo email dell'utente
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Restituisce il nome dell'utente
	 * @return il nome dell'utente
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Imposta il nome dell'utente
	 * @param nome il nome dell'utente
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
    
	/**
	 * Restituisce il cognome dell'utente
	 * @return il cognome dell'utente
	 */
	public String getCognome() {
		return cognome;
	}


	/**
	 * Imposta il cognome dell'utente
	 * @param cognome il cognome dell'utente
	 */
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	/**
	 * Restituisce la stringa rappresentante le informazioni dell'utente
	 * @return la stringa rappresentante le informazioni dell'utente
	 */
	@Override
	public String toString() {
		return "InfoUtente [email=" + email + ", nome=" + nome + ", cognome=" + cognome + "]";
	}

	/**
	 * Confronta questo oggetto con un'altra istanza di UtentiRecord.
	 * @param o l'altra istanza di UtentiRecord
	 * @return 1 se i nomi e cognomi sono uguali, 0 altrimenti
	 */
	@Override
	public int compareTo(UtentiRecord o) {
		if(o.getNome().equals(nome) && o.getCognome().equals(cognome))
			return 1;
		return 0;
	}	
}
