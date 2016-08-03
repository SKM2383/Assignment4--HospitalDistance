/**
 *   APPLICATION: LoginSystem
 *         CLASS: HospitalSearchControl
 *        AUTHOR: Samuel Myles
 *   JDK VERSION: 1.8.0_73
 *   JRE VERSION: 1.8.0_73
 *   APP PURPOSE: Prototype login system that supports a mock user database. Users are given the ability
 *                to create a new account and login from that point forward.
 * CLASS PURPOSE: Used to represent a weighted graph data structure by providing methods to add vertexes, edges, and weights.
 *                Also provides methods to query different attributes of the graph such as the weight between two vertexes
 *                as well as methods to mark certain vertexes. This graph is bounded in size.
 *       PACKAGE: util.graph
 *     PROFESSOR: Tanes Kanchanawanchai [CSC 202-061N]
 */

package util.graph;

import util.queue.IQueue;
import util.queue.LinkedListQueue;
import util.stack.LinkedListStack;

public class WeightedGraph<T> implements IWeightedGraph<T> {
	public static final int NULL_EDGE = -1;
	
	private int amountOfVertices;
	private int maximumVertices;
	
	private T[] vertices;
	private int[][] edges;
	private boolean[] marks;
	
	public WeightedGraph(){
		amountOfVertices = 0;
		maximumVertices = 50;
		vertices = (T[]) new Object[50];
		marks = new boolean[50];
		edges = new int[50][50];
	}
	
	public WeightedGraph(int size){
		amountOfVertices = 0;
		maximumVertices = size;
		vertices = (T[]) new Object[size];
		marks = new boolean[size];
		edges = new int[size][size];
	}
	
	@Override
	public boolean isEmpty(){ return (amountOfVertices == 0); }
	
	@Override
	public boolean isFull(){ return (amountOfVertices == maximumVertices); }

	@Override
	public int size(){ return amountOfVertices; }
	
	@Override
	public void addVertex(T newVertex){
		//Add the new vertex to the end of the vertex array
		vertices[amountOfVertices] = newVertex;
		
		// Set the edge values for this new vertex to null value because
		// no edges have been set for it
		for(int i = 0; i < amountOfVertices; i++){
			edges[amountOfVertices][i] = NULL_EDGE;
			edges[i][amountOfVertices] = NULL_EDGE;
		}
		
		//Now increase the graph size
		amountOfVertices++;
	}
	
	@Override
	public boolean hasVertex(T vertex){
		boolean foundVertex = false;
		
		// Iterate through the vertices and check is vertex is equal
		for(int index = 0; index < amountOfVertices; index++){
			if(vertex.equals(vertices[index])){
				foundVertex = true;
				break;
			}
		}

		return foundVertex;
	}
	
	// Helper method to find the index of a vertex within this classes vertex array
	private int getIndex(T vertex){
		int vertexArrayIndex = 0;
		
		while(!vertex.equals(vertices[vertexArrayIndex])){
			vertexArrayIndex++;
		}
		
		return vertexArrayIndex;
	}
	
	@Override
	public void addEdge(T startVertex, T endVertex, int weight){
		// Get the indexes of the vertexes as they are in this.vertices
		int startVertexIndex = getIndex(startVertex);
		int endVertexIndex = getIndex(endVertex);
		
		// Now set the weight in the two-dimensional edges array
		// The startIndex must be first since this is a directed graph
		edges[startVertexIndex][endVertexIndex] = weight;
	}
	
	@Override
	public int weightIs(T startVertex, T endVertex){
		int startIndex = getIndex(startVertex);
		int endIndex = getIndex(endVertex);
		
		// This returns the weight value if it is set, otherwise the null value is returned
		return (edges[startIndex][endIndex]);
	}
	
	@Override
	public IQueue<T> getToVertices(T vertex){
		IQueue<T> vertexQueue = new LinkedListQueue<>();
		
		// A row in the edges array indicates the vertexes that a particular vertex
		// is linked to, therefore the row for vertex needs to be found
		int edgeRow = getIndex(vertex);
		
		// Now just iterate through the edges array, and whenever a weight isn't a
		// NULL_EDGE, it means that this vertex is connected to it, so add it to the queue
		for(int currentIndex = 0; currentIndex < amountOfVertices; currentIndex++){
			if(edges[edgeRow][currentIndex] != NULL_EDGE)
				vertexQueue.enqueue(vertices[currentIndex]);
		}
		
		return vertexQueue;
	}
	
	@Override
	public void clearMarks(){
		for(int i = 0; i < amountOfVertices; i++){
			marks[i] = false;
		}
	}
	
	@Override
	public void markVertex(T vert){
		marks[getIndex(vert)] = true;
	}
	
	@Override
	public boolean isMarked(T vert){
		return (marks[getIndex(vert)]);
	}
	
	@Override
	public T getUnmarked(){
		// If any vertexes are marked false return them using their index from marks
		for(int index = 0; index < amountOfVertices; index++){
			if(marks[index] == false)
				return vertices[index];
		}
		
		// If none were found return null
		return null;
	}
	
	@Override
	public boolean isPath(T startVertex, T endVertex){
		boolean found = false;
		this.clearMarks();
		
		LinkedListStack<T> stack = new LinkedListStack();
		T currentVertex;
		T adjacentVertex;
		
		stack.push(startVertex);
		
		do{
			currentVertex = stack.top();
			stack.pop();
			
			if(currentVertex.equals(endVertex))
				found = true;
			else{
				if(!this.isMarked(currentVertex)){
					this.markVertex(currentVertex);
					
					IQueue<T> queue = this.getToVertices(currentVertex);
					while(!queue.isEmpty()){
						adjacentVertex = queue.dequeue();
						if(!this.isMarked(adjacentVertex))
							stack.push(adjacentVertex);
					}
				}
			}
		}while(!stack.isEmpty() && !found);
		
		return found;
	}

	@Override
	public String toString(){
		String representation = "";
		for(int i = 0; i < vertices.length; i++){
			representation += (vertices[i] + "\n");
		}

		return representation;
	}
}
