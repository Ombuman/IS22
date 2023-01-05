package com.librarium.authentication.session;

import com.librarium.application.views.MainLayout;
import com.librarium.database.generated.org.jooq.tables.records.UtentiRecord;
import com.vaadin.flow.server.VaadinSession;

public class SessionManager {
	
	public static boolean isLogged() {
		return VaadinSession.getCurrent().getAttribute("datiUtente") != null;
	}
	
	public static void creaNuovaSessione(UtentiRecord datiUtente) {
		VaadinSession.getCurrent().setAttribute("datiUtente", datiUtente);
		MainLayout.updateAuthButtonsLayout();
	}
	
	public static UtentiRecord getDatiUtente() {
		return (UtentiRecord) VaadinSession.getCurrent().getAttribute("datiUtente");
	}
	
	public static void eliminaSessione() {
		VaadinSession.getCurrent().setAttribute("datiUtente", null);
		MainLayout.updateAuthButtonsLayout();
	}
	
}
