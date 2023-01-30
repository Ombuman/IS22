package com.librarium.view.admin;

import com.librarium.controller.components.admin.DialogoGestioneGeneri;
import com.librarium.controller.components.admin.DialogoInserimentoLibro;
import com.librarium.controller.components.admin.TabellaGestioneCatalogo;
import com.librarium.model.entities.Libro;
import com.librarium.model.enums.RuoloAccount;
import com.librarium.view.AdminLayout;
import com.librarium.view.PrivatePage;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
 * Classe che rappresenta la pagina per la gestione del catalogo dei libri in biblioteca.
 * La pagina è accessibile solo ai bibliotecari ed è composta da una tabella per la visualizzazione dei 
 * libri e un pulsante per l'aggiunta di un nuovo libro.
 */

@PageTitle("Gestione Catalogo")
@Route(value = "/gestione-catalogo", layout = AdminLayout.class)
public class GestioneCatalogoPage extends PrivatePage{

	private static final long serialVersionUID = 7013705902143209837L;
	
	private TabellaGestioneCatalogo tabella;
	
	/**
	 * Metodo invocato prima dell'entrata nella pagina, controlla che l'utente abbia il ruolo di bibliotecario.
	 * 
	 * @param event evento prima dell'entrata nella pagina
	 */
	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		super.beforeEnter(event, RuoloAccount.BIBLIOTECARIO);
	}
	
	/**
	 * Costruttore che inizializza la pagina con la tabella dei libri e il pulsante per l'aggiunta di un nuovo libro.
	 */
	public GestioneCatalogoPage() {
		addClassNames(LumoUtility.Padding.SMALL, LumoUtility.Padding.Bottom.LARGE);
		
		tabella = new TabellaGestioneCatalogo();
		
		
		add(creaPulsanteAggiungi(), tabella);
		setSizeFull();
	}
	
	/**
	 * Crea e restituisce il pulsante per l'aggiunta di un nuovo libro.
	 * 
	 * @return pulsante per l'aggiunta di un nuovo libro
	 */
	private Button creaPulsanteAggiungi() {
		Icon plus = new Icon(VaadinIcon.PLUS);
		plus.addClassName(LumoUtility.IconSize.SMALL);
		
		Button aggiungiLibro = new Button(new HorizontalLayout(new Span("Aggiungi libro"), plus));
		aggiungiLibro.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		
		aggiungiLibro.addClickListener(e -> {
			DialogoGestioneGeneri gestioneGeneri = new DialogoGestioneGeneri();
			
			gestioneGeneri.addCloseListener(close -> {
				tabella.aggiornaListaGeneri();
			});
			
			DialogoInserimentoLibro dialog = new DialogoInserimentoLibro(gestioneGeneri);
			dialog.pulsanteAggiungi.addClickListener(click -> {
				try {
					Libro libro = dialog.getLibro();
					if(libro == null)
						return;
					
					tabella.aggiungiLibro(libro);
					dialog.close();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
			dialog.open();
		});
		
		return aggiungiLibro;
	}
	
}
