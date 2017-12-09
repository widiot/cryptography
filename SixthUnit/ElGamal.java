package sixthUnit;

public class ElGamal {
	private int p;
	private int alphi;
	private int a;
	private int beta;

	public static void main(String[] args) {
		ElGamal elgamal = new ElGamal(31847, 5, 7899, 18074);
		elgamal.decrypt(3781, 14409);
		elgamal.decrypt(31552, 3930);
		elgamal.decrypt(27214, 15442);
		elgamal.decrypt(5809, 30274);
	}

	public ElGamal(int p, int alphi, int a, int beta) {
		this.p = p;
		this.alphi = alphi;
		this.a = a;
		this.beta = beta;
	}

	public void encrypt(int x) {
		int k = (int) (Math.random() * (p - 1) + 1);
		int y1 = alphi, y2 = x;
		for (int i = 0; i < k - 1; ++i) {
			y1 = (y1 * alphi) % p;
		}
		for (int i = 0; i < k; ++i) {
			y2 = (y2 * beta) % p;
		}
		System.out.println("(" + y1 + "," + y2 + ")");
	}

	public void decrypt(int y1, int y2) {
		int x = y1;
		for (int i = 0; i < a - 1; ++i) {
			x = (x * y1) % p;
		}
		x = Euclidean(p, x);
		x = (y2 * x) % p;
		char[] text = new char[3];

		text[0] = (char) (x / 676 + 65);
		x = x - x / 676 * 676;
		text[1] = (char) (x / 26 + 65);
		x = x - x / 26 * 26;
		text[2] = (char) (x % 26 + 65);
		for (char c : text) {
			System.out.print(c);
		}
		System.out.print(" ");
	}

	public int Euclidean(int a, int b) {
		int a0 = a;
		int b0 = b;
		int t0 = 0;
		int t = 1;
		int q = (int) Math.floor((double) a0 / b0);
		int r = a0 - q * b0;
		while (r > 0) {
			int temp = (t0 - q * t) % a;
			t0 = t;
			t = temp;
			a0 = b0;
			b0 = r;
			q = (int) Math.floor((double) a0 / b0);
			r = a0 - q * b0;
		}
		if (b0 != 1) {
			System.out.println("没有逆元!");
		}
		while (t < 0) {
			t += a;
		}
		return t;
	}
}
