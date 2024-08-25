package ru.yandex.task_traker.model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Subtask> subtasks = new ArrayList<>();

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }

    public Epic(String name, String description) {
        super(name, description);
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
}
