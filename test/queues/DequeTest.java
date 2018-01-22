package queues;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

class DequeTest {

    @Test
    void test_check_size() {
        Deque<String> deque = new Deque<>();
        assertEquals(0, deque.size());
        deque.addFirst("a");
        assertEquals(1, deque.size());
        deque.addFirst("a");
        assertEquals(2, deque.size());
        deque.removeFirst();
        deque.removeLast();
        assertEquals(0, deque.size());
    }

    @Test
    void test_addFirst_removeLast_sequence() {
        Deque<String> deque = new Deque<>();
        deque.addFirst("a");
        deque.addFirst("b");
        deque.addFirst("c");
        assertEquals("a", deque.removeLast());
        assertEquals("b", deque.removeLast());
        assertEquals("c", deque.removeLast());
    }

    @Test
    void test_addLast_removeFirst_sequence() {
        Deque<String> deque = new Deque<>();
        deque.addLast("a");
        deque.addLast("b");
        deque.addLast("c");
        assertEquals("a", deque.removeFirst());
        assertEquals("b", deque.removeFirst());
        assertEquals("c", deque.removeFirst());
    }

    @Test
    void test_removeLast_throws_NoSuchElementException() {
        Deque<String> deque = new Deque<>();
        deque.addFirst("a");
        deque.addFirst("b");
        assertEquals("a", deque.removeLast());
        assertEquals("b", deque.removeLast());
        assertThrows(NoSuchElementException.class, deque::removeLast);
    }

    @Test
    void test_iterator() {
        Deque<String> deque = new Deque<>();
        deque.addFirst("a");
        deque.addFirst("b");
        deque.addFirst("c");

        Iterator<String> iterator = deque.iterator();

        assertEquals(true, iterator.hasNext());
        assertEquals("c", iterator.next());

        assertEquals(true, iterator.hasNext());
        assertEquals("b", iterator.next());

        assertEquals(true, iterator.hasNext());
        assertEquals("a", iterator.next());

        assertEquals(false, iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void test_many_iterators_at_once() {
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(1);
        deque.addFirst(2);
        deque.addFirst(3);

        Iterator<Integer> iterator1 = deque.iterator();

        assertEquals(true, iterator1.hasNext());
        assertEquals(Integer.valueOf(3), iterator1.next());

        Iterator<Integer> iterator2 = deque.iterator();

        assertEquals(true, iterator1.hasNext());
        assertEquals(Integer.valueOf(2), iterator1.next());

        assertEquals(true, iterator2.hasNext());
        assertEquals(Integer.valueOf(3), iterator2.next());
    }

    @Test
    void test_randomized_queue_is_empty() {
        RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<>();
        assertEquals(true, randomizedQueue.isEmpty());
    }

    @Test
    void test_randomized_queue_is_not_empty_empty() {
        RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<>();
        randomizedQueue.enqueue(1);
        assertEquals(false, randomizedQueue.isEmpty());
        assertEquals(Integer.valueOf(1), randomizedQueue.dequeue());
        assertEquals(true, randomizedQueue.isEmpty());
    }

    @Test
    void test_randomized_queue_order() {
        RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<>();
        randomizedQueue.enqueue(1);
        randomizedQueue.enqueue(2);
        randomizedQueue.enqueue(3);

        Iterator<Integer> integerIterator1 = randomizedQueue.iterator();
        Iterator<Integer> integerIterator2 = randomizedQueue.iterator();

        assertNotEquals(integerIterator1.next(), integerIterator2.next());
    }
}
