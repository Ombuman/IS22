package com.librarium.application.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.SelectConditionStep;
import org.jooq.impl.DSL;

import com.librarium.database.generated.org.jooq.tables.Autori;
import com.librarium.database.generated.org.jooq.tables.Categorie;
import com.librarium.database.generated.org.jooq.tables.Libri;
import com.librarium.database.generated.org.jooq.tables.records.AutoriRecord;
import com.librarium.database.generated.org.jooq.tables.records.CategorieRecord;
import com.librarium.database.generated.org.jooq.tables.records.LibriCompletiRecord;
import com.librarium.database.generated.org.jooq.tables.records.LibriRecord;

public class DatabaseHelper {

	private static final String DATABASE_PATH = "/data/librarium.db";

	private static Connection connect() {
		String url = "jdbc:sqlite:" + System.getProperty("user.dir") + DATABASE_PATH;
		Connection conn = null;

		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return conn;
	}

	public static void updateAutore(AutoriRecord autore) {
		try (Connection conn = connect()) {
			DSLContext ctx = DSL.using(conn, SQLDialect.SQLITE);
			ctx.update(Autori.AUTORI)
				.set(Autori.AUTORI.NOME, autore.getNome())
				.where(Autori.AUTORI.ID.eq(autore.getId()))
				.execute();
			conn.commit();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public static ArrayList<AutoriRecord> leggiAutori() {
		try (Connection conn = connect()) {

			DSLContext ctx = DSL.using(conn, SQLDialect.SQLITE);
			Result<Record> result = ctx.select().from(Autori.AUTORI).fetch();

			ArrayList<AutoriRecord> autori = new ArrayList<AutoriRecord>();
			for (Record r : result) {
				autori.add(new AutoriRecord((int) r.get("id"), (String) r.get("nome")));
			}
			
			return autori;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	/*==================== LIBRI =====================*/
	
	public static List<LibriCompletiRecord> leggiLibri(String titolo) {
		return leggiLibri(titolo, null, null);
	}
	
	public static List<LibriCompletiRecord> leggiLibri(CategorieRecord categoria) {
		return categoria == null ? null : leggiLibri(null, categoria.getId().toString(), null);
	}
	
	public static List<LibriCompletiRecord> leggiLibri(AutoriRecord autore) {
		return autore == null ? null : leggiLibri(null, null, autore.getId());
	}
	
	public static List<LibriCompletiRecord> leggiLibri(String titolo, CategorieRecord categoria) {
		if(categoria == null)
			return null;
		return leggiLibri(titolo, categoria.getId().toString(), null);
	}
	
	public static List<LibriCompletiRecord> leggiLibri(String titolo, String categoria, Integer autore) {
		
		try(Connection conn = connect()){
			DSLContext ctx = DSL.using(conn, SQLDialect.SQLITE);
			
			Condition condition = DSL.noCondition();
			
			if(titolo != null && !titolo.isBlank())
				condition = condition.and(Libri.LIBRI.TITOLO.contains(titolo));
			if(categoria != null && !categoria.isBlank())
				condition = condition.and(Libri.LIBRI.CATEGORIA.contains(categoria));
			if(autore != null)
				condition = condition.and(Libri.LIBRI.AUTORE.eq(autore));
			
			Result<Record> result = 
					ctx.select()
					.from(Libri.LIBRI
							.join(Autori.AUTORI)
							.on(Libri.LIBRI.AUTORE.eq(Autori.AUTORI.ID)
						)
					)
					.where(condition)
					.orderBy(Libri.LIBRI.TITOLO)
					.fetch();
			
			ArrayList<LibriCompletiRecord> libri = new ArrayList<LibriCompletiRecord>();
			
			result.forEach(libro -> {
				AutoriRecord datiAutore = new AutoriRecord((Integer)libro.get("autore"), (String)libro.get("nome"));
				libri.add(
					new LibriCompletiRecord(
							libro.get("id"), 
							libro.get("titolo"), 
							libro.get("copertina"), 
							libro.get("anno"), 
							libro.get("categoria"), 
							datiAutore, 
							libro.get("casa_editrice")
					)
				);
			});
			
			return libri;
		} catch(SQLException ex){
			System.out.println(ex.getMessage());
			return null;
		}
	}
	
	/*======================================================================*/
	
	public static List<CategorieRecord> leggiCategorie() {
		
		try(Connection conn = connect()){
			DSLContext ctx = DSL.using(conn, SQLDialect.SQLITE);
			Result<Record> result = 
				ctx.select()
					.from(Categorie.CATEGORIE)
					.orderBy(Categorie.CATEGORIE.NOME)
					.fetch();
			
			ArrayList<CategorieRecord> categorie = new ArrayList<CategorieRecord>();
			
			result.forEach(categoria -> categorie.add((CategorieRecord) categoria));
			
			return categorie;
		} catch(SQLException ex){
			System.out.println(ex.getMessage());
			return null;
		}
	}
}
