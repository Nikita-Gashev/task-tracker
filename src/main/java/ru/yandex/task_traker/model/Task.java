package ru.yandex.task_traker.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected TaskStatus status = TaskStatus.NEW;
    protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    protected LocalDateTime startTime;
    protected Duration duration;

    @Override
    public String toString() {
        return id + "," + TaskType.TASK + "," + name + "," + status + "," + description + "," + duration.toMinutes()
                + "," + startTime.format(formatter);
    }

    public Task(String name, String description, String startTime, int duration) {
        this.name = name;
        this.description = description;
        this.startTime = LocalDateTime.parse(startTime, formatter);
        this.duration = Duration.ofMinutes(duration);
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    protected Task(String id, String name, String description, String status) {
        this.id = Integer.parseInt(id);
        this.name = name;
        this.description = description;
        this.status = TaskStatus.valueOf(status);
    }

    public static Task makeTaskFromString(String value) {
        String[] splitValue = value.split(",");
        return new Task(splitValue[0], splitValue[2], splitValue[4], splitValue[3]);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }
}
