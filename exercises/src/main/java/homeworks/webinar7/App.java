package homeworks.webinar7;

import net.datafaker.Faker;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        TV tv1 = new TV(55, "LG");
        TV tv2 = new TV(34, "Philips");
        TV tv3 = new TV(84, "Samsung");
        TV tv4 = getTV();

        System.out.println("Телевизор, сгенерированный случайным образом: " +
                            getFaker().getMark() + " " + getFaker().getDiagonal() + "\"");
    }

    public static TV getTV() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите диагональ и марку телевизора через пробел," +
                " например, \"52 Sony\": ");
        String inputUser = scanner.nextLine();
        String[] tvParam = inputUser.split(" ");
        scanner.close();
        TV tv = new TV(Integer.parseInt(tvParam[0]), tvParam[1]);
        System.out.println("Вы добавили телевизор марки " + tv.getMark()
                + " c диагональю " + tv.getDiagonal() + "\"");
        return tv;
    }

    public static TV getFaker() {
        Faker faker = new Faker();
        return new TV(
                (int)faker.number().randomDouble(1, 32, 120),
                faker.options().option( "Philips", "Toshiba", "Panasonic", "Sony", "LG", "Samsung")
        );
    }

}

class TV {

    private int diagonal;
    private String mark;

    public TV(int diagonal, String mark) {
        this.diagonal = diagonal;
        this.mark = mark;
    }

    public int getDiagonal() {
        return diagonal;
    }

    public String getMark() {
        return mark;
    }
}