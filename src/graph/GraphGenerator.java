package graph;

import benchmark.BenchmarkScenario;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class GraphGenerator {
    private static final long SEED = 20260603L;

    private GraphGenerator() {
    }

    public static List<BenchmarkScenario> defaultScenarios() {
        int[] sizes = {10, 100, 1000, 10000};
        List<BenchmarkScenario> scenarios = new ArrayList<>();

        for (int size : sizes) {
            scenarios.add(connectedScenario("esparso-com-solucao-" + size, size, 2, "SPARSE"));
            scenarios.add(connectedScenario("denso-com-solucao-" + size, size, Math.min(25, Math.max(3, size / 4)), "DENSE"));
            scenarios.add(disconnectedScenario("sem-solucao-" + size, size, "SPARSE"));
        }

        return scenarios;
    }

    private static BenchmarkScenario connectedScenario(String name, int vertices, int extraEdgesPerVertex, String density) {
        Graph graph = new Graph(vertices);
        Random random = new Random(SEED + vertices + extraEdgesPerVertex);

        for (int vertex = 0; vertex < vertices - 1; vertex++) {
            graph.addUndirectedEdge(vertex, vertex + 1, randomWeight(random));
        }

        addRandomEdges(graph, vertices, extraEdgesPerVertex, random);

        int start = 0;
        int goal = vertices - 1;
        return new BenchmarkScenario(
                name,
                graph,
                start,
                goal,
                density,
                true,
                Double.NaN,
                linearDistanceHeuristic(goal)
        );
    }

    private static BenchmarkScenario disconnectedScenario(String name, int vertices, String density) {
        Graph graph = new Graph(vertices);
        Random random = new Random(SEED - vertices);
        int split = Math.max(1, vertices / 2);

        for (int vertex = 0; vertex < split - 1; vertex++) {
            graph.addUndirectedEdge(vertex, vertex + 1, randomWeight(random));
        }
        for (int vertex = split; vertex < vertices - 1; vertex++) {
            graph.addUndirectedEdge(vertex, vertex + 1, randomWeight(random));
        }

        int start = 0;
        int goal = vertices - 1;
        return new BenchmarkScenario(
                name,
                graph,
                start,
                goal,
                density,
                false,
                Double.NaN,
                linearDistanceHeuristic(goal)
        );
    }

    private static void addRandomEdges(Graph graph, int vertices, int extraEdgesPerVertex, Random random) {
        for (int vertex = 0; vertex < vertices; vertex++) {
            for (int i = 0; i < extraEdgesPerVertex; i++) {
                int to = random.nextInt(vertices);
                if (to != vertex) {
                    graph.addUndirectedEdge(vertex, to, randomWeight(random));
                }
            }
        }
    }

    private static int randomWeight(Random random) {
        return 1 + random.nextInt(20);
    }

    private static Heuristic linearDistanceHeuristic(int goal) {
        return vertex -> Math.abs(goal - vertex);
    }
}
