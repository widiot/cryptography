package sixthUnit;

import java.util.Arrays;

public class Shanks {

	public static void main(String[] args) {
		Shanks shanks = new Shanks();
		int n, alphi, beta;

		n = 24691;
		alphi = 106;
		beta = 12375;
		shanks.shanks(n, alphi, beta);

		n = 458009;
		alphi = 6;
		beta = 248388;
		shanks.shanks(n, alphi, beta);
	}

	public void shanks(int p, int alphi, int beta) {
		int m = (int) Math.ceil(Math.sqrt(p - 1));
		long[] aj = new long[m];
		long[] bi = new long[m];
		int i, j;

		// 首先计算alphi的m次方
		int aTemp = alphi;
		for (i = 0; i < m - 1; ++i) {
			aTemp = (aTemp * alphi) % p;
			//System.out.println(aTemp);
		}

		// 计算alphi的mj次方，得到列表aj
		// 然后复制表aj得到ajCopy，将ajCopy排序
		aj[0] = 1;
		for (j = 1; j < m; ++j) {
			aj[j] = (aj[j - 1] * aTemp) % p;
			//System.out.println(aj[j]);
		}
		long[] ajCopy = Arrays.copyOf(aj, aj.length);
		Arrays.sort(ajCopy);

		// 计算alphi的-i次方，得到列表bi
		// 然后复制表bi得到biCopy，将biCopy排序
		bi[0] = beta;
		int bTemp = Euclidean(p, alphi);
		for (i = 1; i < m; ++i) {
			bi[i] = (bi[i - 1] * bTemp) % p;
		}
		long[] biCopy = Arrays.copyOf(bi, bi.length);
		Arrays.sort(biCopy);

		// 寻找ajCopy和biCopy中相等的值
		i = 0;
		j = 0;
		long same = 0;
		boolean isFound = false;
		while (i < m && j < m) {
			if (ajCopy[j] < biCopy[i]) {
				++j;
			} else if (ajCopy[j] > biCopy[i]) {
				++i;
			} else {
				isFound = true;
				same = ajCopy[j];
				break;
			}
		}

		// 如果找到了相等值就计算log
		if (isFound) {
			i = 0;
			j = 0;
			while (aj[j] != same) {
				++j;
			}
			while (bi[i] != same) {
				++i;
			}
			System.out.println("p:" + p + "\nalphi:" + alphi + "\nbeta:" + beta + "\nlog:" + (m * j + i) % p + "\n");
		} else {
			System.out.println("没有结果");
		}
	}

	// 扩展欧几里得算法，求逆元
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
