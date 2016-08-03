/**
 *   APPLICATION: LoginSystem
 *         CLASS: HeapTest
 *        AUTHOR: Samuel Myles
 *   JDK VERSION: 1.8.0_73
 *   JRE VERSION: 1.8.0_73
 *   APP PURPOSE: Prototype login system that supports a mock user database. Users are given the ability
 *                to create a new account and login from that point forward.
 * CLASS PURPOSE: Provides test cases for a Heap
 *       PACKAGE: tests.util.queue
 *     PROFESSOR: Tanes Kanchanawanchai [CSC 202-061N]
 */

package util.queue;

import org.junit.Test;

import static org.junit.Assert.*;

public class HeapTest {
    @Test
    public void isEmpty() throws Exception {
        Heap<String> heap = new Heap<>(5);

        assertTrue(heap.isEmpty());

        heap.enqueue("New Element");
        assertFalse(heap.isEmpty());

        heap.dequeue();
        assertTrue(heap.isEmpty());
    }

    @Test
    public void isFull() throws Exception {
        Heap<String> heap = new Heap<>(1);

        assertFalse(heap.isFull());

        heap.enqueue("Adam");
        assertTrue(heap.isFull());

        heap.dequeue();
        assertFalse(heap.isFull());
    }

    @Test
    public void enqueue() throws Exception {
        Heap<String> heap = new Heap<>(6);

        heap.enqueue("J");
        heap.enqueue("A");
        heap.enqueue("M");
        heap.enqueue("B");
        heap.enqueue("L");
        heap.enqueue("E");

        String correctFormat = "M\nL\nJ\nA\nB\nE\n";

        assertEquals(correctFormat, heap.toString());
    }

    @Test
    public void dequeue() throws Exception {
        Heap<String> heap = new Heap<>(6);

        heap.enqueue("J");
        heap.enqueue("A");
        heap.enqueue("M");
        heap.enqueue("B");
        heap.enqueue("L");
        heap.enqueue("E");

        assertEquals("M", heap.dequeue());
        assertEquals("L", heap.dequeue());

        String correctFormat = "J\nE\nB\nA\n";

        assertEquals(correctFormat, heap.toString());
    }

}