package manager;

import exceptions.ManagerSaveException;
import models.Epic;
import models.Subtask;
import models.Task;
import models.TaskType;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public void save() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            bufferedWriter.write("id,type,name,status,description,epic\n");

            for (Integer id : tasks.keySet()) {
                bufferedWriter.write(CSVFileAction.toString(tasks.get(id)) + "\n");
            }
            for (Integer id : epics.keySet()) {
                bufferedWriter.write(CSVFileAction.toString(epics.get(id)) + "\n");
            }
            for (Integer id : subtasks.keySet()) {
                bufferedWriter.write(CSVFileAction.toString(subtasks.get(id)) + "\n");
            }
            bufferedWriter.write("\n");
            bufferedWriter.write(CSVFileAction.historyToString(this.historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи в файл");
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        List<String> lines = new ArrayList<>();
        try (FileReader reader = new FileReader(file, StandardCharsets.UTF_8)) {
            BufferedReader br = new BufferedReader(reader);
            boolean firstLine = true;
            String line;
            List<Integer> history;

            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                lines.add(line);
            }

            for (int i = 0; i < lines.size() - 2; i++) {
                Task workTask = CSVFileAction.fromString(lines.get(i));
                TaskType workTaskType = workTask.typeClass();
                switch (workTaskType) {
                    case SUBTASK:
                        fileBackedTasksManager.subtasks.put(workTask.getId(), (Subtask) workTask);
                        if (fileBackedTasksManager.epics.containsKey(((Subtask) workTask).getEpicId())) {
                            fileBackedTasksManager.epics.get(((Subtask) workTask).getEpicId()).getIncomingSubtasksId().add(workTask.getId());
                            break;
                        }
                    case EPIC:
                        fileBackedTasksManager.epics.put(workTask.getId(), (Epic) workTask);
                        break;
                    case TASK:
                        fileBackedTasksManager.tasks.put(workTask.getId(), workTask);
                        break;
                }
            }

            history = CSVFileAction.historyFromString(lines.get(lines.size() - 1));
            for (int id : history) {
                if (fileBackedTasksManager.tasks.get(id) != null) {
                    fileBackedTasksManager.historyManager.add(fileBackedTasksManager.tasks.get(id));
                } else if (fileBackedTasksManager.epics.get(id) != null) {
                    fileBackedTasksManager.historyManager.add(fileBackedTasksManager.epics.get(id));
                } else if (fileBackedTasksManager.subtasks.get(id) != null) {
                    fileBackedTasksManager.historyManager.add(fileBackedTasksManager.subtasks.get(id));
                }
            }
            return fileBackedTasksManager;
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при чтении из файла");
        }
    }

    public static void main(String[] args) throws IOException {

        File file1 = new File("src/file1.csv");
        if (!Files.exists(file1.toPath())) {
            if (!Files.exists(Path.of(file1.getParent()))) {
                Files.createDirectory(Path.of(file1.getParent()));
            }
            Files.createFile(file1.toPath());
        }
        Path file1Path = file1.toPath();

        FileBackedTasksManager fileBackedTasksManager1 = new FileBackedTasksManager(file1);
        Task task1 = new Task("Task1", "description task1");
        fileBackedTasksManager1.createNewTask(task1);
        Task task2 = new Task("Task2", "description task2");
        fileBackedTasksManager1.createNewTask(task2);
        Epic epic3 = new Epic("Epic3", "description epic3");
        fileBackedTasksManager1.createNewEpic(epic3);
        Subtask subtask4 = new Subtask("Subtask4", "description subtask4", 3);
        fileBackedTasksManager1.createNewSubtask(subtask4);
        Subtask subtask5 = new Subtask("Subtask5", "description subtask5", 3);
        fileBackedTasksManager1.createNewSubtask(subtask5);

        fileBackedTasksManager1.getTaskById(2);
        fileBackedTasksManager1.getEpicById(3);
        fileBackedTasksManager1.getTaskById(1);
        fileBackedTasksManager1.getSubtaskById(4);
        fileBackedTasksManager1.getSubtaskById(5);
        fileBackedTasksManager1.getEpicById(3);

        File file2 = new File("src/file2.csv");
        if (!Files.exists(file2.toPath())) {
            if (!Files.exists(Path.of(file2.getParent()))) {
                Files.createDirectory(Path.of(file2.getParent()));
            }
            Files.createFile(file2.toPath());
        }
        Path file2Path = file2.toPath();

        Files.copy(file1Path, file2Path, StandardCopyOption.REPLACE_EXISTING);

        FileBackedTasksManager fileBackedTasksManager2 = FileBackedTasksManager.loadFromFile(file2);

        Task task6 = new Task("Task6", "description task6");
        fileBackedTasksManager2.createNewTask(task6);
        fileBackedTasksManager2.getTaskById(2);
        fileBackedTasksManager2.getTaskById(1);
        fileBackedTasksManager2.getSubtaskById(4);
    }

    @Override
    public void createNewTask(Task task) {
        super.createNewTask(task);
        save();
    }

    @Override
    public void createNewEpic(Epic epic) {
        super.createNewEpic(epic);
        save();
    }

    @Override
    public void createNewSubtask(Subtask subtask) {
        super.createNewSubtask(subtask);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteSubtaskByIdEpic(int epicId) {
        super.deleteSubtaskByIdEpic(epicId);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }
}
