package librarium.utenti;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;

import com.librarium.database.PrestitiManager;
import com.librarium.database.UsersManager;
import com.librarium.model.entities.Prestito;
import com.librarium.model.entities.Sollecito;

import librarium.TestDB;
import librarium.prestiti.TestPrestiti;

public class TestSolleciti extends TestDB{
	
	@Test
	public void testSolleciti() {
		// invio sollecito
		TestPrestiti prestiti = new TestPrestiti();
		Integer idPrestito = prestiti.creaPrestito();
		Prestito prestito = PrestitiManager.getInstance().getPrestito(idPrestito);
		
		UsersManager.getInstance().inviaSollecito(prestito);
		
		List<Sollecito> solleciti = UsersManager.getInstance().getSollecitiUtente(prestito.getIdUtente());
		
		assertThat(solleciti.size() > 0);
		
		// rimozione solleciti
		List<Sollecito> sollecitiUtenteIniziale = UsersManager.getInstance().getSollecitiUtenteLibro(1, 1);
		UsersManager.getInstance().rimuoviSolleciti(1, 1);
		List<Sollecito> sollecitiUtenteFinale = UsersManager.getInstance().getSollecitiUtenteLibro(1, 1);
		
		assertThat(sollecitiUtenteFinale.size() < sollecitiUtenteIniziale.size());
	}
	
	
	@Test
	public void testGetSolleciti() {
		List<Sollecito> sollecitiUtente = UsersManager.getInstance().getSollecitiUtente(1);
		List<Sollecito> sollecitiNull = UsersManager.getInstance().getSollecitiUtente(null);
		
		assertNotNull(sollecitiUtente);
		assertNull(sollecitiNull);
	}
	
	@Test
	public void testGetSollecitiLibro() {
		List<Sollecito> sollecitiUtenteLibro = UsersManager.getInstance().getSollecitiUtenteLibro(1, 1);
		List<Sollecito> sollecitiUtenteNull = UsersManager.getInstance().getSollecitiUtenteLibro(null, 1);
		List<Sollecito> sollecitiLibroNull = UsersManager.getInstance().getSollecitiUtenteLibro(1, null);
		List<Sollecito> sollecitiNull = UsersManager.getInstance().getSollecitiUtenteLibro(null, null);
		
		assertNotNull(sollecitiUtenteLibro);
		assertNull(sollecitiUtenteNull);
		assertNull(sollecitiLibroNull);
		assertNull(sollecitiNull);
	}
	
}
