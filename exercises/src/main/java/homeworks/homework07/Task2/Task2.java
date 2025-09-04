package homeworks.homework07.Task2;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
// С консоли на вход подается две строки s и t. Необходимо вывести true, если одна строка является валидной анаграммой другой строки, и false – если это не так.
//Анаграмма – это слово, или фраза, образованная путем перестановки букв другого слова или фразы, обычно с использованием всех исходных букв ровно один раз.
//Для проверки: Бейсбол – бобслей
// Героин – регион
// Клоака – околка
public class Task2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите две строки через дефис (например: бейсбол - бобслей): ");
        String inputStr = scanner.nextLine();

        String[] line = inputStr.split("\\s*-\\s*", 2);
        if (line.length < 2) {
            System.out.println("Ошибка: введите две строки через дефис");
            return;
        }
        String s = line[0].trim().toLowerCase();
        String t = line[1].trim().toLowerCase();

        System.out.println(isAnagram(s, t));
    }
    private static boolean isAnagram(String s, String t) {
        if (s == null || t == null || s.length() != t.length()) return false;
        if (s.equals(t)) return true;

        Map<Character, Integer> myMap = new HashMap();
        for (char ch : s.toCharArray()) {
            myMap.put(ch, myMap.getOrDefault(ch, 0) + 1);
        }
        for (char ch : t.toCharArray()) {
            if (!myMap.containsKey(ch)) return false;

            if (myMap.get(ch) == 1) myMap.remove(ch);
            else myMap.put(ch, myMap.get(ch) - 1);
        }
        return myMap.isEmpty();
    }
}
