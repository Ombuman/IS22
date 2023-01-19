package com.librarium.application.views;


import java.util.Optional;

import com.librarium.application.components.BetterDialog;
import com.librarium.application.components.LinkButton;
import com.librarium.application.components.nav.IconNavLink;
import com.librarium.application.navigate.Navigation;
import com.librarium.application.views.authentication.LoginPage;
import com.librarium.application.views.authentication.SignupPage;
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
public class AdminLayout extends AppLayout{
    private String title;
    private H2 viewTitle;
    
    public AdminLayout() {
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
        
        Button logout = new Button("Logout");
    	logout.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    	logout.addClickListener(e -> {
    		SessionManager.eliminaSessione();
    		Navigation.navigateTo("/");
    	});

        drawerContent.add(header, logout, new Hr(), createNavigation(), createFooter());
        addToDrawer(drawerContent);
    }

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
}
