/*
 * This file is generated by jOOQ.
 */
package com.librarium.database.generated.org.jooq.tables;


import com.librarium.database.generated.org.jooq.DefaultSchema;
import com.librarium.database.generated.org.jooq.Keys;
import com.librarium.database.generated.org.jooq.tables.records.CategorieRecord;

import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row2;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Categorie extends TableImpl<CategorieRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>Categorie</code>
     */
    public static final Categorie CATEGORIE = new Categorie();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<CategorieRecord> getRecordType() {
        return CategorieRecord.class;
    }

    /**
     * The column <code>Categorie.id</code>.
     */
    public final TableField<CategorieRecord, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>Categorie.nome</code>.
     */
    public final TableField<CategorieRecord, String> NOME = createField(DSL.name("nome"), SQLDataType.CLOB.nullable(false), this, "");

    private Categorie(Name alias, Table<CategorieRecord> aliased) {
        this(alias, aliased, null);
    }

    private Categorie(Name alias, Table<CategorieRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>Categorie</code> table reference
     */
    public Categorie(String alias) {
        this(DSL.name(alias), CATEGORIE);
    }

    /**
     * Create an aliased <code>Categorie</code> table reference
     */
    public Categorie(Name alias) {
        this(alias, CATEGORIE);
    }

    /**
     * Create a <code>Categorie</code> table reference
     */
    public Categorie() {
        this(DSL.name("Categorie"), null);
    }

    public <O extends Record> Categorie(Table<O> child, ForeignKey<O, CategorieRecord> key) {
        super(child, key, CATEGORIE);
    }

    @Override
    public Schema getSchema() {
        return DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public UniqueKey<CategorieRecord> getPrimaryKey() {
        return Keys.PK_CATEGORIE;
    }

    @Override
    public List<UniqueKey<CategorieRecord>> getKeys() {
        return Arrays.<UniqueKey<CategorieRecord>>asList(Keys.PK_CATEGORIE);
    }

    @Override
    public Categorie as(String alias) {
        return new Categorie(DSL.name(alias), this);
    }

    @Override
    public Categorie as(Name alias) {
        return new Categorie(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Categorie rename(String name) {
        return new Categorie(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Categorie rename(Name name) {
        return new Categorie(name, null);
    }

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row2<Integer, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }
}
