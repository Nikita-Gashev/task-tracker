package ru.yandex.task_traker.util;

import ru.yandex.task_traker.service.HistoryManager;
import ru.yandex.task_traker.service.impl.InMemoryHistoryManager;
import ru.yandex.task_traker.service.impl.InMemoryTaskManager;
import ru.yandex.task_traker.service.TaskManager;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
