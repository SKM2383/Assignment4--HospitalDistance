/**
 *   APPLICATION: LoginSystem
 *         CLASS: WeightedGraphTest
 *        AUTHOR: Samuel Myles
 *   JDK VERSION: 1.8.0_73
 *   JRE VERSION: 1.8.0_73
 *   APP PURPOSE: Prototype login system that supports a mock user database. Users are given the ability
 *                to create a new account and login from that point forward.
 * CLASS PURPOSE: Provides test cases for a WeightedGraph
 *       PACKAGE: tests.util.graph
 *     PROFESSOR: Tanes Kanchanawanchai [CSC 202-061N]
 */

package util.graph;

import org.junit.Test;
import util.queue.LinkedListQueue;

import static org.junit.Assert.*;

public class WeightedGraphTest {
    @Test
    public void isEmpty() throws Exception {
        WeightedGraph<String> graph = new WeightedGraph<>(7);

        assertTrue(graph.isEmpty());

        graph.addVertex("Houston");
        assertFalse(graph.isEmpty());
    }

    @Test
    public void isFull() throws Exception {
        WeightedGraph<String> graph = new WeightedGraph<>(1);

        assertFalse(graph.isFull());

        graph.addVertex("Houston");
        assertTrue(graph.isFull());
    }

    @Test
    public void addVertex() throws Exception {
        WeightedGraph<String> graph = new WeightedGraph<>(1);

        graph.addVertex("Houston");

        assertEquals("Houston", graph.getUnmarked());
    }

    @Test
    public void hasVertex() throws Exception {
        WeightedGraph<String> graph = new WeightedGraph<>(2);

        graph.addVertex("Houston");
        graph.addVertex("San Francisco");

        assertTrue(graph.hasVertex("Houston"));
        assertTrue(graph.hasVertex("San Francisco"));
    }

    @Test
    public void addEdge() throws Exception {
        WeightedGraph<String> graph = new WeightedGraph<>(2);

        graph.addVertex("Houston");
        graph.addVertex("San Francisco");

        graph.addEdge("Houston", "San Francisco", 500);

        assertEquals(500, graph.weightIs("Houston", "San Francisco"));
    }

    @Test
    public void weightIs() throws Exception {
        WeightedGraph<String> graph = new WeightedGraph<>(5);

        graph.addVertex("Houston");
        graph.addVertex("San Francisco");
        graph.addVertex("Phoenix");
        graph.addVertex("Helena");
        graph.addVertex("New York");

        graph.addEdge("Houston", "San Francisco", 500);
        graph.addEdge("Helena", "New York", 1100);
        graph.addEdge("Phoenix", "Houston", 300);
        graph.addEdge("New York", "San Francisco", 1050);

        assertEquals(500, graph.weightIs("Houston", "San Francisco"));
        assertEquals(1100, graph.weightIs("Helena", "New York"));
        assertEquals(1050, graph.weightIs("New York", "San Francisco"));
        assertEquals(-1, graph.weightIs("Houston", "New York"));
    }

    @Test
    public void getToVertices() throws Exception {
        WeightedGraph<String> graph = new WeightedGraph<>(5);

        graph.addVertex("Houston");
        graph.addVertex("San Francisco");
        graph.addVertex("Phoenix");
        graph.addVertex("Helena");
        graph.addVertex("New York");

        graph.addEdge("Houston", "San Francisco", 500);
        graph.addEdge("Houston", "New York", 800);
        graph.addEdge("Houston", "Helena", 750);

        LinkedListQueue<String> comparisonQueue = new LinkedListQueue<>();
        comparisonQueue.enqueue("Houston");
        comparisonQueue.enqueue("San Francisco");
        comparisonQueue.enqueue("Helena");
        comparisonQueue.enqueue("New York");

        assertEquals(comparisonQueue.toString(), graph.getToVertices("Houston").toString());
    }

    @Test
    public void clearMarks() throws Exception {
        WeightedGraph<String> graph = new WeightedGraph<>(5);

        graph.addVertex("Houston");
        graph.addVertex("San Francisco");

        graph.markVertex("San Francisco");
        assertTrue(graph.isMarked("San Francisco"));

        graph.clearMarks();
        assertFalse(graph.isMarked("San Francisco"));
    }

    @Test
    public void markVertex() throws Exception {
        WeightedGraph<String> graph = new WeightedGraph<>(3);

        graph.addVertex("Houston");
        graph.addVertex("San Francisco");

        graph.markVertex("San Francisco");
        assertTrue(graph.isMarked("San Francisco"));
    }

    @Test
    public void isMarked() throws Exception {
        WeightedGraph<String> graph = new WeightedGraph<>(2);

        graph.addVertex("Houston");
        graph.addVertex("San Francisco");

        graph.markVertex("San Francisco");
        assertTrue(graph.isMarked("San Francisco"));
    }

    @Test
    public void getUnmarked() throws Exception {
        WeightedGraph<String> graph = new WeightedGraph<>(5);

        graph.addVertex("Phoenix");
        graph.addVertex("Helena");
        graph.addVertex("New York");

        assertEquals("Phoenix", graph.getUnmarked());

        graph.markVertex("Phoenix");

        assertEquals("Helena", graph.getUnmarked());
    }

    @Test
    public void isPath() throws Exception {
        WeightedGraph<String> graph = new WeightedGraph<>(5);

        graph.addVertex("Houston");
        graph.addVertex("San Francisco");
        graph.addVertex("Phoenix");
        graph.addVertex("Helena");
        graph.addVertex("New York");

        graph.addEdge("Houston", "Phoenix", 400);
        graph.addEdge("Phoenix", "San Francisco", 300);

        assertTrue(graph.isPath("Houston", "San Francisco"));
    }
}