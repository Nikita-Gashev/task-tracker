package ru.yandex.task_traker.util;

import ru.yandex.task_traker.model.Epic;
import ru.yandex.task_traker.model.Subtask;
import ru.yandex.task_traker.model.Task;
import ru.yandex.task_traker.service.TaskManager;

public class Starter {
    TaskManager manager = new TaskManager();

    Task task1 = new Task("Задча 1", "Описание задачи 1");
    Task task2 = new Task("Задча 2", "Описание задачи 2");
    Epic epic1 = new Epic("Епик 1", "Описание епик 1");
    Epic epic2 = new Epic("Епик 2", "Описание епик 2");
    Subtask subtask1 = new Subtask("Подзадача 1", "Подзадача 1 епика 1", epic1);
    Subtask subtask2 = new Subtask("Подзадача 2", "Подзадача 2 епика 1", epic1);
    Subtask subtask3 = new Subtask("Подзадача 3", "Подзадача 3 епика 2", epic2);

    public void test1() {
        manager.makeTask(task1);
        manager.makeTask(task2);
        manager.makeEpic(epic1);
        manager.makeEpic(epic2);
        manager.makeSubtask(subtask1);
        manager.makeSubtask(subtask2);
        manager.makeSubtask(subtask3);

        System.out.println(manager.getTasksList());
        System.out.println(manager.getEpicList());
        System.out.println(manager.getSubtaskList());
        System.out.println();

        task1.setStatus("IN_PROGRESS");
        task2.setStatus("DONE");
        subtask1.setStatus("IN_PROGRESS");
        subtask3.setStatus("DONE");

        manager.updateSubtask(subtask1);
        manager.updateSubtask(subtask3);

        System.out.println(manager.getTasksList());
        System.out.println(manager.getEpicList());
        System.out.println(manager.getSubtaskList());
        System.out.println();

        manager.removeTask(2);
        manager.removeSubtask(5);
        manager.removeSubtask(7);

        System.out.println(manager.getTasksList());
        System.out.println(manager.getEpicList());
        System.out.println(manager.getSubtaskList());
        System.out.println();

    }
}
