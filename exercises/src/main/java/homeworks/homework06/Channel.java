package homeworks.homework06;

import java.util.Objects;

class Channel {
    private final String channelName;
    private final int channelNumber;
    private Program program;

    public Channel(int channelNumber, String channelName, Program program) {
        this.channelNumber = channelNumber;
        this.channelName = channelName;
        this.program = program;
    }

    public String getChannelName() {
        return channelName;
    }

    public int getChannelNumber() {
        return channelNumber;
    }

    public Program getProgram() {
        return program;
    }

    public void changeProgram(Program newProgram) {
        this.program = newProgram;
        System.out.println("Программа на канале " + channelName + " изменена");
    }

    public void showCurrentProgram() {
        System.out.println("Сейчас на канале " + channelName + ": " + program.getProgramName());
    }

    @Override
    public String toString() {
        return "\n" + "{" +
                "номер = " + channelNumber +
                ", наименование = '" + channelName + '\'' +
                ", программа :" + program +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Channel channel = (Channel) o;
        return channelNumber == channel.channelNumber &&
                Objects.equals(channelName, channel.channelName) &&
                Objects.equals(program, channel.program);
    }

    @Override
    public int hashCode() {
        return Objects.hash(channelNumber, channelName, program);
    }
}
