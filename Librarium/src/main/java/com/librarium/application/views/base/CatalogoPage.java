package com.librarium.application.views.base;

import java.util.List;
import java.util.Map;

import com.librarium.application.components.catalogo.Catalogo;
import com.librarium.application.views.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@PageTitle("Catalogo")
@Route(value = "/", layout = MainLayout.class)
public class CatalogoPage extends VerticalLayout{
	public CatalogoPage() {
		
		Catalogo catalogo = new Catalogo();
		addClassName(LumoUtility.Padding.NONE);
		add(catalogo);
	}
}
