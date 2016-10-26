package com.wut.support.cipher;

import java.io.UnsupportedEncodingException;

import com.wut.support.StringHelper;
import com.wut.support.binary.Base64;
import com.wut.support.settings.SystemSettings;

/*
 * one-time pad algorithm
 * http://en.wikipedia.org/wiki/One-time_pad
 */
public class Cipher {
	private static final String KEY = SystemSettings.getInstance().getSetting("cipherKey");
	private static final String PREFIX = "OTP";

	public String encrypt(final String text) throws UnsupportedEncodingException {
		byte[] bytes = text.getBytes();
		byte[] xored = this.xor(bytes);
		//byte[] base64ed = Base64Coder.encode(xored);
		byte[] base64ed = Base64.encodeBase64(xored);
		String base64edString = new String(base64ed, "UTF-8");
		return PREFIX + base64edString;
	}

	public String decrypt(String hash) {
		if (hash.startsWith(PREFIX)) {
			hash = StringHelper.removePrefix(PREFIX, hash);
			
			try {
				return new String(this.xor(Base64.decodeBase64(hash.getBytes())),
						"UTF-8");
			} catch (java.io.UnsupportedEncodingException ex) {
				throw new IllegalStateException(ex);
			}
		}
		
		return null;
	}

	private byte[] xor(final byte[] input) {
		final byte[] output = new byte[input.length];
		final byte[] secret = KEY.getBytes();
		int spos = 0;
		for (int pos = 0; pos < input.length; ++pos) {
			output[pos] = (byte) (input[pos] ^ secret[spos]);
			spos += 1;
			if (spos >= secret.length) {
				spos = 0;
			}
		}
		return output;
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		Cipher fucked = new Cipher();
		
		String original = "holy_roller-!@#$%^&*(){}[]:;'?<>.,+=-\\/|~`1 2 3 4 5 6 7 8 9 0	And that's everything!";
		String encryted = fucked.encrypt(original);
		String dycryptedOriginal = fucked.decrypt(encryted);
		String reEncrypted = fucked.encrypt(dycryptedOriginal);
		String dycryptedAgain = fucked.decrypt(reEncrypted);
		
		System.out.println("1: " + original);
		System.out.println("2: " + encryted);
		System.out.println("3: " + dycryptedOriginal);
		System.out.println("4: " + reEncrypted);
		System.out.println("5: " + dycryptedAgain);
		
		Cipher c = new Cipher();
		

	}
	
	private static void printEncyptified(String text) {
		System.out.println(text);
	}
}

