package models;

import java.util.Objects;

public class Subtask extends Task {

    protected int epicId;

    public Subtask(String name, String description, int id, String status, int epicId) {
        // этот конструктор нужен для создания нового объекта, который подается на вход для обновления старого объекта,
        // поэтому id мы должны указать не новый, а тот, который уже есть в системе, также статус может быть отличным
        // от "NEW": "IN_PROGRESS", "DONE"
        super(name, description, id, status);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                ", epicId=" + epicId +
                '}';
    }
}
