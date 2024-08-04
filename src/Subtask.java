public class Subtask extends Task {
    private Epic parent;
    public Subtask(String name, String description, TaskState state, Epic parentEpic) {
        super(name, description, state);
        parent = parentEpic;
        parent.updateState(state);
    }
    @Override
    public void updateState(TaskState newState) {
        state = newState;
        parent.updateState(newState);
    }
    public Epic getParent() {
        return parent;
    }
}
