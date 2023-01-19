package com.librarium.application.components;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.librarium.database.entities.Prestito;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class ListTab<E> extends TitledTab {
	
	private static final long serialVersionUID = -8967484225053647583L;
	
	private List<E> lista;
	
	public ListTab(String title) {
		this(title, "");
	}
	
	public ListTab(String title, String label) {
		super(title, label);
		lista = new ArrayList<>();
	}
	
	public ListTab(String title, Component... components) {
		super(title, components);
		lista = new ArrayList<>();
	}
	
	public ListTab(String title, List<E> lista) {
		super(title);
		setLista(lista);
	}
	
	public ListTab(String title, String label, List<E> lista) {
		super(title, label);
		setLista(lista);
	}
	
	public void setLista(List<E> lista) {
		this.lista = lista;
		replace(new Span(title), createBadge(getListaPrestitiSize().toString()));
	}
	
	public List<E> getLista(){
		return lista;
	}
	
	public Integer getListaPrestitiSize() {
		return Integer.valueOf(lista.size());
	}
	
	private Span createBadge(String text) {
		Span badge = new Span(text);
		badge.getElement().getThemeList().add("badge");
		badge.addClassNames(LumoUtility.Margin.Left.SMALL);
		return badge;
	}
	
}
