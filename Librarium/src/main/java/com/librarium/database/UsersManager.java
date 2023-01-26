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
import com.librarium.model.authentication.LoginInfo;
import com.librarium.model.authentication.SignupInfo;
import com.librarium.model.entities.InfoProfiloUtente;
import com.librarium.model.entities.Prestito;
import com.librarium.model.entities.Sollecito;
import com.librarium.model.entities.Utente;
import com.librarium.model.enums.RuoloAccount;
import com.librarium.model.enums.StatoAccountUtente;

public class UsersManager extends DatabaseConnection {
	
	private static UsersManager instance;
	
	public static UsersManager getInstance() {
		if(instance == null)
			instance = new UsersManager();
		
		return instance;
	}
	
	public boolean verificaDisponibilitaEmail(String email) {
		if(email == null || email.isBlank())
			return false;
		
		try{
			DSLContext ctx = DSL.using(connection, SQLDialect.SQLITE);
			Result<Record1<String>> result = 
				ctx.select(Utenti.UTENTI.EMAIL)
					.from(Utenti.UTENTI)
					.where(Utenti.UTENTI.EMAIL.eq(email))
					.fetch();
			
			// se l'email è già presente nel database ritorna false, altrimenti true
			return result.size() == 0;
			
		} catch(Exception ex){
			System.out.println(ex.getMessage());
			return false;
		}
	}
	
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
	
	public UtentiRecord autenticaUtente(LoginInfo datiUtente) {
		if(datiUtente == null)
			return null;
		
		try{
			DSLContext ctx = DSL.using(connection, SQLDialect.SQLITE);
			
			Record result = ctx.select()
				.from(Utenti.UTENTI)
				.where(
					Utenti.UTENTI.EMAIL.eq(datiUtente.getEmail())
					.and(Utenti.UTENTI.PASSWORD.eq(datiUtente.getEncryptedPassword()))
				).fetchOne();
			
			return result != null ? result.into(Utenti.UTENTI) : null;
			
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
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
	
	public List<Utente> getUtenti(){
		return getUtenti(null, RuoloAccount.UTENTE.name());
	}
	
	public List<Utente> getUtentiSospesi(){
		return getUtenti(StatoAccountUtente.SOSPESO.name(), RuoloAccount.UTENTE.name());
	}
	
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
	
	public void setStatoAccount(Integer idUtente, String nuovoStato) {
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
			e.printStackTrace();
			return;
		}
	}
	
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
	
	public List<Sollecito> getSollecitiUtenteLibro(Integer idUtente, Integer idLibro) {
		if(idUtente == null)
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
