package com.librarium.controller.components.admin;

import java.util.List;

import com.librarium.controller.components.BetterDialog;
import com.librarium.database.CatalogManager;
import com.librarium.database.generated.org.jooq.tables.records.GeneriRecord;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.theme.lumo.LumoUtility;
/**
 * DialogoGestioneGeneri è l'estensione della classe BetterDialog
 * è la finestra che permette digestire i generi
 *
 */
public class DialogoGestioneGeneri extends BetterDialog{
	
	private static final long serialVersionUID = -4011142881661210008L;

	private List<GeneriRecord> listaGeneri;
	
	private Grid<GeneriRecord> gridGeneri;
	private Binder<GeneriRecord> binder;
	
	
	public DialogoGestioneGeneri() {
		setHeaderTitle("Gestione Generi");
		setWidth("min(500px, 90vw)");
		
		creaAggiuntaGenere();
		creaGrid();
		
		listaGeneri = CatalogManager.getInstance().leggiGeneri();
		gridGeneri.setItems(listaGeneri);
		
		add(gridGeneri);
	}
	
	/** 
	 * creaAggiuntaGenere crea un campo per l'inserimento del nome e un pulsante per aggiungere il genere
	 */
	private void creaAggiuntaGenere() {
		binder = new Binder<>(GeneriRecord.class);
		
		TextField nomeField = new TextField("", "Inserisci un nuovo genere");
		nomeField.setWidth("min(250px, 80vw)");
		binder.forField(nomeField).asRequired("Il nome del genere non può essere vuoto").bind(GeneriRecord::getNome, GeneriRecord::setNome);
		
		Button aggiungiGenere = new Button("Aggiungi");
		aggiungiGenere.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		aggiungiGenere.addClickListener(e -> {
			if(binder.validate().isOk()){
				try {
					GeneriRecord genere = new GeneriRecord();
					binder.writeBean(genere);
					
					int id = CatalogManager.getInstance().aggiungiGenere(genere);
					genere.setId(id);
					listaGeneri.add(genere);
					gridGeneri.setItems(listaGeneri);
					
					binder.refreshFields();
				} catch (ValidationException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		add(new HorizontalLayout(nomeField, aggiungiGenere));
	}
	
	/** 
	 * creaGrid è la tabella dei generi
	 */
	private void creaGrid() {
		gridGeneri = new Grid<GeneriRecord>();
		gridGeneri.setMaxHeight("min(300px, 80vh)");
		gridGeneri.addClassNames(LumoUtility.Margin.Top.MEDIUM);
		
		gridGeneri.addColumn(GeneriRecord::getNome).setHeader("Nome").setAutoWidth(true).setSortable(true);
		gridGeneri.addComponentColumn(genere -> {
			return creaPulsanteRimuoviGenere(genere);
		}).setHeader("Azioni").setWidth("100px");
	}
	
	/** 
	 * creaPulsanteRimuoviGenere è un pulsante che permette di rimuovere il genere dal catalogo
	 * se non ha libri associati. 
	 */
	private Button creaPulsanteRimuoviGenere(GeneriRecord genere){
		Button buttonRimuovi = new Button(new Icon(VaadinIcon.TRASH));
		buttonRimuovi.addThemeVariants(ButtonVariant.LUMO_ERROR);
		
		buttonRimuovi.addClickListener(e -> {
			int numeroLibriGenere = CatalogManager.getInstance().getNumeroLibriGenere(genere.getId());
			
			if(numeroLibriGenere > 0) {
				Dialog dialog = new Dialog();
				dialog.add("Il genere \"" + genere.getNome() + "\" non può essere eliminato poichè ci sono dei libri collegati");	
				dialog.open();
				return;
			}
			
			creaDialogoConferma(genere);
		});
		
		return buttonRimuovi;
	}
	
	/** 
	 * creaDialogoConferma è la finestra di dialogo che permette di rimuovere il genere
	 */
	private void creaDialogoConferma(GeneriRecord genere) {
		ConfirmDialog dialogConferma = new ConfirmDialog();
		dialogConferma.setHeader("Elimina Genere");
		dialogConferma.add("Sei sicuro di voler eliminare il genere \"" + genere.getNome() + "\"?");
		
		Button conferma = new Button("Elimina");
		conferma.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		
		Button annulla = new Button("Annulla");
		annulla.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		
		dialogConferma.setConfirmButton(conferma);
		dialogConferma.setRejectable(true);
		dialogConferma.setRejectButton(annulla);
		
		dialogConferma.addConfirmListener(ev -> {
			CatalogManager.getInstance().rimuoviGenere(genere.getId());
			listaGeneri.remove(genere);
			gridGeneri.setItems(listaGeneri);
		});
		
		dialogConferma.open();
	}
}
