package proxy.test.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
import org.knowm.xchart.Histogram;
import org.knowm.xchart.VectorGraphicsEncoder;
import org.knowm.xchart.VectorGraphicsEncoder.VectorGraphicsFormat;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.Styler.LegendPosition;

import com.google.gson.Gson;

import proxy.A;
import proxy.ProxyFactory;
import proxy.test.data.MinMax;
import proxy.test.data.ProxyResolutionResult;
import proxy.test.data.ResolutionResult;

public final class ProxyResolutionUtility {
    private ProxyResolutionUtility() {}

    public static final int REPETITION_PROXY_RESOLUTION = 10000;

    public static void measureProxyResolution(boolean unresolvable, String outputPrefix) throws IOException {
        var rootA = ModelGenerator.createResource(URI.createURI("model://a.xmi")).root();
        var outputDir = TestUtility.OUTPUT_PATH.resolve(outputPrefix);
        Files.createDirectories(outputDir);
		int[] lengths = {0, 2, 10, 17, 20, 100, 1000, 5000, 10000, 50000, 100000};

		ResolutionResult[] allTimes = new ResolutionResult[lengths.length];
		double[] maxValues = new double[lengths.length];

		// Prepare a scatter plot with all measurements.
		var allPointsScatter = new XYChartBuilder().title("Measurements (with Regression)").xAxisTitle("# Levels").yAxisTitle("Time (ns)").build();
		allPointsScatter.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Scatter).setLegendPosition(LegendPosition.InsideNE);

		// Perform the actual measurements.
		for (var j = 0; j < lengths.length; j++) {
			int hierarchyLength = lengths[j];
			allTimes[j] = executeAndMeasureProxyResolution(rootA, hierarchyLength, REPETITION_PROXY_RESOLUTION, unresolvable);
			var times = allTimes[j].times();
			maxValues[j] = Arrays.stream(times).max().getAsDouble();
			
			// Fill the scatter plot with the current measurements.
			var xValues = new double[times.length];
			Arrays.fill(xValues, hierarchyLength);
			allPointsScatter.addSeries(hierarchyLength + " levels", xValues, times);
		}

		// Create a histogram for all measurements.
		var timeHistogram = new CategoryChartBuilder().title("Histogram").xAxisTitle("Time (ms)").yAxisTitle("# Measurements").build();
		timeHistogram.getStyler().setDefaultSeriesRenderStyle(CategorySeriesRenderStyle.Line).setOverlapped(true).setLegendPosition(LegendPosition.InsideNE);
		timeHistogram.getStyler().setxAxisTickLabelsFormattingFunction((d) -> {
			return String.format("%.2f", d / 1000000);
		});
		var histogramMax = Arrays.stream(maxValues).max().getAsDouble() * 0.1;
		for (var idx = 0; idx < lengths.length; idx++) {
			var h = new Histogram(Arrays.stream(allTimes[idx].times()).mapToObj(d -> d).toList(), 20, 0, histogramMax);
			timeHistogram.addSeries(lengths[idx] + " levels", h.getxAxisData(), h.getyAxisData());
		}
		VectorGraphicsEncoder.saveVectorGraphic(timeHistogram, outputDir.resolve(outputPrefix + "-histogram.pdf").toAbsolutePath().toString(), VectorGraphicsFormat.PDF);

		// Finish the scatter plot with all measurements.
		VectorGraphicsEncoder.saveVectorGraphic(allPointsScatter, outputDir.resolve(outputPrefix + "-scatter.pdf").toAbsolutePath().toString(), VectorGraphicsFormat.PDF);

		// Store the results.
		var result = new ProxyResolutionResult(allTimes, unresolvable);
		Files.writeString(outputDir.resolve(outputPrefix + ".json"), new Gson().toJson(result));
	}

	private static ResolutionResult executeAndMeasureProxyResolution(A root, int hierarchyLength, int repetitions, boolean unresolvable) {
		var millis = System.currentTimeMillis();
		var currentA = ModelGenerator.createLinearModel(root, hierarchyLength);
		millis = System.currentTimeMillis() - millis;
		System.out.format("Time for model creation (%d): %d ms%n", hierarchyLength, millis);

		var uri = EcoreUtil.getURI(currentA);
		if (unresolvable) {
			uri = uri.appendFragment(uri.fragment() + "a");
		}
		var proxyA = ProxyFactory.eINSTANCE.createA();
		((MinimalEObjectImpl) proxyA).eSetProxyURI(uri);

		double[] resolutionTimes = new double[repetitions];
		for (var idx = 0; idx < repetitions; idx++) {
			root.setProxyNoCon(proxyA);
			var nanos = System.nanoTime();
			var b = root.getProxyNoCon();
			resolutionTimes[idx] = System.nanoTime() - nanos;
			if (unresolvable) {
				assertTrue(b.eIsProxy());
			} else {
				assertEquals(currentA, b);
			}
		}

		return new ResolutionResult(hierarchyLength, resolutionTimes, millis, calculateMinMax(resolutionTimes));
	}

	private static MinMax calculateMinMax(double[] data) {
		return new MinMax(Arrays.stream(data).min().getAsDouble(), Arrays.stream(data).max().getAsDouble());
	}
}
