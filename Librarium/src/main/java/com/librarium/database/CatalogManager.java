package com.librarium.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import com.librarium.database.entities.Libro;
import com.librarium.database.enums.StatoLibro;
import com.librarium.database.generated.org.jooq.tables.Generi;
import com.librarium.database.generated.org.jooq.tables.Libri;
import com.librarium.database.generated.org.jooq.tables.records.GeneriRecord;
import com.librarium.database.generated.org.jooq.tables.records.LibriRecord;

public class CatalogManager extends DatabaseConnection{

	/*========================== LIBRI ===========================*/
	
	public static List<Libro> leggiLibri(String filtroParole, GeneriRecord Generi, String casaEditrice){
		return leggiLibri(filtroParole, Generi.getId().toString(), casaEditrice);
	}
	
	public static List<Libro> leggiLibri(String filtroParole, String filtroGeneri, String casaEditrice) {
		
		try(Connection conn = connect()){
			DSLContext ctx = DSL.using(conn, SQLDialect.SQLITE);
			
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
		} catch(SQLException ex){
			System.out.println(ex.getMessage());
			return null;
		}
	}
	
	private static List<Libro> listaRecordToLibri(Result<Record> result) {
		List<GeneriRecord> listaGeneri = leggiGeneri();
		List<Libro> libri = new ArrayList<>();
		
		result.forEach(libro -> {
			LibriRecord recordLibro = libro.into(Libri.LIBRI);
			List<GeneriRecord> generi = listaGeneri.stream().filter(genere -> recordLibro.getGenere().contains(genere.getId().toString())).toList();
			libri.add(new Libro(recordLibro, generi));
		});
		
		return libri;
	}
	
	private static Libro recordToLibro(Record result) {
		List<GeneriRecord> listaGeneri = leggiGeneri();
		
		LibriRecord recordLibro = result.into(Libri.LIBRI);
		List<GeneriRecord> generi = listaGeneri.stream().filter(genere -> recordLibro.getGenere().contains(genere.getId().toString())).toList();
		return new Libro(recordLibro, generi);
	}
	
	public static Libro leggiLibro(int idLibro) {
		try(Connection conn = connect()){
			DSLContext ctx = DSL.using(conn, SQLDialect.SQLITE);
			
			Record result = 
			ctx.select()
				.from(Libri.LIBRI)
				.where(Libri.LIBRI.ID.eq(idLibro))
				.fetchOne();
			
			return recordToLibro(result);
			
		} catch(SQLException ex){
			System.out.println(ex.getMessage());
			return null;
		}
	}
	
	public static Integer aggiungiLibro(Libro libro) {
		if(libro == null)
			return null;
		
		libro.setStato(StatoLibro.DISPONIBILE.name());
		
		try(Connection conn = connect()){
			DSLContext ctx = DSL.using(conn, SQLDialect.SQLITE);
			
			Record1<Integer> result = ctx.insertInto(Libri.LIBRI, 
				Libri.LIBRI.TITOLO, Libri.LIBRI.DESCRIZIONE, Libri.LIBRI.COPERTINA, Libri.LIBRI.AUTORE, Libri.LIBRI.CASA_EDITRICE, Libri.LIBRI.ANNO, Libri.LIBRI.GENERE, Libri.LIBRI.STATO)
			.values(libro.getTitolo(), libro.getDescrizione(), libro.getCopertina(), libro.getAutore(), libro.getCasaEditrice(), libro.getAnno(), libro.getGeneriID(), libro.getStato())
			.returningResult(Libri.LIBRI.ID)
			.fetchOne();
			
			// ritorna l'id del libro inserito
			return result.getValue(Libri.LIBRI.ID);
		} catch(SQLException ex){
			System.out.println(ex.getMessage());
			return null;
		}
	}
	
	public static void rimuoviLibro(int idLibro) {		
		try(Connection conn = connect()){
			DSLContext ctx = DSL.using(conn, SQLDialect.SQLITE);
			
			ctx.deleteFrom(Libri.LIBRI)
				.where(Libri.LIBRI.ID.eq(idLibro))
				.execute();
			
			// dopo aver rimosso il libro rimuovi anche tutti i prestiti con tale libro collegato
			PrestitiManager.rimuoviPrestiti(idLibro);
			
		} catch(SQLException ex){
			System.out.println(ex.getMessage());
		}
	}
	
	public static void aggiornaLibro(Libro libro) {
		try(Connection conn = connect()){
			DSLContext ctx = DSL.using(conn, SQLDialect.SQLITE);
			
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
		} catch(SQLException ex){
			System.out.println(ex.getMessage());
		}
	}
	
	public static void aggiornaStatoLibro(int idLibro, StatoLibro nuovoStato) {
		try(Connection conn = connect()){
			DSLContext ctx = DSL.using(conn, SQLDialect.SQLITE);
			
			ctx.update(Libri.LIBRI)
				.set(Libri.LIBRI.STATO, nuovoStato.name())
				.where(Libri.LIBRI.ID.eq(idLibro))
				.execute();
		} catch(SQLException ex){
			System.out.println(ex.getMessage());
		}
	}
	/*======================================================================*/
	
	public static List<GeneriRecord> leggiGeneri() {
		
		try(Connection conn = connect()){
			DSLContext ctx = DSL.using(conn, SQLDialect.SQLITE);
			Result<Record> result = 
				ctx.select()
					.from(Generi.GENERI)
					.orderBy(Generi.GENERI.NOME)
					.fetch();
			
			ArrayList<GeneriRecord> generi = new ArrayList<GeneriRecord>();
			
			result.forEach(genere -> generi.add(genere.into(Generi.GENERI)));
			
			return generi;
		} catch(SQLException ex){
			System.out.println(ex.getMessage());
			return null;
		}
	}
	
	public static Integer aggiungiGenere(GeneriRecord genere) {
		if(genere == null)
			return null;
		
		try(Connection conn = connect()){
			DSLContext ctx = DSL.using(conn, SQLDialect.SQLITE);
			
			Record1<Integer> result = ctx.insertInto(Generi.GENERI, Generi.GENERI.NOME)
			.values(genere.getNome())
			.returningResult(Generi.GENERI.ID)
			.fetchOne();
			
			// ritorna l'id del libro inserito
			return result.getValue(Generi.GENERI.ID);
		} catch(SQLException ex){
			System.out.println(ex.getMessage());
			return null;
		}
	}
	
	public static void rimuoviGenere(Integer idGenere){		
		try(Connection conn = connect()){
			DSLContext ctx = DSL.using(conn, SQLDialect.SQLITE);
			
			ctx.deleteFrom(Generi.GENERI)
				.where(Generi.GENERI.ID.eq(idGenere))
				.execute();
			
		} catch(SQLException ex){
			System.out.println(ex.getMessage());
		}
	}
	
	public static Integer getNumeroLibriGenere(Integer idGenere) {
		try(Connection conn = connect()){
			DSLContext ctx = DSL.using(conn, SQLDialect.SQLITE);
			
			Result<Record> result = ctx.select()
					.from(Libri.LIBRI)
					.where(Libri.LIBRI.GENERE.contains(idGenere.toString()))
					.fetch();
			
			return result.size();
			
		} catch(SQLException ex){
			System.out.println(ex.getMessage());
			return null;
		}
	}
	
	/*========================== CASE EDITRICI ===========================*/
	
	public static ArrayList<String> leggiCaseEditrici() {
		try (Connection conn = connect()) {

			DSLContext ctx = DSL.using(conn, SQLDialect.SQLITE);
			Result<Record1<String>> result = ctx.selectDistinct(Libri.LIBRI.CASA_EDITRICE)
					.from(Libri.LIBRI)
					.orderBy(Libri.LIBRI.CASA_EDITRICE)
					.fetch();

			ArrayList<String> caseEditrici = new ArrayList<String>();
			for (Record1<String> casaEditrice : result) {
				caseEditrici.add(casaEditrice.getValue(Libri.LIBRI.CASA_EDITRICE));
			}
			
			return caseEditrici;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	/*=====================================================================*/

	
}
