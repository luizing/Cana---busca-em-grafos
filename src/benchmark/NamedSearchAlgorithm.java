package benchmark;

import Algorithms.SearchAlgorithm;
import Algorithms.SearchRequest;
import Algorithms.SearchResult;

public record NamedSearchAlgorithm(
        String name,
        AlgorithmCategory category,
        SearchAlgorithm algorithm
) {
    public SearchResult execute(SearchRequest request) {
        return algorithm.search(request);
    }
}
