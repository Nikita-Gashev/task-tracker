package ru.yandex.task_traker.service.impl;

import org.junit.jupiter.api.Test;
import ru.yandex.task_traker.model.Epic;
import ru.yandex.task_traker.model.Subtask;
import ru.yandex.task_traker.model.Task;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTasksManagerTest {

    final File fileForSaving = new File("D:\\Никита\\Программирование\\dev\\task-tracker",
            "fileForSaving.csv");
    final FileBackedTasksManager manager = new FileBackedTasksManager(fileForSaving);

    final Task task1 = new Task("Задча 1", "Описание задачи 1", "20-10-2024 11:00",
            120);
    final Epic epic1 = new Epic("Епик 1", "Описание епик 1");
    final Subtask subtask1 = new Subtask("Подзадача 1", "Подзадача 1 епика 1", 1,
            "21-10-2024 12:00", 120);

    @Test
    void shouldSaveAndLoadTask() throws IOException {
        manager.createTask(task1);
        FileBackedTasksManager managerAfterLoading = FileBackedTasksManager.loadFromFile(fileForSaving);
        assertEquals(task1, managerAfterLoading.getTaskById(1), "Задачи не совпадают");
    }

    @Test
    void shouldLoadEmptyTaskList() throws IOException {
        manager.createTask(task1);
        manager.removeTask(1);
        FileBackedTasksManager managerAfterLoading = FileBackedTasksManager.loadFromFile(fileForSaving);
        assertEquals(0, managerAfterLoading.getTasksList().size(), "В списке есть задачи");
    }

    @Test
    void shouldSaveAndLoadEpic() throws IOException {
        manager.createEpic(epic1);
        FileBackedTasksManager managerAfterLoading = FileBackedTasksManager.loadFromFile(fileForSaving);
        assertEquals(epic1, managerAfterLoading.getEpicById(1), "Епики не совпадают");
    }

    @Test
    void shouldLoadEmptyEpicList() throws IOException {
        manager.createEpic(epic1);
        manager.removeEpic(1);
        FileBackedTasksManager managerAfterLoading = FileBackedTasksManager.loadFromFile(fileForSaving);
        assertEquals(0, managerAfterLoading.getEpicList().size(), "В списке есть эпики");
    }

    @Test
    void shouldSaveAndLoadSubtask() throws IOException {
        manager.createEpic(epic1);
        manager.createSubtask(subtask1);
        FileBackedTasksManager managerAfterLoading = FileBackedTasksManager.loadFromFile(fileForSaving);
        assertEquals(subtask1, managerAfterLoading.getSubtaskById(2), "Подзадачи не совпадают");
    }

    @Test
    void shouldLoadEmptySubtaskList() throws IOException {
        manager.createEpic(epic1);
        manager.createSubtask(subtask1);
        manager.removeSubtask(2);
        FileBackedTasksManager managerAfterLoading = FileBackedTasksManager.loadFromFile(fileForSaving);
        assertEquals(0, managerAfterLoading.getSubtaskList().size(), "В списке есть подзадачи");
    }

    @Test
    void shouldLoadHistoryList() throws IOException {
        manager.createEpic(epic1);
        manager.createSubtask(subtask1);
        manager.createTask(task1);
        manager.getEpicById(1);
        manager.getTaskById(3);
        manager.getSubtaskById(2);
        FileBackedTasksManager managerAfterLoading = FileBackedTasksManager.loadFromFile(fileForSaving);
        assertEquals(subtask1, managerAfterLoading.getHistory().get(2), "Списки не совпадают");
    }
}