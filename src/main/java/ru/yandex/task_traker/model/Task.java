package ru.yandex.task_traker.model;

public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected TaskStatus status = TaskStatus.NEW;

    @Override
    public String toString() {
        return id + "," + TaskType.TASK + "," + name + "," + status + "," + description + "\n";
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    protected Task(String id, String name, String description, String status){
        this.id = Integer.parseInt(id);
        this.name = name;
        this.description = description;
        this.status = TaskStatus.valueOf(status);
    }

    public static Task makeTaskFromString (String value) {
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
}
