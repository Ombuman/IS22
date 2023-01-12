package com.librarium.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import com.librarium.database.entities.Prestito;
import com.librarium.database.enums.StatoAccountUtente;
import com.librarium.database.enums.StatoLibro;
import com.librarium.database.enums.StatoPrestito;
import com.librarium.database.generated.org.jooq.tables.Libri;
import com.librarium.database.generated.org.jooq.tables.Prestiti;
import com.librarium.database.generated.org.jooq.tables.Utenti;
import com.librarium.database.generated.org.jooq.tables.records.LibriRecord;
import com.librarium.database.generated.org.jooq.tables.records.PrestitiRecord;
import com.librarium.database.generated.org.jooq.tables.records.UtentiRecord;

public class PrestitiManager extends DatabaseConnection {
	
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
			
			// data di oggi
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			String oggi = dtf.format(LocalDateTime.now()).toString();
			
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
			
		} catch(SQLException ex){
			throw ex;
		}
	}
	
}
