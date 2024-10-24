package ru.yandex.task_traker.util;

import ru.yandex.task_traker.service.HistoryManager;
import ru.yandex.task_traker.service.impl.FileBackedTasksManager;
import ru.yandex.task_traker.service.impl.InMemoryHistoryManager;
import ru.yandex.task_traker.service.impl.InMemoryTaskManager;
import ru.yandex.task_traker.service.TaskManager;

import java.io.File;

public class Managers {

    public static TaskManager getInMemoryTaskManager() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getFileBackedTasksManager(File fileForSaving) {
        return new FileBackedTasksManager(fileForSaving);
    }
}
