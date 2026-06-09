package benchmark;

public final class BenchmarkResult {
    private final String scenarioName;
    private final int vertices;
    private final int edges;
    private final String density;
    private final boolean solutionExpected;
    private final String algorithmName;
    private final AlgorithmCategory category;
    private final int repetition;
    private final String status;
    private final boolean foundSolution;
    private final int visitedNodes;
    private final int expandedNodes;
    private final int solutionDepth;
    private final double pathCost;
    private final double qualityRatio;
    private final long elapsedNanos;
    private final long memoryDeltaBytes;
    private final String message;

    public BenchmarkResult(
            String scenarioName,
            int vertices,
            int edges,
            String density,
            boolean solutionExpected,
            String algorithmName,
            AlgorithmCategory category,
            int repetition,
            String status,
            boolean foundSolution,
            int visitedNodes,
            int expandedNodes,
            int solutionDepth,
            double pathCost,
            double qualityRatio,
            long elapsedNanos,
            long memoryDeltaBytes,
            String message
    ) {
        this.scenarioName = scenarioName;
        this.vertices = vertices;
        this.edges = edges;
        this.density = density;
        this.solutionExpected = solutionExpected;
        this.algorithmName = algorithmName;
        this.category = category;
        this.repetition = repetition;
        this.status = status;
        this.foundSolution = foundSolution;
        this.visitedNodes = visitedNodes;
        this.expandedNodes = expandedNodes;
        this.solutionDepth = solutionDepth;
        this.pathCost = pathCost;
        this.qualityRatio = qualityRatio;
        this.elapsedNanos = elapsedNanos;
        this.memoryDeltaBytes = memoryDeltaBytes;
        this.message = message;
    }

    public String toCsvLine() {
        return String.join(",",
                escape(scenarioName),
                String.valueOf(vertices),
                String.valueOf(edges),
                escape(density),
                String.valueOf(solutionExpected),
                escape(algorithmName),
                category.name(),
                String.valueOf(repetition),
                escape(status),
                String.valueOf(foundSolution),
                String.valueOf(visitedNodes),
                String.valueOf(expandedNodes),
                String.valueOf(solutionDepth),
                number(pathCost),
                number(qualityRatio),
                String.valueOf(elapsedNanos),
                String.valueOf(memoryDeltaBytes),
                escape(message)
        );
    }

    public static String csvHeader() {
        return "scenario,vertices,edges,density,solution_expected,algorithm,category,repetition,status,"
                + "found_solution,visited_nodes,expanded_nodes,solution_depth,path_cost,quality_ratio,"
                + "elapsed_nanos,memory_delta_bytes,message";
    }

    private static String number(double value) {
        return Double.isNaN(value) ? "" : String.valueOf(value);
    }

    private static String escape(String value) {
        if (value == null) {
            return "";
        }
        String escaped = value.replace("\"", "\"\"");
        if (escaped.contains(",") || escaped.contains("\"") || escaped.contains("\n")) {
            return "\"" + escaped + "\"";
        }
        return escaped;
    }
}
