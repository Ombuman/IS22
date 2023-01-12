package com.librarium.application.components.catalogo;

import java.util.List;

import com.librarium.database.CatalogManager;
import com.librarium.database.generated.org.jooq.tables.records.GeneriRecord;
import com.librarium.database.generated.org.jooq.tables.records.LibriRecord;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class Catalogo extends VerticalLayout {
	
	private HorizontalLayout layoutFiltriNascosti;
	private static Select<GeneriRecord> filtroGenere;
	private static Select<String> filtroCasaEditrice;
	
	private static VerticalLayout listaLibri;
	private static TextField filtroNome;
	
	public Catalogo() {
		this("");
	}
	
	public Catalogo(String title) {
		addClassName("catalogo");
		addClassNames(LumoUtility.Padding.SMALL);
		
		if(title != null && !title.isBlank())
			add(new H1(title));
		
		VerticalLayout filtri = creaFiltri();
		filtri.addClassName("filtri");
		listaLibri = new VerticalLayout();
		listaLibri.addClassNames(LumoUtility.Padding.NONE);
		
		aggiornaListaLibri();
		
		add(filtri, listaLibri);
	}
	
	private VerticalLayout creaFiltri() {
		VerticalLayout filtri = new VerticalLayout();
		filtri.addClassName(LumoUtility.Padding.NONE);
		
		inizializzaFiltroGenere();
		inizializzaFiltroCasaEditrice();
		
		layoutFiltriNascosti = new HorizontalLayout();
		layoutFiltriNascosti.addClassNames(LumoUtility.Padding.SMALL, LumoUtility.FlexWrap.WRAP, LumoUtility.Gap.SMALL);
		layoutFiltriNascosti.add(filtroGenere, filtroCasaEditrice);
		layoutFiltriNascosti.setVisible(false);
		layoutFiltriNascosti.setSizeUndefined();
		
		filtri.add(creaLayoutFiltroNome(), layoutFiltriNascosti);
		filtri.setSizeFull();
		
		return filtri;
	}

	private HorizontalLayout creaLayoutFiltroNome() {
		HorizontalLayout nameFilterLayout = new HorizontalLayout();
		nameFilterLayout.addClassName(LumoUtility.Padding.NONE);
		nameFilterLayout.setAlignItems(Alignment.END);
		
		filtroNome = new TextField();
		filtroNome.addClassName(LumoUtility.Padding.SMALL);
		filtroNome.setWidth("min(300px, 90vw)");
		filtroNome.setLabel("Ricerca per nome");
		filtroNome.setPlaceholder("Cerca...");
		filtroNome.getElement().setAttribute("aria-label", "search");
		filtroNome.setPrefixComponent(VaadinIcon.SEARCH.create());
		filtroNome.setClearButtonVisible(true);
		
		filtroNome.setValueChangeMode(ValueChangeMode.LAZY);
		filtroNome.addValueChangeListener(e -> aggiornaListaLibri());
		
		
		Button toggleFiltriNascosti = new Button(new Icon(VaadinIcon.FILTER));
		toggleFiltriNascosti.addClassName(LumoUtility.Padding.NONE);
		toggleFiltriNascosti.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		toggleFiltriNascosti.addClickListener(e -> {
			layoutFiltriNascosti.setVisible(!layoutFiltriNascosti.isVisible());
		});
		
		nameFilterLayout.add(filtroNome, toggleFiltriNascosti);
		return nameFilterLayout;
	}
	
	private void inizializzaFiltroGenere() {
		filtroGenere = new Select<>();
		filtroGenere.setLabel("Filtro Genere");
		filtroGenere.addClassName(LumoUtility.Padding.NONE);
		filtroGenere.setWidth("min(300px, 90vw)");
		
		List<GeneriRecord> generi = CatalogManager.leggiGeneri();
		filtroGenere.setItems(generi);
		filtroGenere.setItemLabelGenerator(genere -> {
			return genere != null ? genere.getNome() : new String("");
		});
		filtroGenere.setEmptySelectionAllowed(true);
		filtroGenere.setEmptySelectionCaption("- Qualsiasi -");
		
		filtroGenere.addValueChangeListener(e -> aggiornaListaLibri());
	}
	
	private void inizializzaFiltroCasaEditrice() {
		filtroCasaEditrice = new Select<>();
		filtroCasaEditrice.addClassName(LumoUtility.Padding.NONE);
		filtroCasaEditrice.setWidth("min(300px, 90vw)");
		
		filtroCasaEditrice.setLabel("Filtro Casa Editrice");
		filtroCasaEditrice.setItems(CatalogManager.leggiCaseEditrici());
		filtroCasaEditrice.setItemLabelGenerator(casaEditrice -> {
			return casaEditrice != null ? casaEditrice: new String("");
		});
		filtroCasaEditrice.setEmptySelectionAllowed(true);
		filtroCasaEditrice.setEmptySelectionCaption("- Qualsiasi -");
		
		filtroCasaEditrice.addValueChangeListener(e -> {
			aggiornaListaLibri();
		});
	}
	
	public static void aggiornaListaLibri() {
		listaLibri.removeAll();
		
		String testo = filtroNome.getValue() == null ? null : filtroNome.getValue();
		GeneriRecord genere = filtroGenere.getValue();
		String idGenere = genere == null ? null : genere.getId().toString();
		String casaEditrice = filtroCasaEditrice.getValue() == null ? null : filtroCasaEditrice.getValue();
		
		try {
			List<LibriRecord> libriFiltrati = CatalogManager.leggiLibri(testo, idGenere, casaEditrice);
			
			if(libriFiltrati.size() > 0) {
				VerticalLayout layoutCategoria = new VerticalLayout();
				layoutCategoria.addClassName(LumoUtility.Padding.SMALL);
				
				H1 titolo = new H1(genere == null ? "Tutti i libri" : genere.getNome());
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
