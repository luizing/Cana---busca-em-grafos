package Algorithms;

import java.util.List;

public record SearchResult(
        boolean foundSolution,
        List<Integer> path,
        int visitedNodes,
        int expandedNodes,
        int solutionDepth,
        double pathCost,
        String message
) {
    public static SearchResult found(List<Integer> path, int visitedNodes, int expandedNodes, double pathCost) {
        return new SearchResult(
                true,
                List.copyOf(path),
                visitedNodes,
                expandedNodes,
                Math.max(0, path.size() - 1),
                pathCost,
                ""
        );
    }

    public static SearchResult notFound(int visitedNodes, int expandedNodes, String message) {
        return new SearchResult(
                false,
                List.of(),
                visitedNodes,
                expandedNodes,
                -1,
                Double.NaN,
                message
        );
    }

    @Override
    public String toString() {
        String depth = foundSolution ? String.valueOf(solutionDepth) : "N/A";
        String cost = foundSolution ? String.valueOf(pathCost) : "N/A";

        return """
                Resultado da busca:
                  Solucao encontrada: %s
                  Caminho: %s
                  Nos visitados: %d
                  Nos expandidos: %d
                  Profundidade da solucao: %s
                  Custo do caminho: %s
                """.formatted(
                foundSolution,
                path,
                visitedNodes,
                expandedNodes,
                depth,
                cost
        );
    }
}
