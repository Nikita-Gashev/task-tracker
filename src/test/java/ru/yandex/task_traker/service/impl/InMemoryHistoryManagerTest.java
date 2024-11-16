package ru.yandex.task_traker.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.task_traker.model.Task;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {

    final InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

    @Test
    @DisplayName("Проверка пустой истории")
    void getHistory1() {
        assertEquals(0, historyManager.getHistory().size(), "В истории есть задачи");
    }

    @Test
    @DisplayName("Проверка дублирования")
    void getHistory2() {
        Task task1 = new Task("Задача 1", "Описание");
        task1.setId(1);
        historyManager.add(task1);
        historyManager.add(task1);
        assertEquals(1, historyManager.getHistory().size(), "В истории две задачи");
    }

    @Test
    @DisplayName("Проверка удаления из истории")
    void getHistory3() {
        Task task1 = new Task("Задача 1", "Описание");
        task1.setId(1);
        historyManager.add(task1);
        historyManager.remove(1);
        assertEquals(0, historyManager.getHistory().size(), "В истории есть задачи");
    }
}