package com.librarium.application.components.catalogo;

import java.util.List;

import com.librarium.database.DatabaseHelper;
import com.librarium.database.generated.org.jooq.tables.records.GeneriRecord;
import com.librarium.database.generated.org.jooq.tables.records.LibriRecord;
import com.vaadin.flow.component.Text;
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
	
	public ListaLibri(List<LibriRecord> listaLibri) {
		this();
		
		setItems(listaLibri);
	}
	
	public void setItems(List<LibriRecord> listaLibri) {
		setItems(listaLibri, DatabaseHelper.leggiGeneri());
	}
	
	public void setItems(List<LibriRecord> listaLibri, List<GeneriRecord> categorie) {
		// rimuovi i libri vecchi
		removeAll();
		
		for(LibriRecord datiLibro : listaLibri) {
			BookDialog dialog = new BookDialog(datiLibro);
			
			VerticalLayout infoLibro = new VerticalLayout();
			infoLibro.addClassName("miniatura-libro");
			
			Image copertina = new Image();
			copertina.setSrc(datiLibro.getCopertina());
			
			Paragraph p = new Paragraph();
			p.add(new H5(datiLibro.getTitolo()), new Text(datiLibro.getAutore()));
			
			infoLibro.add(copertina, p);
			infoLibro.addClickListener(click -> dialog.open());
			
			add(dialog, infoLibro);
		}
	}
	
}
