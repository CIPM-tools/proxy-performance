package proxy.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.Histogram;
import org.knowm.xchart.VectorGraphicsEncoder;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
import org.knowm.xchart.VectorGraphicsEncoder.VectorGraphicsFormat;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.Styler.LegendPosition;

import com.google.gson.Gson;

import proxy.A;
import proxy.ProxyFactory;

public class ProxyTest {
	private static record ResolutionResult(int hierarchyLength, double[] times, long creationTime) {};
	private static record Stats(double mean, double std) {};
	private static record RegressionResult(double intercept, double slope, double rSquare) {};
	private static record NoProxyResolutionResult(double[] noProxyTimes, double[] proxyTimes, Stats noProxyStats, Stats proxyStats) {};
	private static record ProxyResolutionResult(ResolutionResult[] times, RegressionResult regression, boolean unresolvable) {};

	private static final Path OUTPUT_PATH = Paths.get("target");
	private static final int REPETITION_NO_PROXY_RESOLUTION = 10000000;
	private static final int REPETITION_PROXY_RESOLUTION = 10000;

	private Resource aResource;
	private A a;
	
	@BeforeAll
	public static void initialize() {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
	}

	@BeforeEach
	public void setUp() {
		var set = new ResourceSetImpl();
		aResource = set.createResource(URI.createURI("model://a.xmi"));
		a = ProxyFactory.eINSTANCE.createA();
		aResource.getContents().add(a);
	}

	@Test
	public void testNoProxyResolution() throws IOException {
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

		// Calculate descriptive statistics.
		var proxyStat = calculateStats(proxyMeasurements);
		var noProxyStat = calculateStats(noProxyMeasurements);
		System.out.format("With proxy resolution: %f ns (std. %f ns), Without proxy resolution: %f ns (std. %f ns)%n",
			proxyStat.mean(), proxyStat.std(), noProxyStat.mean(), noProxyStat.std());

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
		VectorGraphicsEncoder.saveVectorGraphic(chart, OUTPUT_PATH.resolve("no-proxy-resolution-histogram.pdf").toAbsolutePath().toString(), VectorGraphicsFormat.PDF);

		// Store the results.
		var result = new NoProxyResolutionResult(noProxyMeasurements, proxyMeasurements, noProxyStat, proxyStat);
		Files.writeString(OUTPUT_PATH.resolve("no-proxy-resolution.json"), new Gson().toJson(result));
		
		System.out.println();
	}

	@Test
	public void testDeepProxyResolution() throws IOException {
		executeProxyResolutionAndEvaluate(false, "deep-resolution");
		System.out.println();
	}

	@Test
	public void testUnresolvableProxyResolution() throws IOException {
		executeProxyResolutionAndEvaluate(true, "unresolvable");

		for (var idx = 0; idx < 2; idx++) {
			executeUnresolvableProxyResolutionWithArbitraryUri(REPETITION_PROXY_RESOLUTION*10, "models://B.json");
			executeUnresolvableProxyResolutionWithArbitraryUri(REPETITION_PROXY_RESOLUTION*10, "models://C.java#//@noProxyCon/@noProxyCon/@noProxyCon/@noProxyCon/@noProxyCon/@noProxyCon/@noProxyCon/@noProxyCon/@noProxyCon/@noProxyCon");
		}

		System.out.println();
	}

	private void executeProxyResolutionAndEvaluate(boolean unresolvable, String outputPrefix) throws IOException {
		int[] lengths = {0, 10, 100, 1000, 5000, 10000, 50000, 100000};

		ResolutionResult[] allTimes = new ResolutionResult[lengths.length];
		double[] maxValues = new double[lengths.length];

		// Prepare a simple linear regression.
		var regression = new SimpleRegression();
		// Prepare a scatter plot with all measurements.
		var allPointsScatter = new XYChartBuilder().title("Measurements (with Regression)").xAxisTitle("# Levels").yAxisTitle("Time (ns)").build();
		allPointsScatter.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Scatter).setLegendPosition(LegendPosition.InsideNE);

		// Perform the actual measurements.
		for (var j = 0; j < lengths.length; j++) {
			int hierarchyLength = lengths[j];
			allTimes[j] = executeDeepProxyResolution(hierarchyLength, REPETITION_PROXY_RESOLUTION, unresolvable);
			var times = allTimes[j].times();
			maxValues[j] = Arrays.stream(times).max().getAsDouble();
			
			// Fill the scatter plot with the current measurements.
			var xValues = new double[times.length];
			Arrays.fill(xValues, hierarchyLength);
			allPointsScatter.addSeries(hierarchyLength + " levels", xValues, times);

			// Update the regression.
			Arrays.stream(times).forEach(d -> regression.addData(hierarchyLength, d));

			// Calculate descriptive statistics.
			var stats = calculateStats(times);
			System.out.println("Deep resolution time (unresolvable: " + unresolvable + "): " + stats.mean() + " ns (std. " + stats.std() + " ns) for " + hierarchyLength + " levels");
		}

		System.out.format("Regression: y = %f * x + %f (%f)%n", regression.getSlope(), regression.getIntercept(), regression.getRSquare());

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
		VectorGraphicsEncoder.saveVectorGraphic(timeHistogram, OUTPUT_PATH.resolve(outputPrefix + "-histogram.pdf").toAbsolutePath().toString(), VectorGraphicsFormat.PDF);

		// Finish the scatter plot with all measurements and the regression line.
		double[] regY = new double[lengths.length];
		for (var idx = 0; idx < lengths.length; idx++) {
			regY[idx] = regression.getIntercept() + regression.getSlope() * (double) lengths[idx];
		}
		var regSeries = allPointsScatter.addSeries("Regression Line (r² = " + regression.getRSquare() + ")", Arrays.stream(lengths).mapToDouble(i -> i).toArray(), regY);
		regSeries.setXYSeriesRenderStyle(XYSeriesRenderStyle.Line);
		VectorGraphicsEncoder.saveVectorGraphic(allPointsScatter, OUTPUT_PATH.resolve(outputPrefix + "-scatter.pdf").toAbsolutePath().toString(), VectorGraphicsFormat.PDF);

		// Store the results.
		var result = new ProxyResolutionResult(allTimes, new RegressionResult(regression.getIntercept(), regression.getSlope(), regression.getRSquare()), unresolvable);
		Files.writeString(OUTPUT_PATH.resolve(outputPrefix + ".json"), new Gson().toJson(result));
	}

	private ResolutionResult executeDeepProxyResolution(int hierarchyLength, int repetitions, boolean unresolvable) {
		var millis = System.currentTimeMillis();
		var currentA = a;
		for (var idx = 0; idx < hierarchyLength; idx++) {
			var nextA = ProxyFactory.eINSTANCE.createA();
			currentA.setNoProxyCon(nextA);
			currentA = nextA;
		}
		millis = System.currentTimeMillis() - millis;
		System.out.println("Time for level creation: " + millis + " ms");

		var uri = EcoreUtil.getURI(currentA);
		if (unresolvable) {
			uri = uri.appendFragment(uri.fragment() + "a");
		}
		var proxyA = ProxyFactory.eINSTANCE.createA();
		((MinimalEObjectImpl) proxyA).eSetProxyURI(uri);

		double[] resolutionTimes = new double[repetitions];
		for (var idx = 0; idx < repetitions; idx++) {
			a.setProxyNoCon(proxyA);
			var nanos = System.nanoTime();
			var b = a.getProxyNoCon();
			resolutionTimes[idx] = System.nanoTime() - nanos;
			if (unresolvable) {
				assertTrue(b.eIsProxy());
			} else {
				assertEquals(currentA, b);
			}
		}

		return new ResolutionResult(hierarchyLength, resolutionTimes, millis);
	}

	private void executeUnresolvableProxyResolutionWithArbitraryUri(int repetitions, String uri) {
		var proxyA = ProxyFactory.eINSTANCE.createA();
		((MinimalEObjectImpl) proxyA).eSetProxyURI(URI.createURI(uri));
		a.setProxyNoCon(proxyA);

		var times = new double[repetitions];

		for (var idx = 0; idx < repetitions; idx++) {
			var nanos = System.nanoTime();
			var b = a.getProxyNoCon();
			times[idx] = System.nanoTime() - nanos;
			assertTrue(b.eIsProxy());
		}

		var stats = calculateStats(times);
		System.out.format("Unresolvable: %f ns (std. %f ns) for %s%n", stats.mean(), stats.std(), uri);
	}

	private Stats calculateStats(double[] values) {
		return new Stats(StatUtils.mean(values), Math.sqrt(StatUtils.variance(values)));
	}
}
