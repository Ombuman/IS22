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

import com.librarium.controller.utility.DateUtility;
import com.librarium.database.generated.org.jooq.tables.Prestiti;
import com.librarium.database.generated.org.jooq.tables.Solleciti;
import com.librarium.database.generated.org.jooq.tables.Utenti;
import com.librarium.database.generated.org.jooq.tables.records.UtentiRecord;
import com.librarium.model.authentication.LoginInfo;
import com.librarium.model.authentication.SignupInfo;
import com.librarium.model.entities.InfoProfiloUtente;
import com.librarium.model.entities.Prestito;
import com.librarium.model.entities.Sollecito;
import com.librarium.model.entities.Utente;
import com.librarium.model.enums.RuoloAccount;
import com.librarium.model.enums.StatoAccountUtente;

public class UsersManager extends DatabaseConnection {
	
	public static boolean verificaValiditaEmail(String email) {
		try(Connection conn = connect()){
			DSLContext ctx = DSL.using(conn, SQLDialect.SQLITE);
			Result<Record1<String>> result = 
				ctx.select(Utenti.UTENTI.EMAIL)
					.from(Utenti.UTENTI)
					.where(Utenti.UTENTI.EMAIL.eq(email))
					.fetch();
			
			// se l'email è già presente nel database ritorna false, altrimenti true
			return result.size() == 0;
			
		} catch(SQLException ex){
			System.out.println(ex.getMessage());
			return false;
		}
	}
	
	public static void aggiungiUtente(SignupInfo datiUtente) throws Exception {
		if(datiUtente == null)
			throw new Exception("Dati utente non inseriti");
		
		try(Connection conn = connect()){
			DSLContext ctx = DSL.using(conn, SQLDialect.SQLITE);
			
			ctx.insertInto(Utenti.UTENTI, Utenti.UTENTI.NOME, Utenti.UTENTI.COGNOME, Utenti.UTENTI.EMAIL, Utenti.UTENTI.PASSWORD, Utenti.UTENTI.STATO, Utenti.UTENTI.RUOLO)
			.values(datiUtente.getNome(), datiUtente.getCognome(), datiUtente.getEmail(), datiUtente.getEncryptedPassword(), StatoAccountUtente.ATTIVO.toString(), RuoloAccount.UTENTE.toString())
			.execute();
		} catch(Exception e) {
			throw e;
		}
	}
	
	public static UtentiRecord autenticaUtente(LoginInfo datiUtente) {
		if(datiUtente == null)
			return null;
		
		try(Connection conn = connect()){
			DSLContext ctx = DSL.using(conn, SQLDialect.SQLITE);
			
			Result<Record> result = ctx.select()
				.from(Utenti.UTENTI)
				.where(
					Utenti.UTENTI.EMAIL.eq(datiUtente.getEmail())
					.and(Utenti.UTENTI.PASSWORD.eq(datiUtente.getEncryptedPassword()))
				).fetch();
			
			return (result.size() > 0) ? result.get(0).into(Utenti.UTENTI) : null;
			
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<Utente> getUtenti(){
		return getUtenti(null, RuoloAccount.UTENTE.name());
	}
	
	public static List<Utente> getUtentiSospesi(){
		return getUtenti(StatoAccountUtente.SOSPESO.name(), RuoloAccount.UTENTE.name());
	}
	
	private static List<Utente> getUtenti(String statoAccount, String ruoloAccount) {
		try(Connection conn = connect()){
			
			Condition condition = DSL.noCondition();
			if(statoAccount != null && !statoAccount.isBlank())
				condition = condition.and(Utenti.UTENTI.STATO.eq(statoAccount));
			
			if(ruoloAccount != null && !ruoloAccount.isBlank())
				condition = condition.and(Utenti.UTENTI.RUOLO.eq(ruoloAccount));
			
			DSLContext ctx = DSL.using(conn, SQLDialect.SQLITE);
			
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
	
	public static void setStatoAccount(Integer idUtente, String nuovoStato) {
		try(Connection conn = connect()){
			DSLContext ctx = DSL.using(conn, SQLDialect.SQLITE);
			
			ctx.update(Utenti.UTENTI)
				.set(Utenti.UTENTI.STATO, nuovoStato)
				.where(Utenti.UTENTI.ID.eq(idUtente))
				.execute();
			
		} catch(Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	public static StatoAccountUtente getStatoAccount(Integer idUtente) {
		try(Connection conn = connect()){
			DSLContext ctx = DSL.using(conn, SQLDialect.SQLITE);
			
			Result<Record1<String>> result = ctx.select(Utenti.UTENTI.STATO)
				.from(Utenti.UTENTI)
				.where(
					Utenti.UTENTI.ID.eq(idUtente)
				).fetch();
			
			return result.size() > 0 ? StatoAccountUtente.valueOf(result.get(0).component1()) : null;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static UtentiRecord aggiornaAccountUtente(Integer idUtente, InfoProfiloUtente datiUtente) {
		if(datiUtente == null)
			return null;
		
		try(Connection conn = connect()){
			DSLContext ctx = DSL.using(conn, SQLDialect.SQLITE);
			
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

	public static void inviaSollecito(Prestito prestito) {
		if(prestito == null) 
			return;
		
		try(Connection conn = connect()){
			DSLContext ctx = DSL.using(conn, SQLDialect.SQLITE);
			
			String oggi = DateUtility.getDataOggi();
			
			ctx.insertInto(Solleciti.SOLLECITI, Solleciti.SOLLECITI.UTENTE, Solleciti.SOLLECITI.LIBRO, Solleciti.SOLLECITI.DATA)
				.values(prestito.getIdUtente(), prestito.getIdLibro(), oggi)
				.execute();
			
			// imposta la data dell'ultimo sollecito nel prestito
			PrestitiManager.aggiornaDataUltimoSollecito(prestito.getId(), oggi);
			
		} catch(Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	public static List<Sollecito> getSollecitiUtente(Integer idUtente) {
		if(idUtente == null)
			return null;
		
		try(Connection conn = connect()){
			DSLContext ctx = DSL.using(conn, SQLDialect.SQLITE);
			
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
	
	public static List<Sollecito> getSollecitiUtenteLibro(Integer idUtente, Integer idLibro) {
		if(idUtente == null)
			return null;
		
		try(Connection conn = connect()){
			DSLContext ctx = DSL.using(conn, SQLDialect.SQLITE);
			
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

	public static void rimuoviSolleciti(Integer idUtente, Integer idLibro) {
		if(idUtente == null || idLibro == null)
			return;
		
		try(Connection conn = connect()){
			DSLContext ctx = DSL.using(conn, SQLDialect.SQLITE);
			
			ctx.deleteFrom(Solleciti.SOLLECITI)
				.where(Solleciti.SOLLECITI.UTENTE.eq(idUtente).and(Solleciti.SOLLECITI.LIBRO.eq(idLibro)))
				.execute();
			
		} catch(Exception e) {
			e.printStackTrace();
			return;
		}
		
	}
	
}
