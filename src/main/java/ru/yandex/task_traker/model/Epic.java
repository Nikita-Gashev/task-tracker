package ru.yandex.task_traker.model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Subtask> subtasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    private Epic(String id, String name, String description, String status) {
        super(id, name, description, status);
    }

    public static Epic makeEpicFromString (String value) {
        String[] splitValue = value.split(",");
        return new Epic(splitValue[0], splitValue[2], splitValue[4], splitValue[3]);
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(Subtask subtask) {
        this.subtasks.add(subtask);
    }

    public void removeSubtasks(Subtask subtask) {
        this.subtasks.remove(subtask);
    }

    @Override
    public String toString() {
        return id + "," + TaskType.EPIC + "," + name + "," + status + "," + description + "\n";
    }
}
