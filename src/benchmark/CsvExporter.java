package benchmark;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class CsvExporter {
    private CsvExporter() {
    }

    public static void write(Path output, List<BenchmarkResult> results) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(output)) {
            writer.write(BenchmarkResult.csvHeader());
            writer.newLine();
            for (BenchmarkResult result : results) {
                writer.write(result.toCsvLine());
                writer.newLine();
            }
        }
    }
}
