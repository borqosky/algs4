package queues;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int n;

    // helper linked list class
    private class Node {
        private Item item;
        private Node next;
        private Node previous;
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        n = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return first == null && last == null;
    }

    // return the number of items on the deque
    public int size() {
        return n;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Item can't be null.");
        Node oldFirst = first;

        Node node = new Node();
        node.item = item;

        if (oldFirst != null) {
            node.next = oldFirst;
            oldFirst.previous = node;
            first = node;
        } else {
            first = node;
            last = node;
        }
        n++;
    }

    // add the item to the end
    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Item can't be null.");
        Node oldLast = last;

        Node node = new Node();
        node.item = item;

        if (oldLast != null) {
            node.previous = oldLast;
            oldLast.next = node;
            last = node;
        } else {
            first = node;
            last = node;
        }

        n++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException("Can't remove from empty deque.");

        Node oldFirst = first;
        first = oldFirst.next;
        if (first != null)
            first.previous = null;

        n--;

        if (n == 0) {
            first = null;
            last = null;
        }

        return oldFirst.item;
    }

    // remove and return the item from the end
    public Item removeLast() {
        if (isEmpty())
            throw new NoSuchElementException("Can't remove from empty deque.");

        Node oldLast = last;
        last = oldLast.previous;
        if (last != null)
            last.next = null;

        n--;

        if (n == 0)
            first = last;

        return oldLast.item;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException("No more items to return.");
            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException("Operation not supported.");
        }
    }

    // unit testing (optional)
    public static void main(String[] args) {
        Deque<String> deque = new Deque<>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-"))
                deque.addFirst(item);
            else if (!deque.isEmpty())
                StdOut.print(deque.removeFirst() + " ");
        }
        StdOut.println("(" + deque.size() + " left on deque)");
    }
}
