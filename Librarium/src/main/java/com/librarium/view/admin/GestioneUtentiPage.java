package com.librarium.view.admin;

import com.librarium.controller.components.BetterConfirmDialog;
import com.librarium.controller.components.ListTab;
import com.librarium.database.UsersManager;
import com.librarium.model.entities.Utente;
import com.librarium.model.enums.RuoloAccount;
import com.librarium.model.enums.StatoAccountUtente;
import com.librarium.view.AdminLayout;
import com.librarium.view.PrivatePage;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.MultiSortPriority;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Gestione Utenti")
@Route(value = "/gestione-utenti", layout = AdminLayout.class)
public class GestioneUtentiPage extends PrivatePage{

	private static final long serialVersionUID = 6393738022482601686L;

	private Tabs tabs;
	private ListTab<Utente> tabTutti;
	private ListTab<Utente> tabSospesi;
	
	private Grid<Utente> gridUtenti;
	
	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		super.beforeEnter(event, RuoloAccount.BIBLIOTECARIO);
	}
	
	public GestioneUtentiPage() {
		creaTabs();
		creaGrid();
		
		setSizeFull();
		add(tabs, gridUtenti);
	}
	
	private void creaTabs() {
		tabs = new Tabs();
		tabTutti = new ListTab<Utente>("Tutti", "Tutti");
		tabSospesi = new ListTab<Utente>("Sospesi", "Sospesi");
		tabs.add(tabTutti, tabSospesi);
		tabs.setWidthFull();
		
		tabs.addSelectedChangeListener(e -> cambiaTab());
	}
	
	private void creaGrid() {
		gridUtenti = new Grid<>(Utente.class, false);
		gridUtenti.setMultiSort(true, MultiSortPriority.APPEND);
		
		gridUtenti.addColumn(Utente::getId).setHeader("ID").setResizable(true).setSortable(true).setAutoWidth(true).setFrozen(true);
		gridUtenti.addColumn(Utente::getNome).setHeader("Nome").setResizable(true).setSortable(true).setAutoWidth(true);
		gridUtenti.addColumn(Utente::getCognome).setHeader("Cognome").setResizable(true).setSortable(true).setAutoWidth(true);
		gridUtenti.addColumn(Utente::getEmail).setHeader("Email").setResizable(true).setSortable(true).setAutoWidth(true);
		gridUtenti.addColumn(Utente::getNumeroPrestiti).setHeader("Nr. Prestiti").setResizable(true).setSortable(true).setAutoWidth(true);
		gridUtenti.addColumn(Utente::getNumeroSolleciti).setHeader("Nr. Solleciti").setResizable(true).setSortable(true).setAutoWidth(true);
		creaColonnaStato();
		creaColonnaAzioni();
		
		aggiornaListe();
	}
	
	private void creaColonnaStato() {
		gridUtenti.addComponentColumn(utente -> {
			Span statoUtente = new Span();
			
			switch(StatoAccountUtente.valueOf(utente.getStato())) {
				case ATTIVO:
					statoUtente.setText("ATTIVO");
					statoUtente.getElement().getThemeList().add("badge primary success");
					break;
				case SOSPESO:
					statoUtente.setText("SOSPESO");
					statoUtente.getElement().getThemeList().add("badge primary error");
					break;
			}
			return statoUtente;
		}).setHeader("Stato").setAutoWidth(true);
	}
	
	private void creaColonnaAzioni() {
		gridUtenti.addComponentColumn(utente -> {
			BetterConfirmDialog confirmDialog = new BetterConfirmDialog();
			confirmDialog.setRejectable(true);
			
			Button azioneAccount = new Button();
			azioneAccount.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
			azioneAccount.addClickListener(click -> confirmDialog.open());
			
			switch(StatoAccountUtente.valueOf(utente.getStato())) {
				case ATTIVO:
					azioneAccount.setText("Sospendi");
					//azioneAccount.addThemeVariants(ButtonVariant.LUMO_ERROR);
					confirmDialog.setHeader("Sospensione Account");
					confirmDialog.add("Vuoi davvero sospendere l'account di " + utente.getNome() + " " + utente.getCognome() + "?");
					confirmDialog.addConfirmListener(confirm -> {
						UsersManager.setStatoAccount(utente.getId(), StatoAccountUtente.SOSPESO.name());
						aggiornaListe();
					});
					break;
				case SOSPESO:
					azioneAccount.setText("Riattiva");
					//azioneAccount.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
					confirmDialog.setHeader("Riattivazione Account");
					confirmDialog.add("Vuoi davvero riattivare l'account di " + utente.getNome() + " " + utente.getCognome() + "?");
					confirmDialog.addConfirmListener(confirm -> {
						UsersManager.setStatoAccount(utente.getId(), StatoAccountUtente.ATTIVO.name());
						aggiornaListe();
					});
					break;
			}
			
			return azioneAccount;
		}).setHeader("Azioni").setAutoWidth(true);
	}
	
	private void cambiaTab() {
		@SuppressWarnings("unchecked")
		ListTab<Utente> tab = (ListTab<Utente>) tabs.getSelectedTab();
		
		if(tab == null)
			return;
		
		gridUtenti.setItems(tab.getLista());
	}
	
	private void aggiornaListe() {
		tabTutti.setLista(UsersManager.getUtenti());
		tabSospesi.setLista(UsersManager.getUtentiSospesi());
		
		// aggiorna il tab attivo
		cambiaTab();
	}
}
