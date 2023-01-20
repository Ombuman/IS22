package com.librarium.application.views.admin;

import java.util.List;

import com.librarium.application.components.BetterConfirmDialog;
import com.librarium.application.components.ListTab;
import com.librarium.application.views.AdminLayout;
import com.librarium.application.views.PrivatePage;
import com.librarium.database.PrestitiManager;
import com.librarium.database.UsersManager;
import com.librarium.model.entities.Prestito;
import com.librarium.model.entities.Sollecito;
import com.librarium.model.enums.RuoloAccount;
import com.librarium.model.enums.StatoAccountUtente;
import com.librarium.model.enums.StatoPrestito;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.Grid.MultiSortPriority;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Gestione Prestiti")
@Route(value = "/gestione-prestiti", layout = AdminLayout.class)
public class GestionePrestitiPage extends PrivatePage{

	private static final long serialVersionUID = -1125039884282775763L;
	
	private Tabs tabs;
	private ListTab<Prestito> tabPrenotazioni;
	private ListTab<Prestito> tabAttivi;
	private ListTab<Prestito> tabConclusi;
	
	private Grid<Prestito> gridPrestiti;
	private Column<Prestito> colonnaAnnullaPrenotazione;
	private Column<Prestito> colonnaAttivaPrestito;
	private Column<Prestito> colonnaConcludiPrestito;
	private Column<Prestito> colonnaAzioniAccount;
	private Column<Prestito> colonnaDataUltimoSollecito;
	private Column<Prestito> colonnaDataInizio;
	private Column<Prestito> colonnaDataFine;
	
	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		super.beforeEnter(event, RuoloAccount.BIBLIOTECARIO);
	}
	
	public GestionePrestitiPage() {
		tabs = new Tabs();
		tabs.setWidthFull();
		
		tabPrenotazioni = new ListTab<Prestito>("Prenotazioni", "Prenotazioni");
		tabAttivi = new ListTab<Prestito>("Attivi", "Attivi");
		tabConclusi = new ListTab<Prestito>("Conclusi", "Conclusi");
		
		tabs.add(
			tabPrenotazioni,
			tabAttivi,
			tabConclusi
		);
		
		tabs.addSelectedChangeListener(e -> {
			aggiornaGrid();
		});
		
		gridPrestiti = new Grid<>(Prestito.class, false);
		gridPrestiti.setMultiSort(true, MultiSortPriority.APPEND);
		
		creaColonnaAttivaPrestito();
		creaColonnaAnnullaPrenotazione();
		creaColonnaConcludiPrestito();
		creaColonnaAzioniAccount();
		colonnaDataUltimoSollecito = gridPrestiti.addColumn(Prestito::getDataUltimoSollecito).setHeader("Ultimo Sollecito").setAutoWidth(true).setResizable(true).setSortable(true);
		gridPrestiti.addColumn(Prestito::getIdUtente).setHeader("ID utente").setAutoWidth(true).setResizable(true).setSortable(true);
		gridPrestiti.addColumn(Prestito::getEmail).setHeader("Email").setAutoWidth(true).setResizable(true);
		gridPrestiti.addColumn(Prestito::getTitolo).setHeader("Titolo").setAutoWidth(true).setResizable(true).setSortable(true);
		gridPrestiti.addColumn(Prestito::getDataPrenotazione).setHeader("Data Prenotazione").setAutoWidth(true).setResizable(true).setSortable(true);
		colonnaDataInizio = gridPrestiti.addColumn(Prestito::getDataInizio).setHeader("Data Inizio").setAutoWidth(true).setResizable(true).setSortable(true);
		colonnaDataFine = gridPrestiti.addColumn(Prestito::getDataFine).setHeader("Data Conclusione").setAutoWidth(true).setResizable(true).setSortable(true);
		aggiornaColonneGrid();
		aggiornaListe();
		
		gridPrestiti.setSizeFull();
		setSizeFull();
		add(tabs, gridPrestiti);
	}

	private void aggiornaColonneGrid() {
		@SuppressWarnings("unchecked")
		ListTab<Prestito> tab = (ListTab<Prestito>)tabs.getSelectedTab();
		
		switch(tab.getTitle()) {
		case "Prenotazioni":
			colonnaAttivaPrestito.setVisible(true);
			colonnaAnnullaPrenotazione.setVisible(true);
			colonnaConcludiPrestito.setVisible(false);
			colonnaAzioniAccount.setVisible(false);
			colonnaDataUltimoSollecito.setVisible(false);
			colonnaDataInizio.setVisible(false);
			colonnaDataFine.setVisible(false);
			break;
		case "Attivi":
			colonnaAttivaPrestito.setVisible(false);
			colonnaAnnullaPrenotazione.setVisible(false);
			colonnaConcludiPrestito.setVisible(true);
			colonnaAzioniAccount.setVisible(true);
			colonnaDataUltimoSollecito.setVisible(true);
			colonnaDataInizio.setVisible(true);
			colonnaDataFine.setVisible(false);
			break;
		case "Conclusi":
			colonnaAttivaPrestito.setVisible(false);
			colonnaAnnullaPrenotazione.setVisible(false);
			colonnaConcludiPrestito.setVisible(false);
			colonnaAzioniAccount.setVisible(false);
			colonnaDataUltimoSollecito.setVisible(false);
			colonnaDataInizio.setVisible(true);
			colonnaDataFine.setVisible(true);
			break;
		}
	}
	
	private void aggiornaGrid() {
		@SuppressWarnings("unchecked")
		ListTab<Prestito> tab = (ListTab<Prestito>) tabs.getSelectedTab();
		
		gridPrestiti.setItems(tab.getLista());
		aggiornaColonneGrid();
	}
	
	private void aggiornaListe() {
		tabPrenotazioni.setLista(PrestitiManager.getPrestiti(StatoPrestito.PRENOTATO.name()));
		tabAttivi.setLista(PrestitiManager.getPrestiti(StatoPrestito.RITIRATO.name()));
		tabConclusi.setLista(PrestitiManager.getPrestiti(StatoPrestito.CONCLUSO.name()));
		
		// aggiorna i dati visualizzati nella grid
		aggiornaGrid();
	}
	
	private void creaColonnaAttivaPrestito() {
		colonnaAttivaPrestito = gridPrestiti.addComponentColumn(prestito -> {
			Button confermaRitiro = new Button("Attiva");
			confermaRitiro.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
			
			confermaRitiro.addClickListener(e -> {
				BetterConfirmDialog confermaAttivazione = new BetterConfirmDialog("Conferma Attivazione", "Confermare l'attivazione del prestito?");
				confermaAttivazione.setRejectable(true);
				
				confermaAttivazione.addConfirmListener(confirm -> {
					PrestitiManager.attivaPrestito(prestito);
					// aggiorna le liste dei prestiti dei vari tab
					aggiornaListe();
				});
				
				confermaAttivazione.open();
			});
			
			// se l'account è sospeso disabilita il pulsante
			confermaRitiro.setEnabled(prestito.getStatoUtente().equals(StatoAccountUtente.ATTIVO.name()));
			
			return confermaRitiro;
		}).setWidth("130px").setFrozen(true);
	}
	
	private void creaColonnaAnnullaPrenotazione() {
		colonnaAnnullaPrenotazione = gridPrestiti.addComponentColumn(prestito -> {
			Button annullaPrenotazione = new Button("Annulla Prenotazione");
			annullaPrenotazione.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
			
			annullaPrenotazione.addClickListener(e -> {
				BetterConfirmDialog confermaAnnullamento = new BetterConfirmDialog("Annulla prenotazione", "Annullare la prenotazione?");
				confermaAnnullamento.setRejectable(true);
				
				confermaAnnullamento.addConfirmListener(confirm -> {
					try {
						PrestitiManager.annullaPrenotazione(prestito);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					// aggiorna le liste dei prestiti dei vari tab
					aggiornaListe();
				});
				
				confermaAnnullamento.open();
			});
			
			return annullaPrenotazione;
		}).setWidth("200px");
	}
	
	private void creaColonnaConcludiPrestito() {
		colonnaConcludiPrestito = gridPrestiti.addComponentColumn(prestito -> {
			Button confermaConcludi = new Button("Concludi");
			confermaConcludi.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
			
			confermaConcludi.addClickListener(e -> {
				BetterConfirmDialog confermaConclusione = new BetterConfirmDialog("Concludi Prestito", "Confermare la conclusione del prestito?");
				confermaConclusione.setRejectable(true);
				
				confermaConclusione.addConfirmListener(confirm -> {
					PrestitiManager.concludiPrestito(prestito);
					// aggiorna le liste dei prestiti dei vari tab
					aggiornaListe();
				});
				
				confermaConclusione.open();
			});
			
			return confermaConcludi;
		}).setWidth("130px").setFrozen(true);
	}
	
	private void creaColonnaAzioniAccount() {
		colonnaAzioniAccount = gridPrestiti.addComponentColumn(prestito -> {
			// se l'account non è bloccato verifico se devo inviare dei solleciti
			Button azione = new Button();
			azione.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
			
			// se l'account è bloccato mostro il pulsante non attivo
			if(prestito.getStatoUtente().equals(StatoAccountUtente.SOSPESO.name())) {
				azione.setText("Account sospeso");
				azione.setEnabled(false);
			} else {
				// se l'utente ha già 3 solleciti (sullo stesso libro?) mostro il pulsante per bloccare l'account
				List<Sollecito> sollecitiUtente = UsersManager.getSollecitiUtenteLibro(prestito.getIdUtente(), prestito.getIdLibro());
				
				if(sollecitiUtente.size() >= 3) {
					azione.setText("Sospendi Account");
					azione.addClickListener(e -> {
						// sospendi l'account utente
						UsersManager.setStatoAccount(prestito.getIdUtente(), StatoAccountUtente.SOSPESO.name());
						aggiornaListe();
					});
				}
				else{
					// altrimenti mostro il pulsante per inviare un sollecito se possibile
					azione.setText("Invia sollecito");
					azione.setEnabled(prestito.isInvioSollecitoPossibile());
					azione.addClickListener(e -> {
						UsersManager.inviaSollecito(prestito);
						aggiornaListe();
					});
				}
			}
			return azione;
		}).setHeader("Azioni Account").setWidth("200px");
	}
}
