package librarium.entities;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import com.librarium.database.generated.org.jooq.tables.records.PrestitiRecord;
import com.librarium.model.entities.Libro;
import com.librarium.model.entities.Prestito;
import com.librarium.model.entities.Utente;
import com.librarium.model.enums.StatoAccountUtente;
import com.librarium.model.enums.StatoPrestito;

public class TestEntitaPrestito {
	
	private Prestito creaPrestito() {
		TestEntitaLibro testEntitaLibro = new TestEntitaLibro();
		Libro libro = testEntitaLibro.creaLibro();
		
		TestEntitaUtente testEntitaUtente = new TestEntitaUtente();
		Utente utente = testEntitaUtente.creaUtente();
		
		PrestitiRecord prestito = new PrestitiRecord(1, "01/12/2022", "04/12/2022", null, StatoPrestito.PRENOTATO.name(), libro.getID(), utente.getId(), "20/01/2023");
		return new Prestito(prestito, utente.getDatiUtente(), libro.getLibro());
	}
	
	@Test
	public void testGetPrestito() {
		Prestito prestito = creaPrestito();
		
		assertEquals(prestito.getId(), 1);
		assertEquals(prestito.getDataPrenotazione(), "01/12/2022");
		assertEquals(prestito.getDataInizio(), "04/12/2022");
		assertEquals(prestito.getDataFine(), null);
		assertEquals(prestito.getStato(), StatoPrestito.PRENOTATO);
		assertEquals(prestito.getDataUltimoSollecito(), "20/01/2023");
		assertEquals(prestito.getStatoUtente(), StatoAccountUtente.ATTIVO.name());
		assertEquals(prestito.getEmail(), "Email");
		assertNotNull(prestito.getUtente());
		assertNotNull(prestito.getLibro());
		assertEquals(prestito.getTitolo(), "Titolo");
		assertEquals(prestito.isInvioSollecitoPossibile(), true);
	}
	
}
