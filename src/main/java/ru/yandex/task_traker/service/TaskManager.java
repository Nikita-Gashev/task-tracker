package ru.yandex.task_traker.service;

import ru.yandex.task_traker.model.Epic;
import ru.yandex.task_traker.model.Subtask;
import ru.yandex.task_traker.model.Task;
import ru.yandex.task_traker.util.TaskStatus;

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
        return new ArrayList<>(tasks.values());
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public Task getTaskById(int id) {
        if (tasks.get(id) == null) {
            throw new IllegalArgumentException("Введено некорретное значение индентификатора");
        } else {
            return tasks.get(id);
        }
    }

    public void createTask(Task task) {
        id++;
        task.setId(id);
        tasks.put(id, task);
    }

    public void updateTask(Task task) {
        if (!(tasks.containsValue(task))) {
            throw new IllegalArgumentException("Задача отсутствует в списке");
        } else {
            tasks.put(task.getId(), task);
        }
    }

    public void removeTask(int id) {
        if (tasks.get(id) == null) {
            throw new IllegalArgumentException("Введено некорретное значение индентификатора");
        } else {
            tasks.remove(id);
        }
    }

    public List<Task> getSubtaskList() {
        return new ArrayList<>(subtasks.values());
    }

    public void removeAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasks().clear();
            epic.setStatus(TaskStatus.NEW);
        }
    }

    public Subtask getSubtaskById(int id) {
        if (subtasks.get(id) == null) {
            throw new IllegalArgumentException("Введено некорретное значение индентификатора");
        } else {
            return subtasks.get(id);
        }
    }

    public void createSubtask(Subtask subtask) {
        id++;
        subtask.setId(id);
        subtasks.put(id, subtask);
        subtask.getEpic().setSubtasks(subtask);
    }

    public void updateSubtask(Subtask subtask) {
        if (!(subtasks.containsValue(subtask))) {
            throw new IllegalArgumentException("Подзадача отсутствует в списке");
        } else {
            subtasks.put(subtask.getId(), subtask);
            if (subtask.getStatus().equals(TaskStatus.IN_PROGRESS)) {
                subtask.getEpic().setStatus(TaskStatus.IN_PROGRESS);
            } else if (subtask.getStatus().equals(TaskStatus.DONE)) {
                TaskStatus statusEpic = TaskStatus.DONE;
                for (Subtask subtaskForChecking : subtask.getEpic().getSubtasks()) {
                    if (subtaskForChecking.getStatus().equals(TaskStatus.DONE)) {
                        statusEpic = TaskStatus.DONE;
                    } else {
                        statusEpic = TaskStatus.IN_PROGRESS;
                    }
                }
                subtask.getEpic().setStatus(statusEpic);
            } else {
                TaskStatus statusEpic = TaskStatus.NEW;
                for (Subtask subtask1 : subtask.getEpic().getSubtasks()) {
                    if (subtask1.getStatus().equals(TaskStatus.NEW)) {
                        statusEpic = TaskStatus.NEW;
                    } else {
                        statusEpic = TaskStatus.IN_PROGRESS;
                    }
                }
                subtask.getEpic().setStatus(statusEpic);
            }
        }
    }

    public void removeSubtask(int id) {
        if (subtasks.get(id) == null) {
            throw new IllegalArgumentException("Введено некорретное значение индентификатора");
        } else {
            Subtask subtask = subtasks.get(id);
            subtask.getEpic().removeSubtasks(subtask);
            TaskStatus statusEpic = subtask.getEpic().getStatus();
            if (subtask.getEpic().getSubtasks().isEmpty()) {
                subtask.getEpic().setStatus(TaskStatus.NEW);
            } else if (statusEpic.equals(TaskStatus.IN_PROGRESS)) {
                for (Subtask subtaskForChecking : subtask.getEpic().getSubtasks()) {
                    if (subtaskForChecking.getStatus().equals(TaskStatus.DONE)) {
                        statusEpic = TaskStatus.DONE;
                    } else if (subtaskForChecking.getStatus().equals(TaskStatus.NEW)) {
                        for (Subtask subtask2 : subtask.getEpic().getSubtasks()) {
                            if (subtask2.getStatus().equals(TaskStatus.NEW)) {
                                statusEpic = TaskStatus.NEW;
                            } else {
                                statusEpic = TaskStatus.IN_PROGRESS;
                            }
                        }
                    } else {
                        statusEpic = TaskStatus.IN_PROGRESS;
                    }
                    subtask.getEpic().setStatus(statusEpic);
                }
            }
        }
        subtasks.remove(id);
    }

    public List<Epic> getEpicList() {
        return new ArrayList<>(epics.values());
    }

    public void removeAllEpics() {
        epics.clear();
    }

    public Epic getEpicById(int id) {
        if (epics.get(id) == null) {
            throw new IllegalArgumentException("Введено некорретное значение индентификатора");
        } else {
            return epics.get(id);
        }
    }

    public void createEpic(Epic epic) {
        id++;
        epic.setId(id);
        epics.put(id, epic);
    }

    public void updateEpic(Epic epic) {
        if (!(epics.containsValue(epic))) {
            throw new IllegalArgumentException("Епик отсутствует в списке");
        } else {
            epics.put(epic.getId(), epic);
        }
    }

    public void removeEpic(int id) {
        if (epics.get(id) == null) {
            throw new IllegalArgumentException("Введено некорретное значение индентификатора");
        } else {
            Epic epic = epics.get(id);
            for (Subtask subtask : epic.getSubtasks()) {
                subtasks.remove(subtask.getId());
            }
            epics.remove(id);
        }
    }

    public List<Subtask> getSubtaskOfEpicList(Epic epic) {
        return epic.getSubtasks();
    }
}
