package benchmark;

@FunctionalInterface
public interface SearchAlgorithm {
    SearchResult search(SearchRequest request);
}
