package com.librarium.application.components.admin;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.librarium.application.components.BetterDialog;
import com.librarium.database.CatalogManager;
import com.librarium.database.entities.Libro;
import com.librarium.database.generated.org.jooq.tables.records.GeneriRecord;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class DialogoInserimentoLibro extends BetterDialog {
	
	private static final long serialVersionUID = 908501892196802025L;

	private List<GeneriRecord> listaGeneri;
	
	public Binder<Libro> binder;
	public Button pulsanteAggiungi;
	
	private MultiSelectComboBox<GeneriRecord> generiField;
	private DialogoGestioneGeneri gestioneGeneri;
	
	public DialogoInserimentoLibro(DialogoGestioneGeneri gestioneGeneri) {
		this.gestioneGeneri = gestioneGeneri;
		
		listaGeneri = CatalogManager.leggiGeneri();
		
		setHeaderTitle("Aggiunta Libro");
		add(creaFormInserimentoDatiLibro());
	}
	
	private FormLayout creaFormInserimentoDatiLibro() {
		binder = new Binder<>(Libro.class);
		
		TextField titoloField = creaFieldTitolo();
		TextArea descrizioneField = creaFieldDescrizione();
		TextField copertinaField = creaFieldCopertina();
		TextField autoreField = creaFieldAutore();
		TextField casaEditriceField = creaFieldCasaEditrice();
		TextField annoField = creaFieldAnno();
		HorizontalLayout generiField = creaFieldGeneri();
		
		pulsanteAggiungi = new Button("Aggiungi");
		pulsanteAggiungi.setWidth("min(300px, 90vw)");
		pulsanteAggiungi.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		HorizontalLayout layoutPulsante = new HorizontalLayout(pulsanteAggiungi);
		layoutPulsante.addClassNames(LumoUtility.Margin.Top.MEDIUM, LumoUtility.Display.FLEX, LumoUtility.JustifyContent.CENTER);
		
		FormLayout form = new FormLayout(
			titoloField, 
			generiField,
			descrizioneField,
			copertinaField, 
			autoreField, 
			casaEditriceField, 
			annoField,
			layoutPulsante
		);
		
		form.setWidth("min(700px, 90vw)");
		form.addClassNames(LumoUtility.Padding.Bottom.MEDIUM);
		form.setResponsiveSteps(new ResponsiveStep("0", 1), new ResponsiveStep("500px", 2));
		form.setColspan(descrizioneField, 2);
		form.setColspan(layoutPulsante, 2);
		
		return form;
	}
	
	private TextField creaFieldTitolo() {
		TextField titoloField = new TextField("Titolo", "Inserisci il titolo");
		titoloField.setWidthFull();
		binder.forField(titoloField)
			.asRequired("Il titolo non deve essere vuoto")
			.bind(Libro::getTitolo, Libro::setTitolo);
		
		return titoloField;
	}
	
	private TextArea creaFieldDescrizione() {
		TextArea descrizioneField = new TextArea("Descrizione", "Inserisci la descrizione");
		descrizioneField.setMaxHeight("100px");
		descrizioneField.setWidthFull();
		binder.forField(descrizioneField)
			.asRequired("La descrizione non deve essere vuota")
			.bind(Libro::getDescrizione, Libro::setDescrizione);
		
		return descrizioneField;
	}
	
	private TextField creaFieldCopertina() {
		TextField copertinaField = new TextField("Copertina", "Inserisci il link della copertina");
		copertinaField.setWidthFull();
		
		binder.forField(copertinaField)
			.asRequired("Il campo copertina non deve essere vuoto")
			.withValidator(link -> {
				Pattern pattern = Pattern.compile("[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)", Pattern.CASE_INSENSITIVE);
				Matcher matcher = pattern.matcher(link);
				return matcher.find();
			}, "Il link inserito non è corretto")
			.bind(Libro::getCopertina, Libro::setCopertina);
		
		return copertinaField;
	}
	
	private TextField creaFieldAutore() {
		TextField autoreField = new TextField("Autore", "Inserisci il nome dell'autore");
		autoreField.setWidthFull();
		binder.forField(autoreField)
			.asRequired("Il campo autore non deve essere vuoto")
			.bind(Libro::getAutore, Libro::setAutore);
		
		return autoreField;
	}
	
	private TextField creaFieldCasaEditrice() {
		TextField casaEditriceField = new TextField("Casa Editrice", "Inserisci il nome della casa editrice");
		casaEditriceField.setWidthFull();
		binder.forField(casaEditriceField)
			.asRequired("Il campo casa editrice non deve essere vuoto")
			.bind(Libro::getCasaEditrice, Libro::setCasaEditrice);
		
		return casaEditriceField;
	}
	
	private TextField creaFieldAnno() {
		TextField annoField = new TextField("Anno", "Inserisci l'anno di pubblicazione");
		annoField.setAllowedCharPattern("[\\d*]");
		annoField.setWidthFull();
		binder.forField(annoField)
			.asRequired("Il campo anno non deve essere vuoto")
			.bind(Libro::getAnno, Libro::setAnno);
		return annoField;
	}
	
	private HorizontalLayout creaFieldGeneri() {
		generiField = new MultiSelectComboBox<GeneriRecord>("Generi");
		generiField.setPlaceholder("Seleziona i generi");
		generiField.setSizeFull();
		generiField.setItems(listaGeneri);
		generiField.setItemLabelGenerator(genere -> {
			return genere != null ? genere.getNome() : new String("");
		});
		
		binder.forField(generiField)
			.asRequired("Deve esserci almeno un genere")
			.bind(Libro::getGeneri, Libro::setGeneri);
		
		// quando si chiude la pagina di gestione dei generi aggiorna la lista di questa finestra
		gestioneGeneri.addCloseListener(close -> aggiornaListaGeneri());
		gestioneGeneri.addDialogCloseActionListener(close -> aggiornaListaGeneri());
		
		Button aggiungiGenere = new Button(new Icon(VaadinIcon.PLUS));
		aggiungiGenere.addClickListener(e -> {
			// mostra la finestra per gestire i generi
			gestioneGeneri.open();
		});
		
		HorizontalLayout layout = new HorizontalLayout(generiField, aggiungiGenere);
		layout.setAlignItems(Alignment.END);
		return layout;
	}
	
	public Libro getLibro() throws Exception{
		if(!binder.validate().isOk())
			return null;
			
		Libro libro = new Libro();
		binder.writeBean(libro);
		
		return libro;
	}
	
	public void aggiornaListaGeneri() {
		listaGeneri = CatalogManager.leggiGeneri();
		Set<GeneriRecord> generiSelezionati = generiField.getSelectedItems();
		generiField.setItems(listaGeneri);
		generiField.select(generiSelezionati);
	}
}
