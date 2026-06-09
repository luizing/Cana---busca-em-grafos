package benchmark;

import graph.Graph;
import graph.Heuristic;

public record BenchmarkScenario(
        String name,
        Graph graph,
        int startVertex,
        int goalVertex,
        String densityLabel,
        boolean solutionExpected,
        double expectedBestCost,
        Heuristic heuristic
) {
}
