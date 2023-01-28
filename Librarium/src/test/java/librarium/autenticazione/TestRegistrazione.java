package librarium.autenticazione;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import com.librarium.database.AuthenticationManager;
import com.librarium.database.UsersManager;
import com.librarium.model.authentication.SignupInfo;
import com.librarium.model.entities.Utente;

import librarium.TestDB;

public class TestRegistrazione extends TestDB{

	@Test
	public void testDisponibilitaEmail() {
		AuthenticationManager autenticazioneUtenti = AuthenticationManager.getInstance();
		
		boolean disponibile = autenticazioneUtenti.verificaDisponibilitaEmail("nonvalida@gmail.com");
		boolean nonDisponibile = autenticazioneUtenti.verificaDisponibilitaEmail("prova@gmail.com");
		boolean emailNull = autenticazioneUtenti.verificaDisponibilitaEmail(null);
		boolean emailEmpty = autenticazioneUtenti.verificaDisponibilitaEmail("");
		boolean emailBlank = autenticazioneUtenti.verificaDisponibilitaEmail(" ");
		
		assertEquals(disponibile, true);
		assertEquals(nonDisponibile, false);
		assertEquals(emailNull, false);
		assertEquals(emailEmpty, false);
		assertEquals(emailBlank, false);
	}
	
	@Test
	public void testRegistrazione() {
		Utente nuovoUtente = null;
		Utente utenteNull = null;
		
		try {
			Integer idUtente = AuthenticationManager.getInstance().registraUtente(new SignupInfo("Pippo", "Rossi", "pippo@test.it", "pippo"));
			nuovoUtente = UsersManager.getInstance().getUtente(idUtente);
		} catch (Exception e) {}
		
		try {
			Integer idUtente = AuthenticationManager.getInstance().registraUtente(null);
			utenteNull = UsersManager.getInstance().getUtente(idUtente);
		} catch (Exception e) {}
		
		
		assertEquals(nuovoUtente.getNome(), "Pippo");
		assertEquals(nuovoUtente.getCognome(), "Rossi");
		assertEquals(nuovoUtente.getEmail(), "pippo@test.it");

		assertNull(utenteNull);
	}
}
