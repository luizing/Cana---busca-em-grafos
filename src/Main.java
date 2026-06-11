import Algorithms.SearchAlgorithms;
import Algorithms.SearchRequest;
import Algorithms.SearchResult;
import Benchmark.RepetitionSearch;
import Benchmark.RequestGenerator;
import graph.Graph;
import graph.Heuristic;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException{

        // Cidades Exemplo
        Graph mapa = new Graph(4);
        mapa.addUndirectedEdge(0,1,10);
        mapa.addUndirectedEdge(0,2,5);
        mapa.addUndirectedEdge(0,3,15);
        mapa.addUndirectedEdge(1,2,2);
        mapa.addUndirectedEdge(2,3,3);
        Heuristic heuristic = vertex -> {
        return 0;
        };

        SearchRequest cidade = new SearchRequest(mapa, 0 , 3, heuristic); //0=a,1=b...
        System.out.println(cidade.toString());
        SearchResult busca = SearchAlgorithms.aStarSearch(cidade);
        System.out.println(busca.toString());

        System.out.println("===");

        // Outro exemplo
        SearchRequest req = RequestGenerator.smallSparseWithoutSolution();
        System.out.println(req.toString());
        SearchResult res = SearchAlgorithms.breadthFirstSearch(req);
        System.out.println(res.toString());

        System.out.println("===");

        //Guardar metricas em csv
        RepetitionSearch.main(args);
        System.out.println("Dados salvos em benchmark-csv");
    }
}
