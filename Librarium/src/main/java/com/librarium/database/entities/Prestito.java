package com.librarium.database.entities;

import com.librarium.database.generated.org.jooq.tables.records.LibriRecord;
import com.librarium.database.generated.org.jooq.tables.records.PrestitiRecord;

public class Prestito {
	
	private PrestitiRecord dati;
	private LibriRecord libro;
	
	public Prestito(PrestitiRecord dati, LibriRecord libro) {
		this.dati = dati;
		this.libro = libro;
	}

	public PrestitiRecord getDati() {
		return dati;
	}
	
	public LibriRecord getLibro() {
		return libro;
	}
}
