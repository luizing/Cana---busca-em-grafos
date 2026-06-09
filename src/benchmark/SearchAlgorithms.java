package benchmark;

import graph.Edge;
import graph.Graph;
import graph.Heuristic;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public final class SearchAlgorithms {
    private SearchAlgorithms() {
    }

    public static SearchResult breadthFirstSearch(SearchRequest request) {
        Graph graph = request.graph();
        int start = request.startVertex();
        int goal = request.goalVertex();

        boolean[] visited = new boolean[graph.vertexCount()];
        int[] parent = new int[graph.vertexCount()];
        double[] cost = new double[graph.vertexCount()];
        Queue<Integer> queue = new ArrayDeque<>();

        Arrays.fill(parent, -1);
        Arrays.fill(cost, Double.POSITIVE_INFINITY);

        visited[start] = true;
        cost[start] = 0.0;
        queue.add(start);

        int visitedNodes = 1;
        int expandedNodes = 0;

        if (start == goal) {
            return SearchResult.found(List.of(start), visitedNodes, expandedNodes, 0.0);
        }

        while (!queue.isEmpty()) {
            int current = queue.remove();
            expandedNodes++;

            for (Edge edge : graph.neighborsOf(current)) {
                int neighbor = edge.to();
                if (visited[neighbor]) {
                    continue;
                }

                visited[neighbor] = true;
                parent[neighbor] = current;
                cost[neighbor] = cost[current] + edge.weight();
                visitedNodes++;

                if (neighbor == goal) {
                    return SearchResult.found(
                            reconstructPath(parent, start, goal),
                            visitedNodes,
                            expandedNodes,
                            cost[goal]
                    );
                }

                queue.add(neighbor);
            }
        }

        return SearchResult.notFound(visitedNodes, expandedNodes, "BFS nao encontrou caminho ate o objetivo.");
    }

    private static List<Integer> reconstructPath(int[] parent, int start, int goal) {
        List<Integer> path = new ArrayList<>();
        int current = goal;

        while (current != -1) {
            path.add(current);
            if (current == start) {
                break;
            }
            current = parent[current];
        }

        Collections.reverse(path);
        return path;
    }

    public static SearchResult depthFirstSearch(SearchRequest request) {
        Graph graph = request.graph();
        int start = request.startVertex();
        int goal = request.goalVertex();

        boolean[] visited = new boolean[graph.vertexCount()];
        int[] parent = new int[graph.vertexCount()];
        double[] cost = new double[graph.vertexCount()];
        ArrayDeque<Integer> stack = new ArrayDeque<>();

        Arrays.fill(parent, -1);
        Arrays.fill(cost, Double.POSITIVE_INFINITY);

        visited[start] = true;
        cost[start] = 0.0;
        stack.push(start);

        int visitedNodes = 1;
        int expandedNodes = 0;

        if (start == goal) {
            return SearchResult.found(List.of(start), visitedNodes, expandedNodes, 0.0);
        }

        while (!stack.isEmpty()) {
            int current = stack.pop();
            expandedNodes++;

            List<Edge> neighbors = graph.neighborsOf(current);
            for (int i = neighbors.size() - 1; i >= 0; i--) {
                Edge edge = neighbors.get(i);
                int neighbor = edge.to();
                if (visited[neighbor]) {
                    continue;
                }

                visited[neighbor] = true;
                parent[neighbor] = current;
                cost[neighbor] = cost[current] + edge.weight();
                visitedNodes++;

                if (neighbor == goal) {
                    return SearchResult.found(
                            reconstructPath(parent, start, goal),
                            visitedNodes,
                            expandedNodes,
                            cost[goal]
                    );
                }

                stack.push(neighbor);
            }
        }

        return SearchResult.notFound(visitedNodes, expandedNodes, "DFS nao encontrou caminho ate o objetivo.");
    }

    public static SearchResult bestFirstSearch(SearchRequest request) {
        return prioritySearch(
                request,
                SearchMode.MARK_ON_INSERT,
                (vertex, pathCost, requestHeuristic) -> requestHeuristic.estimate(vertex),
                "Best-First Search nao encontrou caminho ate o objetivo."
        );
    }

    public static SearchResult greedySearch(SearchRequest request) {
        return prioritySearch(
                request,
                SearchMode.MARK_ON_INSERT,
                (vertex, pathCost, requestHeuristic) -> requestHeuristic.estimate(vertex),
                "Greedy Search nao encontrou caminho ate o objetivo."
        );
    }

    public static SearchResult aStarSearch(SearchRequest request) {
        return prioritySearch(
                request,
                SearchMode.ALLOW_BETTER_PATH,
                (vertex, pathCost, requestHeuristic) -> pathCost + requestHeuristic.estimate(vertex),
                "A-Star nao encontrou caminho ate o objetivo."
        );
    }

    public static SearchResult researchedAlgorithmOne(SearchRequest request) {
        return prioritySearch(
                request,
                SearchMode.ALLOW_BETTER_PATH,
                (vertex, pathCost, requestHeuristic) -> pathCost,
                "Busca de Custo Uniforme nao encontrou caminho ate o objetivo."
        );
    }

    public static SearchResult researchedAlgorithmTwo(SearchRequest request) {
        return prioritySearch(
                request,
                SearchMode.ALLOW_BETTER_PATH,
                (vertex, pathCost, requestHeuristic) -> pathCost,
                "Dijkstra nao encontrou caminho ate o objetivo."
        );
    }

    private static SearchResult prioritySearch(
            SearchRequest request,
            SearchMode mode,
            PriorityFunction priorityFunction,
            String notFoundMessage
    ) {
        Graph graph = request.graph();
        int start = request.startVertex();
        int goal = request.goalVertex();
        int vertices = graph.vertexCount();

        boolean[] visited = new boolean[vertices];
        boolean[] closed = new boolean[vertices];
        int[] parent = new int[vertices];
        double[] cost = new double[vertices];
        PriorityQueue<SearchNode> frontier = new PriorityQueue<>(
                Comparator.comparingDouble(SearchNode::priority)
                        .thenComparingInt(SearchNode::vertex)
        );

        Arrays.fill(parent, -1);
        Arrays.fill(cost, Double.POSITIVE_INFINITY);

        cost[start] = 0.0;
        visited[start] = true;
        Heuristic heuristic = request.heuristic() == null ? vertex -> 0.0 : request.heuristic();
        frontier.add(new SearchNode(start, priorityFunction.apply(start, 0.0, heuristic), 0.0));

        int visitedNodes = 1;
        int expandedNodes = 0;

        while (!frontier.isEmpty()) {
            SearchNode node = frontier.remove();
            int current = node.vertex();

            if (mode == SearchMode.ALLOW_BETTER_PATH && node.pathCost() > cost[current]) {
                continue;
            }
            if (closed[current]) {
                continue;
            }

            expandedNodes++;

            if (current == goal) {
                return SearchResult.found(
                        reconstructPath(parent, start, goal),
                        visitedNodes,
                        expandedNodes,
                        cost[goal]
                );
            }

            closed[current] = true;

            for (Edge edge : graph.neighborsOf(current)) {
                int neighbor = edge.to();
                double newCost = cost[current] + edge.weight();

                if (mode == SearchMode.MARK_ON_INSERT) {
                    if (visited[neighbor]) {
                        continue;
                    }

                    visited[neighbor] = true;
                    parent[neighbor] = current;
                    cost[neighbor] = newCost;
                    visitedNodes++;
                    frontier.add(new SearchNode(
                            neighbor,
                            priorityFunction.apply(neighbor, newCost, heuristic),
                            newCost
                    ));
                    continue;
                }

                if (closed[neighbor] || newCost >= cost[neighbor]) {
                    continue;
                }

                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    visitedNodes++;
                }
                parent[neighbor] = current;
                cost[neighbor] = newCost;
                frontier.add(new SearchNode(
                        neighbor,
                        priorityFunction.apply(neighbor, newCost, heuristic),
                        newCost
                ));
            }
        }

        return SearchResult.notFound(visitedNodes, expandedNodes, notFoundMessage);
    }

    private enum SearchMode {
        MARK_ON_INSERT,
        ALLOW_BETTER_PATH
    }

    @FunctionalInterface
    private interface PriorityFunction {
        double apply(int vertex, double pathCost, Heuristic heuristic);
    }

    private record SearchNode(int vertex, double priority, double pathCost) {
    }
}
