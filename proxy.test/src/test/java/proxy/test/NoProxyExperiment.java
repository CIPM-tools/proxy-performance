package proxy.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

import org.eclipse.emf.common.util.URI;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.Histogram;
import org.knowm.xchart.VectorGraphicsEncoder;
import org.knowm.xchart.VectorGraphicsEncoder.VectorGraphicsFormat;
import org.knowm.xchart.style.Styler.LegendPosition;

import com.google.gson.Gson;

import proxy.test.utility.ModelGenerator;
import proxy.test.utility.TestUtility;

/**
 * This experiment measures the performance of two getters for references: one with and one without a check for proxy objects.
 */
public class NoProxyExperiment {
	private static record NoProxyResolutionResult(double[] noProxyTimes, double[] proxyTimes) {};

	private static final int REPETITION_NO_PROXY_RESOLUTION = 10000000;

	@BeforeAll
	public static void initialize() {
		TestUtility.initialize();
	}

	@Test
	public void measureNoProxyResolution() throws IOException {
        System.out.println("No Proxy Resolution");

        var a = ModelGenerator.createResource(URI.createURI("model://a.xmi")).root();
		a.setNoProxyCon(a);
		double[] noProxyMeasurements = new double[REPETITION_NO_PROXY_RESOLUTION];
		a.setProxyNoCon(a);
		double[] proxyMeasurements = new double[REPETITION_NO_PROXY_RESOLUTION];

		// Perform the actual measurements.
		for (var idx = 0; idx < REPETITION_NO_PROXY_RESOLUTION; idx++) {
			long nanos = System.nanoTime();
			var b = a.getProxyNoCon();
			proxyMeasurements[idx] = System.nanoTime() - nanos;
			assertEquals(a, b);

			nanos = System.nanoTime();
			b = a.getNoProxyCon();
			noProxyMeasurements[idx] = System.nanoTime() - nanos;
			assertEquals(a, b);
		}

        // Store the results.
        var outputDir = TestUtility.OUTPUT_PATH.resolve("no-proxy");
        Files.createDirectories(outputDir);
        var result = new NoProxyResolutionResult(noProxyMeasurements, proxyMeasurements);
        Files.writeString(outputDir.resolve("no-proxy-resolution.json"), new Gson().toJson(result));

		// Create a histogram for the data.
		var noBins = 10;
		var max = 500;
		var hNoProxy = new Histogram(Arrays.stream(noProxyMeasurements).mapToObj(d -> d).toList(), noBins, 0, max);
		var hProxy = new Histogram(Arrays.stream(proxyMeasurements).mapToObj(d -> d).toList(), noBins, 0, max);
		var chart = new CategoryChartBuilder().title("Histogram").xAxisTitle("Time (ns)").yAxisTitle("# Measurements").build();
		chart.getStyler().setLabelsVisible(true).setLabelsRotation(90).setLabelsFontColorAutomaticEnabled(false);
		chart.getStyler().setLegendPosition(LegendPosition.InsideNE);
		chart.addSeries("No Proxy Resolution", hNoProxy.getxAxisData(), hNoProxy.getyAxisData()).setFillColor(new Color(0xaaffaa));
		chart.addSeries("Proxy Resolution", hProxy.getxAxisData(), hProxy.getyAxisData()).setFillColor(new Color(0xaaaaff));
		VectorGraphicsEncoder.saveVectorGraphic(chart, outputDir.resolve("no-proxy-histogram.pdf").toAbsolutePath().toString(), VectorGraphicsFormat.PDF);
	}
}
