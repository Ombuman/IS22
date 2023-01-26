package librarium.autenticazione;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.librarium.database.UsersManager;
import com.librarium.database.generated.org.jooq.tables.records.UtentiRecord;
import com.librarium.model.authentication.LoginInfo;

public class TestAccesso { 
	
	@Test
	public void testAutenticazioneUtente() {
		LoginInfo credenzialiValide = new LoginInfo("prova@gmail.com", "Prova1234");
		LoginInfo credenzialiErrate = new LoginInfo("errore@gmail.com", "PasswordErrata");
		LoginInfo credenzialiEmpty = new LoginInfo("", "");
		LoginInfo credenzialiNull = null;
		
		UsersManager gestioneUtenti = UsersManager.getInstance();
		
		UtentiRecord utenteAutenticato = gestioneUtenti.autenticaUtente(credenzialiValide);
		UtentiRecord utenteErrato = gestioneUtenti.autenticaUtente(credenzialiErrate);
		UtentiRecord utenteEmpty = gestioneUtenti.autenticaUtente(credenzialiEmpty);
		UtentiRecord utenteNull = gestioneUtenti.autenticaUtente(credenzialiNull);
		
		assertNotNull(utenteAutenticato);
		assertNull(utenteErrato);
		assertNull(utenteEmpty);
		assertNull(utenteNull);
	}
}
