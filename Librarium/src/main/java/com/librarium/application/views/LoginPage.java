package com.librarium.application.views;

import com.librarium.application.components.BetterDialog;
import com.librarium.application.navigate.Navigation;
import com.librarium.authentication.LoginInfo;
import com.librarium.authentication.session.SessionManager;
import com.librarium.database.UsersManager;
import com.librarium.database.generated.org.jooq.tables.Utenti;
import com.librarium.database.generated.org.jooq.tables.records.UtentiRecord;
import com.librarium.database.security.EncryptionUtility;
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

//@Route("/login")
public class LoginPage extends BetterDialog {
	
	private static LoginPage instance;
	
	public static LoginPage getInstance() {
		if(instance == null)
			instance = new LoginPage();
		
		return instance;
	}
	
	private Binder<LoginInfo> binder;
	
	private EmailField email;
	private PasswordField password;
	
	private Span errorMessage;
	
	private LoginPage() {
		super();
		
		addOpenedChangeListener(e -> {
			if(errorMessage != null)
				errorMessage.setVisible(false);
			
			if(binder != null) {
				binder.getFields().forEach(f -> f.clear());
				binder.refreshFields();
			}
		});
		
		/* Creazione Form */
		
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
		errorMessage.addClassName(LumoUtility.TextColor.ERROR);
		errorMessage.addClassName(LumoUtility.Padding.NONE);
		hideErrorMessage();
		
		// Link che riporta alla finestra di registrazione
		Span linkRegistration = new Span("Registrati");
		linkRegistration.addClassName("link-action");
		linkRegistration.addClickListener(e -> {
			this.close();
			SignupPage.getInstance().open();
		});
		
		Span a;
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
		
		/* Gestione dei Dati */
		addBindingAndValidation();
	}
	
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
	
	private void hideErrorMessage() {
		errorMessage.setVisible(false);
	}
	
	private void showErrorMessage(String errorText) {
		errorMessage.setVisible(true);
		errorMessage.setText(errorText);
	}
	
	private void tentaAccesso() {
		hideErrorMessage();
		
		if(!binder.validate().isOk())
			return;
		
		LoginInfo datiAccesso = new LoginInfo();
		try {
			binder.writeBean(datiAccesso);
			
			UtentiRecord datiUtente = UsersManager.autenticaUtente(datiAccesso);
			if(datiUtente != null) {
				SessionManager.creaNuovaSessione(datiUtente);
				this.close();
			} else {
				password.clear();
				showErrorMessage("I dati inseriti sono errati");
			}
			
		} catch (ValidationException e) {
			showErrorMessage("Errore nel controllo dei dati");
			//e.printStackTrace();
		}
	}
}
