package com.librarium.application.views.user;

import com.librarium.application.components.catalogo.Catalogo;
import com.librarium.application.views.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@PageTitle("Catalogo")
@Route(value = "/", layout = MainLayout.class)
public class CatalogoPage extends VerticalLayout{

	private static final long serialVersionUID = 8967061080385115981L;

	public CatalogoPage() {
		Catalogo catalogo = new Catalogo();
		addClassName(LumoUtility.Padding.NONE);
		add(catalogo);
	}
}
