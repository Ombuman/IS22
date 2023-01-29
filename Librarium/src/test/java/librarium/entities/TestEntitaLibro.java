package librarium.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Set;

import org.junit.Test;

import com.librarium.database.generated.org.jooq.tables.records.GeneriRecord;
import com.librarium.database.generated.org.jooq.tables.records.LibriRecord;
import com.librarium.model.entities.Libro;
import com.librarium.model.enums.StatoLibro;

public class TestEntitaLibro {
	
	public Libro creaLibro() {
		GeneriRecord genere = new GeneriRecord(1, "Genere");
		ArrayList<GeneriRecord> generi = new ArrayList<>();
		generi.add(genere);
		
		LibriRecord datiLibro = new LibriRecord(1, "Titolo", "Descrizione", "Copertina", null, "2022", "Autore", "Casa Editrice", StatoLibro.DISPONIBILE.name());
		
		Libro libro = new Libro();
		libro.setLibro(datiLibro);
		libro.setGeneri(generi);
		
		return libro;
	}
	
	@Test
	public void testGetLibro() {
		Libro libro = creaLibro();
		
		assertEquals(libro.getID(), 1);
		assertEquals(libro.getTitolo(), "Titolo");
		assertEquals(libro.getDescrizione(), "Descrizione");
		assertEquals(libro.getCopertina(), "Copertina");
		assertThat(libro.getGeneri().size() == 1);
		assertEquals(libro.getGeneriID(), "1");
		assertEquals(libro.getGeneriString(), "Genere");
		assertEquals(libro.getAnno(), "2022");
		assertEquals(libro.getAutore(), "Autore");
		assertEquals(libro.getCasaEditrice(), "Casa Editrice");
		assertEquals(libro.getStato(), StatoLibro.DISPONIBILE.name());
	}
	
	@Test
	public void testSetLibro() {
		Libro libro = creaLibro();
		
		GeneriRecord nuovoGenere = new GeneriRecord(2, "Genere_2");
		Set<GeneriRecord> generiLibro = libro.getGeneri();
		generiLibro.add(nuovoGenere);
		libro.setGeneri(new ArrayList<>(generiLibro));
		
		libro.setId(null);
		libro.setTitolo("Nuovo Titolo");
		libro.setDescrizione("Nuovo Descrizione");
		libro.setCopertina("Nuova Copertina");
		libro.setAnno("2023");
		libro.setAutore("Nuovo Autore");
		libro.setCasaEditrice("Nuova Casa Editrice");
		libro.setStato(StatoLibro.NON_DISPONIBILE.name());
		
		assertEquals(libro.getID(), 1);
		assertEquals(libro.getTitolo(), "Nuovo Titolo");
		assertEquals(libro.getDescrizione(), "Nuovo Descrizione");
		assertEquals(libro.getCopertina(), "Nuova Copertina");
		assertThat(libro.getGeneri().size() == 2);
		assertEquals(libro.getGeneriID(), "1, 2");
		assertEquals(libro.getGeneriString(), "Genere, Genere_2");
		assertEquals(libro.getAnno(), "2023");
		assertEquals(libro.getAutore(), "Nuovo Autore");
		assertEquals(libro.getCasaEditrice(), "Nuova Casa Editrice");
		assertEquals(libro.getStato(), StatoLibro.NON_DISPONIBILE.name());
		
		libro.setId(2);
		libro.setStato(StatoLibro.DISPONIBILE);
		
		assertEquals(libro.getID(), 2);
		assertEquals(libro.getStato(), StatoLibro.DISPONIBILE.name());
	}
	
}
