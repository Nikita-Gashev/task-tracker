package ru.yandex.task_traker.util;

import ru.yandex.task_traker.model.Epic;
import ru.yandex.task_traker.model.Subtask;
import ru.yandex.task_traker.model.Task;
import ru.yandex.task_traker.service.TaskManager;

public class Starter {
    TaskManager manager = new TaskManager();

    final Task task1 = new Task("Задча 1", "Описание задачи 1");
    final Task task2 = new Task("Задча 2", "Описание задачи 2");
    final Epic epic1 = new Epic("Епик 1", "Описание епик 1");
    final Epic epic2 = new Epic("Епик 2", "Описание епик 2");
    final Subtask subtask1 = new Subtask("Подзадача 1", "Подзадача 1 епика 1", epic1);
    final Subtask subtask2 = new Subtask("Подзадача 2", "Подзадача 2 епика 1", epic1);
    final Subtask subtask3 = new Subtask("Подзадача 3", "Подзадача 3 епика 2", epic2);

    public void test1() {
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createEpic(epic1);
        manager.createEpic(epic2);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.createSubtask(subtask3);

        System.out.println(manager.getTasksList());
        System.out.println(manager.getEpicList());
        System.out.println(manager.getSubtaskList());
        System.out.println();
        System.out.println(manager.getSubtaskOfEpicList(epic1));
        System.out.println();

        task1.setStatus(TaskStatus.IN_PROGRESS);
        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        epic1.setName("Епик 1 с новым названием");
        epic1.setDescription("Новое описание епика 1");

        manager.updateTask(task1);
        manager.updateSubtask(subtask1);
        manager.updateEpic(epic1);

        System.out.println(manager.getTasksList());
        System.out.println(manager.getEpicList());
        System.out.println(manager.getSubtaskList());
        System.out.println();

        manager.removeTask(2);
        manager.removeSubtask(5);
        manager.removeEpic(4);

        System.out.println(manager.getTasksList());
        System.out.println(manager.getEpicList());
        System.out.println(manager.getSubtaskList());
        System.out.println();

        System.out.println(manager.getTaskById(1));
        System.out.println(manager.getEpicById(3));
        System.out.println(manager.getSubtaskById(6));
        System.out.println();

        manager.removeAllTasks();
        manager.removeAllSubtasks();
        manager.removeAllEpics();

        System.out.println(manager.getTasksList());
        System.out.println(manager.getEpicList());
        System.out.println(manager.getSubtaskList());
        System.out.println();
    }
}