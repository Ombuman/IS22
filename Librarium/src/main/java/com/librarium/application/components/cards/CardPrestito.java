package com.librarium.application.components.cards;

import com.librarium.application.views.user.PrestitiUtentePage;
import com.librarium.database.PrestitiManager;
import com.librarium.database.entities.Prestito;
import com.librarium.database.enums.StatoPrestito;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class CardPrestito extends HorizontalLayout{
	
	private Prestito prestito;
	public PrestitiUtentePage paginaPrestitiUtente;
	
	public CardPrestito(Prestito prestito, PrestitiUtentePage paginaPrestitiUtente){
		this.prestito = prestito;
		this.paginaPrestitiUtente = paginaPrestitiUtente;

		addClassName("card-prestito");
		
		Details details = new Details(creaHeaderPrestito(), creaInfoPrestito());
		details.setSizeFull();
		
		add(details);
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
		annullaPrenotazione.addClickListener(e -> rimuoviPrenotazione());
		
		String dataInizio = prestito.getDati().getDataInizio();
		String dataFine = prestito.getDati().getDataFine();
		
		VerticalLayout infoPrestito = new VerticalLayout(
			new Hr(),
			new Span("Data prenotazione: " + prestito.getDati().getDataPrenotazione()),
			dataInizio != null ? new Span("Data inizio:" + dataInizio) : new Text(""),
			dataFine != null ? new Span("Data fine: " + dataFine) : new Text(""),
			getStatoPrestito() == StatoPrestito.PRENOTATO ? annullaPrenotazione : null
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
	
	private void rimuoviPrenotazione() {
		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setHeader("Annullamento prenotazione");
		dialog.setText("Vuoi davvero cancellare la prenotazione per il libro \"" + prestito.getLibro().getTitolo() + "\"?");
		dialog.setRejectable(true);
		
		Button confirmButton = new Button("Sì, eliminala");
		confirmButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		
		Button rejectButton = new Button("No, mantienila");
		rejectButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		
		dialog.setConfirmButton(confirmButton);
		dialog.setRejectButton(rejectButton);
		
		dialog.addConfirmListener(e -> {
			try {
				PrestitiManager.annullaPrenotazione(prestito);
				paginaPrestitiUtente.aggiornaLista();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		dialog.open();
	}
	
}
