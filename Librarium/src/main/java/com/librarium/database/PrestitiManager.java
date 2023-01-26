package com.librarium.database;

import java.util.ArrayList;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
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

public class PrestitiManager extends DatabaseConnection {
	
	private static PrestitiManager instance;
	
	public static PrestitiManager getInstance() {
		if(instance == null)
			instance = new PrestitiManager();
		
		return instance;
	}
	
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
	
	public boolean creaPrestito(UtentiRecord utente, LibriRecord libro) {
		// verifico che i dati inseriti non siano nulli
		if(utente == null || libro == null)
			return false;
		
		try{
			// controllo se l'utente non è sospeso
			if(UsersManager.getInstance().getStatoAccount(utente.getId()) == StatoAccountUtente.SOSPESO)
				return false;
			
			// verifico se il libro sia realmente disponibile
			if(StatoLibro.valueOf(libro.getStato()) == StatoLibro.NON_DISPONIBILE)
				return false;
			
			String oggi = DateUtility.getDataOggi();
			
			//aggiungi il prestito
			DSLContext ctx = DSL.using(connection, SQLDialect.SQLITE);
			ctx.insertInto(Prestiti.PRESTITI, Prestiti.PRESTITI.DATA_PRENOTAZIONE, Prestiti.PRESTITI.STATO, Prestiti.PRESTITI.LIBRO, Prestiti.PRESTITI.UTENTE)
				.values(oggi, StatoPrestito.PRENOTATO.toString(), libro.getId(), utente.getId())
				.execute();
			
			// aggiorna lo stato del libro
			CatalogManager.getInstance().aggiornaStatoLibro(libro.getId(), StatoLibro.NON_DISPONIBILE);
			
			return true;
			
		} catch(Exception ex){
			System.out.println(ex.getMessage());
			return false;
		}
	}
	
	public void annullaPrenotazione(Prestito prestito) throws Exception{
		try{
			DSLContext ctx = DSL.using(connection, SQLDialect.SQLITE);
			
			ctx.deleteFrom(Prestiti.PRESTITI)
				.where(Prestiti.PRESTITI.ID.eq(prestito.getDati().getId()))
				.and(Prestiti.PRESTITI.STATO.eq(StatoPrestito.PRENOTATO.name()))
				.execute();
			
			CatalogManager.getInstance().aggiornaStatoLibro(prestito.getLibro().getId(), StatoLibro.DISPONIBILE);
			
		} catch(Exception e){
			throw e;
		}
	}

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
