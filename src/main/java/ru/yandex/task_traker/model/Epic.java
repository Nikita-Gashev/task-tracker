package ru.yandex.task_traker.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Subtask> subtasks = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
    }

    private Epic(String id, String name, String description, String status) {
        super(id, name, description, status);
    }

    public static Epic makeEpicFromString(String value) {
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

    public void assignStatus() {
        boolean epicDone = false;
        boolean epicNew = false;

        if (getSubtasks().isEmpty()) {
            setStatus(TaskStatus.NEW);
            return;
        }

        for (Subtask subtask : subtasks) {
            if (subtask.getStatus().equals(TaskStatus.DONE)) {
                epicDone = true;
            } else if (subtask.getStatus().equals(TaskStatus.NEW)) {
                epicNew = true;
            } else {
                setStatus(TaskStatus.IN_PROGRESS);
                return;
            }
        }

        if (epicDone & !epicNew) {
            setStatus(TaskStatus.DONE);
        } else if (!epicDone & epicNew) {
            setStatus(TaskStatus.NEW);
        } else {
            setStatus(TaskStatus.IN_PROGRESS);
        }

    }

    private void updateStartTime() {
        if (subtasks.isEmpty()) {
            startTime = null;
            return;
        }
        startTime = subtasks.get(0).getStartTime();
        subtasks.forEach(subtask -> {
            if (subtask.getStartTime().isBefore(startTime)) {
                startTime = subtask.getStartTime();
            }
        });
    }

    private void updateEndTime() {
        if (subtasks.isEmpty()) {
            startTime = null;
            return;
        }
        for (Subtask subtask : subtasks) {
            if (endTime == null) {
                endTime = subtask.getEndTime();
            } else if (subtask.getEndTime().isAfter(endTime)) {
                endTime = subtask.getEndTime();
            }
        }
    }

    private void updateDuration() {
        if (subtasks.isEmpty()) {
            duration = null;
            return;
        }
        if (startTime != null && endTime != null) {
            duration = Duration.between(startTime, endTime);
        }
    }

    public void updateTimeAndDuration() {
        updateStartTime();
        updateEndTime();
        updateDuration();
    }

    @Override
    public String toString() {
        if (duration == null || startTime == null) {
            return id + "," + TaskType.EPIC + "," + name + "," + status + "," + description;
        } else {
            return id + "," + TaskType.EPIC + "," + name + "," + status + "," + description + "," + duration.toMinutes()
                    + "," + startTime.format(formatter);
        }
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
