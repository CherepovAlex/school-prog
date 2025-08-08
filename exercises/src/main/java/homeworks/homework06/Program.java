package homeworks.homework06;

import java.util.Objects;

class Program {
    private final String programName;
    private double programRating;
    private int programViewersCount;

    public Program(String programName, double programRating, int programViewersCount) {
        this.programName = programName;
        this.programRating = programRating;
        this.programViewersCount = programViewersCount;
    }

    public String getProgramName() {
        return programName;
    }

    public int getProgramViewers() {
        return programViewersCount;
    }

    public double getProgramRating() {
        return programRating;
    }

    public void updateRating(double newRating) {
        this.programRating = newRating;
        System.out.println("Рейтинг программы " + programName + " обновлен");
    }

    public void updateProgramViewers(int newProgramViewersCount) {
        this.programViewersCount = newProgramViewersCount;
    }

    @Override
    public String toString() {
        return "{" +
                "наименование = '" + programName + '\'' +
                ", рейтинг = " + programRating +
                ", аудитория =" + programViewersCount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Program program = (Program) o;
        return Double.compare(program.programRating, programRating) == 0 &&
                programViewersCount == program.programViewersCount &&
                Objects.equals(programName, program.programName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(programName, programRating, programViewersCount);
    }
}
