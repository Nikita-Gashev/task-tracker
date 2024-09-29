package ru.yandex.task_traker.service.impl;

import ru.yandex.task_traker.model.Task;
import ru.yandex.task_traker.service.HistoryManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node<Task>> viewedTask = new HashMap<>();
    private Node<Task> head;
    private Node<Task> tail;
    private int size;

    private Node<Task> linkLast(Task task) {
        Node<Task> oldTail = tail;
        Node<Task> newNode = new Node<>(task, null, oldTail);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.setNext(newNode);
        }
        size++;
        return newNode;
    }

    private void removeNode(Node<Task> node) {
        if (node != null) {
            Node<Task> next = node.getNext();
            Node<Task> prev = node.getPrev();

            if (next != null && prev != null) {
                next.setPrev(prev);
                prev.setNext(next);
            } else if (next == null && prev != null) {
                prev.setNext(null);
                tail = prev;
            } else if (next != null) {
                next.setPrev(null);
                head = next;
            } else {
                tail = null;
                head = null;
            }
            size--;
        }
    }

    private List<Task> getTasks() {
        List<Task> historyList = new ArrayList<>(size);
        Node<Task> node = head;
        while (node != null) {
            historyList.add(node.getData());
            node = node.getNext();
        }
        return historyList;
    }

    @Override
    public void removeAllHistory() {
        head = null;
        tail = null;
        size = 0;
        viewedTask.clear();
    }

    @Override
    public void add(Task task) {
        if (size > 10) {
            removeNode(head);
        }
        if (viewedTask.containsKey(task.getId())) {
            removeNode(viewedTask.get(task.getId()));
            viewedTask.put(task.getId(), linkLast(task));
        } else {
            viewedTask.put(task.getId(), linkLast(task));
        }
    }

    @Override
    public void remove(int id) {
        removeNode(viewedTask.get(id));
        viewedTask.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private static class Node<E> {
        private final E data;
        private Node<E> next;
        private Node<E> prev;

        private Node(E data, Node<E> next, Node<E> prev) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }

        private E getData() {
            return data;
        }

        private Node<E> getNext() {
            return next;
        }

        private void setNext(Node<E> next) {
            this.next = next;
        }

        private Node<E> getPrev() {
            return prev;
        }

        private void setPrev(Node<E> prev) {
            this.prev = prev;
        }
    }
}
