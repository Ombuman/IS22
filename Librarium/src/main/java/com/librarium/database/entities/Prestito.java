package com.librarium.database.entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.librarium.application.utility.DateUtility;
import com.librarium.database.generated.org.jooq.tables.records.LibriRecord;
import com.librarium.database.generated.org.jooq.tables.records.PrestitiRecord;
import com.librarium.database.generated.org.jooq.tables.records.UtentiRecord;

public class Prestito {
	
	private PrestitiRecord dati;
	private UtentiRecord utente;
	private LibriRecord libro;
	
	public Prestito(PrestitiRecord dati, LibriRecord libro) {
		this(dati, null, libro);
	}
	
	public Prestito(PrestitiRecord dati, UtentiRecord utente, LibriRecord libro) {
		this.dati = dati;
		this.utente = utente;
		this.libro = libro;
	}

	// =================== Dati Prestito ================= //
	public PrestitiRecord getDati() {
		return dati;
	}
	
	public Integer getId() {
		return dati.getId();
	}
	
	public String getDataPrenotazione() {
		return dati.getDataPrenotazione();
	}
	
	public String getDataInizio() {
		return dati.getDataInizio();
	}
	
	public String getDataFine() {
		return dati.getDataFine();
	}
	
	public boolean isInvioSollecitoPossibile() {
		try {
			// controllo sono passati almeno 5 giorni dall'ultimo sollecito
			String dataUltimoSollecito = dati.getDataUltimoSollecito();
			if(dataUltimoSollecito != null && !dataUltimoSollecito.isBlank())
				return DateUtility.getDifferenzaGiorni(dataUltimoSollecito, DateUtility.getDataOggi()) >= 5;
				
			/*
			 *  Altrimenti se non esistono dei solleciti riguardanti il libro verifico che siano 
			 *  passati 30 giorni dall'inizio del prestito
			 */
			String dataOggi = DateUtility.getDataOggi();
			String dataInizio = getDataInizio();

			long giorniDopoScadenza = DateUtility.getDifferenzaGiorni(dataInizio, dataOggi);
			
			return giorniDopoScadenza > 30;
		} catch(Exception e) {
			return false;
		}
		
	}
	
	public String getDataUltimoSollecito() {
		String dataUltimoSollecito = dati.getDataUltimoSollecito();
		return (dataUltimoSollecito != null && !dataUltimoSollecito.isBlank()) ? dataUltimoSollecito : "-";
	}
	
	// =================== Dati Utente ================= //
	
	public UtentiRecord getUtente() {
		return utente;
	}
	
	public Integer getIdUtente() {
		return utente.getId();
	}
	
	public String getEmail() {
		return utente.getEmail();
	}
	
	public String getStatoUtente() {
		return utente.getStato();
	}
	
	// =================== Dati Libro ================= //
	
	public LibriRecord getLibro() {
		return libro;
	}
	
	public Integer getIdLibro() {
		return libro.getId();
	}
	
	public String getTitolo() {
		return libro.getTitolo();
	}
}
