package com.librarium.view.user;

import com.librarium.controller.components.catalogo.Catalogo;
import com.librarium.view.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
*
* Classe che rappresenta la pagina del catalogo dell'applicazione librarium.
*
*/

@PageTitle("Catalogo")
@Route(value = "/", layout = MainLayout.class)
public class CatalogoPage extends VerticalLayout{

	private static final long serialVersionUID = 8967061080385115981L;
	
	/**
	 * Costruttore per la pagina del catalogo dell'applicazione librarium.
	 * Crea una nuova istanza del componente {@link Catalogo} e lo aggiunge al layout.
	 */

	public CatalogoPage() {
		Catalogo catalogo = new Catalogo();
		addClassName(LumoUtility.Padding.NONE);
		add(catalogo);
	}
}
