package webinar6;

import java.util.Random;

public class exercise2 {

    private static final String[] KMN = {"Камень", "Ножницы", "Бумага"};

    public static void main(String[] args) {
        Random random = new Random();

        var vChoice = random.nextInt(KMN.length);
        var pChoice = random.nextInt(KMN.length);

        System.out.println("У Васи: " + KMN[vChoice] + ", У Пети: " + KMN[pChoice]);
        System.out.println(decideWinner(vChoice, pChoice));

    }

    public static String decideWinner(int vChoice, int pChoice) {
        if (vChoice == pChoice) {
            return "Ничья";
        } else if ((vChoice == 0 && pChoice == 1)
                || (vChoice == 1 && pChoice == 2)
                || (vChoice == 2 && pChoice == 0)) {
            return "Вася победил";
        } else {
            return "Петя победил";
        }
    }
}
