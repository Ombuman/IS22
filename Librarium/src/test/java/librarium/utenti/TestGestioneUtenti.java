package librarium.utenti;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.Test;

import com.librarium.database.UsersManager;
import com.librarium.model.entities.Sollecito;
import com.librarium.model.entities.Utente;
import com.librarium.model.enums.RuoloAccount;
import com.librarium.model.enums.StatoAccountUtente;

public class TestGestioneUtenti {
	
	@Test
	public void testGetUtenti() {
		UsersManager gestioneUtenti = UsersManager.getInstance();
		
		List<Utente> listaBibliotecari = gestioneUtenti.getUtenti(null, RuoloAccount.BIBLIOTECARIO.name());
		List<Utente> listaUtenti = gestioneUtenti.getUtenti(null, RuoloAccount.UTENTE.name());
		List<Utente> listaUtentiSospesi = gestioneUtenti.getUtentiSospesi();
		
		assertNotNull(listaBibliotecari);
		assertNotNull(listaUtenti);
		assertNotNull(listaUtentiSospesi);
	}
	
	@Test
	public void testGetStatoUtente() {
		UsersManager gestioneUtenti = UsersManager.getInstance();
		
		StatoAccountUtente statoAttivo = gestioneUtenti.getStatoAccount(2);
		StatoAccountUtente statoSospeso = gestioneUtenti.getStatoAccount(6);
		StatoAccountUtente statoNull = gestioneUtenti.getStatoAccount(null);
		
		assertEquals(statoAttivo, StatoAccountUtente.ATTIVO);
		assertEquals(statoSospeso, StatoAccountUtente.SOSPESO);
		assertNull(statoNull);
	}
	
	
	@Test
	public void testGetSollecitiUtente() {
		UsersManager gestioneUtenti = UsersManager.getInstance();
		
		List<Sollecito> sollecitiUtenteNull= gestioneUtenti.getSollecitiUtente(null);
		List<Sollecito> sollecitiUtente = gestioneUtenti.getSollecitiUtente(1);
		
		assertNull(sollecitiUtenteNull);
		assertNotNull(sollecitiUtente);
	}
	
}
