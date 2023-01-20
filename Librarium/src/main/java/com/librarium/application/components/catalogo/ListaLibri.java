package com.librarium.application.components.catalogo;

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

public class ListaLibri extends FlexLayout{

	private static final long serialVersionUID = 5166941203668841334L;

	public ListaLibri() {
		setFlexWrap(FlexWrap.WRAP);
		//setJustifyContentMode(JustifyContentMode.START);
		addClassNames("lista-libri");
		setSizeFull();
	}
	
	public ListaLibri(List<Libro> listaLibri) {
		this();
		
		setItems(listaLibri);
	}
	
	public void setItems(List<Libro> listaLibri) {
		// rimuovi i libri vecchi
		removeAll();
		
		for(Libro datiLibro : listaLibri) {
			
			BookDialog dialog = new BookDialog(datiLibro);
			VerticalLayout infoLibro = creaInfoLibro(datiLibro.getLibro(), dialog);
			
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
