package com.librarium.model.entities;

import com.librarium.database.generated.org.jooq.tables.records.SollecitiRecord;

public class Sollecito {
	
	private SollecitiRecord datiSollecito;
	
	public Sollecito(SollecitiRecord datiSollecito) {
		this.datiSollecito = datiSollecito;
	}
	
	public Integer getId() {
		return datiSollecito.getId();
	}
	
	public Integer getIdUtente() {
		return datiSollecito.getUtente();
	}
	
	public Integer getIdLibro() {
		return datiSollecito.getLibro();
	}
	
	public String getData() {
		return datiSollecito.getData();
	}
	
	public void setIdUtente(Integer idUtente) {
		datiSollecito.setUtente(idUtente);
	}
	
	public void setIdLibro(Integer idLibro) {
		datiSollecito.setLibro(idLibro);
	}
	
	public void setData(String data) {
		datiSollecito.setData(data);
	}
}
