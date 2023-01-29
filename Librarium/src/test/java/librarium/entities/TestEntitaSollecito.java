package librarium.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import com.librarium.database.generated.org.jooq.tables.records.SollecitiRecord;
import com.librarium.model.entities.Sollecito;

public class TestEntitaSollecito {

	private Sollecito creaSollecito() {
		SollecitiRecord sollecito = new SollecitiRecord(1, 2, 3, "01/01/2023");
		return new Sollecito(sollecito);
	}
	
	@Test
	public void testGetSollecito() {
		Sollecito sollecito = creaSollecito();
		
		assertEquals(sollecito.getId(), 1);
		assertEquals(sollecito.getIdUtente(), 2);
		assertEquals(sollecito.getIdLibro(), 3);
		assertEquals(sollecito.getData(), "01/01/2023");
	}
	
	@Test
	public void testSetSollecito() {
		Sollecito sollecito = creaSollecito();
		
		sollecito.setIdUtente(3);
		sollecito.setIdLibro(4);
		sollecito.setData("05/01/2023");
		
		assertEquals(sollecito.getIdUtente(), 3);
		assertEquals(sollecito.getIdLibro(), 4);
		assertEquals(sollecito.getData(), "05/01/2023");
	}
	
}
