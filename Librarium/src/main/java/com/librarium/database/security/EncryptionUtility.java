package com.librarium.database.security;

import org.apache.commons.codec.digest.DigestUtils;

public class EncryptionUtility {
	
	public static String encrpyt(String flatString) {
		String ecnryptedString = DigestUtils.sha256Hex(flatString);
		
        return ecnryptedString;
	}
	
}
