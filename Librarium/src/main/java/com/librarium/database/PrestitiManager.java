package com.librarium.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import com.librarium.application.utility.DateUtility;
import com.librarium.database.generated.org.jooq.tables.Libri;
import com.librarium.database.generated.org.jooq.tables.Prestiti;
import com.librarium.database.generated.org.jooq.tables.Utenti;
import com.librarium.database.generated.org.jooq.tables.records.LibriRecord;
import com.librarium.database.generated.org.jooq.tables.records.UtentiRecord;
import com.librarium.model.entities.Prestito;
import com.librarium.model.enums.StatoAccountUtente;
import com.librarium.model.enums.StatoLibro;
import com.librarium.model.enums.StatoPrestito;

public class PrestitiManager extends DatabaseConnection {
	
	public static ArrayList<Prestito> getPrestiti(String stato) {		
		try(Connection conn = connect()){
			DSLContext ctx = DSL.using(conn, SQLDialect.SQLITE);
			
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
			
		} catch(SQLException ex){
			System.out.println(ex.getMessage());
			return null;
		}
	}
	
	public static ArrayList<Prestito> getPrestitiUtente(int idUtente, String stato) {		
		try(Connection conn = connect()){
			DSLContext ctx = DSL.using(conn, SQLDialect.SQLITE);
			
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
			
		} catch(SQLException ex){
			System.out.println(ex.getMessage());
			return null;
		}
	}
	
	public static boolean creaPrestito(UtentiRecord utente, LibriRecord libro) {
		// verifico che i dati inseriti non siano nulli
		if(utente == null || libro == null)
			return false;
		
		try(Connection conn = connect()){
			// controllo se l'utente non è sospeso
			if(UsersManager.getStatoAccount(utente.getId()) == StatoAccountUtente.SOSPESO)
				return false;
			
			// verifico se il libro sia realmente disponibile
			if(StatoLibro.valueOf(libro.getStato()) == StatoLibro.NON_DISPONIBILE)
				return false;
			
			String oggi = DateUtility.getDataOggi();
			
			//aggiungi il prestito
			DSLContext ctx = DSL.using(conn, SQLDialect.SQLITE);
			ctx.insertInto(Prestiti.PRESTITI, Prestiti.PRESTITI.DATA_PRENOTAZIONE, Prestiti.PRESTITI.STATO, Prestiti.PRESTITI.LIBRO, Prestiti.PRESTITI.UTENTE)
				.values(oggi, StatoPrestito.PRENOTATO.toString(), libro.getId(), utente.getId())
				.execute();
			
			// aggiorna lo stato del libro
			CatalogManager.aggiornaStatoLibro(libro.getId(), StatoLibro.NON_DISPONIBILE);
			
			return true;
			
		} catch(SQLException ex){
			System.out.println(ex.getMessage());
			return false;
		}
	}
	
	public static void annullaPrenotazione(Prestito prestito) throws Exception{
		try(Connection conn = connect()){
			DSLContext ctx = DSL.using(conn, SQLDialect.SQLITE);
			
			ctx.deleteFrom(Prestiti.PRESTITI)
				.where(Prestiti.PRESTITI.ID.eq(prestito.getDati().getId()))
				.and(Prestiti.PRESTITI.STATO.eq(StatoPrestito.PRENOTATO.name()))
				.execute();
			
			CatalogManager.aggiornaStatoLibro(prestito.getLibro().getId(), StatoLibro.DISPONIBILE);
			
		} catch(SQLException e){
			throw e;
		}
	}

	public static void rimuoviPrestiti(int idLibro) {
		try(Connection conn = connect()){
			DSLContext ctx = DSL.using(conn, SQLDialect.SQLITE);
			
			ctx.deleteFrom(Prestiti.PRESTITI)
				.where(Prestiti.PRESTITI.LIBRO.eq(idLibro))
				.execute();
			
		} catch(SQLException e){
			System.out.println(e);
		}
	}
	
	public static void attivaPrestito(Prestito prestito) {
		if(prestito == null)
			return;
		
		try(Connection conn = connect()){
			DSLContext ctx = DSL.using(conn, SQLDialect.SQLITE);
			
			String nuovoStato = StatoPrestito.RITIRATO.name();
			String oggi = DateUtility.getDataOggi();
			
			ctx.update(Prestiti.PRESTITI)
				.set(Prestiti.PRESTITI.STATO, nuovoStato)
				.set(Prestiti.PRESTITI.DATA_INIZIO, oggi)
				.where(Prestiti.PRESTITI.ID.eq(prestito.getId()))
				.execute();
			
		} catch(SQLException e){
			System.out.println(e);
		}
	}
	
	public static void concludiPrestito(Prestito prestito) {
		if(prestito == null)
			return;
		
		try(Connection conn = connect()){
			DSLContext ctx = DSL.using(conn, SQLDialect.SQLITE);
			
			String nuovoStato = StatoPrestito.CONCLUSO.name();
			String oggi = DateUtility.getDataOggi();
			
			ctx.update(Prestiti.PRESTITI)
				.set(Prestiti.PRESTITI.STATO, nuovoStato)
				.set(Prestiti.PRESTITI.DATA_FINE, oggi)
				.where(Prestiti.PRESTITI.ID.eq(prestito.getId()))
				.execute();
			
			// aggiorna lo stato del libro
			CatalogManager.aggiornaStatoLibro(prestito.getLibro().getId(), StatoLibro.DISPONIBILE);
			
			// rimuovi tutti i solleciti collegati a questo prestito
			UsersManager.rimuoviSolleciti(prestito.getIdUtente(), prestito.getIdLibro());
			
		} catch(SQLException e){
			System.out.println(e);
		}
	}

	public static void aggiornaDataUltimoSollecito(Integer id, String oggi) {		
		try(Connection conn = connect()){
			DSLContext ctx = DSL.using(conn, SQLDialect.SQLITE);
			
			ctx.update(Prestiti.PRESTITI)
				.set(Prestiti.PRESTITI.DATA_ULTIMO_SOLLECITO, oggi)
				.where(Prestiti.PRESTITI.ID.eq(id))
				.execute();
			
		} catch(SQLException e){
			System.out.println(e);
		}
	}
	
}
