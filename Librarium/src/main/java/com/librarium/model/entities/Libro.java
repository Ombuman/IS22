package com.librarium.model.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.librarium.database.generated.org.jooq.tables.records.GeneriRecord;
import com.librarium.database.generated.org.jooq.tables.records.LibriRecord;

public class Libro {
	
	private LibriRecord libro;
	private List<GeneriRecord> generi;
	
	public Libro() {
		libro = new LibriRecord();
		generi = new ArrayList<>();
	}
	
	public Libro(LibriRecord libro, List<GeneriRecord> generi) {
		this.libro = libro;
		this.generi = generi;
	}

	public LibriRecord getLibro() {
		return libro;
	}

	public void setLibro(LibriRecord libro) {
		this.libro = libro;
	}
	
	public Set<GeneriRecord> getGeneri() {
		Set<GeneriRecord> setGeneri = new HashSet<>(generi);
		return setGeneri;
	}

	public void setGeneri(List<GeneriRecord> generi) {
		this.generi = generi;
	}
	
	public static void setGeneri(Libro libro, Set<GeneriRecord> generi) {
		libro.generi = new ArrayList<>(generi);
	}

	@Override
	public String toString() {
		return "Libro [libro=" + libro + ", genere=" + generi.toArray().toString() + "]";
	}
	
	public String getTitolo() {
		return getLibro().getTitolo();
	}
	
	public String getDescrizione() {
		return getLibro().getDescrizione();
	}
	
	public String getCopertina() {
		return getLibro().getCopertina();
	}
	
	public String getAutore() {
		return getLibro().getAutore();
	}
	
	public String getCasaEditrice() {
		return getLibro().getCasaEditrice();
	}
	
	public String getAnno() {
		return getLibro().getAnno();
	}
	
	public String getStato() {
		return getLibro().getStato();
	}
	
	public String getGeneriString() {
		String ris = new String("");
		for(int i = 0; i < generi.size(); i++) {
			ris += generi.get(i).getNome();
			if(i < generi.size()-1)
				ris += ", ";
		}
		return ris;
	}
	
	public String getGeneriID() {
		String ris = new String("");
		for(int i = 0; i < generi.size(); i++) {
			ris += generi.get(i).getId().toString();
			if(i < generi.size()-1)
				ris += ", ";
		}
		return ris;
	}
	
	public void setTitolo(String titolo) {
		getLibro().setTitolo(titolo);
	}
	
	public void setDescrizione(String descrizione) {
		getLibro().setDescrizione(descrizione);
	}
	
	public void setCopertina(String copertina) {
		getLibro().setCopertina(copertina);
	}
	
	public void setAutore(String autore) {
		getLibro().setAutore(autore);
	}
	
	public void setCasaEditrice(String casaEditrice) {
		getLibro().setCasaEditrice(casaEditrice);
	}
	
	public void setAnno(String anno) {
		getLibro().setAnno(anno);
	}
	
	public void setStato(String stato) {
		getLibro().setStato(stato);
	}
	
}
