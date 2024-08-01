
    import java.util.ArrayList;

    public class Epic extends Task {

        private ArrayList<Subtask> subtasks;

        public Epic(String name, String description, TaskState state) {
            super(name, description, state);
            this.subtasks = new ArrayList<>();
        }

        public void addSubtask(Subtask subtask) {
            subtasks.add(subtask);
        }

        public ArrayList<Subtask> getSubtasks() {
            return subtasks;
        }

        @Override
        public void updateState(TaskState newState) {
            if (newState == TaskState.DONE) {
                for (Subtask task : subtasks) {
                    if (task.state != TaskState.DONE) {
                        return;
                    }
                }
            }
            this.state = newState;
        }
    }

