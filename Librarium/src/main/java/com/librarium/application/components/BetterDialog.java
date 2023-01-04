package com.librarium.application.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

public class BetterDialog extends Dialog {
	
	private Button closeDialog;
	
	public BetterDialog() {
		Icon closeIcon = new Icon(VaadinIcon.CLOSE_SMALL);
		closeIcon.setColor("#333");
		closeDialog = new Button(closeIcon, e -> close());
		closeDialog.getElement().setAttribute("aria-label", "Close");
		closeDialog.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		
		getHeader().add(closeDialog);
	}
	
	public void hideCloseDialogButton() {
		closeDialog.setVisible(false);
	}
	
	public void showCloseDialogButton() {
		closeDialog.setVisible(true);
	}
	
}
