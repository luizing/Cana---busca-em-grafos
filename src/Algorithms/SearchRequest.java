package Algorithms;

import graph.Graph;
import graph.Heuristic;

public record SearchRequest(
        Graph graph,
        int startVertex,
        int goalVertex,
        Heuristic heuristic
) {
    private static final int MAX_VERTICES_TO_PRINT = 20;
    private static final int MAX_EDGES_PER_VERTEX_TO_PRINT = 20;

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append("Requisicao de busca:\n")
                .append("  Quantidade de vertices: ").append(graph.vertexCount()).append('\n')
                .append("  Quantidade de arestas: ").append(graph.edgeCount()).append('\n')
                .append("  Vertice inicial: ").append(startVertex).append('\n')
                .append("  Vertice objetivo: ").append(goalVertex).append('\n')
                .append("  Heuristica informada: ").append(heuristic != null).append('\n')
                .append("  Grafo:\n");

        int verticesToPrint = Math.min(graph.vertexCount(), MAX_VERTICES_TO_PRINT);
        for (int vertex = 0; vertex < verticesToPrint; vertex++) {
            output.append("    ").append(vertex);
            if (heuristic != null) {
                output.append(" [h=").append(heuristic.estimate(vertex)).append(']');
            }
            output.append(" -> ");

            var neighbors = graph.neighborsOf(vertex);
            int edgesToPrint = Math.min(neighbors.size(), MAX_EDGES_PER_VERTEX_TO_PRINT);
            for (int edgeIndex = 0; edgeIndex < edgesToPrint; edgeIndex++) {
                var edge = neighbors.get(edgeIndex);
                if (edgeIndex > 0) {
                    output.append(", ");
                }
                output.append(edge.to()).append("(peso=").append(edge.weight()).append(')');
            }

            if (neighbors.size() > edgesToPrint) {
                output.append(", ... +")
                        .append(neighbors.size() - edgesToPrint)
                        .append(" arestas");
            } else if (neighbors.isEmpty()) {
                output.append("sem arestas");
            }
            output.append('\n');
        }

        if (graph.vertexCount() > verticesToPrint) {
            output.append("    ... +")
                    .append(graph.vertexCount() - verticesToPrint)
                    .append(" vertices\n");
        }

        return output.toString();
    }
}
