package com.librarium.view;

import com.librarium.controller.session.SessionManager;
import com.librarium.model.enums.RuoloAccount;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

/**
*
*Classe astratta che rappresenta una pagina privata all'interno di un'applicazione web.
*Estende {@link VerticalLayout} e implementa {@link BeforeEnterObserver}.
*/
public abstract class PrivatePage extends VerticalLayout implements BeforeEnterObserver {

	private static final long serialVersionUID = 2832631662086495111L;
	
	private final String defaultRoute = "/";
	
	// La classe che estende questa classe deve chiamare uno di questi metodi
	public void beforeEnter(BeforeEnterEvent event, RuoloAccount permissionRole) {
		if(!SessionManager.isLogged() || getRuoloAsRuoloAccount(SessionManager.getDatiUtente().getRuolo()) != permissionRole) {
			event.forwardTo(defaultRoute);
		}
	}
	
	/**
	*
	*Metodo che viene chiamato prima che l'utente acceda alla pagina.
	*Verifica che l'utente sia loggato e che abbia il ruolo richiesto per accedere alla pagina.
	*Se le condizioni non sono soddisfatte, l'utente viene reindirizzato al percorso di default.
	*@param event evento che contiene informazioni sulla navigazione dell'utente
	*@param permissionRole ruolo richiesto per accedere alla pagina
	*/
	public void beforeEnter(BeforeEnterEvent event, String route, RuoloAccount permissionRole) {
		if(!SessionManager.isLogged() || getRuoloAsRuoloAccount(SessionManager.getDatiUtente().getRuolo()) != permissionRole) {
			event.forwardTo(route);
		}
	}
	
	/**
	*
	*Converte una stringa in un oggetto di tipo {@link RuoloAccount}.
	*@param ruolo stringa che rappresenta il ruolo
	*@return oggetto di tipo {@link RuoloAccount} corrispondente alla stringa
	*/
	public RuoloAccount getRuoloAsRuoloAccount(String ruolo) {
		return RuoloAccount.valueOf(ruolo);
	}
}
