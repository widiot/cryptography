package FifthUnit;

import java.math.BigInteger;
import java.util.Random;

public class RSAs {
	private static BigInteger n; // large prime
	private static BigInteger e; // public key
	private static BigInteger b; // private key
	private static BigInteger p; // prime
	private static BigInteger q; // prime

	public static void main(String[] args) {
		String plaintext = "The quick brown fox jumps over the lazy dog";
		RSAs rsas = new RSAs();
		rsas.giveKey();
		BigInteger[] encrypt = rsas.encrypt(plaintext);
		String decrypt = rsas.decrypt(encrypt);
		System.out.println("\nplaintext:" + plaintext + "\n\nencrpyt:");
		for (int i = 0; i < encrypt.length; ++i) {
			System.out.println(encrypt[i]);
		}
		System.out.println("\ndecrypt:" + decrypt);
	}

	// RSA encryption
	public BigInteger[] encrypt(String plaintext) {
		BigInteger[] encrypt = new BigInteger[plaintext.length()];
		BigInteger m, c;
		for (int i = 0; i < plaintext.length(); ++i) {
			m = BigInteger.valueOf(plaintext.charAt(i));
			c = m.modPow(e, n);
			encrypt[i] = c;
		}
		return encrypt;
	}

	// RSA decryption
	public String decrypt(BigInteger[] encrypt) {
		StringBuffer plaintext = new StringBuffer();
		BigInteger m, c;
		for (int i = 0; i < encrypt.length; ++i) {
			c = encrypt[i];
			m = c.modPow(b, n);
			plaintext.append((char) m.intValue());
		}
		return plaintext.toString();
	}

	// give public key and private key
	public void giveKey() {
		// get p,q,n,e,b
		producePQ();
		n = p.multiply(q);
		BigInteger eulerN = p.subtract(new BigInteger("1")).multiply(q.subtract(new BigInteger("1")));
		produceEB(eulerN);
		System.out.println("n:" + n + "\np:" + p + "\nq:" + q + "\ne:" + e + "\nb:" + b);
	}

	// large prime p and q generation
	public void producePQ() {
		p = BigInteger.probablePrime(32, new Random());
		q = BigInteger.probablePrime(32, new Random());
		while (p.equals(q)) {
			p = BigInteger.probablePrime(32, new Random());
			q = BigInteger.probablePrime(32, new Random());
		}
	}

	// produce public key e,private key b
	public void produceEB(BigInteger eulerN) {
		e = BigInteger.probablePrime((int) (Math.random() * 63 + 2), new Random());
		while (e.compareTo(eulerN) != -1 | eulerN.divide(e).equals(new BigInteger("0"))) {
			e = BigInteger.probablePrime((int) (Math.random() * 63 + 2), new Random());
		}
		//e = BigInteger.valueOf(65537);
		b = e.modInverse(eulerN);
	}
}
