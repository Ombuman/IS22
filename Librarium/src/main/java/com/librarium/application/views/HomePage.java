package com.librarium.application.views;

import java.util.List;

import org.jooq.Record;

import com.librarium.application.backend.DatabaseHelper;
import com.librarium.application.components.catalogo.ListaLibri;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("/")
public class HomePage extends VerticalLayout {
	public HomePage() {
		H1 titolo = new H1("Catalogo");
		
		ListaLibri libri = new ListaLibri();
		libri.setItems(DatabaseHelper.leggiLibri());
		
		add(titolo, libri);
	}
}
