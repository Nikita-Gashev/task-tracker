package ru.yandex.task_traker.service.impl;

import org.junit.jupiter.api.Test;
import ru.yandex.task_traker.model.Task;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {

    final InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

    @Test
    void shouldReturnEmptyListWhenNoTasksAdd() {
        assertEquals(0, historyManager.getHistory().size(), "В итстории есть задачи");
    }

    @Test
    void shouldReturn1WhenSameTasksAdd() {
        Task task1 = new Task("Задача 1", "Описание");
        task1.setId(1);
        historyManager.add(task1);
        historyManager.add(task1);
        assertEquals(1, historyManager.getHistory().size(), "В итстории две задачи");
    }

    @Test
    void shouldReturnEmptyListWhenTaskRemove() {
        Task task1 = new Task("Задача 1", "Описание");
        task1.setId(1);
        historyManager.add(task1);
        historyManager.remove(1);
        assertEquals(0, historyManager.getHistory().size(), "В итстории есть задачи");
    }
}