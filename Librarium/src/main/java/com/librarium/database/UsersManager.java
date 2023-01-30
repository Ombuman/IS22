package com.librarium.database;

import java.util.ArrayList;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import com.librarium.controller.utility.DateUtility;
import com.librarium.database.generated.org.jooq.tables.Prestiti;
import com.librarium.database.generated.org.jooq.tables.Solleciti;
import com.librarium.database.generated.org.jooq.tables.Utenti;
import com.librarium.database.generated.org.jooq.tables.records.UtentiRecord;
import com.librarium.model.authentication.SignupInfo;
import com.librarium.model.entities.InfoProfiloUtente;
import com.librarium.model.entities.Prestito;
import com.librarium.model.entities.Sollecito;
import com.librarium.model.entities.Utente;
import com.librarium.model.enums.RuoloAccount;
import com.librarium.model.enums.StatoAccountUtente;

/**
 * La classe UsersManager è una classe che estende DatabaseConnection e serve a gestire le operazioni sugli utenti.
 * Utilizza il pattern singleton per creare un'unica istanza.
 */
public class UsersManager extends DatabaseConnection {
	
	/**
	 * Variabile statica per la gestione del pattern singleton.
	 */
	private static UsersManager instance;
	
	/**
	 * Metodo che restituisce l'unica istanza della classe. Se non esiste ancora, viene creata una nuova istanza.
	 * 
	 * @return L'istanza di UsersManager.
	*/
	public static UsersManager getInstance() {
		if(instance == null)
			instance = new UsersManager();
		
		return instance;
	}
	
	/**
	 * Metodo per l'aggiunta di un utente. Riceve come parametro i dati dell'utente da inserire e restituisce l'ID assegnato dal database.
	 * In caso di errori o di un parametro nullo, restituisce null.
	 * 
	 * @param datiUtente Dati dell'utente da inserire.
	 * @return ID dell'utente inserito o null in caso di errori o parametro nullo.
	 */
	public Integer aggiungiUtente(SignupInfo datiUtente) {
		if(datiUtente == null)
			return null;
		
		try{
			DSLContext ctx = DSL.using(connection, SQLDialect.SQLITE);
			
			Record1<Integer> result = ctx.insertInto(Utenti.UTENTI, Utenti.UTENTI.NOME, Utenti.UTENTI.COGNOME, Utenti.UTENTI.EMAIL, Utenti.UTENTI.PASSWORD, Utenti.UTENTI.STATO, Utenti.UTENTI.RUOLO)
					.values(datiUtente.getNome(), datiUtente.getCognome(), datiUtente.getEmail(), datiUtente.getEncryptedPassword(), StatoAccountUtente.ATTIVO.toString(), RuoloAccount.UTENTE.toString())
					.returningResult(Utenti.UTENTI.ID)
					.fetchOne();
			
			return result.get(Utenti.UTENTI.ID);
		} catch(Exception e) {
			return null;
		}
	}
	
	/**
	 * Questo metodo consente di ottenere l'utente corrispondente all'id specificato.
	 * Il risultato è un oggetto di tipo {@link Utente}.
	 * 
	 * @param idUtente l'id dell'utente da cercare
	 * @return l'utente corrispondente all'id specificato, oppure null se non esiste alcun utente con quell'id
	 */
	public Utente getUtente(Integer idUtente) {
		if(idUtente == null)
			return null;
		try{
			DSLContext ctx = DSL.using(connection, SQLDialect.SQLITE);
			
			Record result = ctx.select(
					Utenti.UTENTI.asterisk(), 
					DSL.selectCount().from(Prestiti.PRESTITI).where(Utenti.UTENTI.ID.eq(Prestiti.PRESTITI.UTENTE)).asField("nPrestiti"), 
					DSL.selectCount().from(Solleciti.SOLLECITI).where(Utenti.UTENTI.ID.eq(Solleciti.SOLLECITI.UTENTE)).asField("nSolleciti")
				)
				.from(Utenti.UTENTI)
				.where(Utenti.UTENTI.ID.eq(idUtente))
				.fetchOne();
			
			return result == null ? null : new Utente(result.into(Utenti.UTENTI), result.get("nPrestiti"), result.get("nSolleciti"));
			
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * Recupera tutti gli utenti presenti nel database.
	 * 
	 * @return La lista di tutti gli utenti presenti nel database, o null in caso di errore.
	 */
	public List<Utente> getUtenti(){
		return getUtenti(null, RuoloAccount.UTENTE.name());
	}
	
	/**
	 * Restituisce la lista di tutti gli utenti con stato account sospeso.
	 * 
	 * @return la lista di tutti gli utenti con stato account sospeso.
	 */
	public List<Utente> getUtentiSospesi(){
		return getUtenti(StatoAccountUtente.SOSPESO.name(), RuoloAccount.UTENTE.name());
	}
	
	/**
	 * Questo metodo ritorna una lista di {@link Utente} che rispettano i criteri di ricerca
	 * indicati come parametri di statoAccount e ruoloAccount. Se un parametro è null o vuoto,
	 * viene ignorato nella ricerca.
	 * 
	 * @param statoAccount lo stato dell'account dell'utente da cercare. Se null o vuoto, viene ignorato
	 * @param ruoloAccount il ruolo dell'account dell'utente da cercare. Se null o vuoto, viene ignorato
	 * @return una lista di {@link Utente} che rispettano i criteri di ricerca, oppure null in caso di errore
	 */
	public List<Utente> getUtenti(String statoAccount, String ruoloAccount) {
		try{
			
			Condition condition = DSL.noCondition();
			if(statoAccount != null && !statoAccount.isBlank())
				condition = condition.and(Utenti.UTENTI.STATO.eq(statoAccount));
			
			if(ruoloAccount != null && !ruoloAccount.isBlank())
				condition = condition.and(Utenti.UTENTI.RUOLO.eq(ruoloAccount));
			
			DSLContext ctx = DSL.using(connection, SQLDialect.SQLITE);
			
			Result<Record> result = 
					ctx.select(
						Utenti.UTENTI.asterisk(), 
						DSL.selectCount().from(Prestiti.PRESTITI).where(Utenti.UTENTI.ID.eq(Prestiti.PRESTITI.UTENTE)).asField("nPrestiti"), 
						DSL.selectCount().from(Solleciti.SOLLECITI).where(Utenti.UTENTI.ID.eq(Solleciti.SOLLECITI.UTENTE)).asField("nSolleciti")
					)
					.from(Utenti.UTENTI)
					.where(condition)
					.fetch();
			
			ArrayList<Utente> utenti = new ArrayList<>();
			result.forEach(utente -> {
				utenti.add(
					new Utente(utente.into(Utenti.UTENTI), utente.get("nPrestiti"), utente.get("nSolleciti"))
				);
			});
			
			return utenti;
			
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Setta lo stato di un account utente specificato dall'ID.
	 * 
	 * @param idUtente l'ID dell'utente di cui si vuole cambiare lo stato
	 * @param nuovoStato il nuovo stato dell'account utente
	 */
	public void setStatoAccount(Integer idUtente, String nuovoStato) {
		if(idUtente == null || nuovoStato == null || nuovoStato.isBlank())
			return;
		
		try{
			DSLContext ctx = DSL.using(connection, SQLDialect.SQLITE);
			
			ctx.update(Utenti.UTENTI)
				.set(Utenti.UTENTI.STATO, nuovoStato)
				.where(Utenti.UTENTI.ID.eq(idUtente))
				.execute();
			
		} catch(Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	/**
	 * Recupera lo stato corrente dell'account di un utente.
	 * 
	 * @param idUtente ID dell'utente per cui recuperare lo stato dell'account
	 * @return Stato dell'account dell'utente se presente, altrimenti null
	 */
	public StatoAccountUtente getStatoAccount(Integer idUtente) {
		try{
			DSLContext ctx = DSL.using(connection, SQLDialect.SQLITE);
			
			Result<Record1<String>> result = ctx.select(Utenti.UTENTI.STATO)
				.from(Utenti.UTENTI)
				.where(Utenti.UTENTI.ID.eq(idUtente))
				.fetch();
			
			return result.size() > 0 ? StatoAccountUtente.valueOf(result.get(0).component1()) : null;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Questo metodo serve per aggiornare i dati del profilo di un utente presente nel sistema.
	 * 
	 * @param idUtente L'identificativo univoco dell'utente.
	 * @param datiUtente I nuovi dati del profilo dell'utente.
	 * @return L'oggetto {@link UtentiRecord} con i nuovi dati del profilo dell'utente,
	 * se l'aggiornamento è stato eseguito correttamente. Null, altrimenti.
	 */
	public UtentiRecord aggiornaAccountUtente(Integer idUtente, InfoProfiloUtente datiUtente) {
		if(datiUtente == null)
			return null;
		
		try{
			DSLContext ctx = DSL.using(connection, SQLDialect.SQLITE);
			
			ctx.update(Utenti.UTENTI)
				.set(Utenti.UTENTI.NOME, datiUtente.getNome())
				.set(Utenti.UTENTI.COGNOME, datiUtente.getCognome())
				.where(Utenti.UTENTI.ID.eq(idUtente))
				.execute();
			
			Result<Record> result = ctx.select()
					.from(Utenti.UTENTI)
					.where(Utenti.UTENTI.ID.eq(idUtente))
					.fetch();
			
			return result.get(0).into(Utenti.UTENTI);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Invia un sollecito per un prestito specifico.
	 * 
	 * @param prestito Il prestito per cui inviare il sollecito.
	 */
	public void inviaSollecito(Prestito prestito) {
		if(prestito == null) 
			return;
		
		try{
			DSLContext ctx = DSL.using(connection, SQLDialect.SQLITE);
			
			String oggi = DateUtility.getDataOggi();
			
			ctx.insertInto(Solleciti.SOLLECITI, Solleciti.SOLLECITI.UTENTE, Solleciti.SOLLECITI.LIBRO, Solleciti.SOLLECITI.DATA)
				.values(prestito.getIdUtente(), prestito.getIdLibro(), oggi)
				.execute();
			
			// imposta la data dell'ultimo sollecito nel prestito
			PrestitiManager.getInstance().aggiornaDataUltimoSollecito(prestito.getId(), oggi);
			
		} catch(Exception e) {
			//e.printStackTrace();
			return;
		}
	}
	
	/**
	 * Recupera tutti i solleciti inviati ad un determinato utente.
	 * 
	 * @param idUtente Identificativo univoco dell'utente di cui si vogliono recuperare i solleciti.
	 * @return Una lista di oggetti {@link Sollecito} relativi ai solleciti inviati all'utente o null se l'id dell'utente non è valido.
	 */
	public List<Sollecito> getSollecitiUtente(Integer idUtente) {
		if(idUtente == null)
			return null;
		
		try{
			DSLContext ctx = DSL.using(connection, SQLDialect.SQLITE);
			
			Result<Record> result = ctx.select()
				.from(Solleciti.SOLLECITI)
				.where(Solleciti.SOLLECITI.UTENTE.eq(idUtente))
				.fetch();
			
			ArrayList<Sollecito> sollecitiUtente = new ArrayList<>();
			result.forEach(sollecito -> {
				sollecitiUtente.add(new Sollecito(sollecito.into(Solleciti.SOLLECITI)));
			});
			
			return sollecitiUtente;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Restituisce la lista di solleciti per un utente e un libro specifici.
	 * 
	 * @param idUtente l'identificativo dell'utente
	 * @param idLibro l'identificativo del libro
	 * @return la lista di solleciti per l'utente e il libro specificati, oppure null se c'è un errore o se i parametri sono nulli.
	 */
	public List<Sollecito> getSollecitiUtenteLibro(Integer idUtente, Integer idLibro) {
		if(idUtente == null || idLibro == null)
			return null;
		
		try{
			DSLContext ctx = DSL.using(connection, SQLDialect.SQLITE);
			
			Result<Record> result = ctx.select()
				.from(Solleciti.SOLLECITI)
				.where(Solleciti.SOLLECITI.UTENTE.eq(idUtente).and(Solleciti.SOLLECITI.LIBRO.eq(idLibro)))
				.fetch();
			
			ArrayList<Sollecito> sollecitiUtente = new ArrayList<>();
			result.forEach(sollecito -> {
				sollecitiUtente.add(new Sollecito(sollecito.into(Solleciti.SOLLECITI)));
			});
			
			return sollecitiUtente;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Rimuove i solleciti relativi ad un utente per un determinato libro.
	 * 
	 * @param idUtente ID dell'utente a cui sono associati i solleciti.
	 * @param idLibro ID del libro a cui sono associati i solleciti.
	 */
	public void rimuoviSolleciti(Integer idUtente, Integer idLibro) {
		if(idUtente == null || idLibro == null)
			return;
		
		try{
			DSLContext ctx = DSL.using(connection, SQLDialect.SQLITE);
			
			ctx.deleteFrom(Solleciti.SOLLECITI)
				.where(Solleciti.SOLLECITI.UTENTE.eq(idUtente).and(Solleciti.SOLLECITI.LIBRO.eq(idLibro)))
				.execute();
			
		} catch(Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
}
