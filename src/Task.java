
public class Task {


    protected String name;


    protected String description;
    protected int id;


    protected TaskState state;

    public Task(String name, String description, TaskState state) {
        this.name = name;
        this.description = description;
        this.state = state;
    }


    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaskState getState() {
        return state;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public void updateState(TaskState state) {
        this.state = state;
    }
}


