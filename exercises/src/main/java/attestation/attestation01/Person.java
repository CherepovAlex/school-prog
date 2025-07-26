package attestation.attestation01;

import java.util.Arrays;
import java.util.Objects;

class Person {
    private String name;
    private int money;
    private Product[] personProducts;

    public Person(String name, int money) {
        if (name.isBlank() || name == null) {
            throw new IllegalArgumentException("Имя не может быть пустым");
        }
        if (name.length() < 3) {
            throw new IllegalArgumentException("Имя не может быть короче 3 символов");
        }
        if (money < 0) {
            throw new IllegalArgumentException("Деньги не могут быть отрицательным числом");
        }
        this.name = name;
        this.money = money;
        this.personProducts = new Product[0];
    }
    public String getName() {
        return name;
    }

    public int getMoney() {
        return money;
    }

    public Product[] getProducts() {
        return personProducts;
    }

    public void setName(String name) {
        if (name == null || name.isBlank() || name.length() <= 3) {
            System.out.println("Имя не может быть пустым или короче 3 символов");
        } else {
            this.name = name;
        }
    }

    public void setMoney(int money) {
        if (money < 0) {
            System.out.println("Деньги не могут быть отрицательным");
        } else {
            this.money = money;
        }
    }

    public void setProducts(Product[] products) {
        this.personProducts = products != null ? products : new Product[0];
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        for (Product product : personProducts) {
            sb.append(product == null ? "" : ", " + product);
        }
        return name + " - " + sb;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return money == person.money &&
                Objects.equals(name, person.name) &&
                Objects.deepEquals(personProducts, person.personProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, money, Arrays.hashCode(personProducts));
    }
}
