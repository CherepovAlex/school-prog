package homeworks.homework07.Task3;

import java.util.HashSet;
import java.util.Set;

// Реализовать класс PowerfulSet, в котором должны быть следующие методы:
// - public <T> Set<T> intersection(Set<T> set1, Set<T> set2) – возвращает пересечение двух наборов.
//Пример: set1 = {1, 2, 3}, set2 = {0, 1, 2, 4}. Вернуть {1, 2}
// - public <T> Set<T> union(Set<T> set1, Set<T> set2) – возвращает объединение двух наборов
//Пример: set1 = {1, 2, 3}, set2 = {0, 1, 2, 4}. Вернуть {0, 1, 2, 3, 4}
// - public <T> Set<T> relativeComplement(Set<T> set1, Set<T> set2) –
//возвращает элементы первого набора без тех, которые находятся также и во втором наборе.
//Пример: set1 = {1, 2, 3}, set2 = {0, 1, 2, 4}. Вернуть {3}
public class Task3 {

    public static void main(String[] args) {

        PowerfulSet powerfulSet = new PowerfulSet();

        Set<Integer> set1 = new HashSet<>(Set.of(4, 7, 5, 0, 1));
        Set<Integer> set2 = new HashSet<>(Set.of(1, 5, 2, 7, 3));

        System.out.println("Intersection: " + powerfulSet.intersection(set1, set2));
        System.out.println("Union: " + powerfulSet.union(set1, set2));
        System.out.println("Relative complement: " + powerfulSet.relativeComplement(set1, set2));

        System.out.println("Set1: " + set1);
        System.out.println("Set2: " + set2);
    }
}

class PowerfulSet {

    public <T> Set<T> intersection(Set<T> set1, Set<T> set2) {
        Set<T> result = new HashSet<>();
        for (T element : set1) {
            if (set2.contains(element)) {
                result.add(element);
            }
        }
        return result;
    }

    public <T> Set<T> union(Set<T> set1, Set<T> set2) {
        Set<T> result = new HashSet<>(set1.size() + set2.size());
        result.addAll(set1);
        result.addAll(set2);
        return result;
    }

    public <T> Set<T> relativeComplement(Set<T> set1, Set<T> set2) {
        Set<T> result = new HashSet<>();
        for (T element : set1) {
            if (!set2.contains(element)) {
                result.add(element);
            }
        }
        return result;
    }

}