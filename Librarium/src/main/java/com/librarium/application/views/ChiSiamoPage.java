package com.librarium.application.views;

import com.librarium.application.navigate.Navigation;
import com.librarium.authentication.session.SessionManager;
import com.librarium.database.enums.RuoloAccount;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Chi Siamo")
@Route(value = "/chi-siamo", layout = MainLayout.class)
public class ChiSiamoPage extends VerticalLayout{
	
	public ChiSiamoPage() {
		// implementare autorizzazione pagine
		if(!SessionManager.isLogged() || SessionManager.getDatiUtente().getRuoloAsRuoloAccount() == RuoloAccount.UTENTE) {
			System.out.println("aaaa");
			UI.getCurrent().navigate("/");
			//LoginPage.getInstance().open();
			//Navigation.navigateTo(, "/");
		}
	}
	
}
