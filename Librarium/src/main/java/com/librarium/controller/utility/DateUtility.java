package com.librarium.controller.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtility {
	
	public static String getDataOggi() {
		// data di oggi
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String oggi = dtf.format(LocalDateTime.now()).toString();
		return oggi;
	}
	
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
