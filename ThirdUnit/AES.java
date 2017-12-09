package ThirdUnit;

import java.util.*;

public class AES {
	public static void main(String[] args) {
		String key = "2B7E151628AED2A6ABF7158809CF4F3C"; // 密钥
		String plaintText = "3243F6A8885A308D313198A2E0370734"; // 明文

		AES aes = new AES();
		aes.encrypt(key, plaintText);
	}

	// 一 加密
	public void encrypt(String key, String plaintText) {
		// 1 明文分组
		ArrayList<StringBuffer> subPlaintText = new ArrayList<StringBuffer>();
		for (int i = 0; i < 30; i += 8) {
			subPlaintText.add(new StringBuffer(plaintText.substring(i, i + 8)));
		}

		// 2 扩展密钥
		ArrayList<StringBuffer> w = keyExpansion(key);

		// 3 前9轮操作
		for (int i = 0; i < 10; ++i) {
			// 3.1 轮密钥加->字节替换
			for (int j = 0; j < 4; ++j) {
				// 3.1.1 轮密钥异或
				subPlaintText.set(j,
						new StringBuffer(Long.toHexString(Long.parseLong(subPlaintText.get(j).toString(), 16)
								^ Long.parseLong(w.get(i * 4 + j).toString(), 16))));
				while (subPlaintText.get(j).length() != 8) {
					subPlaintText.get(j).insert(0, 0);
				}

				// 3.1.2 字节替换
				for (int k = 0; k < 6; k += 2) {
					subPlaintText.get(j).replace(k, k + 2, subBytes(subPlaintText.get(j).substring(k, k + 2)));
				}
				subPlaintText.get(j).replace(6, 8, subBytes(subPlaintText.get(j).substring(6)));
			}

			// 3.2 列移位
			String[][] mBytesTemp1 = new String[4][4];
			for (int j = 0; j < 4; ++j) {
				for (int k = 0; k < 6; k += 2) {
					mBytesTemp1[j][k / 2] = subPlaintText.get(j).substring(k, k + 2);
				}
				mBytesTemp1[j][3] = subPlaintText.get(j).substring(6);
			}
			for (int j = 0; j < 4; ++j) {
				StringBuffer mRawShiftTemp = new StringBuffer();
				for (int k = 0; k < 4; ++k) {
					mRawShiftTemp.append(mBytesTemp1[(j + k) % 4][k]);
				}
				subPlaintText.set(j, mRawShiftTemp);
			}

			// 3.2 行混淆，最后一轮不用混淆
			if (i != 9) {
				// 3.2.1 列混淆操作
				long[][] field = { { 2, 3, 1, 1 }, { 1, 2, 3, 1 }, { 1, 1, 2, 3 }, { 3, 1, 1, 2 } };
				long[][] mBytesTemp2 = new long[4][4];
				long[][] mBytesTemp3 = new long[4][4];
				for (int j = 0; j < 4; ++j) {
					for (int k = 0; k < 6; k += 2) {
						mBytesTemp2[j][k / 2] = Long.parseLong(subPlaintText.get(j).substring(k, k + 2), 16);
					}
					mBytesTemp2[j][3] = Long.parseLong(subPlaintText.get(j).substring(6), 16);
				}
				for (int j = 0; j < 4; ++j) { // 明文的行
					for (int k = 0; k < 4; ++k) { // field的行,明文的列
						long[] mMultiTemp = new long[4];
						for (int l = 0; l < 4; ++l) { // field的列，明文的列
							if (field[k][l] == 1) {
								mMultiTemp[l] = mBytesTemp2[j][l];
							} else if (field[k][l] == 2) {
								if (mBytesTemp2[j][l] < 128) {
									mMultiTemp[l] = mBytesTemp2[j][l] * 2;
								} else {
									mMultiTemp[l] = (mBytesTemp2[j][l] * 2) ^ 27;
								}
							} else if (field[k][l] == 3) {
								long mTemp;
								if (mBytesTemp2[j][l] < 128) {
									mTemp = mBytesTemp2[j][l] * 2;
								} else {
									mTemp = (mBytesTemp2[j][l] * 2) ^ 27;
								}
								mMultiTemp[l] = mBytesTemp2[j][l] ^ mTemp;
							}
						}
						mBytesTemp3[j][k] = (mMultiTemp[0] ^ mMultiTemp[1] ^ mMultiTemp[2] ^ mMultiTemp[3]) % 256;
					}
				}

				// 3.2.2 赋给subPlaintText
				for (int j = 0; j < 4; ++j) {
					StringBuffer mTemp = new StringBuffer();
					for (int k = 0; k < 4; ++k) {
						StringBuffer temp = new StringBuffer(Long.toHexString(mBytesTemp3[j][k]));
						while (temp.length() < 2) {
							temp.insert(0, 0);
						}
						mTemp.append(temp);
					}
					subPlaintText.set(j, mTemp);
				}
			}

			// 4 最后一轮操作
			if (i == 9) {
				System.out.println("加密结果为");
				for (int j = 0; j < 4; ++j) {
					subPlaintText.set(j,
							new StringBuffer(Long.toHexString(Long.parseLong(subPlaintText.get(j).toString(), 16)
									^ Long.parseLong(w.get((i + 1) * 4 + j).toString(), 16))));
					while (subPlaintText.get(j).length() != 8) {
						subPlaintText.get(j).insert(0, 0);
					}
					System.out.println(subPlaintText.get(j));
				}
			}
		}
	}

	///////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////

	// 二 密钥的编排
	public ArrayList<StringBuffer> keyExpansion(String key) {
		final String[] Rcon = { "01000000", "2000000", "04000000", "08000000", "10000000", "20000000", "40000000",
				"80000000", "1B000000", "36000000" };
		ArrayList<StringBuffer> w = new ArrayList<StringBuffer>(); // 扩展后的轮密钥
		ArrayList<String> subKey = new ArrayList<String>();// 存储分组后的密钥

		// 1 把密钥分成16组，每组两个十六进制数，1个字节
		for (int i = 0; i < 32; i += 2) {
			subKey.add(key.substring(i, i + 2));
		}

		// 2 填入w
		for (int i = 0; i < 4; ++i) {
			StringBuffer mWTemp = new StringBuffer();
			for (int j = 0; j < 4; ++j) {
				mWTemp.append(subKey.get(4 * i + j));
			}
			w.add(mWTemp);
		}

		// 3 填入w
		StringBuffer temp = new StringBuffer();
		for (int i = 4; i < 44; ++i) {
			temp.replace(0, temp.length(), w.get(i - 1).toString());
			if (i % 4 == 0) {
				temp.replace(0, temp.length(), Long.toHexString(
						Long.parseLong(subWord(rotWord(temp)).toString(), 16) ^ Long.parseLong(Rcon[i / 4 - 1], 16)));
			}
			w.add(new StringBuffer(Long.toHexString(
					(Long.parseLong(w.get(i - 4).toString(), 16)) ^ (Long.parseLong(temp.toString(), 16)))));
		}
		return w;
	}

	// 对四个字节进行循环移位
	public String rotWord(StringBuffer temp) {
		String firstOne = temp.substring(0, 2);
		String lastThree = temp.substring(2);
		StringBuffer mTemp = new StringBuffer();
		mTemp.append(lastThree);
		mTemp.append(firstOne);
		return mTemp.toString();
	}

	// 对四个字节使用S盒映射
	public StringBuffer subWord(String temp) {
		StringBuffer mTemp = new StringBuffer();
		for (int i = 0; i < 8; i += 2) {
			if (i != 6) {
				mTemp.append(subBytes(temp.substring(i, i + 2).toString()));
			} else {
				mTemp.append(subBytes(temp.substring(6).toString()));
			}
		}
		return mTemp;
	}

	// S盒的映射
	public String subBytes(String temp) {
		final String SBox[][] = {
				{ "63", "7C", "77", "7B", "F2", "6B", "6F", "C5", "30", "01", "67", "2B", "FE", "D7", "AB", "76" },
				{ "CA", "82", "C9", "7D", "FA", "59", "47", "F0", "AD", "D4", "A2", "AF", "9C", "A4", "72", "C0" },
				{ "B7", "FD", "93", "26", "36", "3F", "F7", "CC", "34", "A5", "E5", "F1", "71", "D8", "31", "15" },
				{ "04", "C7", "23", "C3", "18", "96", "05", "9A", "07", "12", "80", "E2", "EB", "27", "B2", "75" },
				{ "09", "83", "2C", "1A", "1B", "6E", "5A", "A0", "52", "3B", "D6", "B3", "29", "E3", "2F", "84" },
				{ "53", "D1", "00", "ED", "20", "FC", "B1", "5B", "6A", "CB", "BE", "39", "4A", "4C", "58", "CF" },
				{ "D0", "EF", "AA", "FB", "43", "4D", "33", "85", "45", "F9", "02", "7F", "50", "3C", "9F", "A8" },
				{ "51", "A3", "40", "8F", "92", "9D", "38", "F5", "BC", "B6", "DA", "21", "10", "FF", "F3", "D2" },
				{ "CD", "0C", "13", "EC", "5F", "97", "44", "17", "C4", "A7", "7E", "3D", "64", "5D", "19", "73" },
				{ "60", "81", "4F", "DC", "22", "2A", "90", "88", "46", "EE", "B8", "14", "DE", "5E", "0B", "DB" },
				{ "E0", "32", "3A", "0A", "49", "06", "24", "5C", "C2", "D3", "AC", "62", "91", "95", "E4", "79" },
				{ "E7", "C8", "37", "6D", "8D", "D5", "4E", "A9", "6C", "56", "F4", "EA", "65", "7A", "AE", "08" },
				{ "BA", "78", "25", "2E", "1C", "A6", "B4", "C6", "E8", "DD", "74", "1F", "4B", "BD", "8B", "8A" },
				{ "70", "3E", "B5", "66", "48", "03", "F6", "0E", "61", "35", "57", "B9", "86", "C1", "1D", "9E" },
				{ "E1", "F8", "98", "11", "69", "D9", "8E", "94", "9B", "1E", "87", "E9", "CE", "55", "28", "DF" },
				{ "8C", "A1", "89", "0D", "BF", "E6", "42", "68", "41", "99", "2D", "0F", "B0", "54", "BB", "16" } };
		return SBox[Integer.valueOf(temp.substring(0, 1), 16)][Integer.valueOf(temp.substring(1), 16)];
	}
}
