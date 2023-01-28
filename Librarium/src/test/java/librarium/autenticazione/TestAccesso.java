package librarium.autenticazione;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.librarium.database.AuthenticationManager;
import com.librarium.database.generated.org.jooq.tables.records.UtentiRecord;
import com.librarium.model.authentication.LoginInfo;

public class TestAccesso { 
	
	@Test
	public void testAutenticazioneUtente() {
		LoginInfo credenzialiValide = new LoginInfo("prova@gmail.com", "Prova1234");
		LoginInfo credenzialiErrate = new LoginInfo("errore@gmail.com", "PasswordErrata");
		LoginInfo credenzialiEmpty = new LoginInfo("", "");
		LoginInfo credenzialiNull = null;
		
		AuthenticationManager autenticazioneUtenti = AuthenticationManager.getInstance();
		
		UtentiRecord utenteAutenticato = autenticazioneUtenti.autenticaUtente(credenzialiValide);
		UtentiRecord utenteErrato = autenticazioneUtenti.autenticaUtente(credenzialiErrate);
		UtentiRecord utenteEmpty = autenticazioneUtenti.autenticaUtente(credenzialiEmpty);
		UtentiRecord utenteNull = autenticazioneUtenti.autenticaUtente(credenzialiNull);
		
		assertNotNull(utenteAutenticato);
		assertNull(utenteErrato);
		assertNull(utenteEmpty);
		assertNull(utenteNull);
	}
}
