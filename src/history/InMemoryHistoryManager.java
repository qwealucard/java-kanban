package history;

import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements interfaces.HistoryManager {

    private TaskNode first;
    private TaskNode last;
    private Map<Integer, TaskNode> nodeMap;
    private List<Task> historyList = new ArrayList<>();

    public InMemoryHistoryManager() {
        this.first = null;
        this.last = null;
        nodeMap = new HashMap<>();
    }

    public List<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        for (TaskNode x = first; x != null; x = x.next) {
            tasks.add(x.task);
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
            removeNode(existingNode);
            nodeMap.remove(taskId);
        }
        if (last == null) {
            first = newNode;
        } else {
            last.next = newNode;
        }
        last = newNode;
        nodeMap.put(taskId, newNode);
        historyList.add(task);
    }

    @Override
    public void removeHistory(int id) {
        TaskNode node = nodeMap.get(id);
        if (node != null) {
            nodeMap.remove(id);
            historyList.removeIf(task -> task.getId() == id);
            removeNode(node);
        }
    }

    public Map<Integer, TaskNode> getNodeMap() {
        return nodeMap;
    }

    public List<Task> getHistoryList() {
        return historyList;
    }

    public static class TaskNode {

        Task task;
        TaskNode prev;
        TaskNode next;

        private TaskNode(Task task, TaskNode prev, TaskNode next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }

        public Task getTask() {
            return task;
        }
    }
}


