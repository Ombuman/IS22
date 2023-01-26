package com.librarium.controller.session;

import com.librarium.database.UsersManager;
import com.librarium.database.generated.org.jooq.tables.records.UtentiRecord;
import com.librarium.model.entities.InfoProfiloUtente;
import com.vaadin.flow.server.VaadinSession;

public class SessionManager {
	
	public static boolean isLogged() {
		return VaadinSession.getCurrent().getAttribute("datiUtente") != null;
	}
	
	private static void setDatiUtente(UtentiRecord datiUtente) {
		VaadinSession.getCurrent().setAttribute("datiUtente", datiUtente);
	}
	
	public static void creaNuovaSessione (UtentiRecord datiUtente) {
		setDatiUtente(datiUtente);
	}
	
	public static void aggiornaDatiUtente(InfoProfiloUtente datiUtente) {
		setDatiUtente(UsersManager.getInstance().aggiornaAccountUtente(getDatiUtente().getId(), datiUtente));
	}
	
	public static UtentiRecord getDatiUtente() {
		return (UtentiRecord) VaadinSession.getCurrent().getAttribute("datiUtente");
	}
	
	public static void eliminaSessione() {
		VaadinSession.getCurrent().setAttribute("datiUtente", null);
	}
	
}
