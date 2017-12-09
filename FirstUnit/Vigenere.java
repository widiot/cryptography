package FirstUnit;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Vigenere {
    private JButton decodeButton;
    private JButton encodeButton;
    private JTextArea ciphertextArea;
    private JTextArea plaintextArea;
    private JTextField keyField;
    private String ciphertext;

    private int keyLength; // 密钥长度
    private ArrayList<String> cipherGroup; // 密文的分组

    public static void main(String[] args) {
        Vigenere vigenere = new Vigenere();
    }

    Vigenere() {
        // 1 初始化框架
        JFrame frame = new JFrame();
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize().width / 2,
                Toolkit.getDefaultToolkit().getScreenSize().height / 2);
        frame.setLocationByPlatform(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("维吉尼亚密文解密和加密");

        // 2.1 密文区
        ciphertextArea = new JTextArea(8, 70);
        JScrollPane ciphertextScrollPane = new JScrollPane(ciphertextArea);
        ciphertextArea.setLineWrap(true);
        ciphertextScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        ciphertextScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        // 2.2 明文区
        plaintextArea = new JTextArea(8, 70);
        JScrollPane plaintextScrollPane = new JScrollPane(plaintextArea);
        plaintextArea.setLineWrap(true);
        plaintextScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        plaintextScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        // 2.3 把密文区和明文区加入Panel
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.add(new JLabel("密文"));
        textPanel.add(ciphertextScrollPane);
        textPanel.add(new JLabel("明文"));
        textPanel.add(plaintextScrollPane);

        // 3.1 解密按钮
        decodeButton = new JButton("解密");
        // 3.2 加密按钮
        encodeButton = new JButton("加密");
        // 3.3 密钥输入框
        JPanel keyPanel = new JPanel();
        keyField = new JTextField(10);
        JLabel keyJLabel = new JLabel("密钥");
        keyPanel.add(keyJLabel);
        keyPanel.add(keyField);
        // 3.4 加入Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(decodeButton);
        buttonPanel.add(encodeButton);
        buttonPanel.add(keyPanel);

        // 4 把组件加入框架
        frame.getContentPane().add(BorderLayout.WEST, textPanel);
        frame.getContentPane().add(BorderLayout.EAST, buttonPanel);
        frame.setVisible(true);

        // 5.1 解密按钮注册事件
        ciphertextArea.setText(
                "KCCPKBGUFDPHQTYAVINRRTMVGRKDNBVFDETDGILTXRGUDDKOTFMBPVGEGLTGCKQRACQCWDNAWCRXIZAKFTLEWRPTYCQKYVXCHKFTPONCQQRHJVAJUWETMCMSPKQDYHJVDAHCTRLSVSKCGCZQQDZXGSFRLSWCWSJTBHAFSIASPRJAHKJRJUMVGKMITZHFPDISPZLVLGWTFPLKKEBDPGCEBSHCTJRWXBAFSPEZQNRWXCVYCGAONWDDKACKAWBBIKFTIOVKCGGHJVLNHIFFSQESVYCLACNVRWBBIREPBBVFEXOSCDYGZWPFDTKFQIYCWHJVLNHIQIBTKHJVNPIST");
        decodeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (ciphertextArea.getText() != null) {
                    ciphertext = ciphertextArea.getText();
                    Friedman();
                    decodeCipher();
                }
            }
        });
        // 5.2 加密按钮注册事件
        encodeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (keyField.getText() != null && plaintextArea.getText() != null) {
                    encodePlain(keyField.getText(), plaintextArea.getText());
                }
            }
        });
    }

    // Friedman测试法确定密钥长度
    public void Friedman() {
        keyLength = 1; // 密钥长度
        double[] IC; // 重合指数
        double average; // 平均重合指数

        while (true) {
            IC = new double[keyLength];
            cipherGroup = new ArrayList<String>();
            average = 0;

            // 1 先根据密钥长度分组
            for (int i = 0; i < keyLength; ++i) {
                StringBuffer temporaryGroup = new StringBuffer();
                for (int j = 0; i + j * keyLength < ciphertext.length(); ++j) {
                    temporaryGroup.append(ciphertext.charAt(i + j * keyLength));
                }
                cipherGroup.add(temporaryGroup.toString());
            }

            // 2 再计算每一组的重合指数
            for (int i = 0; i < keyLength; ++i) {
                String subCipher = new String(cipherGroup.get(i)); // 子串
                HashMap<Character, Integer> occurrenceNumber = new HashMap<Character, Integer>(); // 字母及其出现的次数

                // 2.1 初始化字母及其次数键值对
                for (int h = 0; h < 26; ++h) {
                    occurrenceNumber.put((char) (h + 65), 0);
                }

                // 2.2 统计每个字母出现的次数
                for (int j = 0; j < subCipher.length(); ++j) {
                    occurrenceNumber.put(subCipher.charAt(j), occurrenceNumber.get(subCipher.charAt(j)) + 1);
                }

                // 2.3 计算重合指数
                double denominator = Math.pow((double) subCipher.length(), 2);
                for (int k = 0; k < 26; ++k) {
                    double o = (double) occurrenceNumber.get((char) (k + 65));
                    IC[i] += o * (o - 1);
                }
                IC[i] /= denominator;
            }

            // 3 判断退出条件,重合指数的平均值是否大于0.065
            for (int i = 0; i < keyLength; ++i) {
                average += IC[i];
            }
            average /= (double) keyLength;
            if (average >= 0.06) {
                break;
            } else {
                keyLength++;
            }
        } // while--end

        // 打印密钥长度，分组，重合指数，平均重合指数
        plaintextArea.append("密钥长度为：");
        plaintextArea.append(String.valueOf(keyLength) + "\n\n密文分组及其重合指数为：");
        for (int i = 0; i < keyLength; ++i) {
            plaintextArea.append("\n" + cipherGroup.get(i) + "   重合指数: " + IC[i]);
        }
        plaintextArea.append("\n\n平均重合指数为： " + String.valueOf(average));
    }// Friedman--end

    // 再次使用重合指数法确定密钥
    public void decodeCipher() {

        int[] key = new int[keyLength];
        double[] probability = new double[]{0.082, 0.015, 0.028, 0.043, 0.127, 0.022, 0.02, 0.061, 0.07, 0.002, 0.008,
                0.04, 0.024, 0.067, 0.075, 0.019, 0.001, 0.06, 0.063, 0.091, 0.028, 0.01, 0.023, 0.001, 0.02, 0.001};

        // 1 确定密钥
        for (int i = 0; i < keyLength; ++i) {
            double MG; // 重合指数
            int flag; // 移动位置
            int g = 0; // 密文移动g个位置
            HashMap<Character, Integer> occurrenceNumber; // 字母出现次数
            String subCipher; // 子串

            while (true) {
                MG = 0;
                flag = 65 + g;
                subCipher = new String(cipherGroup.get(i));
                occurrenceNumber = new HashMap<Character, Integer>();

                // 1.1 初始化字母及其次数
                for (int h = 0; h < 26; ++h) {
                    occurrenceNumber.put((char) (h + 65), 0);
                }

                // 1.2 统计字母出现次数
                for (int j = 0; j < subCipher.length(); ++j) {
                    occurrenceNumber.put(subCipher.charAt(j), occurrenceNumber.get(subCipher.charAt(j)) + 1);
                }

                // 1.3 计算重合指数
                for (int k = 0; k < 26; ++k, ++flag) {
                    double p = probability[k];
                    flag = (flag == 91) ? 65 : flag;
                    double f = (double) occurrenceNumber.get((char) flag) / subCipher.length();
                    MG += p * f;
                }

                // 1.4 判断退出条件
                if (MG >= 0.055) {
                    key[i] = g;
                    break;
                } else {
                    ++g;
                }
            } // while--end
        } // for--end

        // 2 打印密钥
        StringBuffer keyString = new StringBuffer();
        for (int i = 0; i < keyLength; ++i) {
            keyString.append((char) (key[i] + 65));
        }
        plaintextArea.append("\n\n密钥为: " + keyString.toString());

        // 3 解密
        StringBuffer plainBuffer = new StringBuffer();
        for (int i = 0; i < ciphertext.length(); ++i) {
            int keyFlag = i % keyLength;
            int change = (int) ciphertext.charAt(i) - 65 - key[keyFlag];
            char plainLetter = (char) ((change < 0 ? (change + 26) : change) + 65);
            plainBuffer.append(plainLetter);
        }
        plaintextArea.append("\n\n明文为：\n" + plainBuffer.toString().toLowerCase());
    }

    // 明文加密
    public void encodePlain(String key, String plaintext) {
        String plaintextUpper = plaintext.toUpperCase(); // 把明文转为大写
        String keyUpper = key.toUpperCase(); // 把密钥转为大写
        StringBuffer plainToCipher = new StringBuffer(); // 存储密文
        int[] move = new int[key.length()]; // 明文移动的位数

        // 1 把密钥改为移动的位数
        for (int i = 0; i < keyUpper.length(); ++i) {
            move[i] = (int) keyUpper.charAt(i) - 65;
        }

        // 2 明文加密
        for (int i = 0; i < plaintext.length(); ++i) {
            int letter = (int) plaintextUpper.charAt(i) + move[i % keyUpper.length()];
            letter = letter > 91 ? (letter - 26) : letter;
            char cipherLetter = (char) (letter);
            plainToCipher.append(cipherLetter);
        }

        ciphertextArea.append(plainToCipher.toString());
    }
}