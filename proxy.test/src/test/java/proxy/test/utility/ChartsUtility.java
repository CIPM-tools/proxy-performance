package proxy.test.utility;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.knowm.xchart.VectorGraphicsEncoder;
import org.knowm.xchart.VectorGraphicsEncoder.VectorGraphicsFormat;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.Styler.LegendPosition;

import proxy.test.data.ModelLoadResult;

public final class ChartsUtility {
    private ChartsUtility() {}
    
    public static void createChartsForModelLoadResult(ModelLoadResult[] results, Path rootOutput) throws IOException {
        var xValues = Stream.of(results).mapToDouble((r) -> r.noLevels()).toArray();

        var chart = new XYChartBuilder().title("Sizes").xAxisTitle("# Levels").build();
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Line).setLegendPosition(LegendPosition.InsideNW);
        chart.addSeries("Model File Size (kB)", xValues, Stream.of(results).mapToDouble((r) -> r.modelSize() / 1024).toArray());
        chart.addSeries("# Model Elements", xValues, Stream.of(results).mapToDouble((r) -> r.noElements()).toArray());
        VectorGraphicsEncoder.saveVectorGraphic(chart, rootOutput.resolve("sizes.pdf").toAbsolutePath().toString(),
            VectorGraphicsFormat.PDF);

        chart = new XYChartBuilder().title("Time").xAxisTitle("# Levels").yAxisTitle("Average Time (ms)").build();
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Line).setLegendVisible(false);
        chart.addSeries("Average Time", xValues, Stream.of(results).mapToDouble((r) -> r.stats().mean()).toArray());
        VectorGraphicsEncoder.saveVectorGraphic(chart, rootOutput.resolve("times.pdf").toAbsolutePath().toString(),
            VectorGraphicsFormat.PDF);
    }
}
