package com.librarium.controller.components;

import com.vaadin.flow.component.Component;

import com.vaadin.flow.component.tabs.Tab;

public class TitledTab extends Tab {

	private static final long serialVersionUID = 7082260706344153382L;
	
	protected String title;
	
	public TitledTab(String title) {
		this.title = title;
	}
	
	public TitledTab(Component... components) {
		super(components);
	}
	
	public TitledTab(String title, String label) {
		super(label);
		this.title = title;
	}
	
	public TitledTab(String title, Component... components) {
		super(components);
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void replace(Component... components) {
		removeAll();
		add(components);
	}
}
