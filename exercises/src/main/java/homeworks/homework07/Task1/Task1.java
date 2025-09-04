package homeworks.homework07.Task1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

// Реализовать метод, который на вход принимает ArrayList<T>, а возвращает набор уникальных элементов этого массива.
// Решить, используя коллекции

public class Task1 {

    public static <T> Set<T> uniqueElements(ArrayList<T> arrayList) {
        Set<T> hashSet = new HashSet<>();

        for (T element : arrayList) {
            hashSet.add(element);
        }
        return hashSet;
    }

    public static void main(String[] args) {
        // Integer
        ArrayList<Integer> intList = new ArrayList<>(Arrays.asList(1, 2, 3, 2, 5, 4, 1, 6, 4));
        System.out.println("Integer : " + uniqueElements(intList));

        // String
        ArrayList<String> strList = new ArrayList<>(Arrays.asList("java", "python", "java", "go", "c++", "c++", "python", "java", "scala"));
        System.out.println("String : " + uniqueElements(strList));

        // Empty
        ArrayList<String> emptyList = new ArrayList<>();
        System.out.println("EmptyList : " + uniqueElements(emptyList));

    }
}
