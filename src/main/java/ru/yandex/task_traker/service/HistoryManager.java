package ru.yandex.task_traker.service;

import ru.yandex.task_traker.model.Task;

import java.util.List;

public interface HistoryManager {

    void add(Task task);

    List<Task> getHistory();
}
