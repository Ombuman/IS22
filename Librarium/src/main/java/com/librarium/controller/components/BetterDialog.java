package com.librarium.controller.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;


/**
 * Better Dialog è un'estensione della classe Dialog, che implementa
 * l'aggiunta di un pulsante per chiudere la finestra, il quale può essere
 * reso visibile o lasciato nascosto.
 */
public class BetterDialog extends Dialog {
	private static final long serialVersionUID = -3736456847457802536L;
	
	private Button closeDialog;
	
	public BetterDialog() {
		Icon closeIcon = new Icon(VaadinIcon.CLOSE_SMALL);
		closeIcon.setColor("#333");
		closeDialog = new Button(closeIcon, e -> close());
		closeDialog.getElement().setAttribute("aria-label", "Close");
		closeDialog.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		
		getHeader().add(closeDialog);
	}
	
	/**
	 * Nasconde il pulsante per chiudere la finestra
	 */
	public void hideCloseDialogButton() {
		closeDialog.setVisible(false);
	}
	
	/**
	 * Mostra il pulsante per chiudere la finestra
	 */
	public void showCloseDialogButton() {
		closeDialog.setVisible(true);
	}
	
	
	/**
	 * @param event l'evento che avviene quando l'utente
	 * preme il pulsante per chiudere la finestra
	 */
	public void addCloseListener(ComponentEventListener<ClickEvent<Button>> event) {
		closeDialog.addClickListener(event);
	}
	
}
