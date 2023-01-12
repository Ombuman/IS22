package com.librarium.application.views;


import java.util.Optional;

import com.librarium.application.components.BetterDialog;
import com.librarium.application.components.LinkButton;
import com.librarium.application.components.nav.IconNavLink;
import com.librarium.application.navigate.Navigation;
import com.librarium.application.views.base.LoginPage;
import com.librarium.application.views.base.SignupPage;
import com.librarium.authentication.session.SessionManager;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout{
    private String title;
    private H2 viewTitle;
    
    private static HorizontalLayout authButtonsLayout;
    
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
        
        initializeAuthButtonsLayout();

        drawerContent.add(header, authButtonsLayout, new Hr(), createNavigation(), createFooter());
        addToDrawer(drawerContent);
    }

    private VerticalLayout createNavigation() {
        VerticalLayout nav = new VerticalLayout();
        nav.add(
        	new IconNavLink("/", VaadinIcon.OPEN_BOOK, " Catalogo"),
            new IconNavLink("/chi-siamo", VaadinIcon.QUESTION_CIRCLE_O, " Chi Siamo")
        );
        nav.addClassNames(LumoUtility.Padding.XSMALL, LumoUtility.Gap.SMALL);
        
        return nav;
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
    
    private void initializeAuthButtonsLayout() {
    	authButtonsLayout = new HorizontalLayout();
        authButtonsLayout.addClassNames(LumoUtility.FlexWrap.WRAP, LumoUtility.Margin.NONE);
        
        updateAuthButtonsLayout();
    }
    
    public static void updateAuthButtonsLayout() {
    	// resetta il layout
    	authButtonsLayout.removeAll();
    	
    	if(!SessionManager.isLogged()) {
    		Button loginButton = new Button("Accedi");
            loginButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            loginButton.addClickListener(e -> {
            	new LoginPage().open();
            });
            
            Button signupButton = new Button("Registrati");
            signupButton.addClickListener(e -> new SignupPage().open());
            
            authButtonsLayout.add(loginButton, signupButton);
    	} else {
    		creaNavAccount();
    	}
    }
    
    private static void creaNavAccount() {
    	Icon icona = new Icon(VaadinIcon.USER);
    	icona.addClassName(LumoUtility.IconSize.SMALL);
    	
    	IconNavLink iMieiPrestitiLink = new IconNavLink("/miei-prestiti", VaadinIcon.BOOK, " I miei prestiti");
        IconNavLink profiloLink = new IconNavLink("/profilo", VaadinIcon.USER, " Profilo");
    	
    	VerticalLayout links = new VerticalLayout(iMieiPrestitiLink, profiloLink);
    	links.addClassNames(LumoUtility.Padding.NONE, LumoUtility.Gap.XSMALL);
    	
    	Button logout = new Button("Logout");
    	logout.addClassName(LumoUtility.Margin.Top.SMALL);
    	logout.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    	logout.addClickListener(e -> {
    		SessionManager.eliminaSessione();
    		updateAuthButtonsLayout();
    		Navigation.navigateTo("/");
    	});
    	links.add(logout);
    	
    	Span testoBenvenuto = new Span("Benvenuto, " + SessionManager.getDatiUtente().getNome());
    	testoBenvenuto.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.Padding.Right.SMALL);
    	
    	Details azioniUtente = new Details(testoBenvenuto, links);
    	azioniUtente.addThemeVariants(DetailsVariant.REVERSE);
    	azioniUtente.setOpened(true);
    	
    	VerticalLayout layoutUserNavigation = new VerticalLayout(azioniUtente);
    	layoutUserNavigation.addClassNames(LumoUtility.Padding.NONE, LumoUtility.Gap.SMALL);
    	
		authButtonsLayout.add(layoutUserNavigation);
    }
}
