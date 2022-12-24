package com.librarium.application.components.catalogo;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.librarium.application.backend.DatabaseHelper;
import com.librarium.database.generated.org.jooq.tables.records.CategorieRecord;
import com.librarium.database.generated.org.jooq.tables.records.LibriCompletiRecord;
import com.librarium.database.generated.org.jooq.tables.records.LibriRecord;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class Catalogo extends VerticalLayout {
	
	CheckboxGroup<CategorieRecord> filtroCategoria;
	Button annullaFiltri;
	VerticalLayout listaCategorie;
	TextField filtroNome;
	
	public Catalogo() {
		this("");
	}
	
	public Catalogo(String title) {
		addClassName("catalogo");
		addClassName(LumoUtility.Padding.SMALL);
		
		if(title != null && !title.isBlank())
			add(new H1(title));
		
		VerticalLayout filtri = creaFiltri();
		filtri.addClassName("filtri");
		listaCategorie = new VerticalLayout();
		listaCategorie.addClassName(LumoUtility.Padding.NONE);
		
		filtraLibri();
		
		add(filtri, listaCategorie);
	}
	
	private VerticalLayout creaFiltri() {
		VerticalLayout filtri = new VerticalLayout();
		filtri.addClassName(LumoUtility.Padding.SMALL);
		
		HorizontalLayout layoutFiltroNome = creaLayoutFiltroNome();
		
		VerticalLayout layoutFiltroCategoria = creaLayoutFiltroCategoria();
		
		filtri.add(layoutFiltroNome, layoutFiltroCategoria);
		filtri.setSizeFull();
		
		return filtri;
	}
	
	private HorizontalLayout creaLayoutFiltroNome() {
		HorizontalLayout nameFilterLayout = new HorizontalLayout();
		nameFilterLayout.addClassName("responsive");
		nameFilterLayout.setAlignItems(Alignment.END);
		nameFilterLayout.addClassName(LumoUtility.Padding.SMALL);
		
		filtroNome = new TextField();
		filtroNome.addClassName(LumoUtility.Padding.NONE);
		filtroNome.setWidth("min(300px, 80vw)");
		filtroNome.setLabel("Ricerca per nome");
		filtroNome.setPlaceholder("Cerca...");
		filtroNome.setValueChangeMode(ValueChangeMode.LAZY);
		filtroNome.addValueChangeListener(e -> {
			annullaFiltri.setVisible(!filtroNome.getValue().isBlank());
			filtraLibri();
		});
		
		annullaFiltri = new Button(new Icon(VaadinIcon.CLOSE_CIRCLE));
		annullaFiltri.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		annullaFiltri.setVisible(false);
		annullaFiltri.addClickListener(e -> {
			filtroNome.setValue("");
		});
		
		Button toggleFiltroCategoria = new Button(new Icon(VaadinIcon.FILTER));
		toggleFiltroCategoria.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		toggleFiltroCategoria.addClickListener(e -> {
			filtroCategoria.setVisible(!filtroCategoria.isVisible());
		});
		
		nameFilterLayout.add(toggleFiltroCategoria, filtroNome, annullaFiltri);
		return nameFilterLayout;
	}
	
	private VerticalLayout creaLayoutFiltroCategoria() {
		VerticalLayout layoutFiltroCategoria = new VerticalLayout();
		layoutFiltroCategoria.addClassName(LumoUtility.Padding.NONE);
		
		filtroCategoria = new CheckboxGroup<>();
		filtroCategoria.setLabel("Filtro Categoria");
		filtroCategoria.addClassName(LumoUtility.Padding.SMALL);
		
		List<CategorieRecord> categorie = DatabaseHelper.leggiCategorie();
		filtroCategoria.setItems(categorie);
		filtroCategoria.setItemLabelGenerator(categoria -> categoria.getNome());
		filtroCategoria.select(categorie);
		
		filtroCategoria.addValueChangeListener(e -> filtraLibri());
		filtroCategoria.setVisible(false);
		
		layoutFiltroCategoria.add(filtroCategoria);
		
		return layoutFiltroCategoria;
	}
	
	private void filtraLibri() {
		listaCategorie.removeAll();
		
		for(CategorieRecord categoria : filtroCategoria.getValue()) {
			try {
				List<LibriCompletiRecord> libriFiltrati = DatabaseHelper.leggiLibri(filtroNome.getValue() ,categoria);
				if(libriFiltrati.size() > 0) {
					VerticalLayout layoutCategoria = new VerticalLayout();
					
					H1 titolo = new H1(new H1(categoria.getNome()));
					ListaLibri listaLibri = new ListaLibri();
					
					listaLibri.setItems(libriFiltrati);
					
					layoutCategoria.setWidthFull();
					layoutCategoria.add(titolo, listaLibri);
					
					listaCategorie.add(layoutCategoria);
				}
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		if(listaCategorie.getComponentCount() == 0)
			listaCategorie.add(new Text("Nessun Risultato"));
	}
}
