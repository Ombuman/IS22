package com.librarium.application.components.catalogo;

import java.util.List;

import com.librarium.database.CatalogManager;
import com.librarium.model.entities.Libro;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class Catalogo extends VerticalLayout {

	private static final long serialVersionUID = -1982388913019511687L;
	
	private static FiltriLibri filtri;
	private static VerticalLayout listaLibri;
	
	public Catalogo() {
		addClassNames("catalogo", LumoUtility.Padding.SMALL);
		
		filtri = new FiltriLibri();
		filtri.setFiltroTestoListener(e -> aggiornaListaLibri());
		filtri.setFiltroGenereListener(e -> aggiornaListaLibri());
		filtri.setFiltroCasaEditriceListener(e -> aggiornaListaLibri());
		
		listaLibri = new VerticalLayout();
		listaLibri.addClassNames(LumoUtility.Padding.NONE);
		
		aggiornaListaLibri();
		
		add(filtri, listaLibri);
	}
	
	public static void aggiornaListaLibri() {
		listaLibri.removeAll();
		
		String testo = filtri.getFiltroTestoValue();
		String idGenere = filtri.getFiltroGenereId();
		String casaEditrice = filtri.getFiltroCasaEditriceValue();
		
		try {
			List<Libro> libriFiltrati = CatalogManager.leggiLibri(testo, idGenere, casaEditrice);
			
			if(libriFiltrati.size() > 0) {
				VerticalLayout layoutCategoria = new VerticalLayout();
				layoutCategoria.addClassName(LumoUtility.Padding.SMALL);
				
				H1 titolo = new H1(filtri.getFiltroGenereValue() == null ? "Tutti i libri" : filtri.getFiltroGenereValue().getNome());
				ListaLibri lista = new ListaLibri();
				
				lista.setItems(libriFiltrati);
				
				layoutCategoria.setWidthFull();
				layoutCategoria.add(titolo, lista);
				
				listaLibri.add(layoutCategoria);
			}
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		if(listaLibri.getComponentCount() == 0)
			listaLibri.add(new Text("Nessun Risultato"));
	}
}
