package com.librarium.controller.navigate;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;

public class Navigation {
	
	public static void navigateTo(String route) {
		UI.getCurrent().navigate(route);
	}
	
	public static void navigateTo (Component component, String route) {
		component.getUI().ifPresent(ui -> ui.navigate(route));
	}
	
}
