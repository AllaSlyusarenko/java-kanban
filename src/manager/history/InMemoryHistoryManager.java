package manager.history;

import models.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    static class Node {
        Node prev;
        Task elem;
        Node next;

        public Node(Node prev, Task elem, Node next) {
            this.prev = prev;
            this.elem = elem;
            this.next = next;
        }
    }

    HashMap<Integer, Node> keyTaskNode = new HashMap<>();
    Node head;
    Node tail;
    int size = 0;

    private void linkLast(Task elem) {
        Node node = new Node(tail, elem, null);
        if (tail == null) {
            head = node;
        } else {
            tail.next = node;
        }
        size++;
        tail = node;
    }

    private void removeNode(Node node) {
        if (node.prev != null) {
            node.prev.next = node.next;
            if (node.next == null) {
                tail = node.prev;
            } else {
                node.next.prev = node.prev;
            }
        } else {
            head = node.next;
            if (head == null) {
                tail = null;
            } else {
                head.prev = null;
            }
        }
        size--;
    }

    private ArrayList<Task> getTasks() {
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
        if (task == null) {
            return;
        }
        if (keyTaskNode.containsKey(task.getId())) {
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