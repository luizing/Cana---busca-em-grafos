package benchmark;

import graph.Graph;

import java.util.ArrayList;
import java.util.List;

import Algorithms.SearchRequest;
import Algorithms.SearchResult;

public final class BenchmarkRunner {
    private final List<NamedSearchAlgorithm> algorithms;
    private final List<BenchmarkScenario> scenarios;
    private final int repetitions;

    public BenchmarkRunner(List<NamedSearchAlgorithm> algorithms, List<BenchmarkScenario> scenarios, int repetitions) {
        this.algorithms = algorithms;
        this.scenarios = scenarios;
        this.repetitions = repetitions;
    }

    public List<BenchmarkResult> runAll() {
        List<BenchmarkResult> results = new ArrayList<>();

        for (BenchmarkScenario scenario : scenarios) {
            for (NamedSearchAlgorithm algorithm : algorithms) {
                for (int repetition = 1; repetition <= repetitions; repetition++) {
                    results.add(runOne(scenario, algorithm, repetition));
                }
            }
        }

        return results;
    }

    private BenchmarkResult runOne(BenchmarkScenario scenario, NamedSearchAlgorithm algorithm, int repetition) {
        SearchRequest request = new SearchRequest(
                scenario.graph(),
                scenario.startVertex(),
                scenario.goalVertex(),
                scenario.heuristic()
        );

        Runtime runtime = Runtime.getRuntime();
        long memoryBefore = usedMemory(runtime);
        long startNanos = System.nanoTime();

        try {
            SearchResult result = algorithm.execute(request);
            long elapsedNanos = System.nanoTime() - startNanos;
            long memoryAfter = usedMemory(runtime);

            return success(scenario, algorithm, repetition, result, elapsedNanos, memoryAfter - memoryBefore);
        } catch (UnsupportedOperationException exception) {
            long elapsedNanos = System.nanoTime() - startNanos;
            return failure(scenario, algorithm, repetition, "NOT_IMPLEMENTED", elapsedNanos, exception.getMessage());
        } catch (RuntimeException exception) {
            long elapsedNanos = System.nanoTime() - startNanos;
            return failure(scenario, algorithm, repetition, "ERROR", elapsedNanos, exception.getMessage());
        }
    }

    private BenchmarkResult success(
            BenchmarkScenario scenario,
            NamedSearchAlgorithm algorithm,
            int repetition,
            SearchResult result,
            long elapsedNanos,
            long memoryDeltaBytes
    ) {
        Graph graph = scenario.graph();
        double qualityRatio = Double.NaN;
        if (result.foundSolution() && scenario.expectedBestCost() > 0.0) {
            qualityRatio = result.pathCost() / scenario.expectedBestCost();
        }

        return new BenchmarkResult(
                scenario.name(),
                graph.vertexCount(),
                graph.edgeCount(),
                scenario.densityLabel(),
                scenario.solutionExpected(),
                algorithm.name(),
                algorithm.category(),
                repetition,
                "OK",
                result.foundSolution(),
                result.visitedNodes(),
                result.expandedNodes(),
                result.solutionDepth(),
                result.pathCost(),
                qualityRatio,
                elapsedNanos,
                memoryDeltaBytes,
                result.message()
        );
    }

    private BenchmarkResult failure(
            BenchmarkScenario scenario,
            NamedSearchAlgorithm algorithm,
            int repetition,
            String status,
            long elapsedNanos,
            String message
    ) {
        Graph graph = scenario.graph();
        return new BenchmarkResult(
                scenario.name(),
                graph.vertexCount(),
                graph.edgeCount(),
                scenario.densityLabel(),
                scenario.solutionExpected(),
                algorithm.name(),
                algorithm.category(),
                repetition,
                status,
                false,
                -1,
                -1,
                -1,
                Double.NaN,
                Double.NaN,
                elapsedNanos,
                0L,
                message
        );
    }

    private long usedMemory(Runtime runtime) {
        return runtime.totalMemory() - runtime.freeMemory();
    }
}
