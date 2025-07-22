package webinar8;

import java.util.Random;

public class Zadacha2 {


    public static void main(String[] args) {
        String chars = "><-";
        Random rand = new Random();
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < 106; i++) {
            sb.append(chars.charAt(rand.nextInt(chars.length())));
        }
        String str = sb.toString();
        System.out.println("Строка: " + str);
        int count1 = countArrows(str, ">>-->");
        int count2 = countArrows(str, "<--<<");
        System.out.println("Количество стрел: " + (count1 + count2));
    }

    private static int countArrows(String str, String arrow) {
        int count = 0;
        int index = 0;
        while ((index = str.indexOf(arrow, index)) != -1) {
            count++;
            index++;
        }
        return count;
    }
}
