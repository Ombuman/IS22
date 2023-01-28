package librarium.prestiti;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import com.librarium.database.CatalogManager;
import com.librarium.database.PrestitiManager;
import com.librarium.database.UsersManager;
import com.librarium.model.entities.Libro;
import com.librarium.model.entities.Prestito;
import com.librarium.model.entities.Utente;
import com.librarium.model.enums.StatoPrestito;

import librarium.TestDB;

public class TestPrestiti extends TestDB{
	
	public Integer creaPrestito() {
		Utente utente = UsersManager.getInstance().getUtente(1);
		Libro libro = CatalogManager.getInstance().leggiLibro(1);
		return PrestitiManager.getInstance().creaPrestito(utente.getDatiUtente(), libro.getLibro());
	}
	
	@Test
	public void testCreaPrestito() {
		ArrayList<Prestito> listaPrestitiIniziale = PrestitiManager.getInstance().getPrestitiUtente(1);
		
		Integer idPrestito = creaPrestito();
				
		ArrayList<Prestito> listaPrestitiFinale = PrestitiManager.getInstance().getPrestitiUtente(1);
		
		assertNotNull(idPrestito);
		assertThat(listaPrestitiFinale.size() == listaPrestitiIniziale.size() + 1);
	}
	
	@Test
	public void testAnnullaPrenotazione() {
		ArrayList<Prestito> listaPrestiti = PrestitiManager.getInstance().getPrestitiUtente(1);
		boolean risultato = PrestitiManager.getInstance().annullaPrenotazione(listaPrestiti.get(0));
		
		assertEquals(risultato, true);
	}
	
	@Test
	public void testAttivaPrestito() {
		Utente utente = UsersManager.getInstance().getUtente(1);
		Libro libro = CatalogManager.getInstance().leggiLibro(1);
		Integer idPrestito = PrestitiManager.getInstance().creaPrestito(utente.getDatiUtente(), libro.getLibro());
		
		Prestito nuovoPrestito = PrestitiManager.getInstance().getPrestito(idPrestito);
		
		PrestitiManager.getInstance().attivaPrestito(nuovoPrestito);
		
		Prestito prestitoAttivo = PrestitiManager.getInstance().getPrestito(idPrestito);
		
		assertEquals(prestitoAttivo.getStato(), StatoPrestito.RITIRATO);
	}
	
	@Test
	public void testConcludiPrestito() {		
		ArrayList<Prestito> nuovoPrestito = PrestitiManager.getInstance().getPrestitiUtente(1, StatoPrestito.RITIRATO.name());
		
		PrestitiManager.getInstance().concludiPrestito(nuovoPrestito.get(0));
		
		Prestito prestitoAttivo = PrestitiManager.getInstance().getPrestito(nuovoPrestito.get(0).getId());
		
		assertEquals(prestitoAttivo.getStato(), StatoPrestito.CONCLUSO);
	}
	
}
