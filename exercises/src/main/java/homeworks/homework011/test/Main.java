package homeworks.homework011.test;

import homeworks.homework011.model.Car;
import homeworks.homework011.repository.CarsRepository;
import homeworks.homework011.repository.CarsRepositoryImpl;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static final String colorToFind = "Black";
    public static final Long mileageToFind = 0L;
    public static final Long n = 700_000L;
    public static final Long m = 800_000L;
    public static final String modelToFind1 = "Toyota";
    public static final String modelToFind2 = "Volvo";

    public static final String INPUT = "data/cars.txt";
    public static final String OUTPUT = "data/cars_output.txt";

    public static void main(String[] args) {

        // Дополнительно:
        // 1. Реализовать ввод и вывод программы в файл *.txt
        // 2. Вынести методы работы с автомобилем в папку repository интерфейс
        //CarsRepository и его реализацию CarsRepositoryImpl.
        System.out.println("Чтение файла: " + INPUT);
        CarsRepository carsRepository = new CarsRepositoryImpl();
        List<Car> loadCarList = carsRepository.loadCarsFromFile(INPUT);
        if (loadCarList.isEmpty()) {
            System.out.println("Файл пустой");
        }

        // 2. Проверить работу в классе Main, методе main.
        System.out.println("Номера автомобилей по цвету или пробегу : " + String.join(" ",
                carsRepository.findCarNumberOrMileage(loadCarList, colorToFind,
                        Integer.valueOf(Math.toIntExact(mileageToFind)))));

        System.out.println("Уникальные автомобили: " +
                carsRepository.carUniqueModel(loadCarList, n, m).size() + " шт.");

        System.out.println("Цвет автомобиля с минимальной стоимостью: " + colorMinCost(loadCarList));

        System.out.println("Средняя стоимость модели " + modelToFind1 + ": "
                + String.format(Locale.GERMAN, "%.2f",
                carsRepository.averagePriceModel(loadCarList, modelToFind1)));
        System.out.println("Средняя стоимость модели " + modelToFind2 + ": "
                + String.format(Locale.GERMAN, "%.2f",
                carsRepository.averagePriceModel(loadCarList, modelToFind2)));
        System.out.println("----------------------------------------------------");

        // 1. Реализовать вывод программы в файл *.txt
        carsRepository.saveCarsToFile(OUTPUT, loadCarList);
        System.out.println("Вывод в файл: " + OUTPUT);

        // 3. Создать объект Java Collections со списком автомобилей.
        List<Car> carList = List.of(
                new Car("a123me", "Mercedes", "White", 0, 8300000),
                new Car("b873of", "Volga", "Black", 0, 673000),
                new Car("w487mn", "Lexus", "Grey", 76000, 900000),
                new Car("p987hj", "Volga", "Red", 610, 704340),
                new Car("c987ss", "Toyota", "White", 254000, 761000),
                new Car("o983op", "Toyota", "Black", 698000, 740000),
                new Car("p146op", "BMW", "White", 271000, 850000),
                new Car("u893ii", "Toyota", "Purple", 210900, 440000),
                new Car("l097df", "Toyota", "Black", 108000, 780000),
                new Car("y876wd", "Toyota", "Black", 160000, 1000000)
        );
        // 4. Используя Java Stream API, вывести (можно сделать любые 2 пункта из 4):

        //      1) Номера всех автомобилей, имеющих заданный в переменной цвет colorToFind или нулевой пробег mileageToFind.
        System.out.println("Номера автомобилей по цвету или пробегу : " + String.join(" ", findCarNumberOrMileage(carList)));

        //      2) Количество уникальных моделей в ценовом диапазоне от n до m тыс.
        System.out.println("Уникальные автомобили: " + carUniqueModel(carList).size() + " шт.");

        //      3) Вывести цвет автомобиля с минимальной стоимостью.
        System.out.println("Цвет автомобиля с минимальной стоимостью: " + colorMinCost(carList));

        //      4) Среднюю стоимость искомой модели modelToFind
        System.out.println("Средняя стоимость модели " + modelToFind1 + ": "
                + String.format(Locale.GERMAN, "%.2f", averagePriceModel(carList, modelToFind1)));
        System.out.println("Средняя стоимость модели " + modelToFind2 + ": "
                + String.format(Locale.GERMAN, "%.2f", averagePriceModel(carList, modelToFind2)));
    }

    public static Set<String> findCarNumberOrMileage(List<Car> carList) {
        Set<String> mySet = new LinkedHashSet<>();

        List<String> listCarNumberOrMileage = carList.stream()
                .filter(x -> x.getCarColor().startsWith(colorToFind) || x.getCarMileage() == mileageToFind)
                .map(Car::getCarNumber)
                .toList();

        mySet.addAll(listCarNumberOrMileage);
        return mySet;
    }

    public static List<String> carUniqueModel(List<Car> carList) {
        return carList.stream()
                .filter(x -> (x.getCarCost() >= n) && (x.getCarCost() <= m))
                .distinct()
                .map(Car::getCarModel)
                .toList();
    }

    public static String colorMinCost(List<Car> carList) {
        return carList.stream()
                .min(Comparator.comparingLong(Car::getCarCost))
                .map(Car::getCarColor)
                .orElse("Не найдено");
    }

    public static double averagePriceModel(List<Car> carList, String modelToFind) {
        return carList.stream()
                .filter(x -> x.getCarModel().equals(modelToFind))
                .collect(Collectors.averagingDouble(Car::getCarCost));
    }
}
