package com.librarium.application.components.catalogo;

import com.librarium.database.generated.org.jooq.tables.records.LibriRecord;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class BookDialog extends Dialog {
	
	public BookDialog(LibriRecord libro) {
		setHeaderTitle(libro.getTitolo());
		Button closeButton = new Button(new Icon(VaadinIcon.CLOSE_SMALL), e -> close());
		closeButton.getElement().setAttribute("aria-label", "Close");
		closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		getHeader().add(closeButton);
		
		VerticalLayout infoLibro = createInfoLibro(libro);
		add(infoLibro);
		
		Button prenotaButton = new Button("Prenota");
		prenotaButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		getFooter().add(prenotaButton);
	}
	
	private VerticalLayout createInfoLibro(LibriRecord libro) {
		VerticalLayout layout = new VerticalLayout();
		layout.addClassName("info-libro");
		
		Image cover = new Image();
		cover.setSrc(libro.getCopertina());
		
		Paragraph p = new Paragraph("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum");
		
		layout.add(cover, p);
		
		return layout;
	}
	
}
