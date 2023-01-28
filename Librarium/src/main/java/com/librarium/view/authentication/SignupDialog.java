package com.librarium.view.authentication;

import com.librarium.controller.components.BetterDialog;
import com.librarium.database.AuthenticationManager;
import com.librarium.model.authentication.SignupInfo;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class SignupDialog extends BetterDialog {

	private static final long serialVersionUID = 1L;

	private Binder<SignupInfo> binder;
	
	private TextField nome;
	private TextField cognome;
	private EmailField email;
	private PasswordField password;
	private PasswordField confermaPassword;
	
	private Button signupButton;
	private Span errorMessage;
	
	public SignupDialog() {
		super();
		
		creaFormRegistrazione();
		addBindingAndValidation();
	}
	
	private void creaFormRegistrazione() {
		// Titolo
		setHeaderTitle("Registrazione");
		
		// Inizializzazione campi
		nome = new TextField("Nome");
		cognome = new TextField("Cognome");
		email = new EmailField("Email");
		password = new PasswordField("Password");
		password.setValueChangeMode(ValueChangeMode.EAGER);
		password.addValueChangeListener(value -> {
			if(value.getValue().length() == 0)
				confermaPassword.clear();
			
			confermaPassword.setEnabled(value.getValue().length() != 0);
		});
		confermaPassword = new PasswordField("Conferma password");
		confermaPassword.setEnabled(false);
		
		// Pulsante 
		signupButton = new Button("Registrati");
		signupButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY); // cambio lo stile del pulsante
		signupButton.addClassName("submit-btt"); // aggiungo la classe CSS
		signupButton.addClickListener(e -> {tentaRegistrazione();});
		
		// Link che riporta alla finestra di login
		Span linkLogin = new Span("Accedi");
		linkLogin.addClassName("link-action");
		linkLogin.addClickListener(e -> {
			mostraPaginaDiAccesso();
		});
		
		// Messaggio d'errore
		errorMessage = new Span("");
		errorMessage.addClassName(LumoUtility.TextColor.ERROR);
		errorMessage.addClassName(LumoUtility.Padding.NONE);
		hideErrorMessage();
		
		Paragraph hint = new Paragraph(new Text("Hai già un account? "), linkLogin);

		// Creo il form
		FormLayout formLayout = new FormLayout();
		formLayout.add(nome, cognome, email, password, confermaPassword, signupButton, hint);
		
		// Setto gli step responsivi del form
		formLayout.setResponsiveSteps(new ResponsiveStep("0", 1), new ResponsiveStep("500px", 2));
		
		formLayout.setColspan(email, 2);
		formLayout.setColspan(signupButton, 2);
		formLayout.setWidth("600px");
		
		// Creo il contenitore del form
		HorizontalLayout formContainer = new HorizontalLayout();
		formContainer.setJustifyContentMode(JustifyContentMode.CENTER);
		formContainer.add(formLayout);
		formContainer.setSizeFull();
		
		// aggiungo il container del form alla pagina
		add(formContainer);
	}

	private void mostraPaginaDiAccesso() {
		this.close();
		new LoginDialog().open();
	}

	private void addBindingAndValidation() {
		// Creo il binder per salvare e gestire i dati inseriti
		binder = new Binder<>(SignupInfo.class);
		
		//collego il binder ai campi nome e cognome
		binder.forField(nome).asRequired().bind(SignupInfo::getNome, SignupInfo::setNome);
		binder.forField(cognome).asRequired().bind(SignupInfo::getCognome, SignupInfo::setCognome);
		// collego il binder al campo email
		binder.forField(email)
		.asRequired()
		.withValidator(new EmailValidator("Formato email errato!"))
		.withValidator(email -> validateEmail(email), "L'email inserita è già stata usata!")
		.bind(SignupInfo::getEmail, SignupInfo::setEmail);
		// collego il binder al campo password
		binder.forField(password).asRequired().bind(SignupInfo::getPassword, SignupInfo::setPassword);
		binder.forField(confermaPassword).asRequired().withValidator(
			v -> (!v.isBlank() && v.equals(password.getValue())), 
			"Le password non coincidono"
		).bind(SignupInfo::getConfermaPassword, SignupInfo::setConfermaPassword);
	}
	
	private boolean validateEmail(String email) {
		return AuthenticationManager.getInstance().verificaDisponibilitaEmail(email);
	}
	
	private void hideErrorMessage() {
		errorMessage.setVisible(false);
	}
	
	private void showErrorMessage(String errorText) {
		errorMessage.setVisible(true);
		errorMessage.setText(errorText);
	}
	
	private void tentaRegistrazione() {
		hideErrorMessage();
		
		if(!binder.validate().isOk())
			return;
		
		signupButton.setEnabled(false);
		
		try {
			SignupInfo datiUtente = new SignupInfo();
			binder.writeBean(datiUtente);
			
			AuthenticationManager.getInstance().registraUtente(datiUtente);
			
			mostraPaginaDiAccesso();
		} catch (Exception e) {
			showErrorMessage("Si è verificato un errore. Riprova più tardi!");
		} finally {
			signupButton.setEnabled(true);
		}
	}
	
}
