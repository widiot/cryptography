package FourthUnit;

public class SHA {
	public static void main(String[] args) {
		String x1 = "";
		String x2 = "The quick brown fox jumps over the lazy dog";
		SHA sha = new SHA();
		String hexDigest1 = sha.encrypt(x1);
		String hexDigest2 = sha.encrypt(x2);
		System.out.println("empty:" + hexDigest1 + "\nstring:" + hexDigest2);
	}

	// padding
	public StringBuffer PAD(String x) {
		int ml; // message length
		StringBuffer mbStr = new StringBuffer(); // message convert to binary string
		for (int i = 0; i < x.length(); ++i) {
			StringBuffer temp = new StringBuffer(Long.toBinaryString(x.charAt(i))); // word binary string
			while (temp.length() < 8) {
				temp.insert(0, 0);
			}
			mbStr.append(temp);
		}
		ml = mbStr.length();

		//calculate the d
		int d = 447 - ml; // the number of zeros to complement the message
		while (d < 0) {
			d += 512;
		}

		//complement the message length to 64 bits
		StringBuffer l = new StringBuffer(Long.toBinaryString(ml));
		while (l.length() < 64) {
			l.insert(0, 0);
		}

		//padding mbStr
		mbStr.append(1);
		for (int i = 0; i < d; ++i) {
			mbStr.append(0);
		}
		mbStr.append(l);
		return mbStr;
	}

	//loop left shift
	public StringBuffer ROTL(StringBuffer temp, int s) {
		while (temp.length() < 32) {
			temp.insert(0, 0);
		}

		//loop left shift
		for (int i = 0; i < s; ++i) {
			temp.append(temp.charAt(0));
			temp.deleteCharAt(0);
		}
		return temp;
	}

	// SHA-1
	public String encrypt(String x) {
		long h0 = 0x67452301L;
		long h1 = 0xEFCDAB89L;
		long h2 = 0x98BADCFEL;
		long h3 = 0x10325476L;
		long h4 = 0xC3D2E1F0L;

		//SHA-1-PAD
		StringBuffer mbStr = PAD(x);

		//group mbStr by 512 bits
		int groupNum = mbStr.length() / 512;
		StringBuffer[] mbStrGroup = new StringBuffer[groupNum];
		for (int i = 0; i < groupNum; ++i) {
			mbStrGroup[i] = new StringBuffer(mbStr.substring(i * 512, (i + 1) * 512));
		}

		//calculate message digest
		for (int i = 0; i < groupNum; ++i) {
			StringBuffer[] w = new StringBuffer[80];

			//initialize ABCDE
			long a = h0;
			long b = h1;
			long c = h2;
			long d = h3;
			long e = h4;

			//initialize w0 to w15
			for (int j = 0; j < 16; ++j) {
				w[j] = new StringBuffer(mbStrGroup[i].substring(j * 32, (j + 1) * 32));
			}

			//initialize w16 to w79
			for (int j = 16; j < 80; ++j) {
				w[j] = ROTL(new StringBuffer(Long
						.toBinaryString(Long.parseLong(w[j - 3].toString(), 2) ^ Long.parseLong(w[j - 8].toString(), 2)
								^ Long.parseLong(w[j - 14].toString(), 2) ^ Long.parseLong(w[j - 16].toString(), 2))),
						1);
			}

			//loop 0 to 79
			long mod = (long) Math.pow(2, 32);
			for (int j = 0; j < 80; ++j) {
				Long f, k;
				if (j >= 0 && j <= 19) {
					f = (b & c) | ((~b) & d);
					k = 0x5A827999L;
				} else if (j >= 20 && j <= 39) {
					f = b ^ c ^ d;
					k = 0x6ED9EBA1L;

				} else if (j >= 40 && j <= 59) {
					f = (b & c) | (b & d) | (c & d);
					k = 0x8F1BBCDCL;
				} else {
					f = b ^ c ^ d;
					k = 0xCA62C1D6L;
				}
				long temp = (Long.parseLong(ROTL(new StringBuffer(Long.toBinaryString(a)), 5).toString(), 2) + f + e
						+ Long.parseLong(w[j].toString(), 2) + k) % mod;
				e = d;
				d = c;
				c = Long.parseLong(ROTL(new StringBuffer(Long.toBinaryString(b)), 30).toString(), 2);
				b = a;
				a = temp;
			}
			h0 = (h0 + a) % mod;
			h1 = (h1 + b) % mod;
			h2 = (h2 + c) % mod;
			h3 = (h3 + d) % mod;
			h4 = (h4 + e) % mod;
		}
		return Long.toHexString(h0) + Long.toHexString(h1) + Long.toHexString(h2) + Long.toHexString(h3)
				+ Long.toHexString(h4);
	}
}
