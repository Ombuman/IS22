package com.librarium.model.entities;

import com.librarium.database.generated.org.jooq.tables.records.SollecitiRecord;
/**

La classe Sollecito rappresenta un sollecito.

*/
public class Sollecito {
	/**

	Oggetto che rappresenta i dati del sollecito.
	*/
	private SollecitiRecord datiSollecito;
	
	/**

	Costruttore che inizializza i dati del sollecito.
	@param datiSollecito Oggetto che rappresenta i dati del sollecito.
	*/
	public Sollecito(SollecitiRecord datiSollecito) {
		this.datiSollecito = datiSollecito;
	}
	
	/**

	Restituisce l'ID del sollecito.
	@return L'ID del sollecito.
	*/
	public Integer getId() {
		return datiSollecito.getId();
	}
	
	/**

	Restituisce l'ID dell'utente associato al sollecito.
	@return L'ID dell'utente associato al sollecito.
	*/
	public Integer getIdUtente() {
		return datiSollecito.getUtente();
	}
	
	/**

	Restituisce l'ID del libro associato al sollecito.
	@return L'ID del libro associato al sollecito.
	*/
	public Integer getIdLibro() {
		return datiSollecito.getLibro();
	}
	
	/**

	Restituisce la data del sollecito.
	@return La data del sollecito.
	*/
	public String getData() {
		return datiSollecito.getData();
	}
	
	/**

	Imposta l'ID dell'utente associato al sollecito.
	@param idUtente L'ID dell'utente associato al sollecito.
	*/
	public void setIdUtente(Integer idUtente) {
		datiSollecito.setUtente(idUtente);
	}
	
	/**

	Imposta l'ID del libro associato al sollecito.
	@param idLibro L'ID del libro associato al sollecito.
	*/
	public void setIdLibro(Integer idLibro) {
		datiSollecito.setLibro(idLibro);
	}
	
	/**

	Imposta la data del sollecito.
	@param data La data del sollecito.
	*/
	public void setData(String data) {
		datiSollecito.setData(data);
	}
}
