package com.librarium.application.views.user;

import java.util.List;

import com.librarium.application.components.ListTab;
import com.librarium.application.components.cards.CardPrestito;
import com.librarium.application.views.MainLayout;
import com.librarium.application.views.PrivatePage;
import com.librarium.authentication.session.SessionManager;
import com.librarium.database.PrestitiManager;
import com.librarium.model.entities.Prestito;
import com.librarium.model.enums.RuoloAccount;
import com.librarium.model.enums.StatoPrestito;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

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
	
	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		super.beforeEnter(event, "/", RuoloAccount.UTENTE);
	}
	
	public PrestitiUtentePage() {
		inizializzaTabs();	
		inizializzaListaPrestitiUtente();
		setSizeFull();
	}
	
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
	
	private void inizializzaListaPrestitiUtente() {
		body = new VerticalLayout();
		body.addClassNames(LumoUtility.Padding.NONE);
		body.setSizeFull();
		add(body);
		
		inizializzaVirtualList();
		aggiornaVirtualList();
	}
	
	private void inizializzaVirtualList() {
		virtualListPrestiti = new VirtualList<>();
		virtualListPrestiti.setRenderer(new ComponentRenderer<CardPrestito, Prestito>(
			prestito -> {
				CardPrestito card = new CardPrestito(prestito);
				card.confirmDialog.addConfirmListener(e -> {
					try {
						PrestitiManager.annullaPrenotazione(prestito);
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

	public void aggiornaListe() {
		tutti.setLista(getListaPrestiti());
		prenotazioni.setLista(getListaPrenotazioni());
		
		aggiornaVirtualList();
	}
	
	private List<Prestito> getListaPrestiti() {
		return PrestitiManager.getPrestitiUtente(SessionManager.getDatiUtente().getId(), null);
	}
	
	private List<Prestito> getListaPrenotazioni() {
		return PrestitiManager.getPrestitiUtente(SessionManager.getDatiUtente().getId(), StatoPrestito.PRENOTATO.name());
	}
	
	private List<Prestito> getListaAttivi() {
		return PrestitiManager.getPrestitiUtente(SessionManager.getDatiUtente().getId(), StatoPrestito.RITIRATO.name());
	}
	
	private List<Prestito> getListaConclusi() {
		return PrestitiManager.getPrestitiUtente(SessionManager.getDatiUtente().getId(), StatoPrestito.CONCLUSO.name());
	}
}
