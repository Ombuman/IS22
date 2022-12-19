package com.librarium.application.components;

import com.librarium.application.navigate.Navigation;
import com.vaadin.flow.component.button.Button;

public class LinkButton extends Button {
	
	public LinkButton(String value) {
		this(value, null);
	}
	
	public LinkButton(String value, String route) {
		super(value);
		
		if(route != null && !route.isBlank())
			addClickListener(e -> Navigation.navigateTo(this, "login"));
	}
	
	
}
