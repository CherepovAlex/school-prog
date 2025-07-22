package webinar8;

import java.util.Scanner;

public class Zadacha1 {

    private static final String keyboard = "qwertyuiop" +
            "asdfghjkl" +
            "zxcvbnm";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String inputLit = "";
        boolean isValidLit = false;

        while (!isValidLit) {
            System.out.print("Введите маленькую букву английского алфавита: ");
            inputLit = scanner.nextLine().trim();

            if (inputLit.length() == 1 && keyboard.contains(inputLit)) {
                isValidLit = true;
            } else System.out.println("Вы ввели не корректный символ.");
        }

        int litIndex = keyboard.indexOf(inputLit);
        int leftInd = (litIndex == 0) ? keyboard.length() - 1 : litIndex - 1;
        System.out.printf("Буква, стоящая слева от заданной буквы: %s", keyboard.charAt(leftInd));
        scanner.close();
    }
}
