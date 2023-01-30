package com.librarium.model.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.librarium.database.generated.org.jooq.tables.records.GeneriRecord;
import com.librarium.database.generated.org.jooq.tables.records.LibriRecord;
import com.librarium.model.enums.StatoLibro;

/**
 * Classe che rappresenta un libro, composto da un record di libri (libro) e da una lista di record di generi (generi).
 *
 */
public class Libro {
	
	private LibriRecord libro;
	private List<GeneriRecord> generi;
	
	/**
     * Costruttore di default che inizializza i membri libro e generi.
     */
	public Libro() {
		libro = new LibriRecord();
		generi = new ArrayList<>();
	}
	
	/**
     * Costruttore che inizializza i membri libro e generi con i valori passati come parametro.
     * 
     * @param libro Record di libri del libro.
     * @param generi Lista di record di generi del libro.
     */
	public Libro(LibriRecord libro, List<GeneriRecord> generi) {
		this.libro = libro;
		this.generi = generi;
	}

	 /**
     * Restituisce il record di libri del libro.
     * 
     * @return Record di libri del libro.
     */
	public LibriRecord getLibro() {
		return libro;
	}

	/**
     * Imposta il record di libri del libro.
     * 
     * @param libro Record di libri del libro.
     */
	public void setLibro(LibriRecord libro) {
		this.libro = libro;
	}
	
	/**
     * Restituisce l'insieme di record di generi del libro.
     * 
     * @return Insieme di record di generi del libro.
     */
	public Set<GeneriRecord> getGeneri() {
		Set<GeneriRecord> setGeneri = new HashSet<>(generi);
		return setGeneri;
	}

	/**
     * Imposta la lista di record di generi del libro.
     * 
     * @param generi Lista di record di generi del libro.
     */
	public void setGeneri(List<GeneriRecord> generi) {
		this.generi = generi;
	}
	
	 /**
     * Imposta la lista di record di generi del libro.
     * 
     * @param libro Libro di cui si vogliono impostare i generi.
     * @param generi Insieme di record di generi del libro.
     */
	public static void setGeneri(Libro libro, Set<GeneriRecord> generi) {
		libro.generi = new ArrayList<>(generi);
	}

	 /**
     * Restituisce una stringa che rappresenta il libro.
     * 
     * @return Stringa che rappresenta il libro.
     */
	@Override
	public String toString() {
		return "Libro [libro=" + libro + ", genere=" + generi.toArray().toString() + "]";
	}
	
	/**

	Restituisce l'ID del libro.
	@return L'ID del libro.
	*/
	public Integer getID() {
		return getLibro().getId();
	}
	
	/**

	Restituisce il titolo del libro.
	@return Il titolo del libro.
	*/
	public String getTitolo() {
		return getLibro().getTitolo();
	}
	
	/**

	Restituisce la descrizione del libro.
	@return La descrizione del libro.
	*/
	public String getDescrizione() {
		return getLibro().getDescrizione();
	}
	
	/**

	Restituisce il path della copertina del libro.
	@return Il path della copertina del libro.
	*/
	public String getCopertina() {
		return getLibro().getCopertina();
	}
	
	/**

	Restituisce l'autore del libro.
	@return L'autore del libro.
	*/
	public String getAutore() {
		return getLibro().getAutore();
	}
	
	/**

	Restituisce la casa editrice del libro.
	@return La casa editrice del libro.
	*/
	public String getCasaEditrice() {
		return getLibro().getCasaEditrice();
	}
	
	/**

	Restituisce l'anno di pubblicazione del libro.
	@return L'anno di pubblicazione del libro.
	*/
	public String getAnno() {
		return getLibro().getAnno();
	}
	
	/**

	Restituisce lo stato del libro.
	@return Lo stato del libro.
	*/
	public String getStato() {
		return getLibro().getStato();
	}
	
	/**

	Restituisce la lista dei generi del libro come stringa separata da virgole.
	@return La lista dei generi del libro come stringa separata da virgole.
	*/
	public String getGeneriString() {
		String ris = new String("");
		for(int i = 0; i < generi.size(); i++) {
			ris += generi.get(i).getNome();
			if(i < generi.size()-1)
				ris += ", ";
		}
		return ris;
	}
	
	/**

	Restituisce la lista degli ID dei generi del libro come stringa separata da virgole.
	@return La lista degli ID dei generi del libro come stringa separata da virgole.
	*/
	public String getGeneriID() {
		String ris = new String("");
		for(int i = 0; i < generi.size(); i++) {
			ris += generi.get(i).getId().toString();
			if(i < generi.size()-1)
				ris += ", ";
		}
		return ris;
	}
	
	/**

	Imposta l'ID del libro.
	@param id L'ID del libro.
	*/
	public void setId(Integer id) {
		if(id != null)
			getLibro().setId(id);
	}
	
	/**

	Imposta il titolo del libro.
	@param titolo Il titolo del libro.
	*/
	public void setTitolo(String titolo) {
		getLibro().setTitolo(titolo);
	}
	
	/**

	Imposta la descrizione del libro.
	@param descrizione descrizione del libro
	*/
	public void setDescrizione(String descrizione) {
		getLibro().setDescrizione(descrizione);
	}
	
	/**

	Imposta la copertina del libro.
	@param copertina copertina del libro
	*/
	public void setCopertina(String copertina) {
		getLibro().setCopertina(copertina);
	}
	
	/**

	Imposta l'autore del libro.
	@param autore autore del libro
	*/
	public void setAutore(String autore) {
		getLibro().setAutore(autore);
	}
	
	/**
	 * Imposta la casa editrice del libro.
	 * 
	 * @param casaEditrice Nome della casa editrice
	 */
	public void setCasaEditrice(String casaEditrice) {
		getLibro().setCasaEditrice(casaEditrice);
	}
	
	/**
	 * Imposta l'anno di pubblicazione del libro.
	 * 
	 * @param anno Anno di pubblicazione
	 */
	public void setAnno(String anno) {
		getLibro().setAnno(anno);
	}
	
	/**
	 * Imposta lo stato del libro come stringa.
	 * 
	 * @param stato Stato del libro
	 */
	public void setStato(String stato) {
		getLibro().setStato(stato);
	}
	
	/**
	 * Imposta lo stato del libro come enumerazione.
	 * 
	 * @param stato Stato del libro
	 */
	public void setStato(StatoLibro stato) {
		getLibro().setStato(stato.name());
	}
	
}
