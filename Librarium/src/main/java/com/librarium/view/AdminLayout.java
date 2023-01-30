package com.librarium.view;

import com.librarium.controller.components.nav.IconNavLink;
import com.librarium.controller.navigate.Navigation;
import com.librarium.controller.session.SessionManager;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
 * Classe che estende la classe {@link AppLayout} e rappresenta il layout per l'interfaccia dell'admin.
 * Contiene il menù di navigazione, il titolo della pagina corrente, un pulsante di logout e un footer.
*/
 
public class AdminLayout extends AppLayout{
	
	private static final long serialVersionUID = 5516654098584989643L;
    
	private String title;
    private H2 viewTitle;
    
    /**
	*
    *Costruttore di default che inizializza il layout con la barra di navigazione, il contenuto del drawer e
    *il contenuto dell'header.
    */
    public AdminLayout() {
        //setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }
    
    /**
	*
    *Aggiunge il contenuto dell'header che consiste in un toggle per il drawer,
    *una vista del titolo della pagina corrente.
    */
	private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.getElement().setAttribute("aria-label", "Menu toggle");
        
        viewTitle = new H2(title);
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        
        addToNavbar(true, toggle, viewTitle);
    }

	/**
	*
	*Aggiunge il contenuto del drawer che consiste in un header con il nome dell'applicazione,
	*un pulsante di logout, un separatore orizzontale, una navigazione a icone e un footer.
	*/
    private void addDrawerContent() {
    	VerticalLayout drawerContent = new VerticalLayout();
        
        H1 appName = new H1("Librarium");
        appName.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);
        
        Button logout = new Button("Logout");
    	logout.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    	logout.addClickListener(e -> {
    		SessionManager.eliminaSessione();
    		Navigation.navigateTo("/");
    	});

        drawerContent.add(header, logout, new Hr(), createNavigation(), createFooter());
        addToDrawer(drawerContent);
    }

    /**
  	*
    *Crea la sezione di navigazione con link a icone per le pagine di gestione catalogo,
    *gestione prestiti e gestione utenti.
    *@return sezione di navigazione.
    */
    private VerticalLayout createNavigation() {
        VerticalLayout nav = new VerticalLayout();
        nav.add(
        	new IconNavLink("/gestione-catalogo", VaadinIcon.BOOK, " Gestione Catalogo"),
        	new IconNavLink("/gestione-prestiti", VaadinIcon.CALENDAR, " Gestione Prestiti"),
        	new IconNavLink("/gestione-utenti", VaadinIcon.USER, " Gestione Utenti")
        );
        nav.addClassNames(LumoUtility.Padding.XSMALL, LumoUtility.Gap.SMALL);
        
        return nav;
    }

  
    /**
	*
    *Crea il footer dell'interfaccia utente.
    *@return Un oggetto Footer vuoto.
    */
    private Footer createFooter() {
        Footer layout = new Footer();

        return layout;
    }

    /**
	*
    *Aggiorna il titolo visualizzato nell'header dopo ogni navigazione.
    */
    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        
        viewTitle.setText(getCurrentPageTitle());
    }
    
    /**
	*
    *Recupera il titolo della pagina corrente.
    *@return Il titolo della pagina, se presente. Altrimenti, una stringa vuota.
    */
    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
