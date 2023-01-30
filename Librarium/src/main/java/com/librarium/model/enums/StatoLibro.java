package com.librarium.model.enums;

/**
 * Enumerazione che rappresenta lo stato di un libro all'interno della biblioteca.
 */
public enum StatoLibro{
	/* Stato che indica che il libro è disponibile per essere prestato. */
	DISPONIBILE, 
	/** Stato che indica che il libro non è disponibile per essere prestato, 
	 * ad esempio perché già prestato ad un altro utente. */
	NON_DISPONIBILE
}
