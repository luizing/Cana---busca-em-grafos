import Algorithms.SearchAlgorithms;
import Algorithms.SearchRequest;
import Algorithms.SearchResult;
import Benchmark.RepetitionSearch;
import Benchmark.RequestGenerator;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException{

        //Para exemplo
        SearchRequest req = RequestGenerator.smallSparseWithSolution();
        System.out.println(req.toString());
        SearchResult res = SearchAlgorithms.breadthFirstSearch(req);
        System.out.println(res.toString());

        System.out.println("===");

        //Guardar metricas em csv
        RepetitionSearch.main(args);
        System.out.println("Dados salvos em benchmark-csv");
    }
}
