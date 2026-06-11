import benchmark.AlgorithmRegistry;
import benchmark.BenchmarkRunner;
import benchmark.BenchmarkScenario;
import benchmark.BenchmarkResult;
import benchmark.CsvExporter;
import benchmark.HtmlReportExporter;
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

        List<BenchmarkResult> results = runner.runAll();
        Path csvOutput = Path.of("benchmark-results.csv");
        Path reportOutput = Path.of("benchmark-report.html");

        CsvExporter.write(csvOutput, results);
        HtmlReportExporter.write(reportOutput, results);

        System.out.println("Benchmark finalizado.");
        System.out.println("CSV: " + csvOutput.toAbsolutePath());
        System.out.println("Grafico: " + reportOutput.toAbsolutePath());
    }
}
