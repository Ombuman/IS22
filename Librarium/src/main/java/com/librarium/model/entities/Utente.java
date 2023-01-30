package com.librarium.model.entities;

import com.librarium.database.generated.org.jooq.tables.records.UtentiRecord;
import com.librarium.model.enums.RuoloAccount;
import com.librarium.model.enums.StatoAccountUtente;

/**

La classe Utente rappresenta un utente del sistema.

*/
public class Utente {
	
	private UtentiRecord datiUtente;
	private Integer numeroPrestiti;
	private Integer numeroSolleciti;
	
	/**

	Crea un nuovo oggetto Utente con i dati forniti.
	@param datiUtente I dati dell'utente come un oggetto UtentiRecord.
	@param numeroPrestiti Il numero di prestiti effettuati dall'utente.
	@param numeroSolleciti Il numero di solleciti inviati all'utente.
	*/
	public Utente(UtentiRecord datiUtente, Object numeroPrestiti, Object numeroSolleciti) {
		this.datiUtente = datiUtente;
		this.numeroPrestiti = (Integer)numeroPrestiti;
		this.numeroSolleciti = (Integer)numeroSolleciti;
	}
	
	/**

	Restituisce i dati dell'utente come un oggetto UtentiRecord.
	@return I dati dell'utente come un oggetto UtentiRecord.
	*/
	public UtentiRecord getDatiUtente() {
		return datiUtente;
	}
	
	/**

	Restituisce l'ID dell'utente.
	@return L'ID dell'utente.
	*/
	public Integer getId() {
		return datiUtente.getId();
	}
	
	/**

	Restituisce il nome dell'utente.
	@return Il nome dell'utente.
	*/
	public String getNome() {
		return datiUtente.getNome();
	}
	
	/**

	Restituisce il cognome dell'utente.
	@return Il cognome dell'utente.
	*/
	public String getCognome() {
		return datiUtente.getCognome();
	}
	
	/**

	Restituisce l'email dell'utente.
	@return L'email dell'utente.
	*/
	public String getEmail() {
		return datiUtente.getEmail();
	}
	
	/**

	Restituisce la password dell'utente.
	@return La password dell'utente.
	*/
	public String getPassword() {
		return datiUtente.getPassword();
	}
	
	/**
	 * Restituisce lo stato dell'account utente.
	 * @return lo stato dell'account utente
	 */
	public String getStato() {
		return datiUtente.getStato();
	}
	
	/**
	 * Restituisce il ruolo dell'account utente.
	 * @return il ruolo dell'account utente
	 */
	public String getRuolo() {
		return datiUtente.getRuolo();
	}
	
	/**
	 * Restituisce il numero di prestiti effettuati dall'account utente.
	 * @return il numero di prestiti
	 */
	public Integer getNumeroPrestiti() {
		return numeroPrestiti;
	}
	
	/**
	 * Restituisce il numero di solleciti ricevuti dall'account utente.
	 * @return il numero di solleciti
	 */
	public Integer getNumeroSolleciti() {
		return numeroSolleciti;
	}
	
	/**
	 * Imposta un nuovo ID per l'account utente.
	 * @param nuovoId il nuovo ID
	 */
	public void setId(Integer nuovoId) {
		if(nuovoId != null)
			datiUtente.setId(nuovoId);
	}
	
	/**
	 * Imposta un nuovo nome per l'account utente.
	 * @param nome il nuovo nome
	 */
	public void setNome(String nome) {
		datiUtente.setNome(nome);
	}
	
	/**
	 * Imposta un nuovo cognome per l'account utente.
	 * @param cognome il nuovo cognome
	 */
	public void setCognome(String cognome) {
		datiUtente.setCognome(cognome);
	}
	
	/**
	 * Imposta una nuova email per l'account utente.
	 * @param email la nuova email
	 */	
	public void setEmail(String email) {
		datiUtente.setEmail(email);
	}
	
	/**
	 * Imposta una nuova password per l'account utente.
	 * @param password la nuova password
	 */
	public void setPassword(String password) {
		datiUtente.setPassword(password);
	}
	
	/**
	 * Imposta un nuovo stato per l'account utente.
	 * @param stato il nuovo stato
	 */
	public void setStato(String stato) {
		datiUtente.setStato(stato);
	}
	
	/**

	Imposta lo stato dell'account utente.
	@param stato Stato dell'account utente
	*/
	public void setStato(StatoAccountUtente stato) {
		datiUtente.setStato(stato.name());
	}
	/**

	Imposta il ruolo dell'account utente.
	@param ruolo Ruolo dell'account utente
	*/
	public void setRuolo(String ruolo) {
		datiUtente.setRuolo(ruolo);
	}
	
	/**

	Imposta il ruolo dell'account utente.
	@param ruolo Ruolo dell'account utente
	*/
	public void setRuolo(RuoloAccount ruolo) {
		datiUtente.setRuolo(ruolo.name());
	}
	
	/**

	Imposta il numero di prestiti effettuati dall'utente.
	@param numeroPrestiti Numero di prestiti effettuati dall'utente
	*/
	public void setNumeroPrestiti(Integer numeroPrestiti) {
		this.numeroPrestiti = numeroPrestiti;
	}
	
	/**

	Imposta il numero di solleciti effettuati per l'utente.
	@param numeroSolleciti Numero di solleciti effettuati per l'utente
	*/
	public void setNumeroSolleciti(Integer numeroSolleciti) {
		this.numeroSolleciti = numeroSolleciti;
	}
	
}
