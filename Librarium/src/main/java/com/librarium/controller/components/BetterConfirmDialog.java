package com.librarium.controller.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;

public class BetterConfirmDialog extends ConfirmDialog{
	
	private static final long serialVersionUID = 6078775775889467184L;
	
	private Button confirmButton;
	private Button rejectButton;
	
	public BetterConfirmDialog() {
		creaPulsanti();
	}
	
	public BetterConfirmDialog(String header) {
		this();
		setHeader(header);
	}
	
	public BetterConfirmDialog(String header, String text) {
		this(header);
		add(text);
	}
	
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
