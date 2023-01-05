package com.librarium.application.views;


import com.librarium.application.components.LinkButton;
import com.librarium.application.components.appnav.AppNav;
import com.librarium.application.components.appnav.AppNavItem;
import com.librarium.application.navigate.Navigation;
import com.librarium.authentication.session.SessionManager;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {
	
    private String title;
    private H2 viewTitle;
    
    private static HorizontalLayout authButtonsLayout;

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
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
        
    	initializeAuthButtonsLayout();
    	
        H1 appName = new H1("Librarium");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        drawerContent.add(authButtonsLayout, new Hr(), header, scroller, createFooter());
        addToDrawer(drawerContent);
    }

    private VerticalLayout createNavigation() {
        VerticalLayout nav = new VerticalLayout();
        nav.addClassNames("navbar", LumoUtility.Padding.NONE, LumoUtility.Gap.SMALL);
        
        Anchor catalogo = new Anchor("/", "Catalogo");
        Anchor chiSiamo = new Anchor("/chi-siamo", "Chi Siamo");
        
        nav.add(catalogo, chiSiamo);
        
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
        authButtonsLayout.addClassName(LumoUtility.FlexWrap.WRAP);
        authButtonsLayout.setClassName(LumoUtility.Margin.NONE);
        
        updateAuthButtonsLayout();
    }
    
    public static void updateAuthButtonsLayout() {
    	// resetta il layout
    	authButtonsLayout.removeAll();
    	
    	if(!SessionManager.isLogged()) {
    		Button loginButton = new Button("Accedi");
            loginButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            loginButton.addClickListener(e -> LoginPage.getInstance().open());
            
            Button signupButton = new Button("Registrati");
            signupButton.addClickListener(e -> SignupPage.getInstance().open());
            
            authButtonsLayout.add(loginButton, signupButton);
    	} else {
    		creaNavAccount();
    	}
    }
    
    private static void creaNavAccount() {
    	Icon icona = new Icon(VaadinIcon.USER);
    	icona.addClassName(LumoUtility.IconSize.SMALL);
    	
    	H1 titolo = new H1("Profilo");
    	titolo.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
    	
    	HorizontalLayout headerTop = new HorizontalLayout(icona, titolo);
    	headerTop.addClassName(LumoUtility.Padding.NONE);
    	
    	Span testoBenvenuto = new Span("Benvenuto, " + SessionManager.getDatiUtente().getNome());
    	testoBenvenuto.addClassName(LumoUtility.FontSize.SMALL);
    	
    	VerticalLayout header = new VerticalLayout(headerTop, testoBenvenuto);
    	header.addClassNames(LumoUtility.Padding.NONE, LumoUtility.Gap.XSMALL);
    	
    	Anchor iMieiPrestitiLink = new Anchor("/", "I miei prestiti");
    	Anchor profiloLink = new Anchor("/", "Profilo");
    	
    	VerticalLayout links = new VerticalLayout(iMieiPrestitiLink, profiloLink);
    	links.addClassNames(LumoUtility.Padding.NONE, LumoUtility.Gap.XSMALL);
    	
    	Button logout = new Button("Logout");
    	logout.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    	logout.addClickListener(e -> {
    		SessionManager.eliminaSessione();
    		Navigation.navigateTo(authButtonsLayout, "/");
    	});
    	
    	VerticalLayout layoutUserNavigation = new VerticalLayout(header, links, logout);
    	layoutUserNavigation.addClassNames(LumoUtility.Padding.NONE, LumoUtility.Gap.SMALL);
    	
		authButtonsLayout.add(layoutUserNavigation);
    }
}
