package ru.yandex.task_traker.util;

import ru.yandex.task_traker.model.Epic;
import ru.yandex.task_traker.model.Subtask;
import ru.yandex.task_traker.model.Task;
import ru.yandex.task_traker.model.TaskStatus;
import ru.yandex.task_traker.service.TaskManager;
import ru.yandex.task_traker.service.impl.FileBackedTasksManager;

import java.io.File;
import java.io.IOException;

public class Starter {
    File fileForSaving = new File("D:\\Никита\\Программирование\\dev\\task-tracker", "fileForSaving.csv");
    TaskManager manager = Managers.getInMemoryTaskManager();
    FileBackedTasksManager managerFileBack = Managers.getFileBackedTasksManager(fileForSaving);

    // id1
    final Task task1 = new Task("Задча 1", "Описание задачи 1", "16-10-2024 12:00",
            120);
    // id2
    final Task task2 = new Task("Задча 2", "Описание задачи 2", "17-10-2024 13:00",
            150);
    // id3
    final Epic epic1 = new Epic("Епик 1", "Описание епик 1");
    // id4
    final Epic epic2 = new Epic("Епик 2", "Описание епик 2");
    // id5
    final Subtask subtask1 = new Subtask("Подзадача 1", "Подзадача 1 епика 1", 3,
            "18-10-2024 12:00", 170);
    // id6
    final Subtask subtask2 = new Subtask("Подзадача 2", "Подзадача 2 епика 1", 3,
            "19-10-2024 12:00", 200);
    // id7
    final Subtask subtask3 = new Subtask("Подзадача 3", "Подзадача 3 епика 2", 4,
            "20-10-2024 12:00", 100);
    // id8
    final Subtask subtask4 = new Subtask("Подзадача 4", "Подзадача 4 епика 1", 3,
            "15-10-2024 12:00", 60);

    public void test1() {
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createEpic(epic1);
        manager.createEpic(epic2);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.createSubtask(subtask3);
        manager.createSubtask(subtask4);

        System.out.println(manager.getPrioritizedTasks());

        System.out.println(manager.getTasksList());
        System.out.println(manager.getEpicList());
        System.out.println(manager.getSubtaskList());
        System.out.println();
        System.out.println(manager.getSubtaskOfEpicList(epic1));
        System.out.println();

        task1.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateTask(task1);
        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateSubtask(subtask1);
        epic1.setName("Епик 1 с новым названием");
        epic1.setDescription("Новое описание епика 1");
        manager.updateEpic(epic1);

        System.out.println(manager.getHistory());
        System.out.println();

        System.out.println(manager.getTaskById(1));
        System.out.println(manager.getEpicById(3));
        System.out.println(manager.getSubtaskById(6));
        System.out.println();

        System.out.println(manager.getHistory());
        System.out.println();

        manager.removeTask(1);
        manager.removeEpic(3);

        System.out.println(manager.getHistory());
        System.out.println();

        manager.removeAllTasks();
        manager.removeAllSubtasks();
        manager.removeAllEpics();
    }

    public void test2() throws IOException {
        managerFileBack.createTask(task1);
        managerFileBack.createTask(task2);
        managerFileBack.createEpic(epic1);
        managerFileBack.createEpic(epic2);
        managerFileBack.createSubtask(subtask1);
        managerFileBack.createSubtask(subtask2);
        managerFileBack.createSubtask(subtask3);
        managerFileBack.createSubtask(subtask4);

        managerFileBack.getEpicById(3);
        managerFileBack.getTaskById(2);
        managerFileBack.getSubtaskById(5);

        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        managerFileBack.updateSubtask(subtask1);

        FileBackedTasksManager managerFileBackAfterSaving = Managers.getFileBackedTasksManager(fileForSaving);
        managerFileBackAfterSaving.loadFromFile();

        System.out.println(managerFileBackAfterSaving.getHistory());
        System.out.println();
        System.out.println(managerFileBackAfterSaving.getTasksList());
    }

    public void test3() {
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(subtask1);
        System.out.println(manager.getPrioritizedTasks());
    }
}
