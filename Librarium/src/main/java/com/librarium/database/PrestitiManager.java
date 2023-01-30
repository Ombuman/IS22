package com.librarium.database;

import java.util.ArrayList;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import com.librarium.controller.utility.DateUtility;
import com.librarium.database.generated.org.jooq.tables.Libri;
import com.librarium.database.generated.org.jooq.tables.Prestiti;
import com.librarium.database.generated.org.jooq.tables.Utenti;
import com.librarium.database.generated.org.jooq.tables.records.LibriRecord;
import com.librarium.database.generated.org.jooq.tables.records.UtentiRecord;
import com.librarium.model.entities.Prestito;
import com.librarium.model.enums.StatoAccountUtente;
import com.librarium.model.enums.StatoLibro;
import com.librarium.model.enums.StatoPrestito;

/**
 * La classe PrestitiManager estende la classe DatabaseConnection e si occupa di gestire i prestiti.
 * Ha metodi per recuperare i prestiti filtrati per id, stato o id dell'utente.
 * Viene utilizzato il pattern singleton per garantire che esista solo un'istanza della classe.
 */
public class PrestitiManager extends DatabaseConnection {

	/**
	 * Variabile statica per la gestione del pattern singleton.
	 */
	private static PrestitiManager instance;
	
	/**
	 * Metodo statico per ottenere l'istanza della classe PrestitiManager.
	 * Se l'istanza non esiste ancora, viene creata.
	 * 
	 * @return L'istanza unica della classe PrestitiManager.
	 */
	public static PrestitiManager getInstance() {
		if(instance == null)
			instance = new PrestitiManager();
		
		return instance;
	}
	
	/**
	 * Metodo per ottenere il prestito con l'id specificato.
	 * 
	 * @param idPrestito L'id del prestito da recuperare.
	 * @return Il prestito con l'id specificato o null se non esiste un prestito con quell'id.
	 */
	public Prestito getPrestito(Integer idPrestito) {		
		if(idPrestito == null)
			return null;
		
		try{
			DSLContext ctx = DSL.using(connection, SQLDialect.SQLITE);
			
			Record result = ctx.select()
				.from(Prestiti.PRESTITI)
				.join(Libri.LIBRI)
				.on(Prestiti.PRESTITI.LIBRO.eq(Libri.LIBRI.ID))
				.join(Utenti.UTENTI)
				.on(Prestiti.PRESTITI.UTENTE.eq(Utenti.UTENTI.ID))
				.where(Prestiti.PRESTITI.ID.eq(idPrestito))
				.orderBy(Prestiti.PRESTITI.DATA_PRENOTAZIONE.desc(), Prestiti.PRESTITI.ID.desc())
				.fetchOne();
			
			return new Prestito(result.into(Prestiti.PRESTITI), result.into(Utenti.UTENTI), result.into(Libri.LIBRI));
			
		} catch(Exception e){
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	/**
	 * Recupera un elenco di prestiti in base allo stato specificato.
	 * 
	 * @param stato Stato dei prestiti che si desidera recuperare (null per tutti).
	 * @return Una lista di prestiti o null in caso di errore.
	 */
	public ArrayList<Prestito> getPrestiti(String stato) {		
		try{
			DSLContext ctx = DSL.using(connection, SQLDialect.SQLITE);
			
			Condition condition = DSL.noCondition();
			if(stato != null && !stato.isBlank())
				condition = condition.and(Prestiti.PRESTITI.STATO.eq(stato));
			
			Result<Record> result = ctx.select()
				.from(Prestiti.PRESTITI)
				.join(Libri.LIBRI)
				.on(Prestiti.PRESTITI.LIBRO.eq(Libri.LIBRI.ID))
				.join(Utenti.UTENTI)
				.on(Prestiti.PRESTITI.UTENTE.eq(Utenti.UTENTI.ID))
				.and(condition)
				.orderBy(Prestiti.PRESTITI.DATA_PRENOTAZIONE.desc(), Prestiti.PRESTITI.ID.desc())
				.fetch();
			
			ArrayList<Prestito> prestiti = new ArrayList<>();
			result.forEach(prestito -> {
				prestiti.add(new Prestito(
					prestito.into(Prestiti.PRESTITI),
					prestito.into(Utenti.UTENTI),
					prestito.into(Libri.LIBRI)
				));
			});
			
			return prestiti;
			
		} catch(Exception e){
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	/**
	 * Restituisce la lista di prestiti per un determinato utente. Se l'utente non ha prestiti o l'ID è invalido, restituisce null.
	 * 
	 * @param idUtente l'ID dell'utente per cui si vogliono i prestiti
	 * @return la lista dei prestiti dell'utente, null altrimenti
	 */
	public ArrayList<Prestito> getPrestitiUtente(int idUtente) {
		return getPrestitiUtente(idUtente, null);
	}
	
	/**
	 * Restituisce la lista di prestiti per un determinato utente e con un determinato stato.
	 * Se l'utente non ha prestiti con quell'eventuale stato o l'ID è invalido, restituisce null.
	 * 
	 * @param idUtente l'ID dell'utente per cui si vogliono i prestiti
	 * @param stato lo stato dei prestiti che si vogliono ottenere (può essere null)
	 * @return la lista dei prestiti dell'utente con lo stato specificato, null altrimenti
	 */
	public ArrayList<Prestito> getPrestitiUtente(int idUtente, String stato) {		
		try{
			DSLContext ctx = DSL.using(connection, SQLDialect.SQLITE);
			
			Condition condition = DSL.noCondition();
			if(stato != null && !stato.isBlank())
				condition = condition.and(Prestiti.PRESTITI.STATO.eq(stato));
			
			Result<Record> result = ctx.select()
				.from(Prestiti.PRESTITI)
				.join(Libri.LIBRI)
				.on(Prestiti.PRESTITI.LIBRO.eq(Libri.LIBRI.ID))
				.where(Prestiti.PRESTITI.UTENTE.eq(idUtente))
				.and(condition)
				.orderBy(Prestiti.PRESTITI.DATA_PRENOTAZIONE.desc(), Prestiti.PRESTITI.ID.desc())
				.fetch();
			
			ArrayList<Prestito> prestitiUtente = new ArrayList<>();
			result.forEach(prestito -> {
				prestitiUtente.add(new Prestito(
					prestito.into(Prestiti.PRESTITI),
					prestito.into(Libri.LIBRI)
				));
			});
			
			return prestitiUtente;
			
		} catch(Exception ex){
			System.out.println(ex.getMessage());
			return null;
		}
	}
	
	/**
	 * Crea un prestito per un utente e un libro specificati.
	 * 
	 * @param utente L'oggetto {@link UtentiRecord} che rappresenta l'utente che vuole prenotare il libro.
	 * @param libro L'oggetto {@link LibriRecord} che rappresenta il libro che viene prenotato.
	 * @return Un intero che rappresenta l'ID del prestito appena creato, o null se non è stato possibile creare il prestito.
	 */
	public Integer creaPrestito(UtentiRecord utente, LibriRecord libro) {
		// verifico che i dati inseriti non siano nulli
		if(utente == null || libro == null)
			return null;
		
		try{
			// controllo se l'utente non è sospeso
			if(UsersManager.getInstance().getStatoAccount(utente.getId()) == StatoAccountUtente.SOSPESO)
				return null;
			
			// verifico se il libro sia realmente disponibile
			if(StatoLibro.valueOf(libro.getStato()) == StatoLibro.NON_DISPONIBILE)
				return null;
			
			String oggi = DateUtility.getDataOggi();
			
			//aggiungi il prestito
			DSLContext ctx = DSL.using(connection, SQLDialect.SQLITE);
			Record1<Integer> result = ctx.insertInto(Prestiti.PRESTITI, Prestiti.PRESTITI.DATA_PRENOTAZIONE, Prestiti.PRESTITI.STATO, Prestiti.PRESTITI.LIBRO, Prestiti.PRESTITI.UTENTE)
				.values(oggi, StatoPrestito.PRENOTATO.toString(), libro.getId(), utente.getId())
				.returningResult(Prestiti.PRESTITI.ID)
				.fetchOne();
			
			// aggiorna lo stato del libro
			CatalogManager.getInstance().aggiornaStatoLibro(libro.getId(), StatoLibro.NON_DISPONIBILE);
			
			return result.getValue(Prestiti.PRESTITI.ID);
			
		} catch(Exception ex){
			System.out.println(ex.getMessage());
			return null;
		}
	}
	
	/**
	 * Questo metodo annulla una prenotazione di un libro.
	 * 
	 * @param prestito L'oggetto Prestito che rappresenta la prenotazione da annullare
	 * @return true se l'annullamento è avvenuto con successo, false altrimenti
	 */
	public boolean annullaPrenotazione(Prestito prestito){
		if(prestito == null)
			return false;
		
		try{
			DSLContext ctx = DSL.using(connection, SQLDialect.SQLITE);
			
			ctx.deleteFrom(Prestiti.PRESTITI)
				.where(Prestiti.PRESTITI.ID.eq(prestito.getDati().getId()))
				.and(Prestiti.PRESTITI.STATO.eq(StatoPrestito.PRENOTATO.name()))
				.execute();
			
			CatalogManager.getInstance().aggiornaStatoLibro(prestito.getLibro().getId(), StatoLibro.DISPONIBILE);
			
			return true;
			
		} catch(Exception e){
			return false;
		}
	}
	
	/**
	 * Rimuove tutti i prestiti associati ad un libro specifico.
	 * 
	 * @param idLibro id del libro per il quale cancellare i prestiti
	 */
	public void rimuoviPrestiti(int idLibro) {
		try{
			DSLContext ctx = DSL.using(connection, SQLDialect.SQLITE);
			
			ctx.deleteFrom(Prestiti.PRESTITI)
				.where(Prestiti.PRESTITI.LIBRO.eq(idLibro))
				.execute();
			
		} catch(Exception e){
			System.out.println(e);
		}
	}
	
	/**
	 * Attiva un prestito, impostando lo stato del prestito come 'RITIRATO' e la data di inizio come la data odierna.
	 * 
	 * @param prestito Il prestito da attivare.
	 */
	public void attivaPrestito(Prestito prestito) {
		if(prestito == null)
			return;
		
		try{
			DSLContext ctx = DSL.using(connection, SQLDialect.SQLITE);
			
			String nuovoStato = StatoPrestito.RITIRATO.name();
			String oggi = DateUtility.getDataOggi();
			
			ctx.update(Prestiti.PRESTITI)
				.set(Prestiti.PRESTITI.STATO, nuovoStato)
				.set(Prestiti.PRESTITI.DATA_INIZIO, oggi)
				.where(Prestiti.PRESTITI.ID.eq(prestito.getId()))
				.execute();
			
		} catch(Exception e){
			System.out.println(e);
		}
	}
	
	/**
	 * Conclude un prestito cambiando il suo stato in "CONCLUSO" e impostando la data di fine con la data di oggi.
	 * Aggiorna anche lo stato del libro a "DISPONIBILE" e rimuove tutti i solleciti collegati a questo prestito.
	 * 
	 * @param prestito {@link Prestito} Il prestito da concludere
	 */
	public void concludiPrestito(Prestito prestito) {
		if(prestito == null)
			return;
		
		try{
			DSLContext ctx = DSL.using(connection, SQLDialect.SQLITE);
			
			String nuovoStato = StatoPrestito.CONCLUSO.name();
			String oggi = DateUtility.getDataOggi();
			
			ctx.update(Prestiti.PRESTITI)
				.set(Prestiti.PRESTITI.STATO, nuovoStato)
				.set(Prestiti.PRESTITI.DATA_FINE, oggi)
				.where(Prestiti.PRESTITI.ID.eq(prestito.getId()))
				.execute();
			
			// aggiorna lo stato del libro
			CatalogManager.getInstance().aggiornaStatoLibro(prestito.getLibro().getId(), StatoLibro.DISPONIBILE);
			
			// rimuovi tutti i solleciti collegati a questo prestito
			UsersManager.getInstance().rimuoviSolleciti(prestito.getIdUtente(), prestito.getIdLibro());
			
		} catch(Exception e){
			System.out.println(e);
		}
	}

	/**
	 * Aggiorna la data dell'ultimo sollecito per un prestito
	 * 
	 * @param id ID del prestito
	 * @param oggi Data dell'ultimo sollecito (oggi)
	 */
	public void aggiornaDataUltimoSollecito(Integer id, String oggi) {		
		try{
			DSLContext ctx = DSL.using(connection, SQLDialect.SQLITE);
			
			ctx.update(Prestiti.PRESTITI)
				.set(Prestiti.PRESTITI.DATA_ULTIMO_SOLLECITO, oggi)
				.where(Prestiti.PRESTITI.ID.eq(id))
				.execute();
			
		} catch(Exception e){
			System.out.println(e);
		}
	}
	
}
