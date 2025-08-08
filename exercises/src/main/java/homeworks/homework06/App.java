package homeworks.homework06;

import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main(String[] args) {
        TV tv = getTV();
        System.out.println(tv);
        System.out.println();

        tv.choiceChannel(2);
        System.out.println();

        tv.switchOnOff();
        System.out.println();
        System.out.println(tv);
        System.out.println();

        tv.choiceChannel(2);
        System.out.println();
        System.out.println(tv);

        System.out.println();
        tv.switchOnOff();
    }

    private static TV getTV() {
        List<Channel> channels = new ArrayList<>();
        channels.add(new Channel(1, "Первый",
                new Program("Доброе утро", 9.0, 1_000_000)));
        channels.add(new Channel(2, "Россия",
                new Program("Кино", 7.2, 500_000)));
        channels.add(new Channel(3, "Волейбол",
                new Program("Ток-шоу", 8.8, 850_000)));

        return new TV(55, "Samsung", channels);
    }
}
