package attestation.attestation01;

import java.util.Arrays;
import java.util.Scanner;

public class InputUtils {

    public static Person[] inputPersons(Scanner scanner) {
        Person[] persons = new Person[0];
        System.out.println("Введите покупателей в формате: \"Имя = Сумма; Имя2 = Сумма2\"");
        System.out.print("Завершите ввод нажатием Enter, Для завершения введите END: ");

        while (true) {
            String inputText = scanner.nextLine();
            if (inputText.equalsIgnoreCase("END")) {
                return null;
            }

            if (!inputText.isEmpty()) {
                String[] buyers = inputText.split(";\\s*");
                boolean hasErrors = false;
                Person[] tempPersons = new Person[0];

                for (String buyer : buyers) {
                    Person[] result = processPersonInput(buyer, tempPersons);
                    if (result == null) {
                        hasErrors = true;
                        break;
                    }
                    tempPersons = result;
                }

                if (!hasErrors) {
                    return tempPersons;
                }
            }
            System.out.print("Пожалуйста, введите данные покупателей заново: ");
        }
    }

    private static Person[] processPersonInput(String buyer, Person[] persons) {
        String[] parts = buyer.trim().split("\\s*=\\s*");
        if (parts.length != 2) {
            System.out.println("Неверный формат ввода. Используйте: \"Имя = Сумма\"");
            return null;
        }

        try {
            Person person = new Person(parts[0], Integer.parseInt(parts[1]));
            Person[] newPersons = Arrays.copyOf(persons, persons.length + 1);
            newPersons[newPersons.length - 1] = person;
            return newPersons;
        } catch (NumberFormatException e) {
            System.out.println("Сумма денег должна быть числом");
            return null;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static Product[] inputGoods(Scanner scanner) {
        Product[] products = new Product[0];
        System.out.println("Введите продукты в формате: \\\"Продукт = Цена; Продукт2 = Цена2\\\"");
        System.out.print("Завершите ввод нажатием Enter: ");

        while (true) {
            String inputText = scanner.nextLine().trim();
            if (inputText.equalsIgnoreCase("END")) {
                return null;
            }

            if (!inputText.isEmpty()) {
                String[] goods = inputText.split(";\\s*");
                boolean hasErrors = false;

                for (String good : goods) {
                    Product[] result = processGoodInput(good, products);
                    if (result == null) {
                        hasErrors = true;
                        break;
                    }
                    products = result;
                }
                if (!hasErrors && products.length > 0) {
                    return products;
                }
            }
            System.out.println("Пожалуйста, введите данные заново или END для выхода: ");
        }
    }

    private static Product[] processGoodInput(String good, Product[] products) {
        String[] parts = good.trim().split("\\s*=\\s*");
        if (parts.length != 2) {
            System.out.println("Неверный формат ввода. Используйте: \"Наименование = Цена\"");
            return null;
        }

        try {
            Product product = new Product(parts[0], Integer.parseInt(parts[1]));
            Product[] newProducts = Arrays.copyOf(products, products.length + 1);
            newProducts[newProducts.length - 1] = product;
            return newProducts;
        } catch (NumberFormatException e) {
            System.out.println("Цена должна быть числом");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}
