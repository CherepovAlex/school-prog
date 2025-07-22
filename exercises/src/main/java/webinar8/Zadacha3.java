package webinar8;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Zadacha3 {

    public static void main(String[] args) {
        // 1 вариант
        String word1 = generateWord();
        String word2 = generateWord();
        System.out.println("Сгенерированная строка: " + word1 + " " + word2);
        System.out.println("Вывод новой строки: " + sortWord(word1) + " " + sortWord(word2));
        // 2 вариант
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите два слова через пробел: ");
        String input = scanner.nextLine();
        String[] words = input.split(" ");
        System.out.println(sortWord(words[0]) + " " + sortWord(words[1]));

    }

    public static String generateWord() {
        Random rand = new Random();
        var sb = new StringBuilder();
        for (int i = 0; i < rand.nextInt(50) + 1; i++) {
            char c;
            boolean flag = rand.nextBoolean();
            if (flag) {
                c = (char) (rand.nextInt(26) + 'a');
            } else {
                c = (char) (rand.nextInt(26) + 'A');
            }
            sb.append(c);
        }
        return sb.toString();
    }

    public static String sortWord(String word) {
        char[] w = word.toLowerCase().toCharArray();
        Arrays.sort(w);
        return new String(w);
    }
}
