package ru.yandex.task_traker.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.task_traker.model.Epic;
import ru.yandex.task_traker.model.Subtask;
import ru.yandex.task_traker.model.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {

    final InMemoryTaskManager manager = new InMemoryTaskManager();

    final Task task1 = new Task("Задача 1", "Описание задачи 1", "20-10-2024 11:00",
            120);
    final Task task2 = new Task("Задача 2", "Описание задачи 2", "20-10-2024 17:00",
            120);
    final Epic epic1 = new Epic("Епик 1", "Описание епик 1");
    final Subtask subtask1 = new Subtask("Подзадача 1", "Подзадача 1 епика 1", 1,
            "21-10-2024 12:00", 120);
    final Subtask subtask2 = new Subtask("Подзадача 2", "Подзадача 2 епика 1", 1,
            "21-10-2024 17:00", 180);

    @Test
    @DisplayName("Проверка сортировки")
    void getPrioritizedTasks() {
        manager.createTask(subtask2);
        manager.createTask(task1);
        manager.createTask(subtask1);
        List<Task> listForChecking = List.of(task1, subtask1, subtask2);
        List<Task> prioritizedListOfTasks = new ArrayList<>(manager.getPrioritizedTasks());
        assertEquals(listForChecking, prioritizedListOfTasks, "Списки не совпадают");
    }

    @Test
    @DisplayName("Проверка пустого списка задач")
    void getTasksList1() {
        assertEquals(0, manager.getTasksList().size(), "В списке есть задачи");
    }

    @Test
    @DisplayName("Проверка списка задач с одной задачей")
    void getTasksList2() {
        manager.createTask(task1);
        assertEquals(1, manager.getTasksList().size(), "Некорректное количество задач в списке");
    }

    @Test
    @DisplayName("Проверка удаления всех задач")
    void removeAllTasks() {
        manager.createTask(task1);
        manager.createTask(task2);
        manager.removeAllTasks();
        assertEquals(0, manager.getTasksList().size(), "В списке есть задачи");
    }

    @Test
    @DisplayName("Проверка выбрасывания исключения при некорректном id")
    void getTaskByIdWithException() {
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    manager.getTaskById(1);
                },
                "Исключение не выброшено"
        );
        assertEquals("Введено некорректное значение идентификатора", exception.getMessage());
    }

    @Test
    @DisplayName("Проверка получения задачи по id")
    void getTaskById() {
        manager.createTask(task1);
        assertEquals(task1, manager.getTaskById(1), "Задачи не совпадают");
    }

    @Test
    @DisplayName("Не должен создавать задачу с пересекающимся временем")
    void checkTimeCrossing() {
        manager.createTask(task1);
        manager.createTask(new Task("Задача 2", "Описание задачи 2", "20-10-2024 12:00",
                120));
        assertEquals(1, manager.getTasksList().size(), "В списке больше одной задачи");
    }

    @Test
    @DisplayName("Проверка выбрасывания исключения при обновлении несуществующей задачи")
    void updateTaskWithException() {
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    manager.updateTask(task1);
                },
                "Исключение не выброшено"
        );
        assertEquals("Задача отсутствует в списке", exception.getMessage());
    }

    @Test
    @DisplayName("Обновление задачи")
    void updateTask() {
        manager.createTask(task1);
        Task taskForUpdate = new Task("Обновленное задание 1", "Описание");
        taskForUpdate.setId(1);
        manager.updateTask(taskForUpdate);
        assertEquals(taskForUpdate, manager.getTaskById(1), "Задачи не совпадают");
    }

    @Test
    @DisplayName("Проверка выбрасывания исключения при удалении несуществующей задачи")
    void removeTaskWithException() {
        manager.createTask(task1);
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    manager.removeTask(10);
                },
                "Исключение не выброшено"
        );
        assertEquals("Введено некорректное значение идентификатора", exception.getMessage());
    }

    @Test
    @DisplayName("Удаление задачи")
    void removeTask() {
        manager.createTask(task1);
        manager.removeTask(1);
        assertEquals(0, manager.getTasksList().size(), "Задача не удалена");
    }

    @Test
    @DisplayName("Проверка пустого списка подзадач")
    void getSubtaskList1() {
        assertEquals(0, manager.getSubtaskList().size(), "В списке есть подзадачи");
    }

    @Test
    @DisplayName("Проверка списка подзадач с одной подзадачей")
    void getSubtaskList2() {
        manager.createEpic(epic1);
        manager.createSubtask(subtask1);
        assertEquals(1, manager.getSubtaskList().size(), "Некорректное количество подзадач в списке");
    }

    @Test
    @DisplayName("Проверка удаления всех подзадач")
    void removeAllSubtasks() {
        manager.createEpic(epic1);
        manager.createTask(subtask1);
        manager.createTask(subtask2);
        manager.removeAllSubtasks();
        assertEquals(0, manager.getSubtaskList().size(), "В списке есть подзадачи");
    }

    @Test
    @DisplayName("Проверка выбрасывания исключения при некорректном id")
    void getSubtaskById1() {
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    manager.getSubtaskById(1);
                },
                "Исключение не выброшено"
        );
        assertEquals("Введено некорректное значение идентификатора", exception.getMessage());
    }

    @Test
    @DisplayName("Проверка получения подзадачи по id")
    void getSubtaskById2() {
        manager.createEpic(epic1);
        manager.createSubtask(subtask1);
        assertEquals(subtask1, manager.getSubtaskById(2), "Задачи не совпадают");
    }

    @Test
    @DisplayName("Проверка связи подзадачи с эпиком")
    void getEpicId() {
        manager.createEpic(epic1);
        manager.createSubtask(subtask1);
        assertEquals(1, manager.getSubtaskById(2).getEpicId(), "Id не совпадает");
    }

    @Test
    @DisplayName("Проверка выбрасывания исключения при обновлении несуществующей подзадачи")
    void updateSubtask1() {
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    manager.updateSubtask(subtask1);
                },
                "Исключение не выброшено"
        );
        assertEquals("Подзадача отсутствует в списке", exception.getMessage());
    }

    @Test
    @DisplayName("Обновление подзадачи")
    void updateSubtask2() {
        manager.createEpic(epic1);
        manager.createSubtask(subtask1);
        Subtask subtaskForUpdate = new Subtask("Обновленная подзадача 1", "обновленная подзадача 1 епика 1", 1,
                "22-10-2024 16:00", 60);
        subtaskForUpdate.setId(2);
        manager.updateSubtask(subtaskForUpdate);
        assertEquals(subtaskForUpdate, manager.getSubtaskById(2), "Подзадачи не совпадают");
    }

    @Test
    @DisplayName("Проверка выбрасывания исключения при удалении несуществующей подзадачи")
    void removeSubtask1() {
        manager.createEpic(epic1);
        manager.createSubtask(subtask1);
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    manager.removeSubtask(10);
                },
                "Исключение не выброшено"
        );
        assertEquals("Введено некорректное значение идентификатора", exception.getMessage());
    }

    @Test
    @DisplayName("Удаление подзадачи")
    void removeSubtask2() {
        manager.createEpic(epic1);
        manager.createSubtask(subtask1);
        manager.removeSubtask(2);
        assertEquals(0, manager.getSubtaskList().size(), "Подзадача не удалена");
        assertEquals(0, manager.getSubtaskOfEpicList(epic1).size(), "Подзадача не удалена");
    }

    @Test
    @DisplayName("Проверка пустого списка эпиков")
    void getEpicList1() {
        assertEquals(0, manager.getEpicList().size(), "В списке есть задачи");
    }

    @Test
    @DisplayName("Проверка списка эпиков с одним эпиком")
    void getEpicList2() {
        manager.createEpic(epic1);
        assertEquals(1, manager.getEpicList().size(), "Некорректное количество эпиков в списке");
    }

    @Test
    @DisplayName("Проверка удаления всех эпиков")
    void removeAllEpics() {
        manager.createEpic(epic1);
        manager.removeAllEpics();
        assertEquals(0, manager.getEpicList().size(), "В списке есть задачи");
    }

    @Test
    @DisplayName("Проверка выбрасывания исключения при некорректном id")
    void getEpicById1() {
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    manager.getEpicById(1);
                },
                "Исключение не выброшено"
        );
        assertEquals("Введено некорректное значение идентификатора", exception.getMessage());
    }

    @Test
    @DisplayName("Проверка получения эпика по id")
    void getEpicById2() {
        manager.createEpic(epic1);
        assertEquals(epic1, manager.getEpicById(1), "Эпики не совпадают");
    }

    @Test
    @DisplayName("Проверка выбрасывания исключения при обновлении несуществующего эпика")
    void updateEpic1() {
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    manager.updateEpic(epic1);
                },
                "Исключение не выброшено"
        );
        assertEquals("Епик отсутствует в списке", exception.getMessage());
    }

    @Test
    @DisplayName("Обновление эпика")
    void updateEpic2() {
        manager.createEpic(epic1);
        Epic epicForUpdate = new Epic("Обновленный эпик 1", "Описание");
        epicForUpdate.setId(1);
        manager.updateEpic(epicForUpdate);
        assertEquals(epicForUpdate, manager.getEpicById(1), "Эпики не совпадают");
    }

    @Test
    @DisplayName("Проверка выбрасывания исключения при удалении несуществующего эпика")
    void removeEpic1() {
        manager.createEpic(epic1);
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    manager.removeEpic(10);
                },
                "Исключение не выброшено"
        );
        assertEquals("Введено некорректное значение идентификатора", exception.getMessage());
    }

    @Test
    @DisplayName("Удаление эпика")
    void removeEpic2() {
        manager.createEpic(epic1);
        manager.removeEpic(1);
        assertEquals(0, manager.getEpicList().size(), "Эпик не удален");
    }
}