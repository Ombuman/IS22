package com.librarium.view.user;

import com.librarium.controller.session.SessionManager;
import com.librarium.database.generated.org.jooq.tables.records.UtentiRecord;
import com.librarium.model.entities.InfoProfiloUtente;
import com.librarium.model.enums.RuoloAccount;
import com.librarium.view.MainLayout;
import com.librarium.view.PrivatePage;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
*
*Classe che gestisce la visualizzazione della pagina del profilo utente.
*
*/

@PageTitle("Profilo")
@Route(value = "/profilo", layout = MainLayout.class)
public class ProfiloUtentePage extends PrivatePage{

	private static final long serialVersionUID = -8997253279837988823L;
	
	/**
	 * Form per la visualizzazione dei dati del profilo
	 */
	private FormLayout formDatiUtente;
	
	/**
	 * Campo per la visualizzazione dell'email dell'utente
	 */
	private TextField email;
	
	/**
	 * Campo per la visualizzazione e la modifica del nome dell'utente
	 */
	private TextField nome;
	
	/**
	 * Campo per la visualizzazione e la modifica del cognome dell'utente
	 */
	private TextField cognome;
	
	/**
	 * Bottone per la conferma della modifica del profilo
	 */
	private Button conferma;
	
	/**
	 * Binder per la gestione dei dati del profilo
	 */
	private Binder<InfoProfiloUtente> binder;
	
	/**
	 * Metodo che gestisce l'accesso alla pagina, verificando se l'utente loggato ha
	 * il ruolo di utente.
	 */
	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		super.beforeEnter(event, "/", RuoloAccount.UTENTE);
	}
	
	/**
	 * Costruttore della classe. Inizializza i componenti della pagina.
	 */
	public ProfiloUtentePage() {
		
		Span title = new Span("Modifica profilo");
		title.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.FontWeight.BOLD);
		
		email = new TextField("Email");
		email.setReadOnly(true);
		
		nome = new TextField("Nome", "Inserisci il tuo nome");
		cognome = new TextField("Cognome", "Inserisci il tuo cognome");
		
		nome.setClearButtonVisible(true);
		cognome.setClearButtonVisible(true);
		
		conferma = new Button("Modifica");
		conferma.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		conferma.addClassNames(LumoUtility.Margin.Top.MEDIUM);
		conferma.addClickListener(e -> modificaProfilo());
		
		formDatiUtente = new FormLayout(email, nome, cognome, conferma);
		formDatiUtente.setWidth("min(500px, 90vw)");
		
		formDatiUtente.setResponsiveSteps(
			new ResponsiveStep("0", 1),
			new ResponsiveStep("500px", 2)
		);
		formDatiUtente.setColspan(email, 2);
		formDatiUtente.setColspan(conferma, 2);
		
		VerticalLayout formContainer = new VerticalLayout(title, formDatiUtente);
		formContainer.setSizeFull();
		
		add(formContainer);
		
		binder = new Binder<>(InfoProfiloUtente.class);
		binder.forField(email).bind(InfoProfiloUtente::getEmail, InfoProfiloUtente::setEmail);
		binder.forField(nome).asRequired().bind(InfoProfiloUtente::getNome, InfoProfiloUtente::setNome);
		binder.forField(cognome).asRequired().bind(InfoProfiloUtente::getCognome, InfoProfiloUtente::setCognome);
		
		UtentiRecord datiUtente = SessionManager.getDatiUtente();
		binder.setBean(new InfoProfiloUtente(datiUtente.getEmail(), datiUtente.getNome(), datiUtente.getCognome()));
	}
	
	/**
	*
    * Modifica i dati del profilo dell'utente corrente.
    * Questo metodo valida i dati inseriti nel binder e, in caso di successo, li scrive nel profilo dell'utente corrente e
    * li aggiorna tramite il SessionManager. Se i dati inseriti non sono validi, il metodo non esegue alcuna azione.
    * In caso di successo, il metodo aggiorna anche il MainLayout.
    * 
    */
	private void modificaProfilo() {
		if(!binder.validate().isOk())
			return;
		
		InfoProfiloUtente dati = new InfoProfiloUtente();
		try {
			binder.writeBean(dati);
			if(dati.compareTo(SessionManager.getDatiUtente()) != 0)
				return;
			
			SessionManager.aggiornaDatiUtente(dati);
			MainLayout.updateNavLayout();
		} catch (ValidationException e) {
			e.printStackTrace();
		}
	}
	
}
