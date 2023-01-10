package com.librarium.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import com.librarium.database.enums.StatoLibro;
import com.librarium.database.generated.org.jooq.tables.Generi;
import com.librarium.database.generated.org.jooq.tables.Libri;
import com.librarium.database.generated.org.jooq.tables.records.GeneriRecord;
import com.librarium.database.generated.org.jooq.tables.records.LibriRecord;

public class CatalogManager extends DatabaseConnection{

	/*========================== LIBRI ===========================*/
	
	public static List<LibriRecord> leggiLibri(String filtroParole, GeneriRecord Generi, String casaEditrice){
		return leggiLibri(filtroParole, Generi.getId().toString(), casaEditrice);
	}
	
	public static List<LibriRecord> leggiLibri(String filtroParole, String Generi, String casaEditrice) {
		
		try(Connection conn = connect()){
			DSLContext ctx = DSL.using(conn, SQLDialect.SQLITE);
			
			Condition condition = DSL.noCondition();
			
			if(filtroParole != null && !filtroParole.isBlank()) {
				condition = condition.and(
					Libri.LIBRI.TITOLO.contains(filtroParole)
					.or(Libri.LIBRI.AUTORE.contains(filtroParole))
				);
			}
			if(Generi != null && !Generi.isBlank())
				condition = condition.and(Libri.LIBRI.GENERE.contains(Generi));
			if(casaEditrice != null && !casaEditrice.isBlank())
				condition = condition.and(Libri.LIBRI.CASA_EDITRICE.eq(casaEditrice));
			
			Result<Record> result = 
					ctx.select()
					.from(Libri.LIBRI)
					.where(condition)
					.orderBy(Libri.LIBRI.TITOLO)
					.fetch();
			
			ArrayList<LibriRecord> libri = new ArrayList<LibriRecord>();
			
			result.forEach(libro -> libri.add(libro.into(Libri.LIBRI)));
			
			return libri;
		} catch(SQLException ex){
			System.out.println(ex.getMessage());
			return null;
		}
	}
	
	public static void aggiornaStatoLibro(int idLibro, StatoLibro nuovoStato) {
		
		try(Connection conn = connect()){
			DSLContext ctx = DSL.using(conn, SQLDialect.SQLITE);
			
			ctx.update(Libri.LIBRI)
				.set(Libri.LIBRI.STATO, nuovoStato.name())
				.where(Libri.LIBRI.ID.eq(idLibro))
				.execute();
		} catch(SQLException ex){
			System.out.println(ex.getMessage());
		}
		
	}
	/*======================================================================*/
	
	public static List<GeneriRecord> leggiGeneri() {
		
		try(Connection conn = connect()){
			DSLContext ctx = DSL.using(conn, SQLDialect.SQLITE);
			Result<Record> result = 
				ctx.select()
					.from(Generi.GENERI)
					.orderBy(Generi.GENERI.NOME)
					.fetch();
			
			ArrayList<GeneriRecord> generi = new ArrayList<GeneriRecord>();
			
			result.forEach(genere -> generi.add(genere.into(Generi.GENERI)));
			
			return generi;
		} catch(SQLException ex){
			System.out.println(ex.getMessage());
			return null;
		}
	}
	
	/*========================== CASE EDITRICI ===========================*/
	
	public static ArrayList<String> leggiCaseEditrici() {
		try (Connection conn = connect()) {

			DSLContext ctx = DSL.using(conn, SQLDialect.SQLITE);
			Result<Record1<String>> result = ctx.selectDistinct(Libri.LIBRI.CASA_EDITRICE)
					.from(Libri.LIBRI)
					.orderBy(Libri.LIBRI.CASA_EDITRICE)
					.fetch();

			ArrayList<String> caseEditrici = new ArrayList<String>();
			for (Record1<String> casaEditrice : result) {
				caseEditrici.add(casaEditrice.getValue(Libri.LIBRI.CASA_EDITRICE));
			}
			
			return caseEditrici;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	/*=====================================================================*/
}
