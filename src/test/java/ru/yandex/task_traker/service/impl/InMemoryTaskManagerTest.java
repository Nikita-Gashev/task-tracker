package ru.yandex.task_traker.service.impl;

import org.junit.jupiter.api.Test;
import ru.yandex.task_traker.model.Epic;
import ru.yandex.task_traker.model.Subtask;
import ru.yandex.task_traker.model.Task;
import ru.yandex.task_traker.util.EmptyListException;
import ru.yandex.task_traker.util.TimeCrossingException;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {

    final InMemoryTaskManager manager = new InMemoryTaskManager();

    final Task task1 = new Task("Задча 1", "Описание задачи 1", "20-10-2024 11:00",
            120);
    final Task task2 = new Task("Задча 2", "Описание задачи 2", "20-10-2024 17:00",
            120);
    final Epic epic1 = new Epic("Епик 1", "Описание епик 1");
    final Subtask subtask1 = new Subtask("Подзадача 1", "Подзадача 1 епика 1", 1,
            "21-10-2024 12:00", 120);
    final Subtask subtask2 = new Subtask("Подзадача 2", "Подзадача 2 епика 1", 1,
            "21-10-2024 17:00", 180);

    @Test
    void shouldThrowEmptyListException() {
        final EmptyListException exception = assertThrows(
                EmptyListException.class,
                () -> {
                    manager.getPrioritizedTasks();
                },
                "Исключение не выброшено"
        );
        assertEquals("Список задач и подзадач пуст", exception.getMessage());
    }

    @Test
    void shouldReturnTask1() throws EmptyListException {
        manager.createTask(task1);
        manager.createTask(subtask1);
        Task task = manager.getPrioritizedTasks().first();
        assertEquals(task1, task, "Список отсортирован некорректно");
    }

    @Test
    void shouldReturnEmptyListWhenNoTasksCreate()  {
        assertEquals(0, manager.getTasksList().size(), "В списке есть задачи");
    }

    @Test
    void shouldReturn1When1TaskCreate()  {
        manager.createTask(task1);
        assertEquals(1, manager.getTasksList().size(), "Некорректное количество задач в списке");
    }

    @Test
    void shouldReturnEmptyListWhenRemoveAllTasks()  {
        manager.createTask(task1);
        manager.createTask(task2);
        manager.removeAllTasks();
        assertEquals(0, manager.getTasksList().size(), "В списке есть задачи");
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenIdIsNotCorrectWhileGetTaskById() {
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    manager.getTaskById(1);
                },
                "Исключение не выброшено"
        );
        assertEquals("Введено некорретное значение индентификатора", exception.getMessage());
    }

    @Test
    void shouldReturnTask1WhenGetTaskById1()  {
        manager.createTask(task1);
        assertEquals(task1, manager.getTaskById(1), "Задачи не совпадают");
    }

    @Test
        // Тут может быть несколько вариантов дял пересечений, для тренирвоки првоерил одно
    void shouldNotCreateCrossingTask() {
        manager.createTask(task1);
        manager.createTask(new Task("Задча 2", "Описание задачи 2", "20-10-2024 12:00",
                120));
        assertEquals(1, manager.getTasksList().size(), "В списке больше одной задачи");
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenTasksListNotContainTaskWhileUpdateTask() {
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
    void shouldUpdateTask()  {
        manager.createTask(task1);
        Task taskForUpdate = new Task("Обновленное задание 1","Описание");
        taskForUpdate.setId(1);
        manager.updateTask(taskForUpdate);
        assertEquals(taskForUpdate, manager.getTaskById(1), "Задачи не совпадают");
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenIdIsNotCorrectWhileRemoveTask() {
        manager.createTask(task1);
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    manager.removeTask(10);
                },
                "Исключение не выброшено"
        );
        assertEquals("Введено некорретное значение индентификатора", exception.getMessage());
    }

    @Test
    void shouldRemoveTask()  {
        manager.createTask(task1);
        manager.removeTask(1);
        assertEquals(0, manager.getTasksList().size(), "Задача не удалена");
    }

    @Test
    void shouldReturnEmptyListWhenNoSubtasksCreate()  {
        assertEquals(0, manager.getSubtaskList().size(), "В списке есть подзадачи");
    }

    @Test
    void shouldReturn1When1SubtaskCreate()  {
        manager.createEpic(epic1);
        manager.createSubtask(subtask1);
        assertEquals(1, manager.getSubtaskList().size(), "Некорректное количество подзадач в списке");
    }

    @Test
    void shouldReturnEmptyListWhenRemoveAllSubtasks()  {
        manager.createEpic(epic1);
        manager.createTask(subtask1);
        manager.createTask(subtask2);
        manager.removeAllSubtasks();
        assertEquals(0, manager.getSubtaskList().size(), "В списке есть подзадачи");
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenIdIsNotCorrectWhileGetSubtaskById() {
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    manager.getSubtaskById(1);
                },
                "Исключение не выброшено"
        );
        assertEquals("Введено некорретное значение индентификатора", exception.getMessage());
    }

    @Test
    void shouldReturnSubtask1WhenGetSubtaskById1()  {
        manager.createEpic(epic1);
        manager.createSubtask(subtask1);
        assertEquals(subtask1, manager.getSubtaskById(2), "Задачи не совпадают");
    }

    @Test
    void shouldReturnEpicId1()  {
        manager.createEpic(epic1);
        manager.createSubtask(subtask1);
        assertEquals(1, manager.getSubtaskById(2).getEpicId(), "Id не совпадает");
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenSubtasksListNotContainSubtaskWhileUpdateSubtask() {
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
    void shouldUpdateSubtask()  {
        manager.createEpic(epic1);
        manager.createSubtask(subtask1);
        assertEquals(1, manager.getSubtaskById(2).getEpicId(), "Id не совпадает");
        Subtask subtaskForUpdate = new Subtask("Обновленная подзадача 1", "обновленная подзадача 1 епика 1", 1,
                "22-10-2024 16:00", 60);
        subtaskForUpdate.setId(2);
        manager.updateSubtask(subtaskForUpdate);
        assertEquals(subtaskForUpdate, manager.getSubtaskById(2), "Подзадачи не совпадают");
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenIdIsNotCorrectWhileRemoveSubtask() {
        manager.createEpic(epic1);
        manager.createSubtask(subtask1);
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    manager.removeSubtask(10);
                },
                "Исключение не выброшено"
        );
        assertEquals("Введено некорретное значение индентификатора", exception.getMessage());
    }

    @Test
    void shouldRemoveSubtask()  {
        manager.createEpic(epic1);
        manager.createSubtask(subtask1);
        manager.removeSubtask(2);
        assertEquals(0, manager.getSubtaskList().size(), "Подзадача не удалена");
        assertEquals(0, manager.getSubtaskOfEpicList(epic1).size(), "Подзадача не удалена");
    }

    @Test
    void shouldReturnEmptyListWhenNoEpicCreate()  {
        assertEquals(0, manager.getEpicList().size(), "В списке есть задачи");
    }

    @Test
    void shouldReturn1When1EpicCreate()  {
        manager.createEpic(epic1);
        assertEquals(1, manager.getEpicList().size(), "Некорректное количество задач в списке");
    }

    @Test
    void shouldReturnEmptyListWhenRemoveAllEpics()  {
        manager.createEpic(epic1);
        manager.removeAllEpics();
        assertEquals(0, manager.getEpicList().size(), "В списке есть задачи");
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenIdIsNotCorrectWhileGetEpicById() {
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    manager.getEpicById(1);
                },
                "Исключение не выброшено"
        );
        assertEquals("Введено некорретное значение индентификатора", exception.getMessage());
    }

    @Test
    void shouldReturnEpic1WhenGetEpicById1()  {
        manager.createEpic(epic1);
        assertEquals(epic1, manager.getEpicById(1), "Задачи не совпадают");
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenEpicsListNotContainEpicWhileUpdateEpic() {
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
    void shouldUpdateEpic()  {
        manager.createEpic(epic1);
        Epic epicForUpdate = new Epic("Обновленый эпик 1","Описание");
        epicForUpdate.setId(1);
        manager.updateEpic(epicForUpdate);
        assertEquals(epicForUpdate, manager.getEpicById(1), "Эпики не совпадают");
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenIdIsNotCorrectWhileRemoveEpic() {
        manager.createEpic(epic1);
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    manager.removeEpic(10);
                },
                "Исключение не выброшено"
        );
        assertEquals("Введено некорретное значение индентификатора", exception.getMessage());
    }

    @Test
    void shouldRemoveEpic()  {
        manager.createEpic(epic1);
        manager.removeEpic(1);
        assertEquals(0, manager.getEpicList().size(), "Эпик не удален");
    }
}