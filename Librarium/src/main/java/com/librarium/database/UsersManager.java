package com.librarium.database;

import java.sql.Connection;
import java.sql.SQLException;

import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import com.librarium.application.authentication.SignupInfo;
import com.librarium.database.enums.RuoloAccount;
import com.librarium.database.enums.StatoAccountUtente;
import com.librarium.database.generated.org.jooq.tables.Utenti;
import com.librarium.database.generated.org.jooq.tables.records.UtentiRecord;

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
			.values(datiUtente.getNome(), datiUtente.getCognome(), datiUtente.getEmail(), datiUtente.getPassword(), StatoAccountUtente.ATTIVO.toString(), RuoloAccount.UTENTE.toString())
			.execute();
		} catch(Exception e) {
			throw e;
		}
	}
	
}
