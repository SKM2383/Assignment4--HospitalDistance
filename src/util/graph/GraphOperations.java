package util.graph;

import model.GeoLocation;
import util.queue.Heap;
import util.queue.IPriorityQueue;
import util.queue.IQueue;

public class GraphOperations {
    public static IPriorityQueue<GeoLocation> getShortestPaths(IWeightedGraph<GeoLocation> graph, GeoLocation source){
        // Create a priority queue to hold unvisited vertices
        IPriorityQueue<GeoLocation> unvisitedVertex = new Heap<>(999);

        // Now mark the source and visit all the other vertices
        graph.clearMarks();
        graph.markVertex(source);

        GeoLocation unmarkedVertex = graph.getUnmarked();
        while(unmarkedVertex != null){
            // For each vertex, mark its distance as being a large value and its predecessor to null
            unmarkedVertex.setDistanceFromMarker(Integer.MAX_VALUE);
            unmarkedVertex.setPreviousLocation(null);

            graph.markVertex(unmarkedVertex);

            unmarkedVertex = graph.getUnmarked();
        }
        graph.clearMarks();

        // Now set source's distance from itself to 0
        source.setDistanceFromMarker(0.0);

        unvisitedVertex.enqueue(source);

        // While there are still unvisited vertices
        while(!unvisitedVertex.isEmpty()){
            // Get an unvisited vertex
            GeoLocation nextVertex = unvisitedVertex.dequeue();

            // Get all its neighbors
            IQueue<GeoLocation> sourceNeighbors = graph.getToVertices(nextVertex);
            while(!sourceNeighbors.isEmpty()){
                GeoLocation currentNeighbor = sourceNeighbors.dequeue();

                if(!graph.isMarked(currentNeighbor)) {
                    // Calculate the distance it took to get to this neighbor vertex on our current path
                    double currentPathDistance = nextVertex.getDistanceFromMarker() + graph.weightIs(nextVertex, currentNeighbor);
                    // Now calculate the distance the neighbor already has stored
                    double neighborDistance = currentNeighbor.getDistanceFromMarker();

                    // If our new path is shorter, set the neighbor's distance to this shorter distance, and set its
                    // reference to the current vertex to tell that is the way we came
                    if (currentPathDistance <= neighborDistance) {
                        currentNeighbor.setDistanceFromMarker(currentPathDistance);
                        currentNeighbor.setPreviousLocation(nextVertex);
                        unvisitedVertex.enqueue(currentNeighbor);
                    }
                }
            }

            // Finally mark the current vertex as being visited
            graph.markVertex(nextVertex);
        }

        // Now that the vertices all have their distance from source, just put them into another
        // priority queue, since they will now be in the right order
        IPriorityQueue<GeoLocation> orderedVertices = new Heap<>(graph.size());

        graph.clearMarks();

        unmarkedVertex = graph.getUnmarked();
        while(unmarkedVertex != null){
            orderedVertices.enqueue(unmarkedVertex);

            graph.markVertex(unmarkedVertex);

            unmarkedVertex = graph.getUnmarked();
        }

        return orderedVertices;
    }
}
