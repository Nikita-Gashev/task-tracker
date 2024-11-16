package ru.yandex.task_traker.service;

import ru.yandex.task_traker.model.Epic;
import ru.yandex.task_traker.model.Subtask;
import ru.yandex.task_traker.model.Task;

import java.util.List;
import java.util.SortedSet;

public interface TaskManager {

    SortedSet<Task> getPrioritizedTasks();

    List<Task> getTasksList();

    void removeAllTasks();

    Task getTaskById(int id);

    void createTask(Task task);

    void updateTask(Task task);

    void removeTask(int id);

    List<Task> getSubtaskList();

    void removeAllSubtasks();

    Subtask getSubtaskById(int id);

    void createSubtask(Subtask subtask);

    void updateSubtask(Subtask subtask);

    void removeSubtask(int id);

    List<Epic> getEpicList();

    void removeAllEpics();

    Epic getEpicById(int id);

    void createEpic(Epic epic);

    void updateEpic(Epic epic);

    void removeEpic(int id);

    List<Subtask> getSubtaskOfEpicList(Epic epic);

    List<Task> getHistory();
}
