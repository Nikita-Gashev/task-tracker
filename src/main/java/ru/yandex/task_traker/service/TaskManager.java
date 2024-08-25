package ru.yandex.task_traker.service;

import ru.yandex.task_traker.model.Epic;
import ru.yandex.task_traker.model.Subtask;
import ru.yandex.task_traker.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManager {
    public Map<Integer, Task> tasks = new HashMap<>();
    public Map<Integer, Subtask> subtasks = new HashMap<>();
    public Map<Integer, Epic> epics = new HashMap<>();
    int id = 0;

    public List<Task> getTasksList() {
        List<Task> tasksList = new ArrayList<>();
        for (Task task : tasks.values()) {
            tasksList.add(task);
        }
        return tasksList;
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public void makeTask(Task task) {
        id++;
        task.setId(id);
        tasks.put(id, task);
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void removeTask(int id) {
        tasks.remove(id);
    }

    public List<Task> getSubtaskList() {
        List<Task> SubtasksList = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            SubtasksList.add(subtask);
        }
        return SubtasksList;
    }

    public void removeAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.setStatus("NEW");
        }
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public void makeSubtask(Subtask subtask) {
        id++;
        subtask.setId(id);
        subtasks.put(id, subtask);
        subtask.getEpic().setSubtasks(subtask);
    }

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        if (subtask.getStatus().equals("IN_PROGRESS")) {
            subtask.getEpic().setStatus("IN_PROGRESS");
        } else if (subtask.getStatus().equals("DONE")) {
            String statusEpic = "";
            for (Subtask subtask1 : subtask.getEpic().getSubtasks()) {
                if (subtask1.getStatus().equals("DONE")) {
                    statusEpic = "DONE";
                } else {
                    statusEpic = "IN_PROGRESS";
                }
            }
            subtask.getEpic().setStatus(statusEpic);
        } else {
            String statusEpic = "";
            for (Subtask subtask1 : subtask.getEpic().getSubtasks()) {
                if (subtask1.getStatus().equals("NEW")) {
                    statusEpic = "NEW";
                } else {
                    statusEpic = "IN_PROGRESS";
                }
            }
            subtask.getEpic().setStatus(statusEpic);
        }

    }

    public void removeSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        subtask.getEpic().removeSubtasks(subtask);
        String statusEpic = subtask.getEpic().getStatus();
        if (subtask.getEpic().getSubtasks().isEmpty()) {
            subtask.getEpic().setStatus("NEW");
        } else if (statusEpic.equals("IN_PROGRESS")) {
            for (Subtask subtask1 : subtask.getEpic().getSubtasks()) {
                if (subtask1.getStatus().equals("DONE")) {
                    statusEpic = "DONE";
                } else if (subtask1.getStatus().equals("NEW")) {
                    for (Subtask subtask2 : subtask.getEpic().getSubtasks()) {
                        if (subtask2.getStatus().equals("NEW")) {
                            statusEpic = "NEW";
                        } else {
                            statusEpic = "IN_PROGRESS";
                        }
                    }
                } else {
                    statusEpic = "IN_PROGRESS";
                }
                subtask.getEpic().setStatus(statusEpic);
            }
        }
        subtasks.remove(id);
    }

    public List<Epic> getEpicList() {
        List<Epic> epicsList = new ArrayList<>();
        for (Epic epic : epics.values()) {
            epicsList.add(epic);
        }
        return epicsList;
    }

    public void removeAllEpics() {
        epics.clear();
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public void makeEpic(Epic epic) {
        id++;
        epic.setId(id);
        epics.put(id, epic);
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void removeEpic(int id) {
        epics.remove(id);
    }

    public List<Subtask> getSubtaskOfEpicList(Epic epic) {
        return epic.getSubtasks();
    }
}
