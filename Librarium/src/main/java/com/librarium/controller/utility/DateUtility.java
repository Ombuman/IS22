package com.librarium.controller.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 
 * Classe per lavorare con le date e le loro formattazioni
 *
 */
public class DateUtility {
	
	/**
	 * Restituisce la data di oggi come stringa
	 * 
	 * @return La data di oggi in formato "dd/MM/yyyy"
	 */
	public static String getDataOggi() {
		// data di oggi
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String oggi = dtf.format(LocalDateTime.now()).toString();
		return oggi;
	}
	
	/**
	 * Calcola la differenza di giorni tra due date
	 * @param dataInizio La data di inizio in formato "dd/MM/yyyy"
	 * @param dataFine La data di fine in formato "dd/MM/yyyy"
	 * 
	 * @return La differenza di giorni tra le due date
	 */
	public static long getDifferenzaGiorni(String dataInizio, String dataFine) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		try {
			Date inizio = sdf.parse(dataInizio);
			Date fine = sdf.parse(dataFine);
			
			long diff = fine.getTime() - inizio.getTime();
		    return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * Aggiunge giorni ad una data
	 * @param date La data di partenza in formato "dd/MM/yyyy"
	 * @param giorni Il numero di giorni da aggiungere
	 * 
	 * @return La data risultante dopo l'aggiunta dei giorni
	 */
	public static String aggiungiGiorni(String date, int giorni) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		Calendar calendar = Calendar.getInstance();
		
		try {
			calendar.setTime(sdf.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		calendar.add(Calendar.DAY_OF_MONTH, giorni);
		
		return sdf.format(calendar.getTime());
	}
}
