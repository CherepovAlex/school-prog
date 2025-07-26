package attestation.attestation01;

import java.util.Arrays;

public class BuyUtils {

    public static void processPurchase(Person[] persons, Product[] products, String input) {
        if (persons == null || products == null || input == null) {
            System.out.println("Ошибка: неверные входные данные");
            return;
        }
        String[] parts = input.split("\\s*-\\s*");
        if (parts.length != 2) {
            System.out.println("Неверный формат: " + input);
            return;
        }

        boolean foundPerson = false;
        boolean foundProduct = false;

        for (var person : persons) {
            if (person.getName().equals(parts[0])) {
                foundPerson = true;
                for (Product product : products) {
                    if (product != null && product.getProductName().equals(parts[1])) {
                        foundProduct = true;
                        doPurchase(person, product);
                        break;
                    }
                }
                break;
            }
        }
        if (!foundPerson) {
            System.out.println("Покупатель не найден: " + parts[0]);
        } else if (!foundProduct) {
            System.out.println("Продукт не найден: " + parts[1]);
        }
    }

    private static void doPurchase(Person person, Product product) {
        if (person == null || product == null) {
            System.out.println("Ошибка: не указан покупатель или продукт");
            return;
        }
        if (person.getMoney() >= product.getPrice()) {
            addProductToCart(person, product);
            person.setMoney(person.getMoney() - product.getPrice());
            System.out.println(person.getName() + " купил(а) " + product.getProductName());
        } else {
            System.out.println(person.getName() + " не может позволить себе " + product.getProductName());
        }
    }

    private static void addProductToCart(Person person, Product product) {
        Product[] newProducts = Arrays.copyOf(person.getProducts(),
                person.getProducts().length + 1);
        newProducts[newProducts.length - 1] = product;
        person.setProducts(newProducts);
    }
}
