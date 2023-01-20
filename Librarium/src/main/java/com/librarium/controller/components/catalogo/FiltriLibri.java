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
	}
	
	public void setFiltroTestoListener(ValueChangeListener<? super ComponentValueChangeEvent<TextField, String>> listener) {
		filtroTesto.addValueChangeListener(listener);
	}
	
	public void setFiltroGenereListener(ValueChangeListener<? super ComponentValueChangeEvent<Select<GeneriRecord>, GeneriRecord>> listener) {
		filtroGenere.addValueChangeListener(listener);
	}
	
	public void setFiltroCasaEditriceListener(ValueChangeListener<? super ComponentValueChangeEvent<Select<String>, String>> listener) {
		filtroCasaEditrice.addValueChangeListener(listener);
	}
	
	public String getFiltroTestoValue() {
		return filtroTesto.getValue() == null ? null : filtroTesto.getValue();
	}
	
	public GeneriRecord getFiltroGenereValue() {
		return filtroGenere.getValue();
	}
	
	public String getFiltroGenereId() {
		return getFiltroGenereValue() == null ? null : getFiltroGenereValue().getId().toString();
	}
	
	public String getFiltroCasaEditriceValue() {
		return filtroCasaEditrice.getValue() == null ? null : filtroCasaEditrice.getValue();
	}
	
}
