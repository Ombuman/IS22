package com.librarium.database.generated.org.jooq.tables.records;

public class LibriCompletiRecord  {
	
	private LibriRecord libro;
	private AutoriRecord autore;
	private CaseeditriciRecord casaEditrice;
	
	public LibriCompletiRecord(Object libro, Object autore, Object casaEditrice) {
		this.libro = (LibriRecord)libro;
		this.autore = (AutoriRecord)autore;
		this.casaEditrice = (CaseeditriciRecord)casaEditrice;
	}
	
	@Override
	public String toString() {
		return libro.toString() + "\n" + autore.toString() + "\n" + casaEditrice.toString();
	}

	public LibriRecord getLibro() {
		return libro;
	}

	public AutoriRecord getAutore() {
		return autore;
	}
	
	public CaseeditriciRecord getCasaEditrice() {
		return casaEditrice;
	}

}