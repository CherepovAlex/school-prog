package homeworks.homework011.model;
/* 1. Реализовать класс Автомобиль. У класса есть поля, свойства и методы.
Поля класса:
а) Номер автомобиля; б) Модель; в) Цвет; г) Пробег; д) Стоимость.
Обратить внимание на переопределение метода toString, на сеттеры и геттеры, модификаторы доступа полей. */
public class Car {
    private String carNumber;
    private String carModel;
    private String carColor;
    private int carMileage;
    private int carCost;

    public Car(String carNumber, String carModel, String carColor, int carMileage, int carCost) {
        this.carNumber = carNumber;
        this.carModel = carModel;
        this.carColor = carColor;
        this.carMileage = carMileage;
        this.carCost = carCost;
    }

    public String getCarNumber() {
        return carNumber;
    }
    public String getCarModel() {
        return carModel;
    }
    public String getCarColor() {
        return carColor;
    }
    public int getCarMileage() {
        return carMileage;
    }
    public int getCarCost() {
        return carCost;
    }

    @Override
    public String toString() {
        return carNumber + "\t" + carModel + "\t" + carColor + "\t" + carMileage + "\t" + carCost;
    }
}
