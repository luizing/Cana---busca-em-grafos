package benchmark;

import java.util.List;

public final class AlgorithmRegistry {
    private AlgorithmRegistry() {
    }

    public static List<NamedSearchAlgorithm> defaultAlgorithms() {
        return List.of(
                new NamedSearchAlgorithm("BFS", AlgorithmCategory.BLIND, SearchAlgorithms::breadthFirstSearch),
                new NamedSearchAlgorithm("DFS", AlgorithmCategory.BLIND, SearchAlgorithms::depthFirstSearch),
                new NamedSearchAlgorithm("Best-First", AlgorithmCategory.HEURISTIC, SearchAlgorithms::bestFirstSearch),
                new NamedSearchAlgorithm("Greedy", AlgorithmCategory.HEURISTIC, SearchAlgorithms::greedySearch),
                new NamedSearchAlgorithm("A-Star", AlgorithmCategory.HEURISTIC, SearchAlgorithms::aStarSearch),
                new NamedSearchAlgorithm("Pesquisado 1", AlgorithmCategory.RESEARCHED, SearchAlgorithms::researchedAlgorithmOne),
                new NamedSearchAlgorithm("Pesquisado 2", AlgorithmCategory.RESEARCHED, SearchAlgorithms::researchedAlgorithmTwo)
        );
    }
}
