package librarium.autenticazione;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import com.librarium.database.UsersManager;
import com.librarium.model.authentication.SignupInfo;
import com.librarium.model.entities.Utente;

public class TestRegistrazione {

	@Before
	public void initTest() {		
		try {
			UsersManager.getInstance().setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	@AfterClass
	public static void endTest() {
		try {
			UsersManager.getInstance().rollback();
			UsersManager.getInstance().setAutoCommit(true);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	@Test
	public void testDisponibilitaEmail() {
		UsersManager gestioneUtenti = UsersManager.getInstance();
		
		boolean disponibile = gestioneUtenti.verificaDisponibilitaEmail("nonvalida@gmail.com");
		boolean nonDisponibile = gestioneUtenti.verificaDisponibilitaEmail("prova@gmail.com");
		boolean emailNull = gestioneUtenti.verificaDisponibilitaEmail(null);
		boolean emailEmpty = gestioneUtenti.verificaDisponibilitaEmail("");
		boolean emailBlank = gestioneUtenti.verificaDisponibilitaEmail(" ");
		
		assertEquals(disponibile, true);
		assertEquals(nonDisponibile, false);
		assertEquals(emailNull, false);
		assertEquals(emailEmpty, false);
		assertEquals(emailBlank, false);
	}
	
	@Test
	public void testRegistrazione() {
		Utente nuovoUtente = null;
		
		try {
			int idUtente = UsersManager.getInstance().aggiungiUtente(new SignupInfo("Pippo", "Rossi", "pippo@test.it", "pippo"));
			nuovoUtente = UsersManager.getInstance().getUtente(idUtente);
		} catch (Exception e) {}
		
		assertEquals(nuovoUtente.getNome(), "Pippo");
		assertEquals(nuovoUtente.getCognome(), "Rossi");
		assertEquals(nuovoUtente.getEmail(), "pippo@test.it");
	}
}
