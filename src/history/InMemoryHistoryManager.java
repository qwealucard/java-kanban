package history;

import tasks.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InMemoryHistoryManager implements interfaces.HistoryManager {

    private TaskNode first;
    private TaskNode last;
    private Map<Integer, TaskNode> nodeMap;

    public InMemoryHistoryManager () {
        this.first = null;
        this.last = null;
        nodeMap = new HashMap<>();
    }

    public void linkLast (Task task) {
        TaskNode l = last;
        TaskNode newNode = new TaskNode(task, l, null);
        last = newNode;
        if (l == null) {
            first = newNode;
        } else {
            l.next = newNode;
        }
    }

    public ArrayList<Task> getTasks () {
        ArrayList<Task> tasks = new ArrayList<>();
        for (TaskNode x = first; x != null; x = x.next) {
            tasks.add(x.task);
        }
        return tasks;
    }

    public void removeNode (TaskNode node) {
        TaskNode prev = node.prev;
        TaskNode next = node.next;
        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            node.prev = null;
        }
        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }
        int taskId = node.task.getId();
        if (nodeMap.get(taskId) == node) {
            nodeMap.put(taskId, next);
        }
    }

    @Override
    public void addToHistory (Task task) {
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
    }

    @Override
    public void removeHistory (int id) {
        TaskNode node = nodeMap.get(id);
        if (node != null) {
            removeNode(node);
            nodeMap.remove(id);
        }
    }

    @Override
    public Map<Integer, TaskNode> getNodeMap () {
        return nodeMap;
    }

    public static class TaskNode {

        Task task;
        TaskNode prev;
        TaskNode next;

        public TaskNode (Task task, TaskNode prev, TaskNode next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }

        public Task getTask () {
            return task;
        }
    }
}


