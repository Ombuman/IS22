package com.librarium.model.enums;

/**
 * Enumerazione che definisce i ruoli degli utenti presenti nell'applicazione.
 */
public enum RuoloAccount {
	/* Utente con privilegi limitati. */
	UTENTE, 
	/* Utente con privilegi estesi, che può eseguire operazioni riservate agli amministratori. */
	BIBLIOTECARIO;
}
