package com.librarium.application.components.catalogo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.librarium.application.backend.DatabaseHelper;
import com.librarium.database.generated.org.jooq.tables.records.CategorieRecord;
import com.librarium.database.generated.org.jooq.tables.records.LibriCompletiRecord;
import com.librarium.database.generated.org.jooq.tables.records.LibriRecord;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ListaLibri extends HorizontalLayout{
	
	//private List<LibriRecord> listaLibri;
	
	public ListaLibri() {
		addClassName("lista-libri");
	}
	
	public ListaLibri(List<LibriCompletiRecord> listaLibri) {
		this();
		
		setItems(listaLibri);
	}
	
	public void setItems(List<LibriCompletiRecord> listaLibri) {
		setItems(listaLibri, DatabaseHelper.leggiCategorie());
	}
	
	public void setItems(List<LibriCompletiRecord> listaLibri, List<CategorieRecord> categorie) {
		// rimuovi i libri vecchi)
		removeAll();
		
		for(LibriCompletiRecord libro : listaLibri) {
			BookDialog dialog = new BookDialog(libro);
			
			VerticalLayout infoLibro = new VerticalLayout();
			infoLibro.addClassName("miniatura-libro");
			
			Image copertina = new Image();
			copertina.setSrc(libro.getCopertina());
			
			Paragraph p = new Paragraph();
			p.add(new H5(libro.getTitolo()), new Text(libro.getAutore().getNome()));
			
			infoLibro.add(copertina, p);
			infoLibro.addClickListener(click -> dialog.open());
			
			add(dialog, infoLibro);
		}
	}
	
}
