package com.librarium.application.views.user;

import java.util.List;

import com.librarium.application.components.cards.CardPrestito;
import com.librarium.application.views.MainLayout;
import com.librarium.application.views.PrivatePage;
import com.librarium.authentication.session.SessionManager;
import com.librarium.database.PrestitiManager;
import com.librarium.database.entities.Prestito;
import com.librarium.database.enums.RuoloAccount;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@PageTitle("I miei prestiti")
@Route(value = "/miei-prestiti", layout = MainLayout.class)
public class PrestitiUtentePage extends PrivatePage{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5555793988357193089L;

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		super.beforeEnter(event, "/", RuoloAccount.UTENTE);
	}
	
	private VirtualList<Prestito> listaPrestiti;
	
	public PrestitiUtentePage() {
		inizializzaListaPrestitiUtente();
		setSizeFull();
	}
	
	private void inizializzaListaPrestitiUtente() {
		listaPrestiti = new VirtualList<>();
		listaPrestiti.setRenderer(new ComponentRenderer<CardPrestito, Prestito>(
			prestito -> new CardPrestito(prestito, this)
		));
		listaPrestiti.addClassNames(LumoUtility.Margin.NONE, LumoUtility.Padding.SMALL);
		aggiornaLista();
	}
	
	public void aggiornaLista() {
		aggiornaLista(PrestitiManager.getPrestitiUtente(SessionManager.getDatiUtente().getId()));
	}
	
	private void aggiornaLista(List<Prestito> prestiti) {
		removeAll();
		if(prestiti.size() == 0) {
			add("Nessun prestito effettuato");
			return;
		}
		
		listaPrestiti.setItems(prestiti);
		add(listaPrestiti);
	}
	
}
