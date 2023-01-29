package librarium.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import com.librarium.database.generated.org.jooq.tables.records.UtentiRecord;
import com.librarium.model.entities.Utente;
import com.librarium.model.enums.RuoloAccount;
import com.librarium.model.enums.StatoAccountUtente;

public class TestEntitaUtente {
	
	private Utente creaUtente() {
		UtentiRecord datiUtente = new UtentiRecord(1, "Nome", "Cognome", "Email", "Password", StatoAccountUtente.ATTIVO.name(), RuoloAccount.UTENTE.name());
		
		return new Utente(datiUtente, 1, 2);
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
	}
	
	@Test
	public void testSetUtente() {
		Utente utente = creaUtente();
		
		utente.setId(2);
		utente.setNome(null);
		utente.setCognome("Nuovo Cognome");
		utente.setEmail("Nuova Email");
		utente.setPassword(" ");
		utente.setStato(StatoAccountUtente.SOSPESO);
		utente.setRuolo(RuoloAccount.BIBLIOTECARIO.name());
		
		assertEquals(utente.getId(), 2);
		assertEquals(utente.getNome(), "Nome");
		assertEquals(utente.getCognome(), "Nuovo Cognome");
		assertEquals(utente.getEmail(), "Nuova Email");
		assertEquals(utente.getPassword(), "Password");
		assertEquals(utente.getStato(), StatoAccountUtente.SOSPESO.name());
		assertEquals(utente.getRuolo(), RuoloAccount.BIBLIOTECARIO.name());
	}
	
}
