package com.librarium.controller.components.catalogo;

import java.util.List;

import com.librarium.database.CatalogManager;
import com.librarium.database.generated.org.jooq.tables.records.GeneriRecord;
import com.vaadin.flow.component.HasValue.ValueChangeListener;
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**

Classe che rappresenta i filtri per la ricerca di libri.

@extends VerticalLayout
*/
public class FiltriLibri extends VerticalLayout{
	
	private static final long serialVersionUID = -2744684770420629871L;
	
	private HorizontalLayout layoutFiltriNascosti;
	private TextField filtroTesto;
	private Select<GeneriRecord> filtroGenere;
	private Select<String> filtroCasaEditrice;
	
	public FiltriLibri() {
		addClassNames("filtri", LumoUtility.Padding.NONE);
		
		inizializzaFiltroGenere();
		inizializzaFiltroCasaEditrice();
		
		layoutFiltriNascosti = new HorizontalLayout();
		layoutFiltriNascosti.addClassNames(LumoUtility.Padding.SMALL, LumoUtility.FlexWrap.WRAP, LumoUtility.Gap.SMALL);
		layoutFiltriNascosti.add(filtroGenere, filtroCasaEditrice);
		layoutFiltriNascosti.setVisible(false);
		layoutFiltriNascosti.setSizeUndefined();
		
		add(creaLayoutFiltroNome(), layoutFiltriNascosti);
		setSizeFull();
	}
	
	/**

	Crea il layout per il filtro nome.

	@return il layout creato
	*/
	private HorizontalLayout creaLayoutFiltroNome() {
		HorizontalLayout nameFilterLayout = new HorizontalLayout();
		nameFilterLayout.addClassName(LumoUtility.Padding.NONE);
		nameFilterLayout.setAlignItems(Alignment.END);
		
		filtroTesto = new TextField();
		filtroTesto.addClassName(LumoUtility.Padding.SMALL);
		filtroTesto.setWidth("min(300px, 90vw)");
		filtroTesto.setLabel("Ricerca per nome");
		filtroTesto.setPlaceholder("Cerca...");
		filtroTesto.getElement().setAttribute("aria-label", "search");
		filtroTesto.setPrefixComponent(VaadinIcon.SEARCH.create());
		filtroTesto.setClearButtonVisible(true);
		
		filtroTesto.setValueChangeMode(ValueChangeMode.LAZY);
		
		
		Button toggleFiltriNascosti = new Button(new Icon(VaadinIcon.FILTER));
		toggleFiltriNascosti.addClassName(LumoUtility.Padding.NONE);
		toggleFiltriNascosti.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		toggleFiltriNascosti.addClickListener(e -> {
			layoutFiltriNascosti.setVisible(!layoutFiltriNascosti.isVisible());
		});
		
		nameFilterLayout.add(filtroTesto, toggleFiltriNascosti);
		return nameFilterLayout;
	}
	
	/**

	Inizializza il filtro genere come un oggetto Select.
	Aggiunge la classe "NONE" del package LumoUtility.Padding.
	Imposta la larghezza minima a 300px o 90vw.
	Assegna la lista dei generi letta da CatalogManager.
	Imposta la label per ciascun elemento genere come il nome del genere.
	Consenti la selezione vuota con caption "- Qualsiasi -".
	*/
	private void inizializzaFiltroGenere() {
		filtroGenere = new Select<>();
		filtroGenere.setLabel("Filtro Genere");
		filtroGenere.addClassName(LumoUtility.Padding.NONE);
		filtroGenere.setWidth("min(300px, 90vw)");
		
		List<GeneriRecord> generi = CatalogManager.getInstance().leggiGeneri();
		filtroGenere.setItems(generi);
		filtroGenere.setItemLabelGenerator(genere -> {
			return genere != null ? genere.getNome() : new String("");
		});
		filtroGenere.setEmptySelectionAllowed(true);
		filtroGenere.setEmptySelectionCaption("- Qualsiasi -");
	}
	
	/**

	Inizializza il filtro casa editrice come un oggetto Select.
	Aggiunge la classe "NONE" del package LumoUtility.Padding.
	Imposta la larghezza minima a 300px o 90vw.
	Assegna la lista delle case editrici lette da CatalogManager.
	Imposta la label per ciascun elemento casa editrice come il nome della casa editrice.
	Consenti la selezione vuota con caption "- Qualsiasi -".
	*/
	private void inizializzaFiltroCasaEditrice() {
		filtroCasaEditrice = new Select<>();
		filtroCasaEditrice.addClassName(LumoUtility.Padding.NONE);
		filtroCasaEditrice.setWidth("min(300px, 90vw)");
		
		filtroCasaEditrice.setLabel("Filtro Casa Editrice");
		filtroCasaEditrice.setItems(CatalogManager.getInstance().leggiCaseEditrici());
		filtroCasaEditrice.setItemLabelGenerator(casaEditrice -> {
			return casaEditrice != null ? casaEditrice: new String("");
		});
		filtroCasaEditrice.setEmptySelectionAllowed(true);
		filtroCasaEditrice.setEmptySelectionCaption("- Qualsiasi -");
	}
	
	/**

	Imposta un listener per il filtro di testo.
	@param listener il listener da impostare
	*/
	public void setFiltroTestoListener(ValueChangeListener<? super ComponentValueChangeEvent<TextField, String>> listener) {
		filtroTesto.addValueChangeListener(listener);
	}
	
	/**

	Imposta un listener per il filtro di genere.
	@param listener il listener da impostare
	*/
	public void setFiltroGenereListener(ValueChangeListener<? super ComponentValueChangeEvent<Select<GeneriRecord>, GeneriRecord>> listener) {
		filtroGenere.addValueChangeListener(listener);
	}
	
	/**

	Imposta un listener per il filtro di casa editrice.
	@param listener il listener da impostare
	*/
	public void setFiltroCasaEditriceListener(ValueChangeListener<? super ComponentValueChangeEvent<Select<String>, String>> listener) {
		filtroCasaEditrice.addValueChangeListener(listener);
	}
	
	/**

	Restituisce il valore del filtro di testo.
	@return il valore del filtro di testo
	*/
	public String getFiltroTestoValue() {
		return filtroTesto.getValue() == null ? null : filtroTesto.getValue();
	}
	
	/**

	Restituisce il valore del filtro di genere.
	@return il valore del filtro di genere
	*/
	public GeneriRecord getFiltroGenereValue() {
		return filtroGenere.getValue();
	}
	
	/**

	Restituisce l'ID del filtro di genere.
	@return l'ID del filtro di genere
	*/
	public String getFiltroGenereId() {
		return getFiltroGenereValue() == null ? null : getFiltroGenereValue().getId().toString();
	}
	
	/**

	Restituisce il valore del filtro di casa editrice.
	@return il valore del filtro di casa editrice
	*/
	public String getFiltroCasaEditriceValue() {
		return filtroCasaEditrice.getValue() == null ? null : filtroCasaEditrice.getValue();
	}
	
}
