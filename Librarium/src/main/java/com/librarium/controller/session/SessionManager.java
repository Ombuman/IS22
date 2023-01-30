package com.librarium.controller.session;

import com.librarium.database.UsersManager;
import com.librarium.database.generated.org.jooq.tables.records.UtentiRecord;
import com.librarium.model.entities.InfoProfiloUtente;
import com.vaadin.flow.server.VaadinSession;

/**
 * Questa classe si occupa di gestire la sessione corrente dell'utente.
 */
public class SessionManager {
	
	/**
	 * Verifica se un utente è loggato.
	 * 
	 * @return True se un utente è loggato, False altrimenti.
	 */
	public static boolean isLogged() {
		return VaadinSession.getCurrent().getAttribute("datiUtente") != null;
	}
	
	/**
	 * Imposta i dati dell'utente corrente nella sessione corrente.
	 * 
	 * @param datiUtente I dati dell'utente.
	 */
	private static void setDatiUtente(UtentiRecord datiUtente) {
		VaadinSession.getCurrent().setAttribute("datiUtente", datiUtente);
	}
	
	/**
	 * Crea una nuova sessione per l'utente.
	 * 
	 * @param datiUtente I dati dell'utente.
	 */
	public static void creaNuovaSessione (UtentiRecord datiUtente) {
		setDatiUtente(datiUtente);
	}
	
	/**
	 * Aggiorna i dati dell'utente corrente.
	 * 
	 * @param datiUtente I nuovi dati dell'utente.
	 */
	public static void aggiornaDatiUtente(InfoProfiloUtente datiUtente) {
		setDatiUtente(UsersManager.getInstance().aggiornaAccountUtente(getDatiUtente().getId(), datiUtente));
	}
	
	/**
	 * Restituisce i dati dell'utente corrente.
	 * 
	 * @return I dati dell'utente corrente.
	 */
	public static UtentiRecord getDatiUtente() {
		return (UtentiRecord) VaadinSession.getCurrent().getAttribute("datiUtente");
	}
	
	/**
	 * Elimina la sessione corrente.
	 */
	public static void eliminaSessione() {
		VaadinSession.getCurrent().setAttribute("datiUtente", null);
	}
	
}
