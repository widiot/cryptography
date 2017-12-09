package Practice;

public class RC4 {

	public static void main(String[] args) {
		RC4 rc4 = new RC4();

		String plaintext = "helloworld";
		String key = "key";

		String ciphertext = rc4.encrypt(plaintext, key);
		String decryptText = rc4.encrypt(ciphertext, key);

		System.out.print(
				"明文为：" + plaintext + "\n" + "密钥为：" + key + "\n\n" + "密文为：" + ciphertext + "\n" + "解密为：" + decryptText);
	}

	// 1 加密
	public String encrypt(final String plaintext, final String key) {
		Integer[] S = new Integer[256]; // S盒
		Character[] keySchedul = new Character[plaintext.length()]; // 生成的密钥流
		StringBuffer ciphertext = new StringBuffer();

		ksa(S, key);
		rpga(S, keySchedul, plaintext.length());

		for (int i = 0; i < plaintext.length(); ++i) {
			ciphertext.append((char) (plaintext.charAt(i) ^ keySchedul[i]));
		}

		return ciphertext.toString();
	}

	// 1.1 KSA--密钥调度算法--利用key来对S盒做一个置换，也就是对S盒重新排列
	public void ksa(Integer[] s, String key) {
		for (int i = 0; i < 256; ++i) {
			s[i] = i;
		}

		int j = 0;
		for (int i = 0; i < 256; ++i) {
			j = (j + s[i] + key.charAt(i % key.length())) % 256;
			swap(s, i, j);
		}
	}

	// 1.2 RPGA--伪随机生成算法--利用上面重新排列的S盒来产生任意长度的密钥流
	public void rpga(Integer[] s, Character[] keySchedul, int plaintextLength) {
		int i = 0, j = 0;
		for (int k = 0; k < plaintextLength; ++k) {
			i = (i + 1) % 256;
			j = (j + s[i]) % 256;
			swap(s, i, j);
			keySchedul[k] = (char) (s[(s[i] + s[j]) % 256]).intValue();
		}
	}

	// 1.3 置换
	public void swap(Integer[] s, int i, int j) {
		Integer mTemp = s[i];
		s[i] = s[j];
		s[j] = mTemp;
	}
}
