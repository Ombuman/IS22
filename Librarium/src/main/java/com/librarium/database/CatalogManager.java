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

import com.librarium.database.generated.org.jooq.tables.Generi;
import com.librarium.database.generated.org.jooq.tables.Libri;
import com.librarium.database.generated.org.jooq.tables.records.GeneriRecord;
import com.librarium.database.generated.org.jooq.tables.records.LibriRecord;
import com.librarium.model.entities.Libro;
import com.librarium.model.enums.StatoLibro;

/**
 * La classe CatalogManager estende la classe {@link DatabaseConnection} e fornisce metodi per gestire il catalogo di libri.
 * 
 * E' implementata come singleton.
 */
public class CatalogManager extends DatabaseConnection{

	/**
	 * Variabile statica che rappresenta l'istanza unica del catalog manager.
	 */
	private static CatalogManager instance;
	
	/**
	 * Metodo che ritorna l'istanza unica del catalog manager.
	 * 
	 * @return l'istanza unica del catalog manager.
	 */
	public static CatalogManager getInstance() {
		if(instance == null)
			instance = new CatalogManager();
		
		return instance;
	}
	
	/**
	 * Metodo che restituisce la lista dei libri in base ai filtri specificati.
	 * 
	 * @param filtroParole parole da filtrare nei titoli e autori dei libri.
	 * @param Generi filtro sui generi dei libri.
	 * @param casaEditrice filtro sulla casa editrice dei libri.
	 * @return lista di libri che soddisfano i filtri specificati.
	 */
	public List<Libro> leggiLibri(String filtroParole, GeneriRecord Generi, String casaEditrice){
		return leggiLibri(filtroParole, Generi.getId().toString(), casaEditrice);
	}
	
	/**
	 * Metodo che restituisce la lista dei libri in base ai filtri specificati.
	 * 
	 * @param filtroParole parole da filtrare nei titoli e autori dei libri.
	 * @param filtroGeneri filtro sui generi dei libri.
	 * @param casaEditrice filtro sulla casa editrice dei libri.
	 * @return lista di libri che soddisfano i filtri specificati.
	 */
	public List<Libro> leggiLibri(String filtroParole, String filtroGeneri, String casaEditrice) {
		try{
			DSLContext ctx = DSL.using(connection, SQLDialect.SQLITE);
			
			Condition condition = DSL.noCondition();
			
			if(filtroParole != null && !filtroParole.isBlank()) {
				condition = condition.and(
					Libri.LIBRI.TITOLO.contains(filtroParole)
					.or(Libri.LIBRI.AUTORE.contains(filtroParole))
				);
			}
			if(filtroGeneri != null && !filtroGeneri.isBlank())
				condition = condition.and(Libri.LIBRI.GENERE.contains(filtroGeneri));
			if(casaEditrice != null && !casaEditrice.isBlank())
				condition = condition.and(Libri.LIBRI.CASA_EDITRICE.eq(casaEditrice));
			
			Result<Record> result = 
					ctx.select()
					.from(Libri.LIBRI)
					.where(condition)
					.orderBy(Libri.LIBRI.TITOLO)
					.fetch();
			
			return listaRecordToLibri(result);
		} catch(Exception ex){
			System.out.println(ex.getMessage());
			return null;
		}
	}
	
	/**
	 * Questo metodo converte una lista di oggetti {@link Record} in una lista di oggetti {@link Libro}.
	 * 
	 * @param listaRecord la lista di oggetti Record da convertire
	 * @return la lista di oggetti Libro convertiti
	 */
	private List<Libro> listaRecordToLibri(Result<Record> result) {
		List<GeneriRecord> listaGeneri = leggiGeneri();
		List<Libro> libri = new ArrayList<>();
		
		result.forEach(libro -> {
			LibriRecord recordLibro = libro.into(Libri.LIBRI);
			List<GeneriRecord> generi = listaGeneri.stream().filter(genere -> recordLibro.getGenere().contains(genere.getId().toString())).toList();
			libri.add(new Libro(recordLibro, generi));
		});
		
		return libri;
	}
	
	/**
	 * Questo metodo viene utilizzato per convertire un singolo record del database in un oggetto di tipo {@link Libro}.
	 * @param record Il record da convertire
	 */
	private Libro recordToLibro(Record result) {
		if(result == null)
			return null;
		
		List<GeneriRecord> listaGeneri = leggiGeneri();
		
		LibriRecord recordLibro = result.into(Libri.LIBRI);
		List<GeneriRecord> generi = listaGeneri.stream().filter(genere -> recordLibro.getGenere().contains(genere.getId().toString())).toList();
		return new Libro(recordLibro, generi);
	}
	
	/**
	 * Questo metodo consente di leggere i dettagli di un libro selezionato dalla lista dei libri.
	 * Il metodo richiede come parametro l'ID del libro selezionato e restituisce un oggetto Libro
	 * che rappresenta i dettagli del libro richiesto.
	 * 
	 * @param idLibro ID del libro selezionato per la lettura dei dettagli
	 * @return Oggetto Libro che rappresenta i dettagli del libro richiesto
	 */
	public Libro leggiLibro(Integer idLibro) {
		if(idLibro == null)
			return null;
		
		try{
			DSLContext ctx = DSL.using(connection, SQLDialect.SQLITE);
			
			Record result = 
			ctx.select()
				.from(Libri.LIBRI)
				.where(Libri.LIBRI.ID.eq(idLibro))
				.fetchOne();
			
			return recordToLibro(result);
			
		} catch(Exception ex){
			System.out.println(ex.getMessage());
			return null;
		}
	}
	
	/**
	 * Questo metodo inserisce un nuovo libro nel database.
	 * 
	 * @param libro Il libro da inserire nel database
	 * @return L'ID del libro appena inserito, oppure null se si è verificato un errore durante l'inserimento
	 */
	public Integer aggiungiLibro(Libro libro) {
		if(libro == null)
			return null;
		
		libro.setStato(StatoLibro.DISPONIBILE.name());
		
		try{
			DSLContext ctx = DSL.using(connection, SQLDialect.SQLITE);
			
			Record1<Integer> result = ctx.insertInto(Libri.LIBRI, 
				Libri.LIBRI.TITOLO, Libri.LIBRI.DESCRIZIONE, Libri.LIBRI.COPERTINA, Libri.LIBRI.AUTORE, Libri.LIBRI.CASA_EDITRICE, Libri.LIBRI.ANNO, Libri.LIBRI.GENERE, Libri.LIBRI.STATO)
			.values(libro.getTitolo(), libro.getDescrizione(), libro.getCopertina(), libro.getAutore(), libro.getCasaEditrice(), libro.getAnno(), libro.getGeneriID(), libro.getStato())
			.returningResult(Libri.LIBRI.ID)
			.fetchOne();
			
			// ritorna l'id del libro inserito
			return result.getValue(Libri.LIBRI.ID);
		} catch(Exception ex){
			System.out.println(ex.getMessage());
			return null;
		}
	}
	
	/**
	 * Rimuove il libro dal database a partire dal suo ID.
	 * 
	 * @param idLibro l'ID del libro da rimuovere
	 */
	public void rimuoviLibro(Integer idLibro) {
		if(idLibro == null)
			return;
		
		try{
			DSLContext ctx = DSL.using(connection, SQLDialect.SQLITE);
			
			ctx.deleteFrom(Libri.LIBRI)
				.where(Libri.LIBRI.ID.eq(idLibro))
				.execute();
			
			// dopo aver rimosso il libro rimuovi anche tutti i prestiti con tale libro collegato
			PrestitiManager.getInstance().rimuoviPrestiti(idLibro);
			
		} catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	/**
	 * Aggiorna un libro esistente nel database.
	 * 
	 * @param libro Il libro da aggiornare.
	 */
	public void aggiornaLibro(Libro libro) {
		try{
			DSLContext ctx = DSL.using(connection, SQLDialect.SQLITE);
			
			ctx.update(Libri.LIBRI)
				.set(Libri.LIBRI.TITOLO, libro.getTitolo())
				.set(Libri.LIBRI.GENERE, libro.getGeneriID())
				.set(Libri.LIBRI.DESCRIZIONE, libro.getDescrizione())
				.set(Libri.LIBRI.COPERTINA, libro.getCopertina())
				.set(Libri.LIBRI.AUTORE, libro.getAutore())
				.set(Libri.LIBRI.CASA_EDITRICE, libro.getCasaEditrice())
				.set(Libri.LIBRI.ANNO, libro.getAnno())
				.where(Libri.LIBRI.ID.eq(libro.getLibro().getId()))
				.execute();
		} catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	/**
	 * Aggiorna lo stato del libro identificato dall'ID fornito con il nuovo stato fornito.
	 * 
	 * @param idLibro L'ID del libro da aggiornare
	 * @param nuovoStato Il nuovo stato del libro
	 */
	public void aggiornaStatoLibro(Integer idLibro, StatoLibro nuovoStato) {
		try{
			DSLContext ctx = DSL.using(connection, SQLDialect.SQLITE);
			
			ctx.update(Libri.LIBRI)
				.set(Libri.LIBRI.STATO, nuovoStato.name())
				.where(Libri.LIBRI.ID.eq(idLibro))
				.execute();
		} catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	/**
	 * Legge il genere a partire dall'ID fornito.
	 * 
	 * @param idGenere ID del genere da leggere.
	 * @return Il record del genere letto dal database, null in caso di errore o se l'ID è null.
	 */
	public GeneriRecord leggiGenere(Integer idGenere) {
		if(idGenere == null)
			return null;
		
		try{
			DSLContext ctx = DSL.using(connection, SQLDialect.SQLITE);
			Record result = 
				ctx.select()
					.from(Generi.GENERI)
					.where(Generi.GENERI.ID.eq(idGenere))
					.fetchOne();
			
			return result.into(Generi.GENERI);
		} catch(Exception ex){
			return null;
		}
	}
	
	/**
	 * Questo metodo legge tutti i generi dal database e li restituisce in una lista.
	 * 
	 * @return una lista di {@link GeneriRecord} che rappresentano i generi presenti nel database.
	 * Se non ci sono generi o c'è stato un errore durante l'operazione, viene restituito null.
	 */
	public List<GeneriRecord> leggiGeneri() {
		try{
			DSLContext ctx = DSL.using(connection, SQLDialect.SQLITE);
			Result<Record> result = 
				ctx.select()
					.from(Generi.GENERI)
					.orderBy(Generi.GENERI.NOME)
					.fetch();
			
			ArrayList<GeneriRecord> generi = new ArrayList<GeneriRecord>();
			
			result.forEach(genere -> generi.add(genere.into(Generi.GENERI)));
			
			return generi;
		} catch(Exception ex){
			System.out.println(ex.getMessage());
			return null;
		}
	}
	/**
	 * Aggiunge un nuovo genere al database.
	 * 
	 * @param genere Il record che rappresenta il nuovo genere da aggiungere.
	 * @return L'id del genere inserito, null se si è verificato un errore durante l'inserimento.
	 */
	public Integer aggiungiGenere(GeneriRecord genere) {
		if(genere == null)
			return null;
		
		try{
			DSLContext ctx = DSL.using(connection, SQLDialect.SQLITE);
			
			Record1<Integer> result = ctx.insertInto(Generi.GENERI, Generi.GENERI.NOME)
			.values(genere.getNome())
			.returningResult(Generi.GENERI.ID)
			.fetchOne();
			
			// ritorna l'id del libro inserito
			return result.getValue(Generi.GENERI.ID);
		} catch(Exception ex){
			System.out.println(ex.getMessage());
			return null;
		}
	}
	
	/**
	 * Rimuove il genere identificato dall'ID specificato.
	 * 
	 * @param idGenere ID del genere da rimuovere
	 */
	public void rimuoviGenere(Integer idGenere){		
		try{
			DSLContext ctx = DSL.using(connection, SQLDialect.SQLITE);
			
			ctx.deleteFrom(Generi.GENERI)
				.where(Generi.GENERI.ID.eq(idGenere))
				.execute();
			
		} catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	/**
	 * Metodo per ottenere il numero di libri appartenenti ad un determinato genere.
	 * 
	 * @param idGenere L'identificativo del genere.
	 * @return Il numero di libri appartenenti al genere o null se l'id del genere non è valido o se si verifica un errore durante l'esecuzione.
	 */
	public Integer getNumeroLibriGenere(Integer idGenere) {
		if(idGenere == null)
			return null;
		
		try{
			DSLContext ctx = DSL.using(connection, SQLDialect.SQLITE);
			
			Result<Record> result = ctx.select()
					.from(Libri.LIBRI)
					.where(Libri.LIBRI.GENERE.contains(idGenere.toString()))
					.fetch();
			
			return result.size();
			
		} catch(Exception ex){
			System.out.println(ex.getMessage());
			return null;
		}
	}
	
	/**
	 * Questo metodo legge le case editrici presenti nei libri e le restituisce in una lista di stringhe.
	 * 
	 * @return ArrayList<String> una lista di stringhe che rappresentano le case editrici presenti nei libri
	 */
	public ArrayList<String> leggiCaseEditrici() {
		try  {

			DSLContext ctx = DSL.using(connection, SQLDialect.SQLITE);
			Result<Record1<String>> result = ctx.selectDistinct(Libri.LIBRI.CASA_EDITRICE)
					.from(Libri.LIBRI)
					.orderBy(Libri.LIBRI.CASA_EDITRICE)
					.fetch();

			ArrayList<String> caseEditrici = new ArrayList<String>();
			for (Record1<String> casaEditrice : result) {
				caseEditrici.add(casaEditrice.getValue(Libri.LIBRI.CASA_EDITRICE));
			}
			
			return caseEditrici;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
}
