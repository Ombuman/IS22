package com.librarium.controller.utility;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Classe che fornisce metodi per la crittografia di stringhe.
 */
public class EncryptionUtility {
	
	/**
	 * Metodo che codifica una stringa in formato SHA-256.
	 * @param flatString stringa da codificare
	 * 
	 * @return stringa codificata in formato SHA-256
	*/
	public static String encrpyt(String flatString) {
		String ecnryptedString = DigestUtils.sha256Hex(flatString);
		
        return ecnryptedString;
	}
	
}
