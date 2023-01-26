package com.librarium.controller.components.nav;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Anchor;

/**
 * NavLink è un'estensione della classe Anchor.
 * Utilizza una classe CSS per modificare l'apparenza del link.
 */
public class NavLink extends Anchor{
	
	private static final long serialVersionUID = 4685018844552392119L;

	/**
	 * @param link è il link al quale navigare al click di questo elemento
	 * @param text è il testo da mostrare
	 */
	public NavLink(String link, String text) {
		this(link, new Text(text));
	}
	
	/**
	 * @param link è il link al quale navigare al click di questo elemento
	 * @param text è il componente contente il testo da mostrare
	 */
	public NavLink(String link, Component text) {
		super(link, text);
		
		addClassName("nav-link");
	}
}
