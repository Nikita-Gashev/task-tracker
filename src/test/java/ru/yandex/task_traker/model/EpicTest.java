package ru.yandex.task_traker.model;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EpicTest {

    private static Epic epic;
    private static Subtask subtask1;
    private static Subtask subtask2;
    private static Subtask subtask3;

    @BeforeEach
    public void beforeEach() {
        epic = new Epic("Эпик 1", "Описание 1");
        subtask1 = new Subtask("Сабтаск 1", "Описание 1", 1,
                "20-10-2024 08:40", 120);
        subtask2 = new Subtask("Сабтаск 2", "Описание 2", 1,
                "20-10-2024 17:40", 100);
        subtask3 = new Subtask("Сабтаск 3", "Описание 3", 1,
                "21-10-2024 10:00", 10);
        epic.setSubtasks(subtask1);
        epic.setSubtasks(subtask2);
        epic.setSubtasks(subtask3);
    }


    @Test
    @DisplayName("Статус эпика без подзадач должен быть NEW")
    void assignStatusNew1() {
        epic.removeSubtasks(subtask1);
        epic.removeSubtasks(subtask2);
        epic.removeSubtasks(subtask3);
        epic.assignStatus();
        assertEquals(TaskStatus.NEW, epic.getStatus());
    }

    @Test
    @DisplayName("Статус эпика с подзадачами NEW должен быть NEW")
    void assignStatusNewW2() {
        epic.assignStatus();
        assertEquals(TaskStatus.NEW, epic.getStatus());
    }

    @Test
    @DisplayName("Статус эпика с подзадачами NEW и DONE должен быть IN_PROGRESS")
    void assignStatusInProgress1() {
        subtask1.setStatus(TaskStatus.DONE);
        epic.assignStatus();
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());
    }

    @Test
    @DisplayName("Статус эпика с подзадачами IN_PROGRESS должен быть IN_PROGRESS")
    void assignStatusInProgress2() {
        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        epic.assignStatus();
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());
    }

    @Test
    @DisplayName("Статус эпика с подзадачами DONE должен быть DONE")
    void assignStatusDone() {
        subtask1.setStatus(TaskStatus.DONE);
        subtask2.setStatus(TaskStatus.DONE);
        subtask3.setStatus(TaskStatus.DONE);
        epic.assignStatus();
        assertEquals(TaskStatus.DONE, epic.getStatus());
    }

    @Test
    @DisplayName("Время начала, окончания и продолжительность эпика должно совпадать с подзадачей 1")
    void updateTimeAndDuration1() {
        epic.removeSubtasks(subtask2);
        epic.removeSubtasks(subtask3);
        epic.updateTimeAndDuration();
        assertEquals("20-10-2024 08:40", epic.getStartTime().format(epic.getFormatter()));
        assertEquals("20-10-2024 10:40", epic.getEndTime().format(epic.getFormatter()));
        assertEquals(120, epic.duration.toMinutes());
    }

    @Test
    @DisplayName("Время начала эпика по подзадаче 1, окончания - подзадача2, продолжительность - 640 минут")
    void updateTimeAndDuration2() {
        epic.removeSubtasks(subtask3);
        epic.updateTimeAndDuration();
        assertEquals("20-10-2024 08:40", epic.getStartTime().format(epic.getFormatter()));
        assertEquals("20-10-2024 19:20", epic.getEndTime().format(epic.getFormatter()));
        assertEquals(640, epic.duration.toMinutes());
    }
}