package com.librarium.application.views.user;

import com.librarium.application.views.MainLayout;
import com.librarium.application.views.PrivatePage;
import com.librarium.authentication.session.SessionManager;
import com.librarium.database.entities.InfoProfiloUtente;
import com.librarium.database.enums.RuoloAccount;
import com.librarium.database.generated.org.jooq.tables.records.UtentiRecord;
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

@PageTitle("Profilo")
@Route(value = "/profilo", layout = MainLayout.class)
public class ProfiloUtentePage extends PrivatePage{

	private static final long serialVersionUID = -8997253279837988823L;
	
	private FormLayout formDatiUtente;
	private TextField email;
	private TextField nome;
	private TextField cognome;
	private Button conferma;
	
	private Binder<InfoProfiloUtente> binder;
	
	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		super.beforeEnter(event, "/", RuoloAccount.UTENTE);
	}
	
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
