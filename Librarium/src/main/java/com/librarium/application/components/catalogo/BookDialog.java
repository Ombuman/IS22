package com.librarium.application.components.catalogo;

import com.librarium.application.components.BetterDialog;
import com.librarium.database.generated.org.jooq.tables.records.LibriRecord;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class BookDialog extends BetterDialog {
	
	public BookDialog(LibriRecord datiLibro) {
		super();
		setHeaderTitle(datiLibro.getTitolo());
		
		VerticalLayout infoLibro = createInfoLibro(datiLibro);
		add(infoLibro);
		
		Button prenotaButton = new Button("Prenota");
		prenotaButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		getFooter().add(prenotaButton);
	}
	
	private VerticalLayout createInfoLibro(LibriRecord datiLibro) {
		VerticalLayout layout = new VerticalLayout();
		layout.addClassName("info-libro");
		
		Image cover = new Image();
		cover.setSrc(datiLibro.getCopertina());
		
		Paragraph infoGenerali = new Paragraph();
		infoGenerali.add(new Div(new Text(datiLibro.getAutore() + ", " + datiLibro.getAnno())));
		infoGenerali.add(new Div(new Text("Editore: " + datiLibro.getCasaEditrice())));
		infoGenerali.addClassName(LumoUtility.TextColor.SECONDARY);
		infoGenerali.addClassName(LumoUtility.Margin.NONE);
		
		Paragraph descrizione = new Paragraph(datiLibro.getDescrizione());
		
		layout.add(cover, infoGenerali, descrizione);
		
		return layout;
	}
	
}
