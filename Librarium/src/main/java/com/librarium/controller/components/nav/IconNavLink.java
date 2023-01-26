package com.librarium.controller.components.nav;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

/**
 * IconaNavLink è un'estensione della classe NavLink.
 * Permette di mostrare un'icona accanto al testo.
 */
public class IconNavLink extends NavLink{

	private static final long serialVersionUID = 7568373576268215050L;

	/**
	 * @param link è il link al quale navigare al click di questo elemento
	 * @param icon è l'icona da mostrare (di tipo VaadinIcon)
	 * @param text è il testo da mostrare
	 */
	public IconNavLink(String link, VaadinIcon icon, String text) {
		this(link, new Icon(icon), new Text(text));
	}
	
	/**
	 * @param link è il link al quale navigare al click di questo elemento
	 * @param icon è l'icona da mostrare (di tipo Icon)
	 * @param text è il componente contente il testo da mostrare
	 */
	public IconNavLink(String link, Icon icon, Component text) {
		super(link, new Paragraph(icon, text));
	}
}
