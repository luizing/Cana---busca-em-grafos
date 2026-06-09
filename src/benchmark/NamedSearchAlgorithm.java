package benchmark;

public record NamedSearchAlgorithm(
        String name,
        AlgorithmCategory category,
        SearchAlgorithm algorithm
) {
    public SearchResult execute(SearchRequest request) {
        return algorithm.search(request);
    }
}
