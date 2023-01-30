package com.librarium.controller.components.catalogo;

import java.util.List;

import com.librarium.database.generated.org.jooq.tables.records.LibriRecord;
import com.librarium.model.entities.Libro;
import com.librarium.model.enums.StatoLibro;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**

La classe ListaLibri estende FlexLayout e rappresenta un layout flessibile che contiene i libri.
*/
public class ListaLibri extends FlexLayout{

	private static final long serialVersionUID = 5166941203668841334L;
	/**

	Costruttore senza parametri. Imposta la proprietà FlexWrap su WRAP, 
	aggiunge la classe "lista-libri" e imposta la dimensione a full.
	*/
	public ListaLibri() {
		setFlexWrap(FlexWrap.WRAP);
		//setJustifyContentMode(JustifyContentMode.START);
		addClassNames("lista-libri");
		setSizeFull();
	}
	/**
	Costruttore con parametro. Chiama il costruttore senza parametri e imposta la lista dei libri.
	@param listaLibri La lista dei libri da visualizzare.
	*/
	public ListaLibri(List<Libro> listaLibri) {
		this();
		
		setItems(listaLibri);
	}
	
	/**

	Imposta la lista dei libri. Rimuove i libri vecchi e crea un dialog e un layout per ogni libro nella lista.
	@param listaLibri La lista dei libri da visualizzare.
	*/
	public void setItems(List<Libro> listaLibri) {
		// rimuovi i libri vecchi
		removeAll();
		
		for(Libro datiLibro : listaLibri) {
			
			BookDialog dialog = new BookDialog(datiLibro);
			VerticalLayout infoLibro = creaInfoLibro(datiLibro.getLibro(), dialog);
			
			add(dialog, infoLibro);
		}
	}
	
	/**

	Crea informazioni sul libro, come copertina, stato e dati dell'autore. Assegna anche un listener per aprire un dialog per il libro.
	@param datiLibro Dati del libro.
	@param dialog Dialog del libro.
	@return Layout con informazioni sul libro.
	*/
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
