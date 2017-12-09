package FifthUnit;

import java.math.BigInteger;
import java.util.Random;

public class RSA {
	private static BigInteger n; // large prime
	private static BigInteger e; // public key
	private static BigInteger b; // private key
	private static BigInteger p; // prime
	private static BigInteger q; // prime

	public static void main(String[] args) {
		String x = "The quick brown fox jumps over the lazy dog";
		RSA rsa = new RSA();
		String ciphertext = rsa.operation(x, "encrypt");
		//System.out.println(
		//		"\npublic key:" + "(" + n + "," + b + ")" + "\n" + "private key:" + "(" + p + "," + q + "," + a + ")");
		//System.out.println("plaintext:" + x + "\n" + "ciphertext:" + ciphertext);
	}

	// RSA encryption
	public String operation(String x, String model) {
		String ciphertext = new String();

		//get n,a,b
		giveKey();

		// encrypt or decrypt
		if (model.equals("encrypt")) {

		} else if (model.equals("decrypt")) {

		} else {
			System.out.println("You have gave a wrong model !");
		}

		return ciphertext;
	}

	// give public key and private key
	public void giveKey() {
		//get n,a,b,p,q
		producePQ();
		n = p.multiply(q);
		BigInteger eulerN = p.subtract(new BigInteger("1")).multiply(q.subtract(new BigInteger("1")));
		randomB(eulerN);
		inverseB(b);
	}

	// large prime generation
	public void producePQ() {
		while (true) {
			// produce p and q randomly
			p = BigInteger.probablePrime(32, new Random());
			q = BigInteger.probablePrime(32, new Random());
			while (p.equals(q)) {
				p = BigInteger.probablePrime(32, new Random());
				q = BigInteger.probablePrime(32, new Random());
			}
			System.out.println("p:" + p + "\n" + "q:" + q);
		}

	}

	// produce a random
	public void randomB(BigInteger eulerN) {
	}

	// calculate the multiplicative inverse
	public void inverseB(BigInteger b) {
	}

	// square and multiply
	public BigInteger squareMultiply(BigInteger x, BigInteger c, BigInteger n) {
		BigInteger z = new BigInteger("");
		return z;
	}
}
