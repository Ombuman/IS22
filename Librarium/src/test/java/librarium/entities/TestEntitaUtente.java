package librarium.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import com.librarium.database.generated.org.jooq.tables.records.UtentiRecord;
import com.librarium.model.entities.InfoProfiloUtente;
import com.librarium.model.entities.Utente;
import com.librarium.model.enums.RuoloAccount;
import com.librarium.model.enums.StatoAccountUtente;

public class TestEntitaUtente {
	
	public Utente creaUtente() {
		UtentiRecord datiUtente = new UtentiRecord(1, "Nome", "Cognome", "Email", "Password", StatoAccountUtente.ATTIVO.name(), RuoloAccount.UTENTE.name());
		
		return new Utente(datiUtente, 1, 2);
	}
	
	private InfoProfiloUtente creaInfoUtente() {
		return new InfoProfiloUtente("Email", "Nome", "Cognome");
	}
	
	@Test
	public void testGetUtente() {
		Utente utente = creaUtente();
		
		assertEquals(utente.getId(), 1);
		assertEquals(utente.getNome(), "Nome");
		assertEquals(utente.getCognome(), "Cognome");
		assertEquals(utente.getEmail(), "Email");
		assertEquals(utente.getPassword(), "Password");
		assertEquals(utente.getStato(), StatoAccountUtente.ATTIVO.name());
		assertEquals(utente.getRuolo(), RuoloAccount.UTENTE.name());
		assertEquals(utente.getNumeroPrestiti(), 1);
		assertEquals(utente.getNumeroSolleciti(), 2);
	}
	
	@Test
	public void testSetUtente() {
		Utente utente = creaUtente();
		
		utente.setId(null);
		utente.setNome(null);
		utente.setCognome("Nuovo Cognome");
		utente.setEmail("Nuova Email");
		utente.setPassword(" ");
		utente.setStato(StatoAccountUtente.SOSPESO);
		utente.setRuolo(RuoloAccount.BIBLIOTECARIO.name());
		utente.setNumeroPrestiti(10);
		utente.setNumeroSolleciti(5);
		
		assertEquals(utente.getId(), 1);
		assertEquals(utente.getNome(), null);
		assertEquals(utente.getCognome(), "Nuovo Cognome");
		assertEquals(utente.getEmail(), "Nuova Email");
		assertEquals(utente.getPassword(), " ");
		assertEquals(utente.getStato(), StatoAccountUtente.SOSPESO.name());
		assertEquals(utente.getRuolo(), RuoloAccount.BIBLIOTECARIO.name());
		assertEquals(utente.getNumeroPrestiti(), 10);
		assertEquals(utente.getNumeroSolleciti(), 5);
		
		utente.setId(2);
		utente.setStato(StatoAccountUtente.ATTIVO.name());
		utente.setRuolo(RuoloAccount.UTENTE);
		
		assertEquals(utente.getId(), 2);
		assertEquals(utente.getStato(), StatoAccountUtente.ATTIVO.name());
		assertEquals(utente.getRuolo(), RuoloAccount.UTENTE.name());
	}

	@Test
	public void testGetInfoProfiloUtente() {
		InfoProfiloUtente infoUtente = creaInfoUtente();
		
		assertEquals(infoUtente.getEmail(), "Email");
		assertEquals(infoUtente.getNome(), "Nome");
		assertEquals(infoUtente.getCognome(), "Cognome");
	}
	
	@Test
	public void testSetInfoProfiloUtente() {
		InfoProfiloUtente infoUtente = new InfoProfiloUtente();
		
		UtentiRecord datiUtente = creaUtente().getDatiUtente();
		
		infoUtente.setEmail(null);
		infoUtente.setNome("Nuovo Nome");
		infoUtente.setCognome("Nuovo Cognome");
		
		assertEquals(infoUtente.getEmail(), null);
		assertEquals(infoUtente.getNome(), "Nuovo Nome");
		assertEquals(infoUtente.getCognome(), "Nuovo Cognome");
		assertEquals(infoUtente.compareTo(datiUtente), 0);
		
		InfoProfiloUtente infoUtenteUguali = creaInfoUtente();
		
		assertEquals(infoUtenteUguali.compareTo(datiUtente), 1);
	}
	
}
