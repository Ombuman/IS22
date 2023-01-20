package com.librarium.application.components.nav;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Anchor;

public class NavLink extends Anchor{
	
	private static final long serialVersionUID = 4685018844552392119L;

	public NavLink(String link, String text) {
		this(link, new Text(text));
	}
	
	public NavLink(String link, Component text) {
		super(link, text);
		
		addClassName("nav-link");
	}
}
