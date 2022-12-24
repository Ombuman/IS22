package com.librarium.database.generated.org.jooq.tables.records;

import java.util.Date;

public class LibriCompletiRecord  {
	
	private Integer id;
	private String titolo;
	private String copertina;
	private String anno;
	private String categoria;
	private AutoriRecord autore;
	private Integer casaEditrice;
	
	public LibriCompletiRecord(Object id, Object titolo, Object copertina, Object anno, Object categoria, Object autore,
			Object casaEditrice) {
		this.id = (Integer)id;
		this.titolo = (String)titolo;
		this.copertina = (String)copertina;
		this.anno = (String)anno;
		this.categoria = (String)categoria;
		this.autore = (AutoriRecord)autore;
		this.casaEditrice = (Integer)casaEditrice;
	}
	
	@Override
	public String toString() {
		return "LibriCompletiRecord [id=" + id + ", titolo=" + titolo + ", copertina=" + copertina + ", categoria="
				+ categoria + ", autore=" + autore + ", casaEditrice=" + casaEditrice + "]";
	}

	public Integer getId() {
		return id;
	}

	public String getTitolo() {
		return titolo;
	}

	public String getCopertina() {
		return copertina;
	}
	
	public String getAnno() {
		return anno;
	}

	public String getCategoria() {
		return categoria;
	}

	public AutoriRecord getAutore() {
		return autore;
	}

	public Integer getCasaEditrice() {
		return casaEditrice;
	}

}