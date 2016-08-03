/**
 *   APPLICATION: LoginSystem
 *         CLASS: IWeightedGraph
 *        AUTHOR: Samuel Myles
 *   JDK VERSION: 1.8.0_73
 *   JRE VERSION: 1.8.0_73
 *   APP PURPOSE: Prototype login system that supports a mock user database. Users are given the ability
 *                to create a new account and login from that point forward.
 * CLASS PURPOSE: Defines methods used by a weighted graph data structure
 *       PACKAGE: util.graph
 *     PROFESSOR: Tanes Kanchanawanchai [CSC 202-061N]
 */

package util.graph;

import util.queue.IQueue;

public interface IWeightedGraph<T> {
	boolean isEmpty();
	
	boolean isFull();

	int size();
	
	void addVertex(T vert);
	
	boolean hasVertex(T vert);
	
	void addEdge(T startVertex, T endVertex, int weight);
	
	int weightIs(T startVertex, T endVertex);
	
	IQueue<T> getToVertices(T vert);
	
	void clearMarks();
	
	void markVertex(T vert);
	
	boolean isMarked(T vert);
	
	T getUnmarked();
	
	boolean isPath(T startVertex, T endVertex);

	String toString();
}
