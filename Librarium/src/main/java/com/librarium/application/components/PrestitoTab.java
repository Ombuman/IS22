package com.librarium.application.components;

import java.util.List;

import com.librarium.database.entities.Prestito;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class PrestitoTab extends TitledTab {
	
	private static final long serialVersionUID = -8967484225053647583L;
	
	private List<Prestito> listaPrestiti;
	
	public PrestitoTab(String title) {
		super(title);
	}
	
	public PrestitoTab(String title, String label) {
		super(title, label);
	}
	
	public PrestitoTab(String title, Component... components) {
		super(title, components);
	}
	
	public PrestitoTab(String title, List<Prestito> listaPrestiti) {
		super(title);
		setListaPrestiti(listaPrestiti);
	}
	
	public void setListaPrestiti(List<Prestito> listaPrestiti) {
		this.listaPrestiti = listaPrestiti;
		replace(new Span(title), createBadge(getListaPrestitiSize().toString()));
	}
	
	public List<Prestito> getListaPrestiti(){
		return listaPrestiti;
	}
	
	public Integer getListaPrestitiSize() {
		return Integer.valueOf(listaPrestiti.size());
	}
	
	private Span createBadge(String text) {
		Span badge = new Span(text);
		badge.getElement().getThemeList().add("badge");
		badge.addClassNames(LumoUtility.Margin.Left.SMALL);
		return badge;
	}
	
}
