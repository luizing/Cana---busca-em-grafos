package Algorithms;

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
        Graph graph = request.graph();
        int start = request.startVertex();
        int goal = request.goalVertex();
        Heuristic heuristic = request.heuristic() == null ? vertex -> 0.0 : request.heuristic();

        boolean[] visited = new boolean[graph.vertexCount()];
        int[] parent = new int[graph.vertexCount()];
        double[] cost = new double[graph.vertexCount()];
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(
                Comparator.comparingDouble(heuristic::estimate)
                        .thenComparingInt(Integer::intValue)
        );

        Arrays.fill(parent, -1);
        Arrays.fill(cost, Double.POSITIVE_INFINITY);

        visited[start] = true;
        cost[start] = 0.0;
        priorityQueue.add(start);

        int visitedNodes = 1;
        int expandedNodes = 0;

        if (start == goal) {
            return SearchResult.found(List.of(start), visitedNodes, expandedNodes, 0.0);
        }

        while (!priorityQueue.isEmpty()) {
            int current = priorityQueue.remove();
            expandedNodes++;

            if (current == goal) {
                return SearchResult.found(
                        reconstructPath(parent, start, goal),
                        visitedNodes,
                        expandedNodes,
                        cost[goal]
                );
            }

            for (Edge edge : graph.neighborsOf(current)) {
                int neighbor = edge.to();
                if (visited[neighbor]) {
                    continue;
                }

                visited[neighbor] = true;
                parent[neighbor] = current;
                cost[neighbor] = cost[current] + edge.weight();
                visitedNodes++;
                priorityQueue.add(neighbor);
            }
        }

        return SearchResult.notFound(
                visitedNodes,
                expandedNodes,
                "Best-First Search nao encontrou caminho ate o objetivo."
        );
    }

    public static SearchResult greedySearch(SearchRequest request) {
        Graph graph = request.graph();
        int start = request.startVertex();
        int goal = request.goalVertex();
        Heuristic heuristic = request.heuristic() == null ? vertex -> 0.0 : request.heuristic();

        boolean[] visited = new boolean[graph.vertexCount()];
        int[] parent = new int[graph.vertexCount()];
        double[] cost = new double[graph.vertexCount()];
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(
                Comparator.comparingDouble(heuristic::estimate)
                        .thenComparingInt(Integer::intValue)
        );

        Arrays.fill(parent, -1);
        Arrays.fill(cost, Double.POSITIVE_INFINITY);

        visited[start] = true;
        cost[start] = 0.0;
        priorityQueue.add(start);

        int visitedNodes = 1;
        int expandedNodes = 0;

        if (start == goal) {
            return SearchResult.found(List.of(start), visitedNodes, expandedNodes, 0.0);
        }

        while (!priorityQueue.isEmpty()) {
            int current = priorityQueue.remove();
            expandedNodes++;

            if (current == goal) {
                return SearchResult.found(
                        reconstructPath(parent, start, goal),
                        visitedNodes,
                        expandedNodes,
                        cost[goal]
                );
            }

            for (Edge edge : graph.neighborsOf(current)) {
                int neighbor = edge.to();
                if (visited[neighbor]) {
                    continue;
                }

                visited[neighbor] = true;
                parent[neighbor] = current;
                cost[neighbor] = cost[current] + edge.weight();
                visitedNodes++;
                priorityQueue.add(neighbor);
            }
        }

        return SearchResult.notFound(
                visitedNodes,
                expandedNodes,
                "Greedy Search nao encontrou caminho ate o objetivo."
        );
    }

    public static SearchResult aStarSearch(SearchRequest request) {
        Graph graph = request.graph();
        int start = request.startVertex();
        int goal = request.goalVertex();
        Heuristic heuristic = request.heuristic() == null ? vertex -> 0.0 : request.heuristic();

        boolean[] visited = new boolean[graph.vertexCount()];
        boolean[] closed = new boolean[graph.vertexCount()];
        int[] parent = new int[graph.vertexCount()];
        double[] cost = new double[graph.vertexCount()];
        PriorityQueue<AStarNode> priorityQueue = new PriorityQueue<>(
                Comparator.comparingDouble(AStarNode::priority)
                        .thenComparingDouble(AStarNode::cost)
                        .thenComparingInt(AStarNode::vertex)
        );

        Arrays.fill(parent, -1);
        Arrays.fill(cost, Double.POSITIVE_INFINITY);

        visited[start] = true;
        cost[start] = 0.0;
        priorityQueue.add(new AStarNode(start, 0.0, heuristic.estimate(start)));

        int visitedNodes = 1;
        int expandedNodes = 0;

        if (start == goal) {
            return SearchResult.found(List.of(start), visitedNodes, expandedNodes, 0.0);
        }

        while (!priorityQueue.isEmpty()) {
            AStarNode node = priorityQueue.remove();
            int current = node.vertex();

            if (node.cost() > cost[current] || closed[current]) {
                continue;
            }

            closed[current] = true;
            expandedNodes++;

            if (current == goal) {
                return SearchResult.found(
                        reconstructPath(parent, start, goal),
                        visitedNodes,
                        expandedNodes,
                        cost[goal]
                );
            }

            for (Edge edge : graph.neighborsOf(current)) {
                int neighbor = edge.to();
                double newCost = cost[current] + edge.weight();

                if (newCost >= cost[neighbor]) {
                    continue;
                }

                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    visitedNodes++;
                }

                closed[neighbor] = false;
                parent[neighbor] = current;
                cost[neighbor] = newCost;
                priorityQueue.add(new AStarNode(
                        neighbor,
                        newCost,
                        newCost + heuristic.estimate(neighbor)
                ));
            }
        }

        return SearchResult.notFound(
                visitedNodes,
                expandedNodes,
                "A-Star nao encontrou caminho ate o objetivo."
        );
    }

    public static SearchResult uniformCost(SearchRequest request) {
        Graph graph = request.graph();
        int start = request.startVertex();
        int goal = request.goalVertex();

        boolean[] visited = new boolean[graph.vertexCount()];
        boolean[] closed = new boolean[graph.vertexCount()];
        int[] parent = new int[graph.vertexCount()];
        double[] cost = new double[graph.vertexCount()];
        PriorityQueue<UniformCostNode> priorityQueue = new PriorityQueue<>(
                Comparator.comparingDouble(UniformCostNode::cost)
                        .thenComparingInt(UniformCostNode::vertex)
        );

        Arrays.fill(parent, -1);
        Arrays.fill(cost, Double.POSITIVE_INFINITY);

        visited[start] = true;
        cost[start] = 0.0;
        priorityQueue.add(new UniformCostNode(start, 0.0));

        int visitedNodes = 1;
        int expandedNodes = 0;

        if (start == goal) {
            return SearchResult.found(List.of(start), visitedNodes, expandedNodes, 0.0);
        }

        while (!priorityQueue.isEmpty()) {
            UniformCostNode node = priorityQueue.remove();
            int current = node.vertex();

            if (closed[current] || node.cost() > cost[current]) {
                continue;
            }

            closed[current] = true;
            expandedNodes++;

            if (current == goal) {
                return SearchResult.found(
                        reconstructPath(parent, start, goal),
                        visitedNodes,
                        expandedNodes,
                        cost[goal]
                );
            }

            for (Edge edge : graph.neighborsOf(current)) {
                int neighbor = edge.to();
                double newCost = cost[current] + edge.weight();

                if (closed[neighbor] || newCost >= cost[neighbor]) {
                    continue;
                }

                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    visitedNodes++;
                }

                parent[neighbor] = current;
                cost[neighbor] = newCost;
                priorityQueue.add(new UniformCostNode(neighbor, newCost));
            }
        }

        return SearchResult.notFound(
                visitedNodes,
                expandedNodes,
                "Busca de Custo Uniforme nao encontrou caminho ate o objetivo."
        );
    }

    public static SearchResult dijkstra(SearchRequest request) {
        Graph graph = request.graph();
        int start = request.startVertex();
        int goal = request.goalVertex();

        boolean[] visited = new boolean[graph.vertexCount()];
        boolean[] closed = new boolean[graph.vertexCount()];
        int[] parent = new int[graph.vertexCount()];
        double[] cost = new double[graph.vertexCount()];
        PriorityQueue<DijkstraNode> priorityQueue = new PriorityQueue<>(
                Comparator.comparingDouble(DijkstraNode::cost)
                        .thenComparingInt(DijkstraNode::vertex)
        );

        Arrays.fill(parent, -1);
        Arrays.fill(cost, Double.POSITIVE_INFINITY);

        visited[start] = true;
        cost[start] = 0.0;
        priorityQueue.add(new DijkstraNode(start, 0.0));

        int visitedNodes = 1;
        int expandedNodes = 0;

        if (start == goal) {
            return SearchResult.found(List.of(start), visitedNodes, expandedNodes, 0.0);
        }

        while (!priorityQueue.isEmpty()) {
            DijkstraNode node = priorityQueue.remove();
            int current = node.vertex();

            if (closed[current] || node.cost() > cost[current]) {
                continue;
            }

            closed[current] = true;
            expandedNodes++;

            if (current == goal) {
                return SearchResult.found(
                        reconstructPath(parent, start, goal),
                        visitedNodes,
                        expandedNodes,
                        cost[goal]
                );
            }

            for (Edge edge : graph.neighborsOf(current)) {
                int neighbor = edge.to();
                double newCost = cost[current] + edge.weight();

                if (closed[neighbor] || newCost >= cost[neighbor]) {
                    continue;
                }

                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    visitedNodes++;
                }

                parent[neighbor] = current;
                cost[neighbor] = newCost;
                priorityQueue.add(new DijkstraNode(neighbor, newCost));
            }
        }

        return SearchResult.notFound(
                visitedNodes,
                expandedNodes,
                "Dijkstra nao encontrou caminho ate o objetivo."
        );
    }

    private record AStarNode(int vertex, double cost, double priority) {
    }

    private record UniformCostNode(int vertex, double cost) {
    }

    private record DijkstraNode(int vertex, double cost) //to acumulated cost
    {}

}
