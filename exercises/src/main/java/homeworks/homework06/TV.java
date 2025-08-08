package homeworks.homework06;

import java.util.List;
import java.util.Objects;

class TV {

    private int diagonal;
    private String mark;
    private List<Channel> channelList;
    private boolean isOn;
    private int currentChannel;

    public TV(int diagonal, String mark, List<Channel> channelList) {
        this.diagonal = diagonal;
        this.mark = mark;
        this.channelList = channelList;
        this.isOn = false;
        this.currentChannel = 1;
    }

    public int getDiagonal() {
        return diagonal;
    }

    public String getMark() {
        return mark;
    }

    public List<Channel> getChannels() {
        return channelList;
    }

    public boolean isOn() {
        return isOn;
    }

    public int getCurrentChannel() {
        return currentChannel;
    }

    public void switchOnOff() {
        System.out.println("Попытка включить/выключить телевизор");
        isOn = !isOn;
        System.out.println("Телевизор " + (isOn ? "включен" : "выключен"));
    }

    public void choiceChannel(int channelNumber) {
        System.out.println("Попытка включить канал: " + channelNumber);
        if (!isOn) {
            System.out.println("Телевизор выключен, нельзя переключить канал");
            return;
        }

        boolean channelExists = false;
        for (Channel channel : channelList) {
            if (channel.getChannelNumber() == channelNumber) {
                channelExists = true;
                break;
            }
        }

        if (channelExists) {
            currentChannel = channelNumber;
            System.out.println("Переключен на канал " + channelNumber);
        } else {
            System.out.println("Канал " + channelNumber + " не найден");
        }
    }

    @Override
    public String toString() {
        return "Телевизор :\n" +
                "диагональ = " + diagonal + '\'' +
                ", марка = '" + mark + '\'' +
                ", Включен = " + (isOn ? "да" : "нет") +
                ", Текущий канал = " + currentChannel +
                ",\nканалы :" + channelList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TV tv = (TV) o;
        return diagonal == tv.diagonal &&
                isOn == tv.isOn &&
                currentChannel == tv.currentChannel &&
                Objects.equals(mark, tv.mark) &&
                Objects.equals(channelList, tv.channelList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(diagonal, mark, channelList, isOn, currentChannel);
    }
}
