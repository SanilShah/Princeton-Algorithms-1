/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] q;
    private int N;

    // construct an empty randomized queue
    public RandomizedQueue() {
        q = (Item[]) new Object[2];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return N == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return N;
    }

    // add the item
    public void enqueue(Item item) {
        if(item == null)
                throw new IllegalArgumentException();
        autoEnlarge();
        q[N++] = item;
    }

    private void resize(int capacity) {
        assert capacity >= N;
        Item[] temp = (Item[]) new Object[capacity];
        for(int i=0; i<N; i++) {
            temp[i] = q[i];
        }
        q = temp;
    }

    private void autoEnlarge() {
        if (N == q.length) {
            resize(2*q.length);
        }
    }

    private void autoShrink() {
        if(N > 0 && N == q.length/4)
            resize(q.length/2);
    }

    private int randomIndex() {
        return StdRandom.uniform(0, N);
    }

    private void assertNotEmpty() {
        if (isEmpty())
            throw new NoSuchElementException();
    }

    // remove and return a random item
    public Item dequeue() {
        assertNotEmpty();
        int index = randomIndex();
        Item item = q[index];
        q[index] = q[N-1];
        q[N-1] = null;
        N--;
        autoShrink();
        return item;
    }



    // return a random item (but do not remove it)
    public Item sample() {
        assertNotEmpty();
        return q[randomIndex()];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedArrayIterator();
    }

    private class RandomizedArrayIterator implements Iterator<Item> {
        private Item[] r;
        private int i;

        public RandomizedArrayIterator() {
            copyQueue();
            StdRandom.shuffle(r);
        }

        private void copyQueue() {
            r = (Item[]) new Object[N];
            for(int i=0; i<N; i++) {
                r[i] = q[i];
            }
        }

        public boolean hasNext() {
            return i < N;
        }

        public Item next() {
            if(!hasNext())
                throw new NoSuchElementException();
            return r[i++];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        // RandomizedQueue<String> q = new RandomizedQueue<String>();
        //
        // q.enqueue("A");
        // q.enqueue("B");
        // q.enqueue("C");
        // q.enqueue("D");
        //
        // //StdOut.println(q.sample());
        //
        // for(String s: q) {
        //     StdOut.println(s);
        // }
    }
}
