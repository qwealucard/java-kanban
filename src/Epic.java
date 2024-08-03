
    import java.util.ArrayList;
    import java.util.List;

    public class Epic extends Task {

        private List<Subtask> subtasks = new ArrayList<>();;

        public Epic(String name, String description, TaskState state) {
            super(name, description, state);
        }

        public void addSubtask(Subtask subtask) {
            subtasks.add(subtask);
        }

        public List<Subtask> getSubtasks() {
            return subtasks;
        }

        @Override
        public void updateState(TaskState newState) {
            if ((newState == TaskState.DONE) || (newState == TaskState.NEW)) {
                for (Subtask task : subtasks) {
                    if (task.state != newState) {
                        return;
                    }
                }
            }
            this.state = newState;
        }
    }

