package Benchmark;

import Algorithms.SearchAlgorithms;
import Algorithms.SearchRequest;
import Algorithms.SearchResult;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class RepetitionSearch {
    private static final int DEFAULT_REPETITIONS = 3;
    private static final Path DEFAULT_OUTPUT_DIRECTORY = Path.of("benchmark-csv");

    private RepetitionSearch() {
    }

    public static void main(String[] args) throws IOException {
        runAll(DEFAULT_OUTPUT_DIRECTORY, DEFAULT_REPETITIONS);
    }

    public static void runAll() throws IOException {
        runAll(DEFAULT_OUTPUT_DIRECTORY, DEFAULT_REPETITIONS);
    }

    public static void runAll(Path outputDirectory, int repetitions) throws IOException {
        if (repetitions <= 0) {
            throw new IllegalArgumentException("A quantidade de repeticoes deve ser maior que zero.");
        }

        Files.createDirectories(outputDirectory);
        List<RequestCase> requestCases = requests();

        for (AlgorithmCase algorithm : algorithms()) {
            Path csvFile = outputDirectory.resolve(algorithm.fileName() + ".csv");
            writeAlgorithmResults(csvFile, algorithm, requestCases, repetitions);
        }
    }

    private static void writeAlgorithmResults(
            Path csvFile,
            AlgorithmCase algorithm,
            List<RequestCase> requests,
            int repetitions
    ) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(csvFile)) {
            writer.write(csvHeader());
            writer.newLine();

            for (RequestCase requestCase : requests) {
                writer.write(runRequest(algorithm, requestCase, repetitions));
                writer.newLine();
            }
        }
    }

    private static String runRequest(
            AlgorithmCase algorithm,
            RequestCase requestCase,
            int repetitions
    ) {
        SearchRequest request = requestCase.request();
        SearchResult result = null;
        long totalElapsedNanos = 0L;

        try {
            for (int repetition = 0; repetition < repetitions; repetition++) {
                long startNanos = System.nanoTime();
                result = algorithm.search().execute(request);
                totalElapsedNanos += System.nanoTime() - startNanos;
            }

            return toCsvLine(
                    requestCase.name(),
                    request,
                    result,
                    repetitions,
                    totalElapsedNanos / (double) repetitions,
                    ""
            );
        } catch (RuntimeException exception) {
            String error = exception.getMessage() == null
                    ? exception.getClass().getSimpleName()
                    : exception.getMessage();
            return toCsvLine(
                    requestCase.name(),
                    request,
                    result,
                    repetitions,
                    totalElapsedNanos / (double) repetitions,
                    error
            );
        }
    }

    private static String toCsvLine(
            String requestName,
            SearchRequest request,
            SearchResult result,
            int repetitions,
            double averageElapsedNanos,
            String error
    ) {
        boolean completed = result != null && error.isBlank();

        return String.join(",",
                escape(requestName),
                String.valueOf(request.graph().vertexCount()),
                String.valueOf(request.graph().edgeCount()),
                String.valueOf(repetitions),
                completed ? String.valueOf(result.foundSolution()) : "",
                completed ? escape(result.path().toString()) : "",
                completed ? String.valueOf(result.visitedNodes()) : "",
                completed ? String.valueOf(result.expandedNodes()) : "",
                completed ? String.valueOf(result.solutionDepth()) : "",
                completed ? number(result.pathCost()) : "",
                String.valueOf(averageElapsedNanos),
                completed ? escape(result.message()) : "",
                escape(error)
        );
    }

    private static String csvHeader() {
        return "request_type,vertices,edges,repetitions,found_solution,path,visited_nodes,"
                + "expanded_nodes,solution_depth,path_cost,average_elapsed_nanos,message,error";
    }

    private static List<AlgorithmCase> algorithms() {
        return List.of(
                new AlgorithmCase("bfs", SearchAlgorithms::breadthFirstSearch),
                new AlgorithmCase("dfs", SearchAlgorithms::depthFirstSearch),
                new AlgorithmCase("best-first", SearchAlgorithms::bestFirstSearch),
                new AlgorithmCase("greedy", SearchAlgorithms::greedySearch),
                new AlgorithmCase("a-star", SearchAlgorithms::aStarSearch),
                new AlgorithmCase("uniform-cost", SearchAlgorithms::uniformCost),
                new AlgorithmCase("dijkstra", SearchAlgorithms::dijkstra)
        );
    }

    private static List<RequestCase> requests() {
        return List.of(
                new RequestCase("small_sparse_with_solution", RequestGenerator.smallSparseWithSolution()),
                new RequestCase("small_sparse_without_solution", RequestGenerator.smallSparseWithoutSolution()),
                new RequestCase("small_dense_with_solution", RequestGenerator.smallDenseWithSolution()),
                new RequestCase("small_dense_without_solution", RequestGenerator.smallDenseWithoutSolution()),
                new RequestCase("medium_sparse_with_solution", RequestGenerator.mediumSparseWithSolution()),
                new RequestCase("medium_sparse_without_solution", RequestGenerator.mediumSparseWithoutSolution()),
                new RequestCase("medium_dense_with_solution", RequestGenerator.mediumDenseWithSolution()),
                new RequestCase("medium_dense_without_solution", RequestGenerator.mediumDenseWithoutSolution()),
                new RequestCase("large_sparse_with_solution", RequestGenerator.largeSparseWithSolution()),
                new RequestCase("large_sparse_without_solution", RequestGenerator.largeSparseWithoutSolution()),
                new RequestCase("large_dense_with_solution", RequestGenerator.largeDenseWithSolution()),
                new RequestCase("large_dense_without_solution", RequestGenerator.largeDenseWithoutSolution())
        );
    }

    private static String number(double value) {
        return Double.isNaN(value) ? "" : String.valueOf(value);
    }

    private static String escape(String value) {
        if (value == null) {
            return "";
        }

        String escaped = value.replace("\"", "\"\"");
        if (escaped.contains(",") || escaped.contains("\"")
                || escaped.contains("\n") || escaped.contains("\r")) {
            return "\"" + escaped + "\"";
        }
        return escaped;
    }

    @FunctionalInterface
    private interface SearchFunction {
        SearchResult execute(SearchRequest request);
    }

    private record AlgorithmCase(String fileName, SearchFunction search) {
    }

    private record RequestCase(String name, SearchRequest request) {
    }
}
