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
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout{
	
	private static final long serialVersionUID = -5626971534512164084L;
	
	private String title;
    private H2 viewTitle;
    
    private static VerticalLayout navLayout;
    
    public MainLayout() {
        //setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

	private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.getElement().setAttribute("aria-label", "Menu toggle");
        
        viewTitle = new H2(title);
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        
        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
    	VerticalLayout drawerContent = new VerticalLayout();
        
        H1 appName = new H1("Librarium");
        appName.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);
        
        drawerContent.add(header, createNavigation(), createFooter());
        addToDrawer(drawerContent);
    }

    private VerticalLayout createNavigation() {
    	 navLayout = new VerticalLayout();
    	 navLayout.addClassNames(LumoUtility.Padding.NONE, LumoUtility.Gap.SMALL);
    	 
    	 updateNavLayout();
    	 
    	 return navLayout;
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        
        viewTitle.setText(getCurrentPageTitle());
    }
    
    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
    
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
