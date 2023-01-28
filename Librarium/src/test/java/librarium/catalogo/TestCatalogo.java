package librarium.catalogo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.librarium.database.CatalogManager;
import com.librarium.database.generated.org.jooq.tables.records.GeneriRecord;
import com.librarium.database.generated.org.jooq.tables.records.LibriRecord;
import com.librarium.model.entities.Libro;
import com.librarium.model.enums.StatoLibro;

import librarium.TestDB;

public class TestCatalogo extends TestDB{
	
	@Test
	public void testAggiungiLibro() {
		LibriRecord datiLibro = new LibriRecord(null, "Libro Test", "Descrizione Test", "Copertina Test", "", "2023", "Autore Test", "Casa Editrice Test", StatoLibro.DISPONIBILE.name());
		
		List<GeneriRecord> listaGeneri = CatalogManager.getInstance().leggiGeneri();
		List<GeneriRecord> listaGeneriLibro = new ArrayList<>();
		listaGeneriLibro.add(listaGeneri.get(0));
		listaGeneriLibro.add(listaGeneri.get(1));
		
		Libro libro = new Libro(datiLibro, listaGeneriLibro);
		Integer idNuovoLibro = CatalogManager.getInstance().aggiungiLibro(libro);
		
		Libro nuovoLibro = CatalogManager.getInstance().leggiLibro(idNuovoLibro);
		
		assertEquals(nuovoLibro.getTitolo(), "Libro Test");
		assertEquals(nuovoLibro.getDescrizione(), "Descrizione Test");
		assertEquals(nuovoLibro.getAnno(), "2023");
		assertEquals(nuovoLibro.getAutore(), "Autore Test");
		assertEquals(nuovoLibro.getCasaEditrice(), "Casa Editrice Test");
		assertEquals(nuovoLibro.getStato(), StatoLibro.DISPONIBILE.name());
	}
	
	@Test
	public void testRicercaLibro() {
		List<Libro> libriTitoloTest = CatalogManager.getInstance().leggiLibri("Libro Test", "", "");
		List<Libro> libriGenereTest = CatalogManager.getInstance().leggiLibri("", CatalogManager.getInstance().leggiGeneri().get(0), "");
		List<Libro> libriCasaEditriceTest = CatalogManager.getInstance().leggiLibri("", "", "Casa Editrice Test");
		
		assertThat(libriTitoloTest.size() > 0);
		assertThat(libriGenereTest.size() > 0);
		assertThat(libriCasaEditriceTest.size() > 0);
	}
	
	@Test
	public void testAggiornaLibro() {
		Libro libroIniziale = CatalogManager.getInstance().leggiLibro(1);
		Libro datiLibroNuovi = libroIniziale;
		
		datiLibroNuovi.setTitolo("Titolo Modificato");
		
		CatalogManager.getInstance().aggiornaLibro(datiLibroNuovi);
		CatalogManager.getInstance().aggiornaStatoLibro(datiLibroNuovi.getID(), StatoLibro.NON_DISPONIBILE);
		
		Libro libroAggiornato = CatalogManager.getInstance().leggiLibro(datiLibroNuovi.getID());
		
		assertEquals(libroAggiornato.getTitolo(), "Titolo Modificato");
		assertEquals(libroAggiornato.getStato(), StatoLibro.NON_DISPONIBILE.name());
		assertNotEquals(libroIniziale, libroAggiornato);
	}
	
	@Test
	public void testRimuoviLibro_Finale() {		
		CatalogManager.getInstance().rimuoviLibro(1);
		
		Libro libroEliminato = CatalogManager.getInstance().leggiLibro(1);
		
		assertNull(libroEliminato);
	}
	
	@Test
	public void testAggiungiGenere() {
		GeneriRecord nuovoGenere = new GeneriRecord(null, "Test Genere");
		Integer idNuovoGenere = CatalogManager.getInstance().aggiungiGenere(nuovoGenere);
		
		GeneriRecord genereInserito = CatalogManager.getInstance().leggiGenere(idNuovoGenere);
		
		assertEquals(genereInserito.getNome(), "Test Genere");
	}
	
	@Test
	public void testRimuoviGenere() {
		List<GeneriRecord> listaGeneri = CatalogManager.getInstance().leggiGeneri();
		GeneriRecord genereDaRimuovere = listaGeneri.get(0);
		
		CatalogManager.getInstance().rimuoviGenere(genereDaRimuovere.getId());
		
		GeneriRecord genereRimosso = CatalogManager.getInstance().leggiGenere(genereDaRimuovere.getId());
		
		assertNull(genereRimosso);
	}
	
	@Test
	public void testGetNumeroLibroGenere() {
		Integer numeroLibroNull = CatalogManager.getInstance().getNumeroLibriGenere(null);
		Integer numeroLibriGenere1 = CatalogManager.getInstance().getNumeroLibriGenere(1);
		
		GeneriRecord genere1 = CatalogManager.getInstance().leggiGenere(1);
		
		List<Libro> libriGenere1 = CatalogManager.getInstance().leggiLibri("", genere1.getId().toString(), "");
		
		assertNull(numeroLibroNull);
		assertEquals(numeroLibriGenere1, libriGenere1.size());
	}
	
}
