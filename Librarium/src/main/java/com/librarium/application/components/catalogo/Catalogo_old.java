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
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class Catalogo_old extends VerticalLayout {
	
	private HorizontalLayout layoutFiltriNascosti;
	private CheckboxGroup<GeneriRecord> filtroCategoria;
	private Select<String> filtroCasaEditrice;
	
	private VerticalLayout listaCategorie;
	private TextField filtroNome;
	
	public Catalogo_old() {
		this("");
	}
	
	public Catalogo_old(String title) {
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
		
		layoutFiltriNascosti = new HorizontalLayout();
		layoutFiltriNascosti.addClassName(LumoUtility.Padding.NONE);
		layoutFiltriNascosti.addClassName(LumoUtility.FlexWrap.WRAP);
		
		layoutFiltriNascosti.add(
				creaLayoutFiltroCategoria(),
				creaLayoutFiltroCasaEditrice()
			);
		layoutFiltriNascosti.setVisible(false);
		
		filtri.add(creaLayoutFiltroNome(), layoutFiltriNascosti);
		filtri.setSizeFull();
		
		return filtri;
	}

	private HorizontalLayout creaLayoutFiltroNome() {
		HorizontalLayout nameFilterLayout = new HorizontalLayout();
		nameFilterLayout.addClassName(LumoUtility.Padding.NONE);
		nameFilterLayout.setAlignItems(Alignment.END);
		
		filtroNome = new TextField();
		filtroNome.addClassName(LumoUtility.Padding.XSMALL);
		filtroNome.setWidth("min(300px, 80vw)");
		filtroNome.setLabel("Ricerca per nome");
		filtroNome.setPlaceholder("Cerca...");
		filtroNome.setValueChangeMode(ValueChangeMode.LAZY);
		filtroNome.addValueChangeListener(e -> {
			filtraLibri();
		});
		filtroNome.setClearButtonVisible(true);
		
		Button toggleFiltriNascosti = new Button(new Icon(VaadinIcon.FILTER));
		toggleFiltriNascosti.addClassName(LumoUtility.Padding.NONE);
		toggleFiltriNascosti.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		toggleFiltriNascosti.addClickListener(e -> {
			layoutFiltriNascosti.setVisible(!layoutFiltriNascosti.isVisible());
		});
		
		nameFilterLayout.add(toggleFiltriNascosti, filtroNome);
		return nameFilterLayout;
	}
	
	private Div creaLayoutFiltroCategoria() {
		Div layoutFiltroCategoria = new Div();
		layoutFiltroCategoria.addClassName(LumoUtility.Padding.NONE);
		
		filtroCategoria = new CheckboxGroup<>();
		filtroCategoria.setLabel("Filtro Categoria");
		filtroCategoria.addClassName(LumoUtility.Padding.SMALL);
		
		List<GeneriRecord> categorie = CatalogManager.leggiGeneri();
		filtroCategoria.setItems(categorie);
		filtroCategoria.setItemLabelGenerator(categoria -> categoria.getNome());
		filtroCategoria.select(categorie);
		
		filtroCategoria.addValueChangeListener(e -> filtraLibri());
		
		layoutFiltroCategoria.add(filtroCategoria);
		
		return layoutFiltroCategoria;
	}
	
	private Div creaLayoutFiltroCasaEditrice() {
		Div layoutFiltroCasaEditrice = new Div();
		layoutFiltroCasaEditrice.addClassName(LumoUtility.Padding.NONE);
		
		filtroCasaEditrice = new Select<>();
		filtroCasaEditrice.addClassName(LumoUtility.Padding.SMALL);
		filtroCasaEditrice.setLabel("Filtro Casa Editrice");
		filtroCasaEditrice.setItems(CatalogManager.leggiCaseEditrici());
		filtroCasaEditrice.setItemLabelGenerator(casaEditrice -> {
			if(casaEditrice != null)
				return casaEditrice;
			
			return new String("");
		});
		filtroCasaEditrice.setEmptySelectionAllowed(true);
		filtroCasaEditrice.setEmptySelectionCaption("- Qualsiasi -");
		
		filtroCasaEditrice.addValueChangeListener(e -> {
			filtraLibri();
		});
		
		layoutFiltroCasaEditrice.add(filtroCasaEditrice);
		
		return layoutFiltroCasaEditrice;
	}
	
	private void filtraLibri() {
		listaCategorie.removeAll();
		
		for(GeneriRecord categoria : filtroCategoria.getValue()) {
			try {
				String casaEditrice = filtroCasaEditrice.getValue();
				List<LibriRecord> libriFiltrati = 
					CatalogManager.leggiLibri(
							filtroNome.getValue(),
							categoria,
							(casaEditrice == null || casaEditrice.isBlank()) ? null : casaEditrice
						);
				
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
				System.out.println(e.getMessage());
			}
		}
		
		if(listaCategorie.getComponentCount() == 0)
			listaCategorie.add(new Text("Nessun Risultato"));
	}
}
