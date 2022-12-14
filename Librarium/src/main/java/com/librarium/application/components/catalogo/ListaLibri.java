package com.librarium.application.components.catalogo;

import java.util.List;

import com.librarium.database.CatalogManager;
import com.librarium.database.enums.StatoLibro;
import com.librarium.database.generated.org.jooq.tables.records.GeneriRecord;
import com.librarium.database.generated.org.jooq.tables.records.LibriRecord;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class ListaLibri extends FlexLayout{
	
	//private List<LibriRecord> listaLibri;
	
	public ListaLibri() {
		setFlexWrap(FlexWrap.WRAP);
		//setJustifyContentMode(JustifyContentMode.START);
		addClassNames("lista-libri");
		setSizeFull();
	}
	
	public ListaLibri(List<LibriRecord> listaLibri) {
		this();
		
		setItems(listaLibri);
	}
	
	public void setItems(List<LibriRecord> listaLibri) {
		setItems(listaLibri, CatalogManager.leggiGeneri());
	}
	
	public void setItems(List<LibriRecord> listaLibri, List<GeneriRecord> categorie) {
		// rimuovi i libri vecchi
		removeAll();
		
		for(LibriRecord datiLibro : listaLibri) {
			
			BookDialog dialog = new BookDialog(datiLibro);
			VerticalLayout infoLibro = creaInfoLibro(datiLibro, dialog);
			
			add(dialog, infoLibro);
		}
	}
	
	private VerticalLayout creaInfoLibro(LibriRecord datiLibro, BookDialog dialog) {		
		VerticalLayout infoLibro = new VerticalLayout();
		infoLibro.addClassNames("miniatura-libro");
		
		Image copertina = new Image();
		copertina.setSrc(datiLibro.getCopertina());
		
		Paragraph datiAutore = new Paragraph();
		datiAutore.add(new H5(datiLibro.getTitolo()), new Text(datiLibro.getAutore()));
		
		Span statoLibro = new Span("");
		statoLibro.addClassName(LumoUtility.FontSize.XSMALL);
		
		switch(StatoLibro.valueOf(datiLibro.getStato())) {
		case DISPONIBILE:
			statoLibro.add(new Span("Disponibile"));
			statoLibro.getElement().getThemeList().add("badge primary success");
			break;
		case NON_DISPONIBILE:
			statoLibro.add(new Span("Non disponibile"));
			statoLibro.getElement().getThemeList().add("badge primary error");
			break;			
		}
		
		infoLibro.add(copertina, statoLibro, datiAutore);
		infoLibro.addClickListener(click -> dialog.open());
		
		return infoLibro;
	}
	
}
