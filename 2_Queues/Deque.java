/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node first, last;
    private int size = 0;

    private class Node {
        Item item;
        Node next;
        Node prev;
    }

    // construct an empty deque
    public Deque() {

    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() { return size; }

    private void assertNotEmpty() {
        if(isEmpty())
            throw new NoSuchElementException();
    }

    private void assertNotNull(Item item) {
        if(item == null)
            throw new IllegalArgumentException();
    }

    // add the item to the back
    public void addFirst(Item item) {
        assertNotNull(item);

        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        //first.prev = null;

        //StdOut.println("oldfirst:" + oldFirst);
        //StdOut.println("first.prev:" + first.prev + " first.item:" + first.item + " first.next:" + first.next);
        if(isEmpty())
            last = first;
        else
            oldFirst.prev = first;
        size++;
    }

    // add the item to the front
    public void addLast(Item item) {
        assertNotNull(item);

        Node oldlast = last;
        last = new Node();
        last.item = item;
        //last.next = null;
        last.prev = oldlast;

        if(isEmpty())
            first = last;
        else
            oldlast.next = last;
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        assertNotEmpty();
        Item item = first.item;

        if(size > 1) {
            first = first.next;
            first.prev = null;
        }
        else {
            first = null;
            last = null;
        }
        size--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        assertNotEmpty();
        Item item = last.item;

        if(size > 1) {
            last = last.prev;
            last.next = null;
        }
        else {
            first = null;
            last = null;
        }
        size--;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (current == null)
                throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        // Deque<String> dq = new Deque<String>();
        //
        // dq.addFirst("A");
        // dq.addLast("B");
        //
        // for (String s: dq)
        //     StdOut.println(s);
    }
}