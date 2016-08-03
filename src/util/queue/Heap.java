/**
 *   APPLICATION: LoginSystem
 *         CLASS: Heap
 *        AUTHOR: Samuel Myles
 *   JDK VERSION: 1.8.0_73
 *   JRE VERSION: 1.8.0_73
 *   APP PURPOSE: Prototype login system that supports a mock user database. Users are given the ability
 *                to create a new account and login from that point forward.
 * CLASS PURPOSE: A generic class used to hold element within a queue structure, but return the highest priority
 *                element within the queue at the time the queue is dequeued. This heap sees higher elements as
 *                a higher priority based on their compareTo method (so if this class had Strings, then those which
 *                come later on in the alphabet are a higher priority)
 *       PACKAGE: util.queue
 *     PROFESSOR: Tanes Kanchanawanchai [CSC 202-061N]
 */

package util.queue;

import java.util.ArrayList;

public class Heap<T extends Comparable<T>> implements IPriorityQueue<T> {
	private ArrayList<T> heap;
	private int lastIndex;
	private int maximumSize;
	
	public Heap(int maxSize){
		heap = new ArrayList(maxSize);
		
		lastIndex = -1;
		maximumSize = maxSize - 1;
	}
	
	@Override
	public boolean isEmpty(){
		return (lastIndex == -1);
	}
	
	@Override
	public boolean isFull(){
		return (lastIndex == maximumSize);
	}

	@Override
	public int size(){ return heap.size(); }
	
	@Override
	public void enqueue(T newElement){
		if(this.isFull()){
			throw new QueueOverflowException("The heap is full, cannot enqueue");
		}
		
		lastIndex++;
		heap.add(lastIndex, newElement);
		reheapUp(newElement);	
	}
	
	private void reheapUp(T element){
		int holeIndex = lastIndex;
		
		// While the hole isn't the root and is greater than the parent
		while((holeIndex > 0) && 
			  (element.compareTo(heap.get((holeIndex-1) / 2)) > 0))
		{
			// Move the parent down the tree
			heap.set(holeIndex, heap.get((holeIndex - 1) / 2));
			
			// Then move the hole up where the parent was
			holeIndex = (holeIndex - 1) / 2;
		}
		
		// Now set the new element where the hole was stopped
		heap.set(holeIndex, element);
	}
	
	@Override
	public T dequeue(){
		// Make sure the heap has at least one element to remove
		if(this.isEmpty()){
			throw new QueueUnderflowException("The heap is empty, cannot dequeue");
		}
		
		// Get the root of the heap (has highest priority)
		T nextOut = heap.get(0);
		
		// Now get the last element in order to reheapDown the heap
		T elementToMove = heap.remove(lastIndex);
		
		// Decrement the lastIndex to decrease the heap size
		lastIndex--;
		
		// If the element removed wasn't the only element in the heap
		// the heap order must be restored
		if(!this.isEmpty())
			reheapDown(elementToMove);
		
		// Return the highest priority element we removed
		return nextOut;
	}
	
	private void reheapDown(T element){
		// Start at root node
		int holeIndex = 0;
		
		// Find which subtree to go down
		int nextHole = getNextHoleIndex(holeIndex, element);
		
		// Until the right index is found, keep traversing down the heap
		while(nextHole != holeIndex){
			heap.set(holeIndex, heap.get(nextHole));
			holeIndex = nextHole;
			nextHole = getNextHoleIndex(holeIndex, element);
		}
		
		// Then set the new element into the correct index position
		heap.set(holeIndex, element);
	}
	
	private int getNextHoleIndex(int currentHole, T element){
		// Calculate the indexes of the left and right children nodes
		int leftIndex = (currentHole * 2) + 1;
		int rightIndex = (currentHole * 2) + 2;
		
		// There are no children nodes
		if(leftIndex > lastIndex){
			return currentHole;
		}
		// There is only a left child node
		else if(leftIndex == lastIndex){
			// If the element is less than the leftIndex we must traverse left
			if(element.compareTo(heap.get(leftIndex)) < 0)
				return leftIndex;
			else
				return currentHole;
		}
		// There are two children
		else{
			if(heap.get(leftIndex).compareTo(heap.get(rightIndex)) < 0){
				// Left child is less than the right child
				if(heap.get(rightIndex).compareTo(element) <= 0)
					// Right child is less than new element
					return currentHole;
				else
					// Otherwise we must traverse the right subtree
					return rightIndex;
			}
			// The left child is greater than the right
			else{
				if(heap.get(leftIndex).compareTo(element) <= 0)
					return currentHole;
				else
					return leftIndex;
			}
		}
	}// End of method getNextHoleIndex
	
	@Override
	public String toString(){
		String representation = "";
		
		for(int i = 0; i <= lastIndex; i++){
			representation += (heap.get(i) + "\n");
		}
		
		return representation;
	}
}
