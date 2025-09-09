package homeworks.webinar6;

import java.util.Scanner;

public class exercise1 {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите ваше имя: ");
        String name = scanner.nextLine();
        scanner.close();

        System.out.println("Привет, " + name);

    }

}
