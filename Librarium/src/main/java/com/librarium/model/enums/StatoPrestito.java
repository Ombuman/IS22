package com.librarium.model.enums;

/**
 * Enumerazione che definisce lo stato di un prestito.
 */
public enum StatoPrestito {
	/* Il libro è stato prenotato ma non ancora ritirato. */
	PRENOTATO, 
	/* Il libro è stato prenotato ed è stato ritirato. */
	RITIRATO, 
	/* Il libro è stato restituito. */
	CONCLUSO
}
