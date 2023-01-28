package com.librarium.view;

import com.librarium.controller.session.SessionManager;
import com.librarium.model.enums.RuoloAccount;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

public abstract class PrivatePage extends VerticalLayout implements BeforeEnterObserver {

	private static final long serialVersionUID = 2832631662086495111L;
	
	private final String defaultRoute = "/";
	
	// La classe che estende questa classe deve chiamare uno di questi metodi
	public void beforeEnter(BeforeEnterEvent event, RuoloAccount permissionRole) {
		if(!SessionManager.isLogged() || getRuoloAsRuoloAccount(SessionManager.getDatiUtente().getRuolo()) != permissionRole) {
			event.forwardTo(defaultRoute);
		}
	}
	
	public void beforeEnter(BeforeEnterEvent event, String route, RuoloAccount permissionRole) {
		if(!SessionManager.isLogged() || getRuoloAsRuoloAccount(SessionManager.getDatiUtente().getRuolo()) != permissionRole) {
			event.forwardTo(route);
		}
	}
	
	public RuoloAccount getRuoloAsRuoloAccount(String ruolo) {
		return RuoloAccount.valueOf(ruolo);
	}
}