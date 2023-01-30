package com.librarium.model.entities;

import com.librarium.controller.utility.DateUtility;
import com.librarium.database.generated.org.jooq.tables.records.LibriRecord;
import com.librarium.database.generated.org.jooq.tables.records.PrestitiRecord;
import com.librarium.database.generated.org.jooq.tables.records.UtentiRecord;
import com.librarium.model.enums.StatoPrestito;

/**

La classe Prestito rappresenta un prestito effettuato da un utente.
Contiene informazioni sul prestito, l'utente e il libro.

*/
class Prestito {
	
	private PrestitiRecord dati;
	private UtentiRecord utente;
	private LibriRecord libro;
	
	/**

	Costruttore che crea un oggetto Prestito a partire dai dati del prestito e del libro prestato.
	@param dati Dati del prestito
	@param libro Libro prestato
	*/
	public Prestito(PrestitiRecord dati, LibriRecord libro) {
		this(dati, null, libro);
	}
	
	/**

	Costruttore che crea un oggetto Prestito a partire dai dati del prestito, dell'utente e del libro prestato.
	@param dati Dati del prestito
	@param utente Utente che ha effettuato il prestito
	@param libro Libro prestato
	*/
	public Prestito(PrestitiRecord dati, UtentiRecord utente, LibriRecord libro) {
		this.dati = dati;
		this.utente = utente;
		this.libro = libro;
	}

	// =================== Dati Prestito ================= //
	
	/**

	Restituisce i dati del prestito.
	@return Dati del prestito
	*/
	public PrestitiRecord getDati() {
		return dati;
	}
	
	/**

	Restituisce l'identificatore univoco del prestito.
	@return Identificatore univoco del prestito
	*/
	public Integer getId() {
		return dati.getId();
	}
	
	/**

	Restituisce la data di prenotazione del prestito.
	@return Data di prenotazione del prestito
	*/
	public String getDataPrenotazione() {
		return dati.getDataPrenotazione();
	}
	
	/**

	Restituisce la data di inizio del prestito.
	@return Data di inizio del prestito
	*/
	public String getDataInizio() {
		return dati.getDataInizio();
	}
	
	/**

	Restituisce la data di fine del prestito.
	@return Data di fine del prestito
	*/
	public String getDataFine() {
		return dati.getDataFine();
	}
	
	/**

	Restituisce lo stato del prestito.
	@return Stato del prestito
	*/
	public StatoPrestito getStato() {
		return StatoPrestito.valueOf(dati.getStato());
	}
	
	/**

	Verifica se è possibile inviare un sollecito per il prestito.
	@return True se è possibile inviare un sollecito, False altrimenti
	*/
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
	
	/**

	Restituisce la data dell'ultimo sollecito inviato per il prestito.
	@return Data dell'ultimo sollecito inviato per il prestito
	*/
	public String getDataUltimoSollecito() {
		String dataUltimoSollecito = dati.getDataUltimoSollecito();
		return (dataUltimoSollecito != null && !dataUltimoSollecito.isBlank()) ? dataUltimoSollecito : "-";
	}
	
	// =================== Dati Utente ================= //
	
	/**

	Restituisce i dati dell'utente che ha effettuato il prestito.
	@return Dati dell'utente che ha effettuato il prestito
	*/
	public UtentiRecord getUtente() {
		return utente;
	}
	
	/**

	Ritorna l'ID dell'utente associato a questo prestito.
	@return ID dell'utente.
	*/
	public Integer getIdUtente() {
		return utente.getId();
	}
	
	/**

	Ritorna l'email dell'utente associato a questo prestito.
	@return email dell'utente.
	*/
	public String getEmail() {
		return utente.getEmail();
	}
	
	/**

	Ritorna lo stato dell'utente associato a questo prestito.
	@return stato dell'utente.
	*/
	public String getStatoUtente() {
		return utente.getStato();
	}
	
	// =================== Dati Libro ================= //
	
	/**

	Ritorna l'oggetto che rappresenta il libro associato a questo prestito.
	@return {@link LibriRecord} che rappresenta il libro associato a questo prestito.
	*/
	public LibriRecord getLibro() {
		return libro;
	}
	
	/**

	Ritorna l'ID del libro associato a questo prestito.
	@return ID del libro associato a questo prestito.
	*/
	public Integer getIdLibro() {
		return libro.getId();
	}
	
	/**

	Ritorna il titolo del libro associato a questo prestito.
	@return titolo del libro associato a questo prestito.
	*/
	public String getTitolo() {
		return libro.getTitolo();
	}
}
