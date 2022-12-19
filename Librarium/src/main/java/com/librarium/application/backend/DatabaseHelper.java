package com.librarium.application.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import com.librarium.database.generated.org.jooq.tables.Autori;
import com.librarium.database.generated.org.jooq.tables.Categorie;
import com.librarium.database.generated.org.jooq.tables.Libri;
import com.librarium.database.generated.org.jooq.tables.records.AutoriRecord;
import com.librarium.database.generated.org.jooq.tables.records.CategorieRecord;
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

	public static List<LibriRecord> leggiLibri() {
		
		try(Connection conn = connect()){
			DSLContext ctx = DSL.using(conn, SQLDialect.SQLITE);
			Result<Record> result = 
				ctx.select()
					.from(Libri.LIBRI)
					.fetch();
			
			ArrayList<LibriRecord> libri = new ArrayList<LibriRecord>();
			
			result.forEach(libro -> libri.add((LibriRecord) libro));
			
			return libri;
		} catch(SQLException ex){
			System.out.println(ex.getMessage());
			return null;
		}
	}
	
	public static List<Record> leggiLibriAutore() {
		
		try(Connection conn = connect()){
			DSLContext ctx = DSL.using(conn, SQLDialect.SQLITE);
			
			return ctx.select()
					.from(Libri.LIBRI)
					.join(Autori.AUTORI).on(Libri.LIBRI.AUTORE.eq(Autori.AUTORI.ID))
					.fetch();
		} catch(SQLException ex){
			System.out.println(ex.getMessage());
			return null;
		}
	}
	
	public static List<CategorieRecord> leggiCategorie() {
		
		try(Connection conn = connect()){
			DSLContext ctx = DSL.using(conn, SQLDialect.SQLITE);
			Result<Record> result = 
				ctx.select()
					.from(Categorie.CATEGORIE)
					.fetch();
			
			ArrayList<CategorieRecord> categorie = new ArrayList<CategorieRecord>();
			
			result.forEach(libro -> categorie.add((CategorieRecord) libro));
			
			return categorie;
		} catch(SQLException ex){
			System.out.println(ex.getMessage());
			return null;
		}
	}
	
	public static List<LibriRecord> leggiLibriCategoria(CategorieRecord categoria) {
		
		if(categoria == null)
			return null;
	
		return leggiLibriCategoria(categoria.getId().toString());
	}
	
	public static List<LibriRecord> leggiLibriCategoria(String id){
		try(Connection conn = connect()){
			DSLContext ctx = DSL.using(conn, SQLDialect.SQLITE);
			Result<Record> result = 
				ctx.select()
					.from(Libri.LIBRI)
					.where(Libri.LIBRI.CATEGORIA.contains(id))
					.fetch();
			
			ArrayList<LibriRecord> libri = new ArrayList<LibriRecord>();
			
			result.forEach(libro -> libri.add((LibriRecord) libro));
			
			return libri;
		} catch(SQLException ex){
			System.out.println(ex.getMessage());
			return null;
		}
	}
}
