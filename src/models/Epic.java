package models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    protected ArrayList<Integer> incomingSubtasksId = new ArrayList<>();

    protected LocalDateTime endTime;

    public Epic(String name, String description) { // тут проверить, может нужен расчет продолж и начало
        super(name, description);
    }

    public Epic(String name, String description, int id, TaskStatus taskStatus, long duration, LocalDateTime startTime) {
        super(name, description, id, taskStatus, duration, startTime);
    }

    public Epic(String name, String description, int id, TaskStatus taskStatus, long duration, LocalDateTime startTime, ArrayList<Integer> incomingSubtasksId) {
        super(name, description, id, taskStatus, duration, startTime);
        this.incomingSubtasksId = incomingSubtasksId;
    }

    public ArrayList<Integer> getIncomingSubtasksId() {
        return incomingSubtasksId;
    }

    public void setIncomingSubtasksId(ArrayList<Integer> incomingSubtasksId) {
        this.incomingSubtasksId = incomingSubtasksId;
    }

    public TaskType typeClass() {
        return TaskType.EPIC;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(incomingSubtasksId, epic.incomingSubtasksId) && Objects.equals(endTime, epic.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), incomingSubtasksId, endTime);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", taskStatus=" + taskStatus +
                ", incomingSubtasksId=" + incomingSubtasksId +
                ", startTime=" + startTime +
                ", duration=" + duration +
                ", endTime=" + endTime +
                '}';
    }
}
