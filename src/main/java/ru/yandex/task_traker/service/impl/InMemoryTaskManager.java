package ru.yandex.task_traker.service.impl;

import ru.yandex.task_traker.model.*;
import ru.yandex.task_traker.service.HistoryManager;
import ru.yandex.task_traker.service.TaskManager;
import ru.yandex.task_traker.util.Managers;
import ru.yandex.task_traker.util.TimeCrossingException;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    private int id = 0;
    protected static final HistoryManager historyManager = Managers.getDefaultHistory();

    private void checkTimeCrossing(Task task) throws TimeCrossingException {
        for (Task taskFromList : getPrioritizedTasks()) {
            LocalDateTime taskStart = task.getStartTime();
            LocalDateTime taskFromListStart = taskFromList.getStartTime();
            boolean timeCrossing1 = taskStart.isBefore(taskFromListStart)
                    && task.getEndTime().isAfter(taskFromListStart);
            boolean timeCrossing2 = taskStart.isAfter(taskFromListStart)
                    && taskStart.isBefore(taskFromList.getEndTime());
            if (taskStart.isEqual(taskFromListStart) || timeCrossing1 || timeCrossing2) {
                throw new TimeCrossingException("Время выполнения задачи пересекается");
            }
        }
    }

    @Override
    public SortedSet<Task> getPrioritizedTasks() {
        SortedSet<Task> prioritizedTasksList = new TreeSet<>();
        if (!tasks.isEmpty()) {
            prioritizedTasksList.addAll(getTasksList());
        }
        if (!subtasks.isEmpty()) {
            prioritizedTasksList.addAll(getSubtaskList());
        }
        if (tasks.isEmpty() && subtasks.isEmpty()) {
            prioritizedTasksList = Collections.emptySortedSet();
        }
        return prioritizedTasksList;
    }

    @Override
    public List<Task> getTasksList() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void removeAllTasks() {
        for (int id : tasks.keySet()) {
            historyManager.remove(id);
        }
        tasks.clear();
    }

    @Override
    public Task getTaskById(int id) {
        if (tasks.get(id) == null) {
            throw new IllegalArgumentException("Введено некорретное значение индентификатора");
        } else {
            historyManager.add(tasks.get(id));
            return tasks.get(id);
        }
    }

    @Override
    public void createTask(Task task) {
        if (tasks.isEmpty() && subtasks.isEmpty()) {
            id++;
            task.setId(id);
            tasks.put(id, task);
        } else {
            try {
                checkTimeCrossing(task);
                id++;
                task.setId(id);
                tasks.put(id, task);
            } catch (TimeCrossingException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void updateTask(Task task) {
        if (!(tasks.containsKey(task.getId()))) {
            throw new IllegalArgumentException("Задача отсутствует в списке");
        }
        tasks.remove(task.getId());
        if (tasks.isEmpty() && subtasks.isEmpty()) {
            tasks.put(task.getId(), task);
        } else {
            try {
                checkTimeCrossing(task);
                tasks.put(task.getId(), task);
            } catch (TimeCrossingException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void removeTask(int id) {
        if (tasks.get(id) == null) {
            throw new IllegalArgumentException("Введено некорретное значение индентификатора");
        } else {
            tasks.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public List<Task> getSubtaskList() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void removeAllSubtasks() {
        for (int id : subtasks.keySet()) {
            historyManager.remove(id);
        }
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasks().clear();
            epic.setStatus(TaskStatus.NEW);
        }
    }

    @Override
    public Subtask getSubtaskById(int id) {
        if (subtasks.get(id) == null) {
            throw new IllegalArgumentException("Введено некорретное значение индентификатора");
        } else {
            historyManager.add(subtasks.get(id));
            return subtasks.get(id);
        }
    }

    @Override
    public void createSubtask(Subtask subtask) {
        if (tasks.isEmpty() && subtasks.isEmpty()) {
            id++;
            subtask.setId(id);
            subtasks.put(id, subtask);
            epics.get(subtask.getEpicId()).setSubtasks(subtask);
            epics.get(subtask.getEpicId()).assignStatus();
            epics.get(subtask.getEpicId()).updateTimeAndDuration();
        } else {
            try {
                checkTimeCrossing(subtask);
                id++;
                subtask.setId(id);
                subtasks.put(id, subtask);
                epics.get(subtask.getEpicId()).setSubtasks(subtask);
                epics.get(subtask.getEpicId()).assignStatus();
                epics.get(subtask.getEpicId()).updateTimeAndDuration();
            } catch (TimeCrossingException e) {
                System.out.println(e.getMessage());
            }

        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (!subtasks.containsKey(subtask.getId())) {
            throw new IllegalArgumentException("Подзадача отсутствует в списке");
        }
        subtasks.remove(subtask.getId());
        if (tasks.isEmpty() && subtasks.isEmpty()) {
            subtasks.put(subtask.getId(), subtask);
        } else {
            try {
                checkTimeCrossing(subtask);
                subtasks.put(subtask.getId(), subtask);
                epics.get(subtask.getEpicId()).assignStatus();
                epics.get(subtask.getEpicId()).updateTimeAndDuration();
            } catch (TimeCrossingException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void removeSubtask(int id) {
        if (subtasks.get(id) == null) {
            throw new IllegalArgumentException("Введено некорретное значение индентификатора");
        } else {
            Subtask subtask = subtasks.get(id);
            epics.get(subtask.getEpicId()).removeSubtasks(subtask);
            epics.get(subtask.getEpicId()).assignStatus();
            epics.get(subtask.getEpicId()).updateTimeAndDuration();
        }
        subtasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public List<Epic> getEpicList() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void removeAllEpics() {
        removeAllSubtasks();
        for (int id : epics.keySet()) {
            historyManager.remove(id);
        }
        epics.clear();
    }

    @Override
    public Epic getEpicById(int id) {
        if (epics.get(id) == null) {
            throw new IllegalArgumentException("Введено некорретное значение индентификатора");
        } else {
            historyManager.add(epics.get(id));
            return epics.get(id);
        }
    }

    @Override
    public void createEpic(Epic epic) {
        id++;
        epic.setId(id);
        epics.put(id, epic);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (!(epics.containsKey(epic.getId()))) {
            throw new IllegalArgumentException("Епик отсутствует в списке");
        } else {
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public void removeEpic(int id) {
        if (epics.get(id) == null) {
            throw new IllegalArgumentException("Введено некорретное значение индентификатора");
        } else {
            Epic epic = epics.get(id);
            for (Subtask subtask : epic.getSubtasks()) {
                subtasks.remove(subtask.getId());
                historyManager.remove(subtask.getId());
            }
            epics.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public List<Subtask> getSubtaskOfEpicList(Epic epic) {
        return epic.getSubtasks();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
