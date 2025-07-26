package attestation.attestation01;

public class PrintUtils {

    public static void printBuyers(Person[] persons) {
        if (persons == null) {
            System.out.println("Нет данных о покупателях");
        }

        for (Person person : persons) {
            if (person == null) continue;

            if (person.getProducts() == null || person.getProducts().length == 0) {
                System.out.println(person.getName() + " - Ничего не куплено");
            } else {
                System.out.print(person.getName() + " - ");
                for (int i = 0; i < person.getProducts().length; i++) {
                    if (i > 0) System.out.print(", ");
                    System.out.print(person.getProducts()[i].getProductName());
                }
                System.out.println();
            }
        }
    }
}
