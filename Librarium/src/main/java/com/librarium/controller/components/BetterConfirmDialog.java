package com.librarium.controller.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;

/**
 * Better Confirm Dialog è un'estensione di Confirm Dialog,
 * con il vantaggio di creare in automatico i pulsanti "Confirm" e "Reject"
 */
public class BetterConfirmDialog extends ConfirmDialog{
	
	private static final long serialVersionUID = 6078775775889467184L;
	
	private Button confirmButton; // Pulsante di conferma
	private Button rejectButton; // Pulsante di rifiuto
	
	public BetterConfirmDialog() {
		creaPulsanti();
	}
	
	/**
	 * @param header il titolo della finestra di dialogo
	 */
	public BetterConfirmDialog(String header) {
		this();
		setHeader(header);
	}
	
	/**
	 * @param header il titolo della finestra di dialogo
	 * @param text il testo all'interno della finestra di dialogo
	 */
	public BetterConfirmDialog(String header, String text) {
		this(header);
		add(text);
	}
	
	/**
	 * Crea i pulsanti <i>"Confirm"</i> e <i>"Reject"</i>
	 */
	private void creaPulsanti() {
		confirmButton = new Button("Conferma");
		confirmButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		
		setConfirmButton(confirmButton);
		
		rejectButton = new Button("Annulla");
		rejectButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		
		setRejectButton(rejectButton);
	}
	
	@Override
	public void setConfirmText(String confirmText) {
		confirmButton.setText(confirmText);
	}
	
	@Override
	public void setRejectText(String rejectText) {
		rejectButton.setText(rejectText);
	}
	
}
