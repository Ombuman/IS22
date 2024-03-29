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

/**
 * La classe AuthenticationManager è una classe singleton che gestisce l'autenticazione degli utenti.
 * Fornisce metodi per la verifica della disponibilità di una email, la registrazione di un nuovo utente
 * e l'autenticazione di un utente esistente.
 */
public class AuthenticationManager extends DatabaseConnection {
	
	/**
	 * Variabile statica che rappresenta l'istanza unica della classe.
	 */
	private static AuthenticationManager instance;
	
	/**
	 * Metodo che ritorna l'istanza unica della classe.
	 * Se non esiste un'istanza viene creata una nuova.
	 * 
	 * @return l'istanza unica della classe.
	 */
	public static AuthenticationManager getInstance() {
		if(instance == null)
			instance = new AuthenticationManager();
		
		return instance;
	}
	
	/**
	 * Metodo che verifica la disponibilità dell'email passata come parametro.
	 * 
	 * @param email l'email da verificare.
	 * @return true se l'email è disponibile, false altrimenti.
	 */
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
	
	/**
	 * Metodo che registra un nuovo utente nel database.
	 * 
	 * @param datiUtente i dati dell'utente da registrare.
	 * @return l'ID dell'utente registrato o -1 in caso di errore.
	 */
	public Integer registraUtente(SignupInfo datiUtente) {
		return UsersManager.getInstance().aggiungiUtente(datiUtente);
	}
	
	/**
	 * Metodo per l'autenticazione di un utente.
	 * 
	 * @param datiUtente oggetto contenente i dati di login dell'utente.
	 * @return un oggetto UtentiRecord con i dati dell'utente se l'autenticazione è andata a buon fine, altrimenti null.
	 */
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
