package com.librarium.controller.components.catalogo;

import com.librarium.controller.components.BetterDialog;
import com.librarium.controller.session.SessionManager;
import com.librarium.database.PrestitiManager;
import com.librarium.database.generated.org.jooq.tables.records.LibriRecord;
import com.librarium.model.entities.Libro;
import com.librarium.model.enums.StatoAccountUtente;
import com.librarium.model.enums.StatoLibro;
import com.librarium.view.authentication.LoginDialog;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class BookDialog extends BetterDialog {
	
	private static final long serialVersionUID = -7939567460963187013L;

	public BookDialog(Libro datiLibro) {
		super();
		setHeaderTitle(datiLibro.getLibro().getTitolo());
		
		VerticalLayout infoLibro = createInfoLibro(datiLibro.getLibro());
		add(infoLibro);
		
		Button prenotaButton = new Button("");
		switch(StatoLibro.valueOf(datiLibro.getLibro().getStato())) {
			case DISPONIBILE:
				prenotaButton.setText("Prenota");
				prenotaButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
				prenotaButton.addClickListener(e -> {
					if(SessionManager.isLogged()) {
						// se l'utente è sospeso non può prenotare
						if(SessionManager.getDatiUtente().getStato().equals(StatoAccountUtente.SOSPESO.name())) {
							Dialog dialog = new Dialog();
							dialog.setWidth("min(90vw, 500px)");
							dialog.setHeaderTitle("Account Sospeso");
							dialog.add("Non puoi prenotare libri finchè il tuo account è sospeso.\nRestutisci tutti i libri per riattivare il tuo account");
							dialog.open();
						}
						// se il prestito va a buon fine
						else if(PrestitiManager.creaPrestito(SessionManager.getDatiUtente(), datiLibro.getLibro())) {
							Catalogo.aggiornaListaLibri(); // aggiorna la lista dei libri del catalogo
							this.close(); // chiudi la finestra
						}
					} else {
						this.close(); 
						new LoginDialog().open();
					}
				});
					
			break;
			case NON_DISPONIBILE:
				prenotaButton.setText("Non Disponibile");
				prenotaButton.setEnabled(false);
			break;
		}
		
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
