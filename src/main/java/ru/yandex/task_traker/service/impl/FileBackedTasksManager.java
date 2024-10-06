package ru.yandex.task_traker.service.impl;

import ru.yandex.task_traker.model.Epic;
import ru.yandex.task_traker.model.Subtask;
import ru.yandex.task_traker.model.Task;
import ru.yandex.task_traker.util.ManagerSaveException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File fileForSaving;
    private static final String HEAD_OF_SAVING_LIST = "id,type,name,status,description,epic,duration,startTime\n";

    public FileBackedTasksManager(File fileForSaving) {
        this.fileForSaving = fileForSaving;
    }

    private void save() throws ManagerSaveException {
        try (Writer writer = new FileWriter(fileForSaving)) {
            writer.write(HEAD_OF_SAVING_LIST);
            for (Task task : super.getTasksList()) {
                writer.write(task.toString() + "\n");
            }
            for (Task epic : super.getEpicList()) {
                writer.write(epic.toString() + "\n");
            }
            for (Task subtask : super.getSubtaskList()) {
                writer.write(subtask.toString() + "\n");
            }
            writer.write("\n");
            for (Task task : super.getHistory()) {
                writer.write(task.getId() + ",");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка при сохранении файла");
        }
    }

    private static List<String> readTaskFromFile(File file) throws IOException {
        List<String> lines = new ArrayList<>();
        try (FileReader reader = new FileReader(file)) {
            BufferedReader br = new BufferedReader(reader);
            while (br.ready()) {
                String line = br.readLine();
                if (!line.isEmpty()) {
                    lines.add(line);
                } else {
                    break;
                }
            }
        }
        return lines;
    }

    private static void makeTasksFromString(String value) {
        Task task;
        switch (value.split(",")[1]) {
            case "TASK":
                task = Task.makeTaskFromString(value);
                tasks.put(task.getId(), task);
                break;
            case "EPIC":
                task = Epic.makeEpicFromString(value);
                epics.put(task.getId(), (Epic) task);
                break;
            case "SUBTASK":
                task = Subtask.makeSubtaskFromString(value);
                subtasks.put(task.getId(), (Subtask) task);
                Epic epic = epics.get(((Subtask) task).getEpicId());
                epic.getSubtasks().add((Subtask) task);
                epic.updateTimeAndDuration();
                break;
        }
    }

    private static List<Integer> readHistoryFromFile(File file) throws IOException {
        List<Integer> idTaskInHistory = new ArrayList<>();
        try (FileReader reader = new FileReader(file)) {
            BufferedReader br = new BufferedReader(reader);
            boolean isHistory = false;
            while (br.ready()) {
                String line = br.readLine();
                if (isHistory) {
                    for (String id : line.split(",")) {
                        idTaskInHistory.add(Integer.parseInt(id));
                    }
                }
                if (line.isEmpty()) {
                    isHistory = true;
                }

            }
        }
        return idTaskInHistory;
    }

    public static FileBackedTasksManager loadFromFile(File file) throws IOException {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        tasks.clear();
        epics.clear();
        subtasks.clear();
        for (String str : readTaskFromFile(file)) {
            makeTasksFromString(str);
        }
        historyManager.removeAllHistory();
        for (Integer id : readHistoryFromFile(file)) {
            if (tasks.containsKey(id)) {
                historyManager.add(tasks.get(id));
            } else if (epics.containsKey(id)) {
                historyManager.add(epics.get(id));
            } else {
                historyManager.add(subtasks.get(id));
            }
        }
        return fileBackedTasksManager;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
        return task;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
        return subtask;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
        return epic;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeTask(int id) {
        super.removeTask(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeSubtask(int id) {
        super.removeSubtask(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeEpic(int id) {
        super.removeEpic(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }
}
