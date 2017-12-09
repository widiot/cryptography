package sixthUnit;

public class DoubleAnd {
	private int p;
	private int a;

	public static void main(String[] args) {
		DoubleAnd doubleAnd = new DoubleAnd(127, 1);

		doubleAnd.operation(new int[] { 2, 6 }, new int[] { 1, 0, -1, 0, -1, 0, 0, -1 });
	}

	public DoubleAnd(int p, int a) {
		this.p = p;
		this.a = a;
	}

	public int[] operation(int[] P, int[] c) {
		int[] Q = new int[] { 0, 0 };
		for (int i = 0; i < c.length; ++i) {
			Q = add(Q, Q);
			if (c[i] == 1) {
				Q = add(Q, P);
			} else if (c[i] == -1) {
				int[] PT = new int[2];
				PT[0] = P[0];
				PT[1] = p - P[1];
				Q = add(Q, PT);
			}
			System.out.println(Q[0] + " " + Q[1]);
		}
		return Q;
	}

	public int[] add(int[] P, int[] Q) {
		int[] R = new int[2];

		if (P[0] == 0 && P[1] == 0) {
			R[0] = Q[0];
			R[1] = Q[1];
			return R;
		} else if (Q[0] == 0 && Q[1] == 0) {
			R[0] = P[0];
			R[1] = P[1];
			return R;
		} else if (P[0] == Q[0] && P[1] == -Q[1]) {
			System.out.println("相加为无穷远点");
		} else {
			int lambda;
			if (P[0] == Q[0] && P[1] == Q[1]) {
				lambda = Euclidean(p, (2 * P[1]) % p);
				lambda = lambda * (3 * P[0] * P[0] + a) % p;
			} else {
				lambda = Euclidean(p, (Q[0] - P[0] + p) % p);
				lambda = (lambda * (Q[1] - P[1] + p)) % p;
			}
			R[0] = (lambda * lambda - P[0] - Q[0]) % p;
			R[1] = (lambda * (P[0] - R[0]) - P[1]) % p;
			while (R[0] < 0) {
				R[0] += p;
			}
			while (R[1] < 0) {
				R[1] += p;
			}
		}
		return R;
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
