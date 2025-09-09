package homeworks.homework011.repository;

import homeworks.homework011.model.Car;

import java.util.List;
import java.util.Set;
// Дополнительно:
// 2. Вынести методы работы с автомобилем в папку repository интерфейс
//CarsRepository и его реализацию CarsRepositoryImpl.
public interface CarsRepository {

    List<Car> loadCarsFromFile(String file);

    void saveCarsToFile(String file, List<Car> carList);

    Set<String> findCarNumberOrMileage(List<Car> carList, String colorToFind, int mileageToFind);

    List<String> carUniqueModel(List<Car> carList, long n, long m);

    String colorMinCost(List<Car> carList);

    double averagePriceModel(List<Car> carList, String modelToFind);


}
