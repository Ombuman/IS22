package com.librarium.controller.navigate;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;

/**
 * Implementa dei metodi statici per gestire la navigazione tra le pagine web
 */
public class Navigation {
	
	/**
	 * @param route l'indirizzo della pagina alla quale navigare
	 */
	public static void navigateTo(String route) {
		UI.getCurrent().navigate(route);
	}
	
	/**
	 * @param component è il componente dal quale ricavare l'istanza della UI per poter
	 * 			navigare in un'altra pagina
	 * @param route l'indirizzo della pagina alla quale andare
	 */
	public static void navigateTo (Component component, String route) {
		component.getUI().ifPresent(ui -> ui.navigate(route));
	}
	
}
