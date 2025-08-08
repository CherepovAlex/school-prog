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

//class CountArrows {
//    public static void main(String[] args) {
//        Scanner scanner= new Scanner(System.in);
//        System.out.println("Введите строку из символов: ‘>’, ‘<’ и ‘-‘");
//        String s= scanner.next();
//        System.out.println("Введенная строка: "+s);
//        int n=s.length();
//        System.out.println("Длина строки: "+n);
//        int count=0;
//
//        for(int i=0; i<=n-5;i++) {
//            String sub=s.substring(i,i+5);
//            System.out.println("Проверяем позицию "+i+": "+sub);
//            if(sub.equals(">>-->")||sub.equals("<--<<")) {
//                count++;
//                System.out.println(" Найдена стрелка! Текущий счет: "+count);
//            }
//        }
//        System.out.println("Всего стрелок: "+count);
//        scanner.close();
//    }
//}
