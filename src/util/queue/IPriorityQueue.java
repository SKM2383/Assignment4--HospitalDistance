/**
 *   APPLICATION: LoginSystem
 *         CLASS: IPriorityQueue
 *        AUTHOR: Samuel Myles
 *   JDK VERSION: 1.8.0_73
 *   JRE VERSION: 1.8.0_73
 *   APP PURPOSE: Prototype login system that supports a mock user database. Users are given the ability
 *                to create a new account and login from that point forward.
 * CLASS PURPOSE: Provides method headers for priority queue data structures
 *       PACKAGE: util.queue
 *     PROFESSOR: Tanes Kanchanawanchai [CSC 202-061N]
 */

package util.queue;

public interface IPriorityQueue<T extends Comparable<T>> {
	boolean isEmpty();
	
	boolean isFull();

	int size();
	
	void enqueue(T element);
	
	T dequeue();
	
	String toString();
}
