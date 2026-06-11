package Benchmark;

import Algorithms.SearchRequest;
import graph.Graph;
import graph.Heuristic;

import java.util.List;

public final class RequestGenerator {
    private static final int SMALL_SIZE = 10;
    private static final int MEDIUM_SIZE = 100;
    private static final int LARGE_SIZE = 1_000;

    private RequestGenerator() {
    }

    public static List<SearchRequest> allRequests() {
        return List.of(
                smallSparseWithSolution(),
                smallSparseWithoutSolution(),
                smallDenseWithSolution(),
                smallDenseWithoutSolution(),
                mediumSparseWithSolution(),
                mediumSparseWithoutSolution(),
                mediumDenseWithSolution(),
                mediumDenseWithoutSolution(),
                largeSparseWithSolution(),
                largeSparseWithoutSolution(),
                largeDenseWithSolution(),
                largeDenseWithoutSolution()
        );
    }

    public static SearchRequest smallSparseWithSolution() {
        return sparseWithSolution(SMALL_SIZE);
    }

    public static SearchRequest smallSparseWithoutSolution() {
        return sparseWithoutSolution(SMALL_SIZE);
    }

    public static SearchRequest smallDenseWithSolution() {
        return denseWithSolution(SMALL_SIZE);
    }

    public static SearchRequest smallDenseWithoutSolution() {
        return denseWithoutSolution(SMALL_SIZE);
    }

    public static SearchRequest mediumSparseWithSolution() {
        return sparseWithSolution(MEDIUM_SIZE);
    }

    public static SearchRequest mediumSparseWithoutSolution() {
        return sparseWithoutSolution(MEDIUM_SIZE);
    }

    public static SearchRequest mediumDenseWithSolution() {
        return denseWithSolution(MEDIUM_SIZE);
    }

    public static SearchRequest mediumDenseWithoutSolution() {
        return denseWithoutSolution(MEDIUM_SIZE);
    }

    public static SearchRequest largeSparseWithSolution() {
        return sparseWithSolution(LARGE_SIZE);
    }

    public static SearchRequest largeSparseWithoutSolution() {
        return sparseWithoutSolution(LARGE_SIZE);
    }

    public static SearchRequest largeDenseWithSolution() {
        return denseWithSolution(LARGE_SIZE);
    }

    public static SearchRequest largeDenseWithoutSolution() {
        return denseWithoutSolution(LARGE_SIZE);
    }

    private static SearchRequest sparseWithSolution(int vertices) {
        Graph graph = new Graph(vertices);

        for (int vertex = 0; vertex < vertices - 1; vertex++) {
            graph.addDirectedEdge(vertex, vertex + 1, 1);
        }

        for (int vertex = 0; vertex + 3 < vertices; vertex += 3) {
            graph.addDirectedEdge(vertex, vertex + 3, 3);
        }

        int goal = vertices - 1;
        return new SearchRequest(graph, 0, goal, distanceTo(goal));
    }

    private static SearchRequest sparseWithoutSolution(int vertices) {
        Graph graph = new Graph(vertices);
        int separation = vertices / 2;

        for (int vertex = 0; vertex < separation - 1; vertex++) {
            graph.addDirectedEdge(vertex, vertex + 1, 1);
        }
        for (int vertex = separation; vertex < vertices - 1; vertex++) {
            graph.addDirectedEdge(vertex, vertex + 1, 1);
        }

        int goal = vertices - 1;
        return new SearchRequest(graph, 0, goal, distanceTo(goal));
    }

    private static SearchRequest denseWithSolution(int vertices) {
        Graph graph = new Graph(vertices);
        int reach = Math.max(2, vertices / 4);

        for (int from = 0; from < vertices; from++) {
            int lastNeighbor = Math.min(vertices - 1, from + reach);
            for (int to = from + 1; to <= lastNeighbor; to++) {
                graph.addDirectedEdge(from, to, to - from);
                graph.addDirectedEdge(to, from, to - from);
            }
        }

        int goal = vertices - 1;
        return new SearchRequest(graph, 0, goal, distanceTo(goal));
    }

    private static SearchRequest denseWithoutSolution(int vertices) {
        Graph graph = new Graph(vertices);
        int separation = vertices / 2;

        connectDensePartition(graph, 0, separation);
        connectDensePartition(graph, separation, vertices);

        int goal = vertices - 1;
        return new SearchRequest(graph, 0, goal, distanceTo(goal));
    }

    private static void connectDensePartition(Graph graph, int start, int end) {
        for (int from = start; from < end; from++) {
            for (int to = from + 1; to < end; to++) {
                int weight = to - from;
                graph.addDirectedEdge(from, to, weight);
                graph.addDirectedEdge(to, from, weight);
            }
        }
    }

    private static Heuristic distanceTo(int goal) {
        return vertex -> Math.abs(goal - vertex);
    }
}
