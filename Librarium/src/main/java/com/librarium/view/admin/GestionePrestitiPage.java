package com.librarium.view.admin;

import java.util.List;

import com.librarium.controller.components.BetterConfirmDialog;
import com.librarium.controller.components.ListTab;
import com.librarium.database.PrestitiManager;
import com.librarium.database.UsersManager;
import com.librarium.model.entities.Prestito;
import com.librarium.model.entities.Sollecito;
import com.librarium.model.enums.RuoloAccount;
import com.librarium.model.enums.StatoAccountUtente;
import com.librarium.model.enums.StatoPrestito;
import com.librarium.view.AdminLayout;
import com.librarium.view.PrivatePage;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.Grid.MultiSortPriority;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 * La classe GestionePrestitiPage rappresenta la pagina per la gestione dei prestiti all'interno dell'applicazione.
 * La classe estende PrivatePage e richiede il ruolo di Bibliotecario per l'accesso.
 */
@PageTitle("Gestione Prestiti")
@Route(value = "/gestione-prestiti", layout = AdminLayout.class)
public class GestionePrestitiPage extends PrivatePage{

	private static final long serialVersionUID = -1125039884282775763L;
	
	/** Tabs per la visualizzazione dei prestiti prenotati, attivi e conclusi */
	private Tabs tabs;
	/* Tab per visualizzare i prestiti prenotati */
	private ListTab<Prestito> tabPrenotazioni;
	/* Tab per visualizzare i prestiti attivi */
	private ListTab<Prestito> tabAttivi;
	/* Tab per visualizzare i prestiti conclusi */
	private ListTab<Prestito> tabConclusi;

	/** Grid per la visualizzazione dei prestiti */
	private Grid<Prestito> gridPrestiti;
	/* Colonna per annullare una prenotazione */
	private Column<Prestito> colonnaAnnullaPrenotazione;
	/* Colonna per attivare un prestito */
	private Column<Prestito> colonnaAttivaPrestito;
	/* Colonna per concludere un prestito */
	private Column<Prestito> colonnaConcludiPrestito;
	/* Colonna per le azioni sul profilo dell'utente */
	private Column<Prestito> colonnaAzioniAccount;
	/* Colonna per la visualizzazione della data dell'ultimo sollecito */
	private Column<Prestito> colonnaDataUltimoSollecito;
	/* Colonna per la visualizzazione della data di inizio prestito */
	private Column<Prestito> colonnaDataInizio;
	/* Colonna per la visualizzazione della data di conclusione prestito */
	private Column<Prestito> colonnaDataFine;
	
	/**
	 * Metodo per la verifica del ruolo utente prima di accedere alla pagina.
	 * 
	 * @param event BeforeEnterEvent
	 */
	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		super.beforeEnter(event, RuoloAccount.BIBLIOTECARIO);
	}
	
	/**
	 * Costruttore di default per la classe GestionePrestitiPage.
	 * Inizializza i tabs, la grid dei prestiti e le colonne.
	*/
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

	/**
	 * Metodo che aggiorna la visibilità delle colonne della grid in base alla scheda selezionata.
	 */
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
	
	/**
	 * Aggiorna il contenuto del {@link Grid} di prestiti in base alla scheda selezionata.
	 * Viene utilizzato il cast verso {@link ListTab} per recuperare la lista di prestiti associata alla scheda selezionata.
	 * La visibilità delle colonne del grid viene poi regolata in base alla scheda selezionata.
	 */
	private void aggiornaGrid() {
		@SuppressWarnings("unchecked")
		ListTab<Prestito> tab = (ListTab<Prestito>) tabs.getSelectedTab();
		
		gridPrestiti.setItems(tab.getLista());
		aggiornaColonneGrid();
	}
	
	/**
	 * Aggiorna le liste dei prestiti presenti nei tre tab della UI. Le liste sono recuperate 
	 * dal {@link PrestitiManager} in base allo stato del prestito.
	 * I dati visualizzati nella grid vengono anche aggiornati di conseguenza.
	 */
	private void aggiornaListe() {
		tabPrenotazioni.setLista(PrestitiManager.getInstance().getPrestiti(StatoPrestito.PRENOTATO.name()));
		tabAttivi.setLista(PrestitiManager.getInstance().getPrestiti(StatoPrestito.RITIRATO.name()));
		tabConclusi.setLista(PrestitiManager.getInstance().getPrestiti(StatoPrestito.CONCLUSO.name()));
		
		// aggiorna i dati visualizzati nella grid
		aggiornaGrid();
	}
	
	/**
	 * Crea la colonna "Attiva Prestito" della {@link Grid} dei prestiti.
	 * La colonna contiene un pulsante "Attiva" che permette di attivare il prestito selezionato.
	 * Se l'account dell'utente è sospeso, il pulsante sarà disabilitato.
	 * Quando il pulsante viene cliccato, viene visualizzata una {@link BetterConfirmDialog} per confermare l'attivazione del prestito.
	 * Se la conferma viene data, il prestito viene attivato tramite il metodo {@link PrestitiManager#attivaPrestito(Prestito)}.
	 * Al termine dell'operazione, vengono aggiornate le liste dei prestiti visualizzati nei vari tab tramite il metodo {@link #aggiornaListe()}.
	*/
	private void creaColonnaAttivaPrestito() {
		colonnaAttivaPrestito = gridPrestiti.addComponentColumn(prestito -> {
			Button confermaRitiro = new Button("Attiva");
			confermaRitiro.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
			
			confermaRitiro.addClickListener(e -> {
				BetterConfirmDialog confermaAttivazione = new BetterConfirmDialog("Conferma Attivazione", "Confermare l'attivazione del prestito?");
				confermaAttivazione.setRejectable(true);
				
				confermaAttivazione.addConfirmListener(confirm -> {
					PrestitiManager.getInstance().attivaPrestito(prestito);
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
	
	/**
	 * Crea la colonna per l'annullamento della prenotazione di un prestito.
	 * La colonna mostrerà un pulsante "Annulla Prenotazione" che, se cliccato,
	 * aprirà una finestra di conferma per l'annullamento effettivo della prenotazione.
	 * Se l'utente conferma l'annullamento, la prenotazione verrà eliminata e le liste dei prestiti verranno aggiornate.
	*/
	private void creaColonnaAnnullaPrenotazione() {
		colonnaAnnullaPrenotazione = gridPrestiti.addComponentColumn(prestito -> {
			Button annullaPrenotazione = new Button("Annulla Prenotazione");
			annullaPrenotazione.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
			
			annullaPrenotazione.addClickListener(e -> {
				BetterConfirmDialog confermaAnnullamento = new BetterConfirmDialog("Annulla prenotazione", "Annullare la prenotazione?");
				confermaAnnullamento.setRejectable(true);
				
				confermaAnnullamento.addConfirmListener(confirm -> {
					try {
						PrestitiManager.getInstance().annullaPrenotazione(prestito);
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
	
	/**
	 * Questo metodo crea la colonna "Concludi" nella {@link Grid} dei prestiti.
	 * La colonna contiene un {@link Button} che, una volta cliccato, apre una finestra di conferma 
	 * della conclusione del prestito tramite {@link BetterConfirmDialog}.
	 * Se l'utente conferma la conclusione, il prestito viene concluso tramite il metodo {@link PrestitiManager#concludiPrestito(Prestito)}.
	 * La lista dei prestiti viene poi aggiornata tramite il metodo {@link #aggiornaListe()}.
	*/
	private void creaColonnaConcludiPrestito() {
		colonnaConcludiPrestito = gridPrestiti.addComponentColumn(prestito -> {
			Button confermaConcludi = new Button("Concludi");
			confermaConcludi.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
			
			confermaConcludi.addClickListener(e -> {
				BetterConfirmDialog confermaConclusione = new BetterConfirmDialog("Concludi Prestito", "Confermare la conclusione del prestito?");
				confermaConclusione.setRejectable(true);
				
				confermaConclusione.addConfirmListener(confirm -> {
					PrestitiManager.getInstance().concludiPrestito(prestito);
					// aggiorna le liste dei prestiti dei vari tab
					aggiornaListe();
				});
				
				confermaConclusione.open();
			});
			
			return confermaConcludi;
		}).setWidth("130px").setFrozen(true);
	}
	
	/**
	 * Crea la colonna delle azioni sugli account dei prestiti nella griglia dei prestiti.
	 * La colonna verrà creata solamente se l'account non è bloccato e se l'utente ha già ricevuto 
	 * meno di 3 solleciti o se l'invio del sollecito è possibile.
	 * Altrimenti, verrà mostrato un pulsante disabilitato per segnalare che l'account è sospeso.
	*/
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
				List<Sollecito> sollecitiUtente = UsersManager.getInstance().getSollecitiUtenteLibro(prestito.getIdUtente(), prestito.getIdLibro());
				
				if(sollecitiUtente.size() >= 3) {
					azione.setText("Sospendi Account");
					azione.addClickListener(e -> {
						// sospendi l'account utente
						UsersManager.getInstance().setStatoAccount(prestito.getIdUtente(), StatoAccountUtente.SOSPESO.name());
						aggiornaListe();
					});
				}
				else{
					// altrimenti mostro il pulsante per inviare un sollecito se possibile
					azione.setText("Invia sollecito");
					azione.setEnabled(prestito.isInvioSollecitoPossibile());
					azione.addClickListener(e -> {
						UsersManager.getInstance().inviaSollecito(prestito);
						aggiornaListe();
					});
				}
			}
			return azione;
		}).setHeader("Azioni Account").setWidth("200px");
	}
}
