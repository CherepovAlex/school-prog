package attestation.attestation01;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Person[] persons = InputUtils.inputPersons(scanner);

        if (persons == null) {
            System.out.println("Завершение работы программы");
            scanner.close();
            return;
        }

        Product[] products = InputUtils.inputGoods(scanner);

        processBuys(scanner, persons, products);

        PrintUtils.printBuyers(persons);

        scanner.close();
    }

    private static void processBuys(Scanner scanner, Person[] persons, Product[] products) {
        System.out.println("Введите покупки в формате: \"Имя - Продукт\" (по одной на строку)");
        System.out.print("Для завершения введите END: ");

        while (true) {
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("END")) {
                break;
            }

            BuyUtils.processPurchase(persons, products, input);

            System.out.println("Введите покупки в формате: \"Имя - Продукт\" (по одной на строку)");

            System.out.print("Для завершения введите END: ");

        }
    }
}
