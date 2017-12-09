package FirstUnit;

public class Pairkey {
	public static void main(String[] args) {
		Pairkey pairkey = new Pairkey();
		pairkey.calculation();
	} // main--end

	public void calculation() {
		int total1 = 0; // 密钥总数
		int total2 = 0;

		// 当detK=1时计算对合密钥
		System.out.println("当detK=1时，对合密钥为：");
		for (int i = 0; i < 26; ++i) { // k11和k22
			for (int j = 0; j < 26; ++j) { // k12
				for (int k = 0; k < 26; ++k) { // k21
					if ((mod26(i * i - j * k) == 1) && (j == mod26(-j) && (k == mod26(-k)))) {
						System.out.print(i + " " + j + " " + k + " " + i + "   ");
						++total1;
					}
				} // for--end
			}
		}
		System.out.println("对合密钥数为：" + total1);

		// 当detK=-1时计算对合密钥
		System.out.println("\n当detK=-1时，对合密钥为：");
		for (int i = 0; i < 26; ++i) { // k11
			for (int j = 0; j < 26; ++j) { // k12
				for (int k = 0; k < 26; ++k) { // k21
					for (int l = 0; l < 26; ++l) { // k22
						if ((mod26(i * l - j * k) == 25) && (i == mod26(-l)) && (l == mod26(-i))) {
							System.out.print(i + " " + j + " " + k + " " + l + "   ");
							++total2;
						}
					}
				}
			}
		}
		System.out.println("密钥数为：" + total2);
		System.out.println("\n密钥总数为：" + (total1 + total2));
	}

	public int mod26(int number) {
		while (number < 0) {
			number += 26;
		}
		return number % 26;
	}
}
