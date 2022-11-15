import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Integer> incomingSubtasksId = new ArrayList<>();

    public Epic(String name, String description, int id, ArrayList<Integer> incomingSubtasksId) {
        super(name, description, id);
        this.incomingSubtasksId = incomingSubtasksId;
    }

    public Epic(String name, String description) {
        super(name, description);
    }

    public ArrayList<Integer> getIncomingSubtasksId() {
        return incomingSubtasksId;
    }

    public void setIncomingSubtasksId(ArrayList<Integer> incomingSubtasksId) {
        this.incomingSubtasksId = incomingSubtasksId;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                ", incomingSubtasks=" + incomingSubtasksId.size() +
                '}';
    }
}
