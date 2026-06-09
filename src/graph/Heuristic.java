package graph;

@FunctionalInterface
public interface Heuristic {
    double estimate(int vertex);
}
