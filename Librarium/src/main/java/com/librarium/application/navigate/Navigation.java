package com.librarium.application.navigate;

import com.vaadin.flow.component.Component;

public class Navigation {
	
	public static void navigateTo (Component component, String route) {
		component.getUI().ifPresent(ui -> ui.navigate(route));
	}
	
}
