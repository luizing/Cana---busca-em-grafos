package Algorithms;

import graph.Graph;
import graph.Heuristic;

public record SearchRequest(
        Graph graph,
        int startVertex,
        int goalVertex,
        Heuristic heuristic
) {
}
