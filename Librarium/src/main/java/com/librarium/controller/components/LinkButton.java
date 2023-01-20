package com.librarium.controller.components;

import com.librarium.controller.navigate.Navigation;
import com.vaadin.flow.component.button.Button;

public class LinkButton extends Button {
	private static final long serialVersionUID = -1311115333239028014L;

	public LinkButton(String value) {
		this(value, null);
	}
	
	public LinkButton(String value, String route) {
		super(value);
		
		if(route != null && !route.isBlank())
			addClickListener(e -> Navigation.navigateTo(this, route));
	}
}
