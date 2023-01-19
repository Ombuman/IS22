package com.librarium.database.entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import com.librarium.application.utility.DateUtility;
import com.librarium.database.generated.org.jooq.tables.records.SollecitiRecord;

public class Sollecito {
	
	private SollecitiRecord datiSollecito;
	
	public Sollecito(SollecitiRecord datiSollecito) {
		this.datiSollecito = datiSollecito;
	}
	
	public Integer getIdUtente() {
		return datiSollecito.getUtente();
	}
	
	public Integer getIdLibro() {
		return datiSollecito.getLibro();
	}
	
	public String getData() {
		return datiSollecito.getData();
	}
	
	public void setData(String data) {
		datiSollecito.setData(data);
	}
}
