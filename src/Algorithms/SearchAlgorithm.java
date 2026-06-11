package Algorithms;

@FunctionalInterface
public interface SearchAlgorithm {
    SearchResult search(SearchRequest request);
}
