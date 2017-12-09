package sixthUnit;

public class ECIES {
	private int a; // 多项式参数
	private int b; // 多项式参数
	private int p; // 模数
	private int m; // 密钥
	private int[] P; // 循环子群

	public static void main(String[] args) {
		ECIES ecies = new ECIES(1, 6, 11, new int[] { 2, 7 }, 7);
		ecies.encrypt(9);
		ecies.decrypt(new int[] { 7, 1 }, 6);

		System.out.println();
		ECIES ecies0 = new ECIES(2, 7, 31, new int[] { 2, 9 }, 8);
		ecies0.decrypt(new int[] { 18, 1 }, 21);
		ecies0.decrypt(new int[] { 3, 1 }, 18);
		ecies0.decrypt(new int[] { 17, 0 }, 19);
		ecies0.decrypt(new int[] { 28, 0 }, 8);
	}

	public ECIES(int a, int b, int p, int[] P, int m) {
		this.a = a;
		this.b = b;
		this.p = p;
		this.P = P;
		this.m = m;
	}

	public void encrypt(int x) {
		// 计算Q=kP
		int[] Q = P;
		for (int i = 0; i < m - 1; ++i) {
			Q = add(Q, P);
		}
		System.out.println(Q[0] + " " + Q[1]);

		// 随机选取秘密整数k
		//int k = (int) (Math.random() * p + 1);
		//System.out.println("k" + " " + k);
		int k = 6;

		// 计算kP和kQ
		int[] kP = P;
		int[] kQ = Q;
		for (int i = 0; i < k - 1; ++i) {
			kP = add(kP, P);
		}
		for (int i = 0; i < k - 1; ++i) {
			kQ = add(kQ, Q);
		}

		// 计算y1和y2，y1的y坐标要模2进行压缩
		int[] y1 = kP;
		int y2 = (x * kQ[0]) % p;
		y1[1] = y1[1] % 2;

		System.out.println("加密为：" + "(" + "(" + y1[0] + "," + y1[1] + ")" + "," + y2 + ")");
	}

	public void decrypt(int[] y1, int y2) {
		// 把x带入椭圆曲线求出z值
		int z = (y1[0] * y1[0] * y1[0] + a * y1[0] + b) % p;

		// 用Euler判别式计算pow(z,(p-1)/2)(mod p)，以此判断z是不是模p的二次剩余
		int modPow = z;
		for (int i = 0; i < (p - 1) / 2 - 1; ++i) {
			modPow = (modPow * z) % p;
		}

		// 如果是二次剩余，利用pow(z,(p+1)/4)(mod p),把z开方
		if (modPow == 1) {
			// 计算pow(z,(p+1)/4)(mod p)
			int y = z;
			for (int i = 0; i < (p + 1) / 4 - 1; ++i) {
				y = y * z % p;
			}

			// 重构E上的点y1，判断根号z模2是否和y1的i值相等，如果相等就用i=y重构点y1
			if (y % 2 == y1[1]) {
				y1[1] = y;
			} else {
				y1[1] = p - y;
			}

			// 计算m*kP，kP由重构得到，PD即Point-Decompress
			int[] PD = y1;
			for (int i = 0; i < m - 1; ++i) {
				PD = add(PD, y1);
			}

			// 利用y2，和mkP的x值进行解密
			int x = (y2 * Euclidean(p, PD[0])) % p;
			System.out.println("解密为：" + x + (char) (x % 26 + 64));
		} else {
			System.out.println("failure");
		}
	}

	public int[] add(int[] P, int[] Q) {
		int[] R = new int[] { 0, 0 }; // 用(0,0)代表无穷远点

		// 如果至少一个点为无穷远点，则返回原值
		// 如果互为加法逆点，则相加为无穷远点
		// 否则通过表达式计算相加后的值
		if (P[0] == 0 && P[1] == 0) {
			return Q;
		} else if (Q[0] == 0 && Q[1] == 0) {
			return P;
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
