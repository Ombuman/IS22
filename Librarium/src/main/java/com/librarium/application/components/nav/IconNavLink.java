package com.librarium.application.components.nav;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

public class IconNavLink extends NavLink{

	private static final long serialVersionUID = 7568373576268215050L;

	public IconNavLink(String link, VaadinIcon icon, String text) {
		this(link, new Icon(icon), new Text(text));
	}
	
	public IconNavLink(String link, Icon icon, Component text) {
		super(link, new Paragraph(icon, text));
	}
}
