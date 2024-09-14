package ru.yandex.task_traker.service;

import ru.yandex.task_traker.model.Epic;
import ru.yandex.task_traker.model.Subtask;
import ru.yandex.task_traker.model.Task;
import ru.yandex.task_traker.util.Managers;
import ru.yandex.task_traker.util.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private int id = 0;
    private final HistoryManager history = Managers.getDefaultHistory();

    @Override
    public List<Task> getTasksList() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void removeAllTasks() {
        tasks.clear();
    }

    @Override
    public Task getTaskById(int id) {
        if (tasks.get(id) == null) {
            throw new IllegalArgumentException("Введено некорретное значение индентификатора");
        } else {
            history.add(tasks.get(id));
            return tasks.get(id);
        }
    }

    @Override
    public void createTask(Task task) {
        id++;
        task.setId(id);
        tasks.put(id, task);
    }

    @Override
    public void updateTask(Task task) {
        if (!(tasks.containsValue(task))) {
            throw new IllegalArgumentException("Задача отсутствует в списке");
        } else {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void removeTask(int id) {
        if (tasks.get(id) == null) {
            throw new IllegalArgumentException("Введено некорретное значение индентификатора");
        } else {
            tasks.remove(id);
        }
    }

    @Override
    public List<Task> getSubtaskList() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void removeAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasks().clear();
            epic.setStatus(TaskStatus.NEW);
        }
    }

    @Override
    public Subtask getSubtaskById(int id) {
        if (subtasks.get(id) == null) {
            throw new IllegalArgumentException("Введено некорретное значение индентификатора");
        } else {
            history.add(subtasks.get(id));
            return subtasks.get(id);
        }
    }

    @Override
    public void createSubtask(Subtask subtask) {
        id++;
        subtask.setId(id);
        subtasks.put(id, subtask);
        subtask.getEpic().setSubtasks(subtask);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (!subtasks.containsKey(subtask.getId())) {
            throw new IllegalArgumentException("Подзадача отсутствует в списке");
        }
        subtasks.put(subtask.getId(), subtask);
        assignStatusForEpic(subtask);

    }

    @Override
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

    @Override
    public List<Epic> getEpicList() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void removeAllEpics() {
        epics.clear();
    }

    @Override
    public Epic getEpicById(int id) {
        if (epics.get(id) == null) {
            throw new IllegalArgumentException("Введено некорретное значение индентификатора");
        } else {
            history.add(epics.get(id));
            return epics.get(id);
        }
    }

    @Override
    public void createEpic(Epic epic) {
        id++;
        epic.setId(id);
        epics.put(id, epic);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (!(epics.containsValue(epic))) {
            throw new IllegalArgumentException("Епик отсутствует в списке");
        } else {
            epics.put(epic.getId(), epic);
        }
    }

    @Override
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

    @Override
    public List<Subtask> getSubtaskOfEpicList(Epic epic) {
        return epic.getSubtasks();
    }

    @Override
    public List<Task> getHistory() {
        return history.getHistory();
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
}
