package com.librarium.application.views;

import com.librarium.application.components.catalogo.Catalogo;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@PageTitle("Catalogo")
@Route(value = "/", layout = MainLayout.class)
public class HomePage extends VerticalLayout{
	public HomePage() {
		
		Catalogo catalogo = new Catalogo();
		addClassName(LumoUtility.Padding.NONE);
		add(catalogo);
	}
}
