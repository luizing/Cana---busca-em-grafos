import benchmark.AlgorithmRegistry;
import benchmark.BenchmarkRunner;
import benchmark.BenchmarkScenario;
import benchmark.CsvExporter;
import graph.GraphGenerator;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        List<BenchmarkScenario> scenarios = GraphGenerator.defaultScenarios();

        BenchmarkRunner runner = new BenchmarkRunner(
                AlgorithmRegistry.defaultAlgorithms(),
                scenarios,
                3
        );

        Path output = Path.of("benchmark-results.csv");
        CsvExporter.write(output, runner.runAll());

        System.out.println("Benchmark finalizado. Resultados em: " + output.toAbsolutePath());
    }
}
