package com.librarium.controller.components.admin;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.librarium.database.CatalogManager;
import com.librarium.database.generated.org.jooq.tables.records.GeneriRecord;
import com.librarium.model.entities.Libro;
import com.librarium.model.enums.StatoLibro;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class TabellaGestioneCatalogo extends Grid<Libro>{
	private static final long serialVersionUID = 527429669895603123L;

	private List<Libro> listaLibri;
	private List<GeneriRecord> listaGeneri;
	
	private Editor<Libro> editor;
	private Binder<Libro> binder;
	
	private Grid.Column<Libro> colonnaTitolo;
	private Grid.Column<Libro> colonnaDescrizione;
	private Grid.Column<Libro> colonnaCopertina;
	private Grid.Column<Libro> colonnaAutore;
	private Grid.Column<Libro> colonnaCasaEditrice;
	private Grid.Column<Libro> colonnaAnno;
	private Grid.Column<Libro> colonnaGeneri;
	private Grid.Column<Libro> colonnaAzioni;
	
	private MultiSelectComboBox<GeneriRecord> generiField;
	
	public TabellaGestioneCatalogo() {
		// inizializza i dati
		listaLibri = CatalogManager.getInstance().leggiLibri("", "", "");
		listaGeneri = CatalogManager.getInstance().leggiGeneri();
		
		setMultiSort(true, MultiSortPriority.APPEND);
		
		colonnaTitolo = addColumn(Libro::getTitolo).setHeader("Titolo").setSortable(true).setAutoWidth(true).setResizable(true);
		colonnaDescrizione = addColumn(Libro::getDescrizione).setHeader("Descrizione").setWidth("250px").setResizable(true);
		colonnaCopertina = addComponentColumn(libro -> {
			return creaColonnaCopertina(libro);
		}).setHeader("Copertina").setWidth("250px").setResizable(true);
		colonnaAutore = addColumn(Libro::getAutore).setHeader("Autore").setSortable(true).setAutoWidth(true).setResizable(true);
		colonnaCasaEditrice = addColumn(Libro::getCasaEditrice).setHeader("Casa Editrice").setSortable(true).setAutoWidth(true).setResizable(true);
		colonnaAnno = addColumn(Libro::getAnno).setHeader("Anno").setSortable(true).setWidth("100px").setResizable(true);
		colonnaGeneri = addColumn(Libro::getGeneriString).setHeader("Generi").setWidth("300px").setResizable(true);
		addComponentColumn(libro -> {
			return creaBadgeStato(libro);
		}).setHeader("Stato").setAutoWidth(true).setResizable(true);
		
		colonnaAzioni = addComponentColumn(libro -> {
			return creaColonnaAzioni(libro);
		}).setAutoWidth(true).setFrozenToEnd(true).setHeader("Azioni");
		
		creaBinder();
		
		setItems(listaLibri);
		//GridListDataView<Libro> dataView = setItems(listaLibri);
	}
	
	private HorizontalLayout creaColonnaCopertina(Libro libro){
		Span linkCopertina = new Span(libro.getCopertina());
		linkCopertina.addClassNames(LumoUtility.Overflow.HIDDEN, LumoUtility.TextOverflow.ELLIPSIS);
		
		Dialog dialog = new Dialog();
		Image immagineCopertina = new Image(libro.getCopertina(), libro.getTitolo());
		immagineCopertina.setWidth("min(200px, 90vw)");
		dialog.add(immagineCopertina);
		
		Button apriCopertina = new Button(new Icon(VaadinIcon.EXTERNAL_LINK));
		apriCopertina.addClickListener(e -> {
			dialog.open();
		});
		
		HorizontalLayout container = new HorizontalLayout(linkCopertina, apriCopertina);
		container.setAlignItems(Alignment.CENTER);
		
		return container;
	}
	
	private Span creaBadgeStato(Libro libro) {
		Span badge = new Span("");
		switch(StatoLibro.valueOf(libro.getLibro().getStato())) {
		case DISPONIBILE:
			badge = new Span("Disponibile");
			badge.getElement().getThemeList().add("badge primary success");
			break;
		case NON_DISPONIBILE:
			badge = new Span("Non disponibile");
			badge.getElement().getThemeList().add("badge primary error");
			break;			
		}
		
		return badge;
	}
	
	private HorizontalLayout creaColonnaAzioni(Libro libro) {
		Button editButton = new Button(new Icon(VaadinIcon.PENCIL));
		editButton.addClickListener(e -> {
			if(editor.isOpen())
				editor.cancel();
			getEditor().editItem(libro);
		});
		
		Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
		deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
		deleteButton.addClickListener(e -> {
			ConfirmDialog confirmDialog = new ConfirmDialog();
			confirmDialog.setHeader("Elimina libro");
			confirmDialog.setText("Sei sicuo di voler eliminare il libro \"" + libro.getTitolo() + "\"?");
			confirmDialog.setRejectable(true);
			
			Button confirmButton = new Button("Sì, elimina");
			confirmButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
			Button cancelButton = new Button("No, non eliminare");
			cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
			
			confirmDialog.setConfirmButton(confirmButton);
			confirmDialog.setRejectButton(cancelButton);
			
			confirmDialog.addConfirmListener(l -> {
				rimuoviLibro(libro);
			});
			confirmDialog.open();
		});
		
		return new HorizontalLayout(editButton, deleteButton);
	}

	private void creaBinder() {
		binder = new Binder<>(Libro.class);
		
		editor = getEditor();
		editor.setBinder(binder);
		editor.setBuffered(true);
		
		editor.addSaveListener(e -> {
			// aggiorna il libro
			CatalogManager.getInstance().aggiornaLibro(e.getItem());
		});
		
		bindTitolo();
		bindDescrizione();
		bindCopertina();
		bindGeneri();
		bindAutore();
		bindCasaEditrice();
		bindAnno();
		bindModifica();
	}

	private void bindTitolo() {
		TextField titoloField = new TextField();
		titoloField.setWidthFull();
		binder.forField(titoloField)
			.asRequired("Il titolo non deve essere vuoto")
			.bind(Libro::getTitolo, Libro::setTitolo);
		colonnaTitolo.setEditorComponent(titoloField);
	}
	
	private void bindDescrizione() {
		TextArea descrizioneField = new TextArea();
		descrizioneField.setMaxHeight("100px");
		descrizioneField.setWidthFull();
		binder.forField(descrizioneField)
			.asRequired("La descrizione non deve essere vuota")
			.bind(Libro::getDescrizione, Libro::setDescrizione);
		colonnaDescrizione.setEditorComponent(descrizioneField);
	}
	
	private void bindCopertina() {
		TextField copertinaField = new TextField();
		copertinaField.setWidthFull();
		
		binder.forField(copertinaField)
			.asRequired("Il campo copertina non deve essere vuoto")
			.withValidator(link -> {
				Pattern pattern = Pattern.compile("[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)", Pattern.CASE_INSENSITIVE);
				Matcher matcher = pattern.matcher(link);
				return matcher.find();
			}, "Il link inserito non è corretto")
			.bind(Libro::getCopertina, Libro::setCopertina);
		colonnaCopertina.setEditorComponent(copertinaField);
	}
	
	private void bindAutore() {
		TextField autoreField = new TextField();
		autoreField.setWidthFull();
		binder.forField(autoreField)
			.asRequired("Il campo autore non deve essere vuoto")
			.bind(Libro::getAutore, Libro::setAutore);
		colonnaAutore.setEditorComponent(autoreField);
	}
	
	private void bindCasaEditrice() {
		TextField casaEditriceField = new TextField();
		casaEditriceField.setWidthFull();
		binder.forField(casaEditriceField)
			.asRequired("Il campo casa editrice non deve essere vuoto")
			.bind(Libro::getCasaEditrice, Libro::setCasaEditrice);
		colonnaCasaEditrice.setEditorComponent(casaEditriceField);
	}
	
	private void bindAnno() {
		TextField annoField = new TextField();
		annoField.setAllowedCharPattern("[\\d*]");
		annoField.setWidthFull();
		binder.forField(annoField)
			.asRequired("Il campo anno non deve essere vuoto")
			.bind(Libro::getAnno, Libro::setAnno);
		colonnaAnno.setEditorComponent(annoField);
	}
	
	private void bindGeneri() {
		generiField = new MultiSelectComboBox<GeneriRecord>();
		generiField.setSizeFull();
		generiField.setItems(listaGeneri);
		generiField.setItemLabelGenerator(genere -> {
			return genere != null ? genere.getNome() : new String("");
		});
		
		binder.forField(generiField)
			.asRequired("Deve esserci almeno un genere")
			.bind(Libro::getGeneri, Libro::setGeneri);
		
		Button aggiungiGenere = new Button(new Icon(VaadinIcon.PLUS));
		aggiungiGenere.addClickListener(e -> {
			// mostra la finestra per gestire le categorie. Se viene aggiunta o rimossa una categoria aggiorna la lista
			DialogoGestioneGeneri dialog = new DialogoGestioneGeneri();
			dialog.addCloseListener(close -> aggiornaListaGeneri());
			dialog.addDialogCloseActionListener(close -> aggiornaListaGeneri());
			dialog.open();
		});
		colonnaGeneri.setEditorComponent(new HorizontalLayout(generiField, aggiungiGenere));
	}
	
	private void bindModifica() {
		Button saveButton = new Button(new Icon(VaadinIcon.CHECK), e -> {
			//modifica il libro
			editor.save();
		});
		saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		
		Button cancelButton = new Button(VaadinIcon.CLOSE.create(), e -> editor.cancel());
		cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
		
		HorizontalLayout actions = new HorizontalLayout(saveButton,cancelButton);
		actions.addClassNames(LumoUtility.FlexWrap.WRAP);
		actions.setPadding(false);
		colonnaAzioni.setEditorComponent(actions);
	}
	
	public void aggiungiLibro(Libro libro) {
		// inserisci il libro e ricava l'ID
		int idLibro = CatalogManager.getInstance().aggiungiLibro(libro);
		// aggiungi alla lista il libro appena inserito
		listaLibri.add(0, CatalogManager.getInstance().leggiLibro(idLibro));
		// aggiorna la lista
		setItems(listaLibri);
	}
	
	private void rimuoviLibro(Libro libro) {
		// rimuovi il libro e aggiorna la lista
		CatalogManager.getInstance().rimuoviLibro(libro.getLibro().getId());
		listaLibri.remove(libro);
		setItems(listaLibri);
	}
	
	public void aggiornaListaGeneri() {
		listaGeneri = CatalogManager.getInstance().leggiGeneri();
		Set<GeneriRecord> generiSelezionati = generiField.getSelectedItems();
		generiField.setItems(listaGeneri);
		generiField.select(generiSelezionati);
	}
}
