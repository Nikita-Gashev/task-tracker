package ru.yandex.task_traker.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected TaskStatus status = TaskStatus.NEW;
    protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    protected LocalDateTime startTime;
    protected Duration duration;

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

    protected Task(String id, String name, String description, String status, String duration, String startTime) {
        this.id = Integer.parseInt(id);
        this.name = name;
        this.description = description;
        this.status = TaskStatus.valueOf(status);
        this.startTime = LocalDateTime.parse(startTime, formatter);
        this.duration = Duration.ofMinutes(Integer.parseInt(duration));

    }

    public static Task makeTaskFromString(String value) {
        String[] splitValue = value.split(",");
        return new Task(splitValue[0], splitValue[2], splitValue[4], splitValue[3], splitValue[5], splitValue[6]);
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

    public DateTimeFormatter getFormatter() {
        return formatter;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    @Override
    public String toString() {
        return id + "," + TaskType.TASK + "," + name + "," + status + "," + description + "," + duration.toMinutes()
                + "," + startTime.format(formatter);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Task otherTask = (Task) obj;
        return Objects.equals(name, otherTask.name) &&
                Objects.equals(description, otherTask.description) &&
                (id == otherTask.id) &&
                Objects.equals(status, otherTask.status) &&
                Objects.equals(startTime, otherTask.startTime) &&
                Objects.equals(duration, otherTask.duration);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
