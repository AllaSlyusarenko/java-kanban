package models;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    protected ArrayList<Integer> incomingSubtasksId = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(String name, String description, int id, ArrayList<Integer> incomingSubtasksId) {
        // этот конструктор нужен для создания нового объекта, который подается на вход для обновления старого объекта,
        // поэтому id мы должны указать не новый, а тот, который уже есть в системе
        super(name, description);
        this.id = id;
        this.incomingSubtasksId = incomingSubtasksId;
    }

    public ArrayList<Integer> getIncomingSubtasksId() {
        return incomingSubtasksId;
    }

    public void setIncomingSubtasksId(ArrayList<Integer> incomingSubtasksId) {
        this.incomingSubtasksId = incomingSubtasksId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(incomingSubtasksId, epic.incomingSubtasksId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), incomingSubtasksId);
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
