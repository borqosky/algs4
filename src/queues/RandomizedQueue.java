package queues;

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] a;
    private  int n;

    // construct an empty randomized queue
    public RandomizedQueue() {
        a = (Item[]) new Object[2];
        n = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    private void resize(int capacity) {
        assert  capacity >= n;
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < a.length; i++)
            temp[i] = a[i];
        a = temp;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException("can't enqueue null item.");
        if (a.length == n)
            resize(2 * a.length);
        a[n++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException("can't dequeue from empty queue.");

        int r;
        do {
            r = StdRandom.uniform(a.length);
        } while (a[r] != null);

        Item item = a[r];
        a[r] = null;
        n--;
        if (n > 0 && n == a.length / 4) resize(a.length / 2);

        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty())
            throw new NoSuchElementException("can't sample from empty queue.");

        int r;
        do {
            r = StdRandom.uniform(a.length);
        } while (a[r] != null);

        return a[r];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int i;
        private Item[] temp;

        public RandomizedQueueIterator() {
            i = n - 1;
            temp = (Item[]) new Object[a.length];
            for (int i = 0; i < a.length; i++) {
                if (a[i] != null)
                    temp[i] = a[i];
            }

            StdRandom.shuffle(a);
        }

        @Override
        public boolean hasNext() {
            return i >= 0;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return temp[i--];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove method not supported in iterator.")
        }
    }

    // unit testing (optional)
    public static void main(String[] args) {

    }
}
