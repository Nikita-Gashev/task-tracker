package ru.yandex.task_traker.model;

import java.util.Comparator;

public class TaskComparator implements Comparator<Task> {

    @Override
    public int compare(Task t1, Task t2) {
        if (t1.getStartTime() == null && t2.getStartTime() != null) {
            return 1;
        }
        if (t1.getStartTime() != null && t2.getStartTime() == null) {
            return -1;
        }
        if (t1.getStartTime() == null && t2.getStartTime() == null || t1.getStartTime().equals(t2.getStartTime())) {
            return 0;
        }
        if (t1.getStartTime().isBefore(t2.getStartTime())) {
            return -1;
        } else {
            return 1;
        }
    }
}
