package com.librarium.application.components.cards;

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
	
	private HorizontalLayout creaHeaderPrestito() {
		Span titolo = new Span(prestito.getLibro().getTitolo());
		titolo.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.FontWeight.BLACK);
		
		HorizontalLayout header = new HorizontalLayout(creaBadgeStatoPrestito(), titolo);
		header.setAlignItems(Alignment.CENTER);
		header.addClassNames(LumoUtility.FlexWrap.WRAP);
		
		return header;
	}
	
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

	private Icon createIcon(VaadinIcon vaadinIcon) {
		Icon icon = new Icon(vaadinIcon);
	    icon.getStyle().set("padding", "var(--lumo-space-xs");
	    return icon;
	}
	
	private StatoPrestito getStatoPrestito() {
		return StatoPrestito.valueOf(prestito.getDati().getStato());
	}
	
}
