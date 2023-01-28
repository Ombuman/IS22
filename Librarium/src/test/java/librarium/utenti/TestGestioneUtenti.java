package librarium.utenti;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;

import org.junit.Test;

import com.librarium.database.UsersManager;
import com.librarium.model.entities.InfoProfiloUtente;
import com.librarium.model.entities.Sollecito;
import com.librarium.model.entities.Utente;
import com.librarium.model.enums.StatoAccountUtente;

import librarium.TestDB;

public class TestGestioneUtenti extends TestDB{
	
	@Test
	public void testGetUtente() {
		UsersManager gestioneUtenti = UsersManager.getInstance();
		
		Utente utenteEsistente = gestioneUtenti.getUtente(1);
		Utente utenteInesistente = gestioneUtenti.getUtente(-1);
		Utente utenteNull = gestioneUtenti.getUtente(null);
		
		assertNotNull(utenteEsistente);
		assertNull(utenteInesistente);
		assertNull(utenteNull);
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
	
	@Test
	public void testStatoAccount() {
		UsersManager.getInstance().setStatoAccount(1, StatoAccountUtente.ATTIVO.name());
		StatoAccountUtente statoAttivo = UsersManager.getInstance().getStatoAccount(1);
		
		UsersManager.getInstance().setStatoAccount(1, StatoAccountUtente.SOSPESO.name());
		List<Utente> utentiSospesi = UsersManager.getInstance().getUtentiSospesi();
		
		UsersManager.getInstance().setStatoAccount(1, null);
		StatoAccountUtente statoNonCambiato = UsersManager.getInstance().getStatoAccount(1);
		
		StatoAccountUtente statoInesistente = UsersManager.getInstance().getStatoAccount(-1);
		
		assertEquals(statoAttivo, StatoAccountUtente.ATTIVO);
		assertThat(utentiSospesi.size() > 0);
		assertNull(statoInesistente);
		assertEquals(statoNonCambiato, StatoAccountUtente.SOSPESO);
	}
	
	@Test
	public void testAggiornaAccount() {
		List<Utente> utenti = UsersManager.getInstance().getUtenti();
		Utente utenteIniziale = utenti.get(0);
		
		UsersManager.getInstance().aggiornaAccountUtente(1, new InfoProfiloUtente(utenteIniziale.getEmail(), "Nuovo Nome", "Nuovo Cognome"));
		Utente utenteFinale = UsersManager.getInstance().getUtente(1);
		
		assertEquals(utenteFinale.getNome(), "Nuovo Nome");
		assertEquals(utenteFinale.getCognome(), "Nuovo Cognome");
		assertNotEquals(utenteIniziale.getNome(), utenteFinale.getNome());
		assertNotEquals(utenteIniziale.getCognome(), utenteFinale.getCognome());
	}
	
}
