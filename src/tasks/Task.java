package tasks;

import states.TaskState;
public class Task {
    protected String name;
    protected String description;
    protected TaskState state;
    protected int id;

    public Task(String name, String description, TaskState state) {
        this.name = name;
        this.description = description;
        this.state = state;
    }

    public void updateState(TaskState newState) {
        state = newState;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TaskState getState() {
        return state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toString() {
        return "Task: " + name + " - " + description + " - " + state;
    }
}



