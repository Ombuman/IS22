package com.librarium.view;

import com.librarium.controller.components.nav.IconNavLink;
import com.librarium.controller.navigate.Navigation;
import com.librarium.controller.session.SessionManager;
import com.librarium.view.authentication.LoginDialog;
import com.librarium.view.authentication.SignupDialog;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
*
*La classe MainLayout estende la classe AppLayout e rappresenta il layout principale dell'applicazione.
*
*/
public class MainLayout extends AppLayout{
	
	private static final long serialVersionUID = -5626971534512164084L;
	
	private String title;
    private H2 viewTitle;
    
    private static VerticalLayout navLayout;
    
    /**
     * Costruttore che definisce il contenuto del drawer e dell'header.
     */
    public MainLayout() {
        //setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    /**
     * Aggiunge il contenuto dell'header, ovvero un toggle per il drawer e il titolo della vista corrente.
     */
	private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.getElement().setAttribute("aria-label", "Menu toggle");
        
        viewTitle = new H2(title);
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        
        addToNavbar(true, toggle, viewTitle);
    }

	/**
	 * Aggiunge il contenuto del drawer, ovvero il nome dell'app, le opzioni di navigazione e il footer.
	 */
    private void addDrawerContent() {
    	VerticalLayout drawerContent = new VerticalLayout();
        
        H1 appName = new H1("Librarium");
        appName.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);
        
        drawerContent.add(header, createNavigation(), createFooter());
        addToDrawer(drawerContent);
    }

    /**
     * Crea il layout di navigazione con opzioni che variano a seconda del fatto che l'utente sia loggato o meno.
     * 
     * @return il layout di navigazione
     */
    private VerticalLayout createNavigation() {
    	 navLayout = new VerticalLayout();
    	 navLayout.addClassNames(LumoUtility.Padding.NONE, LumoUtility.Gap.SMALL);
    	 
    	 updateNavLayout();
    	 
    	 return navLayout;
    }

    /**
     * Crea il footer.
     * @return il footer
     */
    private Footer createFooter() {
        Footer layout = new Footer();

        return layout;
    }

    /**
	*
    *Metodo che viene chiamato dopo la navigazione e che setta il titolo della pagina corrente.
    */
    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        
        viewTitle.setText(getCurrentPageTitle());
    }
    
    /**
	*
    *Metodo che restituisce il titolo della pagina corrente.
    *@return Titolo della pagina corrente.
    */
    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
    
    /**
	*
    *Metodo statico che gestisce la visualizzazione del layout di navigazione in base allo stato di autenticazione dell'utente.
    */
    public static void updateNavLayout() {
    	navLayout.removeAll();
    	
    	if(!SessionManager.isLogged()) {
    		Button loginButton = new Button("Accedi");
            loginButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            loginButton.addClickListener(e -> {
            	new LoginDialog().open();
            });
            
            Button signupButton = new Button("Registrati");
            signupButton.addClickListener(e -> new SignupDialog().open());
            
            HorizontalLayout authButtonsLayout = new HorizontalLayout(loginButton, signupButton);
            authButtonsLayout.addClassNames(LumoUtility.FlexWrap.WRAP, LumoUtility.Margin.NONE);
            
            navLayout.add(authButtonsLayout);
    	} else {
    		navLayout.add(creaNavUtenteRegistrato());
    	}
    }
    
    /**
	*
    *Metodo statico che crea il layout di navigazione per un utente autenticato.
    *@return Layout di navigazione per un utente autenticato.
    */
    private static VerticalLayout creaNavUtenteRegistrato() {
    	Button logout = new Button("Logout");
    	logout.addClassName(LumoUtility.Margin.Top.SMALL);
    	logout.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    	logout.addClickListener(e -> {
    		SessionManager.eliminaSessione();
    		updateNavLayout();
    		Navigation.navigateTo("/");
    	});
    	
    	VerticalLayout links = new VerticalLayout(
    		new IconNavLink("/miei-prestiti", VaadinIcon.BOOK, " I miei prestiti"), 
    		new IconNavLink("/profilo", VaadinIcon.USER, " Profilo"),
    		logout
    	);
    	links.addClassNames(LumoUtility.Padding.NONE, LumoUtility.Gap.XSMALL);
    	
    	Span testoBenvenuto = new Span("Benvenuto, " + SessionManager.getDatiUtente().getNome());
    	testoBenvenuto.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.Padding.Right.SMALL);
    	
    	Details azioniUtente = new Details(testoBenvenuto, links);
    	azioniUtente.addClassNames(LumoUtility.Padding.NONE);
    	azioniUtente.addThemeVariants(DetailsVariant.REVERSE);
    	azioniUtente.setOpened(true);
    	
    	VerticalLayout pagine = new VerticalLayout(
    		new Hr(),
    		new IconNavLink("/", VaadinIcon.OPEN_BOOK, " Catalogo")
    	);
    	pagine.addClassNames(LumoUtility.Padding.NONE, LumoUtility.Gap.XSMALL);
    	
    	VerticalLayout layoutUserNavigation = new VerticalLayout(azioniUtente, pagine);
    	layoutUserNavigation.addClassNames(LumoUtility.Padding.NONE, LumoUtility.Gap.SMALL);
    	
		return layoutUserNavigation;
    }
}
