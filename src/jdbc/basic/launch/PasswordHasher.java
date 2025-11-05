package jdbc.basic.launch;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//🧂 SALT MIGRATION NOTE:
//This implementation uses plain SHA-256 without salting.
//For stronger security (especially in public-facing systems), consider migrating to salted hashes or bcrypt.
//
//🔄 To migrate:
//- Generate a random salt per user (e.g., UUID or SecureRandom)
//- Store salt alongside hashed password in DB (e.g., separate column or JSON blob)
//- Modify hash(...) to accept salt and concatenate: hash(input + salt)
//- During verification, retrieve salt and stored hash, then compare hash(input + salt)
//
//⚠️ Why salt matters:
//- Prevents rainbow table attacks
//- Ensures identical passwords produce different hashes
//- Adds entropy to predictable inputs
//
//🧠 For production-grade login systems, prefer bcrypt (e.g., via jBCrypt) with built-in salting and cost factor.


public class PasswordHasher {
 // 🔐 PasswordHasher: Securely handles password hashing and verification

// ✅ Hashing passwords before storing them in the DB
// ✅ Verifying passwords during login by comparing input with stored hash

// 🔄 Uses SHA-256 — a cryptographic hash function (Secure Hash Algorithm,
// 256-bit output)
//	    - One-way function: converts input string to a fixed-length hash
//	    - Irreversible: original string cannot be recovered from hash
//	    - Commonly used in password hashing, file integrity checks, blockchain, etc.

// 🧰 Java's MessageDigest class provides access to SHA-256
//	    - getInstance("SHA-256") returns a SHA-256 hashing engine
//	    - digest(...) computes the hash as a byte array
//	    - Each byte is converted to a 2-digit hexadecimal string using `%02x`
//	      → e.g., byte 10 becomes "0a", 255 becomes "ff", 60 becomes "3c"

	public static String hash(String input) {
		
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] bytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder();
			for(byte b: bytes) {
				sb.append(String.format("%02x", b));//converts hashed-byte to hex.
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			
			throw new RuntimeException("SHA-256 algorithm not  available",e);
		}
	}
	
	public static  boolean verify(String input,String storedHash) {
		if(input == null || storedHash == null)return false;
		String inputHash = hash(input);
		return inputHash.equals(storedHash);
	}
}
