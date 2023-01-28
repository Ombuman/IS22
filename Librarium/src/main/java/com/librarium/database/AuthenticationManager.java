package com.librarium.database;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import com.librarium.database.generated.org.jooq.tables.Utenti;
import com.librarium.database.generated.org.jooq.tables.records.UtentiRecord;
import com.librarium.model.authentication.LoginInfo;
import com.librarium.model.authentication.SignupInfo;

public class AuthenticationManager extends DatabaseConnection {
	
	private static AuthenticationManager instance;
	
	public static AuthenticationManager getInstance() {
		if(instance == null)
			instance = new AuthenticationManager();
		
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
	
	public Integer registraUtente(SignupInfo datiUtente) {
		return UsersManager.getInstance().aggiungiUtente(datiUtente);
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
	
}
