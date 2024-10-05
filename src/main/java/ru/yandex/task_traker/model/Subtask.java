package ru.yandex.task_traker.model;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    private Subtask(String id, String name, String description, String status, String epicId) {
        super(id, name, description, status);
        this.epicId = Integer.parseInt(epicId);
    }

    public static Subtask makeSubtaskFromString (String value) {
        String[] splitValue = value.split(",");
        return new Subtask(splitValue[0], splitValue[2], splitValue[4], splitValue[3],splitValue[5]);
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return id + "," + TaskType.SUBTASK + "," + name + "," + status + "," + description + "," + epicId + "\n";
    }
}
