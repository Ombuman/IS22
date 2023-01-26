package com.librarium.controller.components;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.theme.lumo.LumoUtility;


/**
 * ListaTab è l'estensione della classe TitledTab.
 * Implementa e permette la gestione di una lista di oggetti
 *
 * @param <E> è la classe degli oggetti che devono essere salvati nella lista
 */
public class ListTab<E> extends TitledTab {
	
	private static final long serialVersionUID = -8967484225053647583L;
	
	private List<E> lista;
	
	/**
	 * @param title è il titolo del Tab
	 */
	public ListTab(String title) {
		this(title, "");
	}
	
	/**
	 * @param title è il titolo del Tab
	 * @param label è il testo che viene mostrato nel Tab
	 */
	public ListTab(String title, String label) {
		super(title, label);
		lista = new ArrayList<>();
	}
	
	/**
	 * @param title è il titolo del Tab
	 * @param components è la lista di componenti che vengono aggiunti al Tab come children
	 */
	public ListTab(String title, Component... components) {
		super(title, components);
		lista = new ArrayList<>();
	}
	
	/**
	 * @param title è il titolo del Tab
	 * @param lista è la lista da memorizzare
	 */
	public ListTab(String title, List<E> lista) {
		super(title);
		setLista(lista);
	}
	
	/**
	 * @param title è il titolo del Tab
	 * @param label è il testo che viene mostrato nel Tab
	 * @param lista è la lista da memorizzare
	 */
	public ListTab(String title, String label, List<E> lista) {
		super(title, label);
		setLista(lista);
	}
	
	/**
	 * @param lista è la lista da memorizzare
	 */
	public void setLista(List<E> lista) {
		this.lista = lista;
		replace(new Span(title), createBadge(getListaPrestitiSize().toString()));
	}
	
	/**
	 * @return la lista salvata nel Tab
	 */
	public List<E> getLista(){
		return lista;
	}
	
	
	/**
	 * @return la lunghezza della lista salvata nel Tab
	 */
	public Integer getListaPrestitiSize() {
		return Integer.valueOf(lista.size());
	}
	
	
	/**
	 * @param text è il testo da mostrare nel badge
	 * @return il badge creato, il quale sarà un'istanza della classe Span
	 */
	private Span createBadge(String text) {
		Span badge = new Span(text);
		badge.getElement().getThemeList().add("badge");
		badge.addClassNames(LumoUtility.Margin.Left.SMALL);
		return badge;
	}
	
}
