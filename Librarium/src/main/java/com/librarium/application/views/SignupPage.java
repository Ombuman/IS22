package com.librarium.application.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("/signup")
public class SignupPage extends VerticalLayout {

	public SignupPage() {
		/* Pagina Registrazione */
		
		// Titolo
		H1 titolo = new H1("Registrazione");
		// Campo Nome
		TextField nome = new TextField("Nome");
		// Campo Cognome
		TextField cognome = new TextField("Cognome");
		// Campo Username
		TextField username = new TextField("Username");
		// Campo Password
		PasswordField password = new PasswordField("Password");
		// Campo Conferma Password
		PasswordField confermaPassword = new PasswordField("Confirm password");
		// Pulsante 
		Button signupButton = new Button("Registrati");
		signupButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY); // cambio lo stile del pulsante
		signupButton.addClassName("submit-btt"); // aggiungo la classe CSS

		// link che riporta alla pagina di login
		Anchor linkLogin = new Anchor("/login", "Accedi");
		Paragraph hint = new Paragraph(new Text("Hai già un account? "), linkLogin);

		// Creo il form
		FormLayout formLayout = new FormLayout();
		formLayout.add(titolo, nome, cognome, username, password, confermaPassword, signupButton, hint);

		// Setto gli step responsivi del form
		formLayout.setResponsiveSteps(new ResponsiveStep("0", 1), new ResponsiveStep("500px", 2));
		
		formLayout.setColspan(titolo, 2);
		formLayout.setColspan(username, 2);
		formLayout.setColspan(signupButton, 2);
		formLayout.setWidth("600px");
		
		// Creo il contenitore del form
		HorizontalLayout formContainer = new HorizontalLayout();
		formContainer.setJustifyContentMode(JustifyContentMode.CENTER);
		formContainer.add(formLayout);
		formContainer.setSizeFull();

		// Layout della pagina a grandezza massima
		setSizeFull();
		
		// aggiungo il container del form alla pagina
		add(formContainer);
	}

}
