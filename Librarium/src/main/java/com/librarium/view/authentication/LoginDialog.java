package com.librarium.view.authentication;

import com.librarium.controller.components.BetterDialog;
import com.librarium.controller.navigate.Navigation;
import com.librarium.controller.session.SessionManager;
import com.librarium.database.AuthenticationManager;
import com.librarium.database.generated.org.jooq.tables.records.UtentiRecord;
import com.librarium.model.authentication.LoginInfo;
import com.librarium.model.enums.RuoloAccount;
import com.librarium.view.MainLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
*
*Classe che rappresenta la finestra di dialogo per l'accesso.
*Estende la classe BetterDialog.
*
*/
public class LoginDialog extends BetterDialog {

	private static final long serialVersionUID = 1488015700942243211L;

	private Binder<LoginInfo> binder;
	
	private EmailField email;
	private PasswordField password;
	
	private Span errorMessage;
	
	/**
	*
	*Costruttore che inizializza la finestra di dialogo per l'accesso.
	*/
	public LoginDialog() {
		super();
		
		creaFormAccesso();
		addBindingAndValidation();
	}
	
	/**
	*
	*Crea il form per l'accesso.
	*/
	private void creaFormAccesso() {
		// Titolo
		setHeaderTitle("Accesso");
		
		// Campo Email
		email = new EmailField("Email");
		
		// Campo Password
		password = new PasswordField("Password");
		
		// Pulsante Login
		Button loginButton = new Button("Accedi");
		loginButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY); // cambio lo stile del pulsante
		loginButton.addClassName("submit-btt"); // aggiungo la classe CSS
		loginButton.addClickListener(e -> {tentaAccesso();});
		
		// Messaggio d'errore
		errorMessage = new Span("");
		errorMessage.addClassNames(LumoUtility.TextColor.ERROR, LumoUtility.Padding.NONE);
		hideErrorMessage();
		
		// Link che riporta alla finestra di registrazione
		Span linkRegistration = new Span("Registrati");
		linkRegistration.addClassName("link-action");
		linkRegistration.addClickListener(e -> {
			this.close();
			new SignupDialog().open();
		});

		Paragraph hint = new Paragraph(new Text("Non hai un account? "), linkRegistration);
		
		// Creo il form
		FormLayout formLayout = new FormLayout();
		formLayout.add(email, password, loginButton, errorMessage, hint); // aggiungo al form i componenti
		formLayout.setWidth("400px");
		
		// Creo il contentiore del form
		HorizontalLayout formContainer = new HorizontalLayout();
		formContainer.setJustifyContentMode(JustifyContentMode.CENTER);
		formContainer.add(formLayout);
		formContainer.setSizeFull();
		
		// aggiungo il container del form alla finestra
		add(formContainer);
	}
	
	/**
	*
	*Questo metodo crea un binder per gestire e salvare i dati inseriti dall'utente.
	*Il binder viene associato ai campi email e password e impostato come richiesto.
	*Viene inoltre applicato un validatore di email al campo email.
	*/
	private void addBindingAndValidation() {
		// Creo il binder per salvare e gestire i dati inseriti
		binder = new Binder<>(LoginInfo.class);
		
		// collego il binder al campo email
		binder.forField(email)
		.asRequired()
		.withValidator(new EmailValidator("Formato email errato!"))
		.bind(LoginInfo::getEmail, LoginInfo::setEmail);
		// collego il binder al campo password
		binder.forField(password).asRequired().bind(LoginInfo::getPassword, LoginInfo::setPassword);
	}
	
	/**
	*
	*Questo metodo nasconde il messaggio di errore.
	*/
	private void hideErrorMessage() {
		errorMessage.setVisible(false);
	}
	
	/**
	*
	*Questo metodo mostra il messaggio di errore con il testo specificato.
	*@param errorText testo del messaggio di errore da visualizzare
	*/
	private void showErrorMessage(String errorText) {
		errorMessage.setVisible(true);
		errorMessage.setText(errorText);
	}
	
	
	/**
	*
	*Questo metodo tenta di autenticare l'utente.
	*Verifica la validità dei dati inseriti, effettua la chiamata di autenticazione e
	*gestisce il risultato. In caso di successo, crea una nuova sessione utente e naviga verso
	*la pagina di gestione del catalogo o il layout principale, a seconda del ruolo dell'utente.
	*In caso di fallimento, mostra un messaggio di errore e pulisce il campo password.
	*/
	private void tentaAccesso() {
		hideErrorMessage();
		
		if(!binder.validate().isOk())
			return;
		
		LoginInfo datiAccesso = new LoginInfo();
		try {
			binder.writeBean(datiAccesso);
			
			UtentiRecord datiUtente = AuthenticationManager.getInstance().autenticaUtente(datiAccesso);
			if(datiUtente != null) {
				SessionManager.creaNuovaSessione(datiUtente);
				
				if(RuoloAccount.valueOf(datiUtente.getRuolo()) == RuoloAccount.BIBLIOTECARIO)
					Navigation.navigateTo("/gestione-catalogo");
				else
					MainLayout.updateNavLayout();
				this.close();
			} else {
				password.clear();
				showErrorMessage("I dati inseriti sono errati");
			}
			
		} catch (ValidationException e) {
			showErrorMessage("Errore nel controllo dei dati");
			e.printStackTrace();
		}
	}
}
