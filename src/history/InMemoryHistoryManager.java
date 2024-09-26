package history;

import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements interfaces.HistoryManager {

    private TaskNode first;
    private TaskNode last;
    private Map<Integer, TaskNode> nodeMap;
    private List<Task> historyList;

    public InMemoryHistoryManager() {
        this.first = null;
        this.last = null;
        nodeMap = new HashMap<>();
        historyList = new ArrayList<>();
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        for (TaskNode node = first; node != null; node = node.next) {
            tasks.add(node.task);
        }
        return tasks;
    }

    private void removeNode(TaskNode node) {
        TaskNode prev = node.prev;
        TaskNode next = node.next;
        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
        }
        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
        }
    }

    @Override
    public void add(Task task) {
        int taskId = task.getId();
        TaskNode existingNode = nodeMap.get(taskId);
        TaskNode newNode = new TaskNode(task, last, null);
        if (existingNode != null) {
            remove(taskId);
        }
        if (last != null) {
            last.next = newNode;
        } else {
            first = newNode;
        }
        last = newNode;
        nodeMap.put(taskId, newNode);
        historyList.add(task);
    }

    @Override
    public void remove(int id) {
        TaskNode node = nodeMap.get(id);
        if (node != null) {
            historyList.remove(id);
            removeNode(node);
            nodeMap.remove(id);
        }
    }

    public List<Task> getHistoryList() {
        return historyList;
    }

    private static class TaskNode {

        Task task;
        TaskNode prev;
        TaskNode next;

        public TaskNode(Task task, TaskNode prev, TaskNode next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }
    }
}


