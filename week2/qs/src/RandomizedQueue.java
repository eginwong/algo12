import edu.princeton.cs.algs4.StdRandom;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] rq; // array of items
    private int size;

    public RandomizedQueue() {
        rq = (Item[]) new Object[2];
        size = 0;

    } // construct an empty randomized queue

    public boolean isEmpty() {
        return size == 0;
    } // is the randomized queue empty?

    public int size() {
        return size;
    } // return the number of items on the randomized queue

    public void enqueue(Item item) {
        if (item == null) {
            // Throw a java.lang.IllegalArgumentException if the client calls enqueue() with a null argument.
            throw new IllegalArgumentException("null argument");
        }
        if (size == rq.length) {
            resize(2 * rq.length); // double size of array if necessary
        }
        rq[size++] = item;
    } // add the item

    public Item dequeue() {
        if (isEmpty()) {
            // Throw a java.util.NoSuchElementException if the client calls either sample() or dequeue() when the randomized queue is empty.
            throw new NoSuchElementException("empty rq");
        }
        int randomDestruction = StdRandom.uniform(size);
        Item item = rq[randomDestruction];
        rq[randomDestruction] = rq[--size];
        rq[size] = null;
        // shrink size of array if necessary
        if (size > 0 && size == rq.length / 4)
            resize(rq.length / 2);
        return item;

    } // remove and return a random item

    public Item sample() {
        if (isEmpty()) {
            // Throw a java.util.NoSuchElementException if the client calls either sample() or dequeue() when the randomized queue is empty.
            throw new NoSuchElementException("empty rq");
        }
        if (size == 1) {
            if (rq[0] == null) {
                return rq[1];
            }
            return rq[0];
        }
        return rq[StdRandom.uniform(size)];

    } // return a random item (but do not remove it)

    // resize the underlying array holding the elements
    private void resize(int capacity) {
        assert capacity >= size;

        // textbook implementation
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            temp[i] = rq[i];
        }
        rq = temp;
        // alternative implementation
    }

    public Iterator<Item> iterator() {
        // Iterator.  Each iterator must return the items in uniformly random order. The order of two or more iterators to the same randomized queue must be mutually independent; each iterator must maintain its own random order.
        return new RandomQueueIterator();

    } // return an independent iterator over items in random order

    public static void main(String[] args) {
        RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<>();
        assert (randomizedQueue.isEmpty());
        assert (randomizedQueue.size() == 0);
        randomizedQueue.enqueue(1);
        assert (randomizedQueue.size() == 1);
        assert (randomizedQueue.sample() == 1);
        randomizedQueue.enqueue(2);
        assert (randomizedQueue.size() == 2);
        if (randomizedQueue.dequeue() == 1) {
            assert (randomizedQueue.sample() == 2);
        } else {
            assert (randomizedQueue.sample() == 1);
        }
        assert (randomizedQueue.size() == 1);

        randomizedQueue.enqueue(99);
        randomizedQueue.enqueue(98);
        randomizedQueue.enqueue(97);
        randomizedQueue.enqueue(96);

        Iterator<Integer> itr1 = randomizedQueue.iterator();
        Iterator<Integer> itr2 = randomizedQueue.iterator();

        while (itr1.hasNext()) {
            while (itr2.hasNext()) {
                System.out.println(itr1.next());
                System.out.println(itr2.next());
            }
        }
        int n = 0;
        while (n < 10) {
            randomizedQueue.enqueue(n);
            n++;
        }

        while (n > 0) {
            randomizedQueue.dequeue();
            n--;
        }

    } // unit testing (optional)

    private class RandomQueueIterator implements Iterator<Item> {
        private int i;
        private final int startingSize = size;
        private Item[] holdingArray;

        public RandomQueueIterator() {
            holdingArray = (Item[]) new Object[size];
            i = 0;
            for (int j = 0; j < size; j++) {
                if (rq[j] != null) {
                    holdingArray[i] = rq[j];
                    i++;
                }
            }

            StdRandom.shuffle(holdingArray);
        }

        public boolean hasNext() {
            if (startingSize != size) {
                throw new ConcurrentModificationException();
            }
            return i > 0;
        }

        // Throw a java.lang.UnsupportedOperationException if the client calls the remove() method in the iterator.
        public void remove() {
            throw new UnsupportedOperationException();
        }

        // Throw a java.util.NoSuchElementException if the client calls the next() method in the iterator when there are no more items to return.
        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();
            Item item = holdingArray[i - 1];
            holdingArray[i - 1] = null;
            i--;
            return item;
        }
    }
}