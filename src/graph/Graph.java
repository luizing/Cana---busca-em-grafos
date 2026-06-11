package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Graph {
    private final List<List<Edge>> adjacencyList;
    private int edgeCount;

    public Graph(int vertices) {
        if (vertices <= 0) {
            throw new IllegalArgumentException("O grafo precisa ter pelo menos um vertice.");
        }
        this.adjacencyList = new ArrayList<>(vertices);
        for (int i = 0; i < vertices; i++) {
            adjacencyList.add(new ArrayList<>());
        }
    }

    public void addDirectedEdge(int from, int to, int weight) {
        validateVertex(from);
        validateVertex(to);
        if (weight < 0.0) {
            throw new IllegalArgumentException("Pesos negativos nao sao suportados pelo benchmark.");
        }
        adjacencyList.get(from).add(new Edge(to, weight));
        edgeCount++;
    }

    public void addUndirectedEdge(int a, int b, int weight) {
        addDirectedEdge(a, b, weight);
        addDirectedEdge(b, a, weight);
    }

    public List<Edge> neighborsOf(int vertex) {
        validateVertex(vertex);
        return Collections.unmodifiableList(adjacencyList.get(vertex));
    }

    public int vertexCount() {
        return adjacencyList.size();
    }

    public int edgeCount() {
        return edgeCount;
    }

    private void validateVertex(int vertex) {
        if (vertex < 0 || vertex >= vertexCount()) {
            throw new IllegalArgumentException("Vertice fora do intervalo: " + vertex);
        }
    }
}
