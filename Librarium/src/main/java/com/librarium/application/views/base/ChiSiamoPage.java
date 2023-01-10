package com.librarium.application.views.base;

import com.librarium.application.navigate.Navigation;
import com.librarium.application.views.MainLayout;
import com.librarium.authentication.session.SessionManager;
import com.librarium.database.enums.RuoloAccount;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterListener;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Chi Siamo")
@Route(value = "/chi-siamo", layout = MainLayout.class)
public class ChiSiamoPage extends VerticalLayout {
	
	public ChiSiamoPage() {
		Button b = new Button("a");
		b.addClickListener(e -> {
			b.getUI().ifPresent(ui -> ui.navigate(""));
		});
		
		add(b);
	}
}
