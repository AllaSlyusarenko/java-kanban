package manager;

import models.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    static class Node {
        private Node prev;
        private Task elem;
        private Node next;

        private Node(Node prev, Task elem, Node next) {
            this.prev = prev;
            this.elem = elem;
            this.next = next;
        }
    }

    private HashMap<Integer, Node> keyTaskNode = new HashMap<>();
    private Node head;
    private Node tail;
    private int size = 0;

    void linkLast(Task elem) {
        Node node = new Node(tail, elem, null);
        if (tail == null) {
            head = node;
        } else {
            tail.next = node;
        }
        size++;
        tail = node;
    }

    void removeNode(Node node) {
        if (node.prev == null) { // первый элемент
            node.next.prev = null;
            head = node.next;
        } else if (node.prev != null && node.next != null) { // элемент в середине
            node.prev.next = node.next;
            node.next.prev = node.prev;
        } else {   //(node.next == null) элемент в конце
            node.prev.next = null;
            tail = node.prev;
        }
        size--;
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> listTasks = new ArrayList<>();
        Node node = head;
        while (node != null) {
            listTasks.add(node.elem);
            node = node.next;
        }
        return listTasks;
    }

    @Override
    public void add(Task task) {
        if (task != null && keyTaskNode.containsKey(task.getId())) {
            Node nodeForDelete = keyTaskNode.get(task.getId());
            removeNode(nodeForDelete);
        }
        linkLast(task);
        keyTaskNode.put(task.getId(), tail);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        if (keyTaskNode.containsKey(id)) {
            removeNode(keyTaskNode.get(id));
        }
    }
}