package ru.yandex.task_traker.model;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description, int epicId, String startTime, int duration) {
        super(name, description, startTime, duration);
        this.epicId = epicId;
    }

    private Subtask(String id, String name, String description, String status, String epicId, String duration,
                    String startTime) {
        super(id, name, description, status, duration, startTime);
        this.epicId = Integer.parseInt(epicId);
    }

    public static Subtask makeSubtaskFromString(String value) {
        String[] splitValue = value.split(",");
        return new Subtask(splitValue[0], splitValue[2], splitValue[4], splitValue[3], splitValue[5], splitValue[6],
                splitValue[7]);
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%d,%s,%s",
                id,
                TaskType.SUBTASK,
                name,
                status,
                description,
                epicId,
                duration.toMinutes(),
                startTime.format(formatter));
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
