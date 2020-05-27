import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Queue;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

/**
 * Implementations of various graph algorithms.
 *
 * @author Scott Heston
 * @version 1.0
 */
public class GraphAlgorithms {

    /**
     * Perform breadth first search on the given graph, starting at the start
     * Vertex.  Returns a List of the vertices in the order that
     * you visited them.  Make sure to include the starting vertex at the
     * beginning of the list.
     * @param start the Vertex you are starting at
     * @param graph the Graph we are searching
     * @param <T>   the data type representing the vertices in the graph.
     * @return a List of vertices in the order that you visited them
     * @throws IllegalArgumentException if any input is null, or if
     *                                  {@code start} doesn't exist in the graph
     */
    public static <T> List<Vertex<T>> breadthFirstSearch(Vertex<T> start,
                                                         Graph<T> graph) {
        if (graph == null) {
            throw new IllegalArgumentException("Cannot traverse null graph");
        } else if (start == null) {
            throw new IllegalArgumentException("Cannot traverse with null "
                    + "start vertex");
        }
        if (!graph.getAdjacencyList().containsKey(start)) {
            throw new IllegalArgumentException("Graph does not contain "
                    + "starting vertex");
        }
        Queue<Vertex<T>> q = new LinkedList<>();
        q.add(start);

        List<Vertex<T>> visited = new LinkedList<>();
        visited.add(start);
        //adj must give back set including self
        Vertex<T> current;
        Map<Vertex<T>, List<VertexDistancePair<T>>> adj = graph
                .getAdjacencyList();
        // the fact that the VertexDistancePair uses a Map will mean that
        // elements with the same data could be overridden(see Vertex's equals)
        while (!q.isEmpty()) {
            current = q.remove();
            for (VertexDistancePair<T> p : adj.get(current)) {
                Vertex<T> cur = p.getVertex();
                if (!visited.contains(cur)) {
                    visited.add(cur);
                    q.add(cur);
                }
            }
        }
        return visited;
    }

    /**
     * Performs depth first search on the given graph, starting at the start
     * Vertex.  Returns a List of the vertices in the order that
     * you visited them, starting vertex at the
     * beginning of the list.
     * @param start the Vertex you are starting at
     * @param graph the Graph we are searching
     * @param <T>   the data type representing the vertices in the graph.
     * @return a List of vertices in the order that you visited them
     * @throws IllegalArgumentException if any input is null, or if
     * doesn't exist in the graph
     */
    public static <T> List<Vertex<T>> depthFirstSearch(Vertex<T> start,
                                                       Graph<T> graph) {
        if (graph == null) {
            throw new IllegalArgumentException("Cannot traverse null graph");
        } else if (start == null) {
            throw new IllegalArgumentException("Cannot traverse with null "
                    + "start vertex");
        }
        if (!graph.getAdjacencyList().containsKey(start)) {
            throw new IllegalArgumentException("Graph does not contain "
                    + "starting vertex");
        }
        return depthFirstSearch(start, graph
                .getAdjacencyList(), new LinkedList<Vertex<T>>());

    }


    /**
     * recursive helper method for DFS
     * @param current vertex
     * @param adj list
     * @param visited already
     * @param <T> paramaterization
     * @return list of vertices
     */
    private static <T> List<Vertex<T>> depthFirstSearch(Vertex<T> current,
        Map<Vertex<T>, List<VertexDistancePair<T>>> adj, List<Vertex<T>>
                                                                visited) {
        visited.add(current);
        for (VertexDistancePair<T> p : adj.get(current)) {
            Vertex<T> cur = p.getVertex();
            if (!visited.contains(cur)) {
                depthFirstSearch(cur, adj, visited);
            }
        }
        return visited;
    }


    /**
     * Find the shortest distance between the start vertex and all other
     * vertices given a weighted graph where the edges only have positive
     * weights.
     *
     *
     * @throws IllegalArgumentException if any input is null, or if
     *         {@code start} doesn't exist in the graph
     * @param start the Vertex you are starting at
     * @param graph the Graph we are searching
     * @param <T> the data type representing the vertices in the graph.
     * @return a map of the shortest distances from start to every other node
     *         in the graph.
     */
    public static <T> Map<Vertex<T>, Integer> dijkstras(Vertex<T> start,
            Graph<T> graph) {
        if (graph == null) {
            throw new IllegalArgumentException("Cannot traverse null graph");
        } else if (start == null) {
            throw new IllegalArgumentException("Cannot traverse with null "
                    + "start vertex");
        }
        if (!graph.getAdjacencyList().containsKey(start)) {
            throw new IllegalArgumentException("Graph does not contain "
                    + "starting vertex");
        }
        Map<Vertex<T>, Integer> settled = new HashMap<>();
        PriorityQueue<VertexDistancePair<T>> pq = new
                PriorityQueue<>(graph.getAdjacencyList().get(start));

        Queue<Vertex<T>> q = new LinkedList<>();
        q.add(start);

        for (Vertex<T> v : graph.getAdjacencyList().keySet()) {
            settled.put(v, Integer.MAX_VALUE);
        }
        //more efficient than check every time
        settled.put(start, 0);

        VertexDistancePair<T> curPair;
        Vertex<T> current;
        while (!q.isEmpty()) {
            current = q.remove();
            pq.addAll(graph.getAdjacencyList().get(current));
            while (!pq.isEmpty()) {
                curPair =  pq.remove();
                if (settled.get(current) + curPair.getDistance()
                        < settled.get(curPair.getVertex())) {
                    settled.put(curPair.getVertex(),
                            settled.get(current) + curPair.getDistance());
                    q.add(curPair.getVertex());
                }
            }
        }
        return settled;
    }

    /**
     * Runs Prim's algorithm on the given graph and return the minimum spanning
     * tree in the form of a set of Edges.  If the graph is disconnected, and
     * therefore there is no valid MST, return null.
     *
     *
     * @throws IllegalArgumentException if any input is null, or if
     *         {@code start} doesn't exist in the graph
     * @param start the Vertex you are starting at
     * @param graph the Graph we are searching
     * @param <T> the data type representing the vertices in the graph.
     * @return the MST of the graph; null if no valid MST exists.
     */
    public static <T> Set<Edge<T>> prims(Vertex<T> start, Graph<T> graph) {
        if (graph == null) {
            throw new IllegalArgumentException("Cannot traverse null graph");
        } else if (start == null) {
            throw new IllegalArgumentException("Cannot traverse with null "
                    + "start vertex");
        }
        if (!graph.getAdjacencyList().containsKey(start)) {
            throw new IllegalArgumentException("Graph does not contain "
                    + "starting vertex");
        }
        Map<Vertex<T>, Integer> settled = new HashMap<>();

        Queue<Vertex<T>> q = new LinkedList<>();
        q.add(start);

        PriorityQueue<Edge<T>> pq = new
                PriorityQueue<>(graph.getEdgeList());

        for (Vertex<T> v : graph.getAdjacencyList().keySet()) {
            settled.put(v, Integer.MAX_VALUE);
        }
        settled.put(start, 0);

        Set<Edge<T>> minTree = new HashSet<>();
        Edge<T> currEdge;
        while (!pq.isEmpty()) {
            currEdge = pq.remove();
            if (settled.get(currEdge.getU()) + currEdge.getWeight()
                    < settled.get(currEdge.getV())) {
                settled.put(currEdge.getV(), settled.get(currEdge.getU())
                        + currEdge.getWeight());
                minTree.add(currEdge);
            }
        }
        return minTree;
    }

}
