package com.librarium.application.views.admin;

import com.librarium.application.components.admin.DialogoGestioneGeneri;
import com.librarium.application.components.admin.DialogoInserimentoLibro;
import com.librarium.application.components.admin.TabellaGestioneCatalogo;
import com.librarium.application.views.AdminLayout;
import com.librarium.application.views.PrivatePage;
import com.librarium.database.entities.Libro;
import com.librarium.database.enums.RuoloAccount;
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

@PageTitle("Gestione Catalogo")
@Route(value = "/gestione-catalogo", layout = AdminLayout.class)
public class GestioneCatalogoPage extends PrivatePage{

	private static final long serialVersionUID = 7013705902143209837L;
	
	private TabellaGestioneCatalogo tabella;
	
	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		super.beforeEnter(event, RuoloAccount.BIBLIOTECARIO);
	}
	
	public GestioneCatalogoPage() {
		addClassNames(LumoUtility.Padding.SMALL, LumoUtility.Padding.Bottom.LARGE);
		
		tabella = new TabellaGestioneCatalogo();
		
		
		add(creaPulsanteAggiungi(), tabella);
		setSizeFull();
	}
	
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
