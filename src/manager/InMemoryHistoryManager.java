package manager;

import models.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
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

    void linkFirst(Task elem) {
        if (head == null) {
            Node node = new Node(null, elem, null);
            head = node;
            tail = node;
            size++;
        } else {
            Node node = new Node(null, elem, head);
            head.prev = node;
            head = node;
            size++;
        }
    }

    Node linkLast(Task elem) {
        Node saveTail = tail;
        Node node = new Node(saveTail, elem, null);
        tail = node;
        if (saveTail == null) {
            head = node;
        } else {
            saveTail.next = node;
        }
        size++;
        return node;
    }

    void removeFirst(Node node) {
        if (head == null) {
            return;
        }
        if (head == tail) {
            head = null;
            tail = null;
            size--;
        } else {
            head = head.next;
            head.prev = null;
            size--;
        }
    }

    void removeLast(Node node) {
        if (tail == null) {
            return;
        }
        if (head == tail) {
            head = null;
            tail = null;
            size--;
        } else {
            tail = tail.prev;
            tail.next = null;
            size--;
        }
    }

    Node getTaskByIdInHistory(Task task) {
        Node iteratorNode = head;
        while (iteratorNode.next != null) {
            if (iteratorNode.elem == task) {
                return iteratorNode;
            } else {
                iteratorNode = iteratorNode.next;
            }
        }
        return null;
    }

    void removeNode(Node node) {
        Node workNode = new Node(node.prev, node.elem, node.next);
        if (workNode.prev == null) {
            workNode.next.prev = null;
            head = workNode.next;
        } else if (workNode.prev != null && workNode.next != null) {
            workNode.prev.next = workNode.next;
            workNode.next.prev = workNode.prev;
        } else if (workNode.next == null) {
            workNode.prev.next = null;
            tail = workNode.prev;
        }
        size--;
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> listTasks = new ArrayList<>();
        Node node = head;
        if (size == 0) {
            return new ArrayList<>();
        } else {
            while (node != null) {
                listTasks.add(node.elem);
                node = node.next;
            }
        }
        return listTasks;
    }

    @Override
    public void add(Task task) {
        if (keyTaskNode.containsKey(task.getId())) {
            Node nodeForDelete = keyTaskNode.get(task.getId());
            removeNode(nodeForDelete);
        }
        Node nodeForPasteInMap = linkLast(task);
        keyTaskNode.put(task.getId(), nodeForPasteInMap);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        if (keyTaskNode.containsKey(id)) {
            Node nodeForDelete = keyTaskNode.get(id);
            removeNode(nodeForDelete);
            keyTaskNode.remove(id);
        }
    }
}