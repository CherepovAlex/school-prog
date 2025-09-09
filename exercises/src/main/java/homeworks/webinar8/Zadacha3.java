package homeworks.webinar8;

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
        System.out.println("Вывод новой строки: "+ sortWord(words[0]) + " " + sortWord(words[1]));
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

//class SortWords {
//    public static void main(String[] args) {
//        Scanner scanner= new Scanner(System.in);
//        System.out.println("Введите два слова через пробел:");
//        String line=scanner.nextLine();
//        System.out.println("Введенная строка: "+line);
//        String lowerLine=line.toLowerCase();
//        System.out.println("Нижний регистр: "+lowerLine);
//        String[] words=lowerLine.split(" ");
//        if(words.length!=2){
//            System.out.println("Ошибка, требуется ввести ровно два слова");
//            scanner.close();
//            return;
//        }
//        StringBuilder result= new StringBuilder();
//        for (int i=0;i< words.length;i++){
//            String word=words[i];
//            System.out.println("Текущее слово перед сортировкой: "+word);
//            char[] chars=word.toCharArray();
//            Arrays.sort(chars);
//            String sortedWord=new String(chars);
//            System.out.println("Отсортированное слово: "+sortedWord);
//            result.append(sortedWord);
//            if(i<words.length-1) {
//                result.append(" ");
//            }
//        }
//        System.out.println("Результат: "+result.toString());
//        scanner.close();
//    }
//}