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
        if (!subtasks.containsKey(subtask.getId())) {
            throw new IllegalArgumentException("Подзадача отсутствует в списке");
        }
        subtasks.put(subtask.getId(), subtask);
        assignStatusForEpic(subtask);

    }

    private void assignStatusForEpic(Subtask subtask) {

        boolean epicDone = false;
        boolean epicNew = false;

        for (Subtask subtaskForChecking : subtask.getEpic().getSubtasks()) {
            if (subtaskForChecking.getStatus().equals(TaskStatus.DONE)) {
                epicDone = true;
            } else if (subtaskForChecking.getStatus().equals(TaskStatus.NEW)) {
                epicNew = true;
            } else {
                subtask.getEpic().setStatus(TaskStatus.IN_PROGRESS);
                break;
            }
        }

        if (epicDone & !epicNew) {
            subtask.getEpic().setStatus(TaskStatus.DONE);
        } else if (!epicDone & epicNew) {
            subtask.getEpic().setStatus(TaskStatus.NEW);
        } else {
            subtask.getEpic().setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    public void removeSubtask(int id) {
        if (subtasks.get(id) == null) {
            throw new IllegalArgumentException("Введено некорретное значение индентификатора");
        } else {
            Subtask subtask = subtasks.get(id);
            subtask.getEpic().removeSubtasks(subtask);
            assignStatusForEpic(subtask);
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
