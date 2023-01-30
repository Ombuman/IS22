package com.librarium.controller.components.cards;

import com.librarium.model.entities.Prestito;
import com.librarium.model.enums.StatoPrestito;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
 * Classe che rappresenta una scheda per un prestito all'interno di una lista di prestiti.
 * La classe estende HorizontalLayout e presenta un layout orizzontale che mostra i dettagli del prestito.
 * Il layout contiene l'header del prestito che rappresenta lo stato e il titolo del libro,
 * e le informazioni sul prestito che includono la data di prenotazione, la data di inizio, la data di fine
 * e un bottone per annullare la prenotazione (solo se lo stato del prestito è "prenotato").
 */
public class CardPrestito extends HorizontalLayout{

	private static final long serialVersionUID = -930172053538253552L;
	
	private Prestito prestito;
	public ConfirmDialog confirmDialog;
	
	public CardPrestito(Prestito prestito){
		this.prestito = prestito;
		
		addClassName("card-prestito");
		
		inizializzaDialogo();
		
		Details details = new Details(creaHeaderPrestito(), creaInfoPrestito());
		details.setSizeFull();
		
		add(details);
	}
	
	/**
	 * Inizializza un nuovo dialogo di conferma per la cancellazione di una prenotazione.
	 * Il testo del dialogo visualizzerà il titolo del libro prenotato.
	 * Il dialogo presenterà due pulsanti: "Sì, eliminala" e "No, mantienila".
	 */
	private void inizializzaDialogo() {
		confirmDialog = new ConfirmDialog();
		confirmDialog.setHeader("Annullamento prenotazione");
		confirmDialog.setText("Vuoi davvero cancellare la prenotazione per il libro \"" + prestito.getLibro().getTitolo() + "\"?");
		confirmDialog.setRejectable(true);
		
		Button confirmButton = new Button("Sì, eliminala");
		confirmButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		
		Button rejectButton = new Button("No, mantienila");
		rejectButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		
		confirmDialog.setConfirmButton(confirmButton);
		confirmDialog.setRejectButton(rejectButton);
	}
	
	/**
	 * Crea un layout orizzontale che rappresenta l'header per il dettaglio del prestito.
	 * Il layout contiene un badge che rappresenta lo stato del prestito e il titolo del libro.
	 * Il layout è allineato al centro e il titolo del libro ha un font medio e pesante.
	 * @return il layout orizzontale creato
	 */
	private HorizontalLayout creaHeaderPrestito() {
		Span titolo = new Span(prestito.getLibro().getTitolo());
		titolo.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.FontWeight.BLACK);
		
		HorizontalLayout header = new HorizontalLayout(creaBadgeStatoPrestito(), titolo);
		header.setAlignItems(Alignment.CENTER);
		header.addClassNames(LumoUtility.FlexWrap.WRAP);
		
		return header;
	}
	
	/**
	 * Crea un layout verticale che rappresenta le informazioni sul prestito.
	 * Il layout contiene la data di prenotazione, la data di inizio (se esistente), la data di fine (se esistente)
	 * e un bottone per annullare la prenotazione (solo se lo stato del prestito è "prenotato").
	 * 
	 * @return il layout verticale creato
	 */
	private VerticalLayout creaInfoPrestito() {
		Button annullaPrenotazione = new Button("Annulla prenotazione");
		annullaPrenotazione.addThemeVariants(ButtonVariant.LUMO_ERROR);
		annullaPrenotazione.addClickListener(e -> confirmDialog.open());
		
		String dataInizio = prestito.getDati().getDataInizio();
		String dataFine = prestito.getDati().getDataFine();
		
		VerticalLayout infoPrestito = new VerticalLayout(
			new Hr(),
			new Span("Data prenotazione: " + prestito.getDati().getDataPrenotazione()),
			(dataInizio != null && !dataInizio.isBlank()) ? new Span("Data inizio:" + dataInizio) : new Text(""),
			(dataFine != null && !dataFine.isBlank()) ? new Span("Data fine: " + dataFine) : new Text(""),
			getStatoPrestito() == StatoPrestito.PRENOTATO ? annullaPrenotazione : new Text("")
		);
		
		infoPrestito.addClassNames(LumoUtility.TextColor.SECONDARY, LumoUtility.Gap.SMALL, LumoUtility.Padding.SMALL);
		
		return infoPrestito;
	}
	
	/**
	 * Crea un badge di stato per il prestito corrente.
	 * Il badge viene creato in base allo stato del prestito, che può essere: prenotato, ritirato o concluso.
	 * 
	 * @return Il badge di stato del prestito.
	 */
	private Span creaBadgeStatoPrestito() {
		Span stato = new Span();
		
		switch(getStatoPrestito()) {
		case PRENOTATO:
			stato.add(createIcon(VaadinIcon.CLOCK), new Span("Prenotato"));
			stato.getElement().getThemeList().add("badge");
			break;
		case RITIRATO:
			stato.add(createIcon(VaadinIcon.CHECK), new Span("Ritirato"));
			stato.getElement().getThemeList().add("badge primary success");
			break;
		case CONCLUSO:
			stato.add(createIcon(VaadinIcon.CLOSE_CIRCLE_O), new Span("Concluso"));
			stato.getElement().getThemeList().add("badge primary error");
			break;
		}
		
		return stato;
	}

	/**
	 * Crea un icona utilizzando la libreria VaadinIcon.
	 * @param vaadinIcon Il tipo di icona da creare
	 * 
	 * @return L'icona creata
	 */
	private Icon createIcon(VaadinIcon vaadinIcon) {
		Icon icon = new Icon(vaadinIcon);
	    icon.getStyle().set("padding", "var(--lumo-space-xs");
	    return icon;
	}
	
	
	/**
	 * @return StatoPrestito: lo stato del prestito
	 */
	private StatoPrestito getStatoPrestito() {
		return StatoPrestito.valueOf(prestito.getDati().getStato());
	}
	
}
