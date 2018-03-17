import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    // Using sentinel nodes
    private final Node head, tail;
    private int size;

    public Deque() {
        head = new Node();
        tail = new Node();
        head.next = tail;
        tail.prev = head;
        size = 0;
    } // construct an empty deque

    public boolean isEmpty() {
        return head.next == tail && tail.prev == head;
    } // is the deque empty?

    public int size() {
        return size;
    } // return the number of items on the deque

    public void addFirst(Item item) {
        if (item == null) {
            // Throw a java.lang.IllegalArgumentException if the client calls either addFirst() or addLast() with a null argument.
            throw new IllegalArgumentException("null argument");
        }
        Node oldFirst = head.next;
        head.next = new Node();
        head.next.item = item;
        head.next.next = oldFirst;
        head.next.prev = head;
        oldFirst.prev = head.next;
        size++;
    } // add the item to the front

    public void addLast(Item item) {
        // from lecture slides
        if (item == null) {
            // Throw a java.lang.IllegalArgumentException if the client calls either addFirst() or addLast() with a null argument.
            throw new IllegalArgumentException("null argument");
        }
        Node oldLast = tail.prev;
        tail.prev = new Node();
        tail.prev.item = item;
        tail.prev.prev = oldLast;
        tail.prev.next = tail;
        oldLast.next = tail.prev;
        size++;
    } // add the item to the end

    public Item removeFirst() {
        if (isEmpty()) {
            // Throw a java.util.NoSuchElementException if the client calls either removeFirst() or removeLast when the deque is empty.
            throw new NoSuchElementException("Deque is empty");
        }
        Node oldFirst = head.next;
        head.next = oldFirst.next;
        oldFirst.next.prev = head;
        size--;

        return oldFirst.item;
    } // remove and return the item from the front

    public Item removeLast() {
        if (isEmpty()) {
            // Throw a java.util.NoSuchElementException if the client calls either removeFirst() or removeLast when the deque is empty.
            throw new NoSuchElementException("Deque is empty");
        }
        Node oldLast = tail.prev;
        tail.prev = oldLast.prev;
        oldLast.prev.next = tail;
        size--;

        return oldLast.item;
    } // remove and return the item from the end

    public Iterator<Item> iterator() {
        // Throw a java.util.NoSuchElementException if the client calls the next() method in the iterator when there are no more items to return.
        return new DequeIterator();
    } // return an iterator over items in order from front to end

    public static void main(String[] args) {
        int counter = 0;
        Deque<String> testDeque = new Deque<>();
        assert (counter == testDeque.size());
        testDeque.addFirst("POOP");
        counter++;
        assert (counter == testDeque.size());
        testDeque.addLast("NOT POOP");
        counter++;
        assert (counter == testDeque.size());
        String item = testDeque.removeFirst();
        assert ("POOP".equals(item));
        counter--;
        assert (counter == testDeque.size());
        item = testDeque.removeLast();
        assert ("NOT POOP".equals(item));
        counter--;
        assert (counter == testDeque.size());

        testDeque.addFirst("PO1");
        testDeque.addFirst("PO2");

        Iterator<String> itr = testDeque.iterator();
        Iterator<String> itr2 = testDeque.iterator();
        while (itr.hasNext()) {
            while (itr2.hasNext()) {
                System.out.println(itr2.next());
                System.out.println(itr.next());
            }
        }
        item = testDeque.removeLast();
        assert ("PO1".equals(item));
        testDeque.removeLast();

        System.out.println("Expect exception");
        testDeque.removeLast();

        System.out.println("Hello");
    } // unit testing (optional)

    private class Node {
        Item item;
        Node next;
        Node prev;
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = head.next;
        private final int startingSize = size;

        public boolean hasNext() {
            if (startingSize != size) {
                throw new ConcurrentModificationException();
            }
            return current != tail;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();
            Item currentItem = current.item; // this line causes the ERROR
            current = current.next;
            return currentItem;
        }

        public void remove() {
            // Throw a java.lang.UnsupportedOperationException if the client calls the remove() method in the iterator.
            throw new UnsupportedOperationException("Method not allowed");
        }
    }
}
