package com.librarium.controller.components;

import com.vaadin.flow.component.Component;

import com.vaadin.flow.component.tabs.Tab;


/**
 * TitledTab è l'estensione della classe Tab.
 * Permette di dare un titolo al Tab per identificarlo
 */
public class TitledTab extends Tab {

	private static final long serialVersionUID = 7082260706344153382L;
	
	protected String title;
	
	/**
	 * @param title è il titolo del Tab
	 */
	public TitledTab(String title) {
		this.title = title;
	}
	
	
	/**
	 * @param components è la lista di componenti che vengono 
	 * aggiunti al Tab come children
	 */
	public TitledTab(Component... components) {
		super(components);
	}
	
	/**
	 * @param title è il titolo del Tab
	 * @param label è il testo che viene mostrato nel Tab
	 */
	public TitledTab(String title, String label) {
		super(label);
		this.title = title;
	}
	
	/**
	 * @param title è il titolo del Tab
	 * @param components è la lista di componenti che vengono aggiunti al Tab come children
	 */
	public TitledTab(String title, Component... components) {
		super(components);
		this.title = title;
	}
	
	/**
	 * @return il titolo del Tab
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Imposta il titolo del Tab
	 * 
	 * @param title è il titolo del Tab
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Rimpiazza i componenti del Tab con dei nuovi
	 * 
	 * @param components è la lista dei nuovi componenti
	 */
	public void replace(Component... components) {
		removeAll();
		add(components);
	}
}
