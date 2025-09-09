package homeworks.homework011.repository;

import homeworks.homework011.model.Car;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

// Дополнительно:
// 2. Вынести методы работы с автомобилем в папку repository интерфейс
//CarsRepository и его реализацию CarsRepositoryImpl.
public class CarsRepositoryImpl implements CarsRepository {
    private List<Car> cars = new ArrayList<>();

    @Override
    public List<Car> loadCarsFromFile(String file) {
        List<Car> loadCars = new ArrayList<>();
        boolean isFirstLine = true;
        file = "/home/westler-555/school-prog/exercises/src/main/java/homeworks/homework011/" + file;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {continue;}
                if (isFirstLine) {isFirstLine = false; continue;}

                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    loadCars.add(new Car(parts[0].trim(), parts[1].trim(), parts[2].trim(),
                            Integer.parseInt(parts[3].trim()), Integer.parseInt(parts[4].trim())));
                }
            }
            this.cars = loadCars;
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        }
        return loadCars;
    }

    @Override
    public void saveCarsToFile(String file, List<Car> listCars) {
        file = "/home/westler-555/school-prog/exercises/src/main/java/homeworks/homework011/" + file;
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println("Автомобили в базе:");
            writer.println("Number\tModel\tColor\tMileage\tCost");
            for (Car car : listCars) {
                writer.println(car.toString());
            }
        } catch (IOException e) {
            System.err.println("Ошибка записи файла: " + e.getMessage());
        }
    }

    @Override
    public Set<String> findCarNumberOrMileage(List<Car> carList, String colorToFind, int mileageToFind) {
        Set<String> mySet = new LinkedHashSet<>();

        List<String> listCarNumberOrMileage = carList.stream()
                .filter(x -> x.getCarColor().startsWith(colorToFind) || x.getCarMileage() == mileageToFind)
                .map(Car::getCarNumber)
                .toList();

        mySet.addAll(listCarNumberOrMileage);
        return mySet;
    }

    @Override
    public List<String> carUniqueModel(List<Car> carList, long n, long m) {
        return carList.stream()
                .filter(x -> (x.getCarCost() >= n) && (x.getCarCost() <= m))
                .map(Car::getCarModel)
                .toList();
    }

    @Override
    public String colorMinCost(List<Car> carList) {
        return carList.stream()
                .min(Comparator.comparingLong(Car::getCarCost))
                .map(Car::getCarColor)
                .orElse("Не найдено");
    }

    @Override
    public double averagePriceModel(List<Car> carList, String modelToFind) {
        return carList.stream()
                .filter(x -> x.getCarModel().equals(modelToFind))
                .collect(Collectors.averagingDouble(Car::getCarCost));
    }
}
