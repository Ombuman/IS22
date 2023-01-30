package com.librarium.view.user;

import java.util.List;

import com.librarium.controller.components.ListTab;
import com.librarium.controller.components.cards.CardPrestito;
import com.librarium.controller.session.SessionManager;
import com.librarium.database.PrestitiManager;
import com.librarium.model.entities.Prestito;
import com.librarium.model.enums.RuoloAccount;
import com.librarium.model.enums.StatoPrestito;
import com.librarium.view.MainLayout;
import com.librarium.view.PrivatePage;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
*
* Classe PrestitiUtentePage che estende PrivatePage e rappresenta la pagina con la lista dei prestiti di un utente.
*
*/

@PageTitle("I miei prestiti")
@Route(value = "/miei-prestiti", layout = MainLayout.class)
public class PrestitiUtentePage extends PrivatePage{

	private static final long serialVersionUID = -5555793988357193089L;
	
	private VerticalLayout body;
	private Tabs tabs;
	
	private ListTab<Prestito> tutti;
	private ListTab<Prestito> prenotazioni;
	private ListTab<Prestito> attivi;
	private ListTab<Prestito> conclusi;

	private VirtualList<Prestito> virtualListPrestiti;
	
	/**
	*
    * Metodo che viene eseguito prima di entrare nella pagina e verifica che l'utente abbia il ruolo di UTENTE.
    * @param event Evento di entrata nella pagina
    */
	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		super.beforeEnter(event, "/", RuoloAccount.UTENTE);
	}
	
	/**
	*
    * Costruttore della classe PrestitiUtentePage che inizializza le tabs e la lista dei prestiti di un utente.
    *
    */
	public PrestitiUtentePage() {
		inizializzaTabs();	
		inizializzaListaPrestitiUtente();
		setSizeFull();
	}
	
	/**
	*
    * Metodo che inizializza le tabs della pagina.
    * 
    */
	private void inizializzaTabs() {
		tabs = new Tabs();
		
		tutti = new ListTab<Prestito>("Tutti", getListaPrestiti());
		prenotazioni = new ListTab<Prestito>("Prenotazioni", getListaPrenotazioni());
		attivi = new ListTab<Prestito>("Attivi", getListaAttivi());
		conclusi = new ListTab<Prestito>("Conclusi", getListaConclusi());
		
		tabs.add(
			tutti,
			prenotazioni,
			attivi,
			conclusi
		);
		
		tabs.addSelectedChangeListener(e -> aggiornaVirtualList());
		tabs.setWidthFull();
		tabs.setMaxWidth("100%");
		
		HorizontalLayout topMenu = new HorizontalLayout(tabs);
		topMenu.setWidthFull();
		
		add(topMenu);
	}
	
	/**
	*
    * Metodo che inizializza la lista dei prestiti di un utente.
    * 
    */
	private void inizializzaListaPrestitiUtente() {
		body = new VerticalLayout();
		body.addClassNames(LumoUtility.Padding.NONE);
		body.setSizeFull();
		add(body);
		
		inizializzaVirtualList();
		aggiornaVirtualList();
	}
	
	/**
	*
    * Inizializza la lista virtuale dei prestiti.
    * La lista virtuale verrà creata con un componente renderer che
    * renderizza ogni elemento della lista come una card di tipo {@link CardPrestito}.
    * All'interno di ogni card verrà aggiunto un listener al dialog di conferma
    * che, in caso di conferma, annulla la prenotazione del prestito tramite il metodo
    * {@link PrestitiManager#annullaPrenotazione(Prestito)} e poi richiama il metodo
    * {@link #aggiornaListe()} per aggiornare la lista.
    *La classe {@link VirtualList} verrà poi associata a classi di stile di Lumo.
    *
    */
	private void inizializzaVirtualList() {
		virtualListPrestiti = new VirtualList<>();
		virtualListPrestiti.setRenderer(new ComponentRenderer<CardPrestito, Prestito>(
			prestito -> {
				CardPrestito card = new CardPrestito(prestito);
				card.confirmDialog.addConfirmListener(e -> {
					try {
						PrestitiManager.getInstance().annullaPrenotazione(prestito);
						aggiornaListe();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				});
				return card;
			}
		));
		
		virtualListPrestiti.addClassNames(LumoUtility.Margin.NONE, LumoUtility.Padding.SMALL);
	}
	
	/**
	*
    * Aggiorna la VirtualList con i prestiti nella scheda attualmente selezionata.
    * Se non ci sono prestiti disponibili, visualizza un messaggio.
    * 
    */
	private void aggiornaVirtualList() {
		body.removeAll();
		
		@SuppressWarnings("unchecked")
		ListTab<Prestito> tabAttuale = (ListTab<Prestito>) tabs.getSelectedTab();
		
		if(tabAttuale.getListaPrestitiSize() == 0) {
			body.add("Nessun prestito disponibile");
			return;
		}
		
		virtualListPrestiti.setItems(tabAttuale.getLista());
		body.add(virtualListPrestiti);
	}

	/**
	*
    * Metodo che permette di aggiornare le liste di prestiti.
    * Verranno utilizzati i metodi {@link #getListaPrestiti()} e {@link #getListaPrenotazioni()}
    * per ottenere le liste dei prestiti. Verrà poi invocato il metodo {@link #aggiornaVirtualList()}
    * per rendere effettive le modifiche.
    * 
    */
	public void aggiornaListe() {
		tutti.setLista(getListaPrestiti());
		prenotazioni.setLista(getListaPrenotazioni());
		
		aggiornaVirtualList();
	}
	
	/**
	*
    * Recupera la lista dei prestiti effettuati dall'utente corrente.
    * @return la lista dei prestiti effettuati dall'utente corrente, null se non esistono prestiti.
    * 
    */
	private List<Prestito> getListaPrestiti() {
		return PrestitiManager.getInstance().getPrestitiUtente(SessionManager.getDatiUtente().getId(), null);
	}
	
	/**
	*
    * Questo metodo restituisce una lista di prestiti prenotati dall'utente corrente.
    * @return List<Prestito> lista dei prestiti prenotati dall'utente corrente
    * 
    */
	private List<Prestito> getListaPrenotazioni() {
		return PrestitiManager.getInstance().getPrestitiUtente(SessionManager.getDatiUtente().getId(), StatoPrestito.PRENOTATO.name());
	}
	
	/**
	*
    * Restituisce la lista dei prestiti attivi dell'utente corrente.
    * @return La lista dei prestiti attivi dell'utente corrente.
    * 
    */
	private List<Prestito> getListaAttivi() {
		return PrestitiManager.getInstance().getPrestitiUtente(SessionManager.getDatiUtente().getId(), StatoPrestito.RITIRATO.name());
	}
	
	/**
	*
    * Restituisce la lista di prestiti conclusi dell'utente corrente.
    * @return La lista di prestiti conclusi dell'utente corrente.
    * 
    */
	private List<Prestito> getListaConclusi() {
		return PrestitiManager.getInstance().getPrestitiUtente(SessionManager.getDatiUtente().getId(), StatoPrestito.CONCLUSO.name());
	}
}
