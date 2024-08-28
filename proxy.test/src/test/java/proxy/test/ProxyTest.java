package proxy.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;
import java.util.Arrays;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.inference.TestUtils;
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
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.Histogram;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.Styler.LegendPosition;

import proxy.A;
import proxy.ProxyFactory;

public class ProxyTest {
	private static record Stats(double mean, double std) {};

	private static final int REPETITION_NO_PROXY_RESOLUTION = 10000000;
	private static final int REPETITION_DEEP_PROXY_RESOLUTION = 10000;

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
	public void testNoProxyResolution() {
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

		// Perform a KS test.
		var pKS = TestUtils.kolmogorovSmirnovStatistic(noProxyMeasurements, proxyMeasurements);
		System.out.println("No proxy / proxy KS p value: " + pKS);

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
		new SwingWrapper<CategoryChart>(chart).displayChart();
		
		System.out.println();
	}

	@Test
	public void testDeepProxyResolution() {
		int[] lengths = {0, 10, 100, 1000, 5000, 10000, 50000, 100000};

		double[][] allTimes = new double[lengths.length][];
		double[] maxValues = new double[lengths.length];

		// Prepare a simple linear regression.
		var regression = new SimpleRegression();
		// Prepare a scatter plot with all measurements.
		var allPointsScatter = new XYChartBuilder().title("Measurements (with Regression)").xAxisTitle("# Levels").yAxisTitle("Time (ns)").build();
		allPointsScatter.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Scatter).setLegendPosition(LegendPosition.InsideNE);

		// Perform the actual measurements.
		for (var j = 0; j < lengths.length; j++) {
			int hierarchyLength = lengths[j];
			allTimes[j] = executeDeepProxyResolution(hierarchyLength, REPETITION_DEEP_PROXY_RESOLUTION);
			maxValues[j] = Arrays.stream(allTimes[j]).max().getAsDouble();
			
			// Fill the scatter plot with the current measurements.
			var xValues = new double[allTimes[j].length];
			Arrays.fill(xValues, hierarchyLength);
			allPointsScatter.addSeries(hierarchyLength + " levels", xValues, allTimes[j]);

			// Update the regression.
			Arrays.stream(allTimes[j]).forEach(d -> regression.addData(hierarchyLength, d));

			// Calculate descriptive statistics.
			var stats = calculateStats(allTimes[j]);
			System.out.println("Deep resolution time: " + stats.mean() + " ns (std. " + stats.std() + " ns) for " + hierarchyLength + " levels");
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
			var h = new Histogram(Arrays.stream(allTimes[idx]).mapToObj(d -> d).toList(), 20, 0, histogramMax);
			timeHistogram.addSeries(lengths[idx] + " levels", h.getxAxisData(), h.getyAxisData());
		}
		new SwingWrapper<CategoryChart>(timeHistogram).displayChart();

		// Finish the scatter plot with all measurements and the regression line.
		double[] regY = new double[lengths.length];
		for (var idx = 0; idx < lengths.length; idx++) {
			regY[idx] = regression.getIntercept() + regression.getSlope() * (double) lengths[idx];
		}
		var regSeries = allPointsScatter.addSeries("Regression Line (r² = " + regression.getRSquare() + ")", Arrays.stream(lengths).mapToDouble(i -> i).toArray(), regY);
		regSeries.setXYSeriesRenderStyle(XYSeriesRenderStyle.Line);
		new SwingWrapper<XYChart>(allPointsScatter).displayChart();

		System.out.println();
	}

	private double[] executeDeepProxyResolution(int hierarchyLength, int repetitions) {
		var millis = System.currentTimeMillis();
		var currentA = a;
		for (var idx = 0; idx < hierarchyLength; idx++) {
			var nextA = ProxyFactory.eINSTANCE.createA();
			currentA.setNoProxyCon(nextA);
			currentA = nextA;
		}
		System.out.println("Time for level creation: " + (System.currentTimeMillis() - millis) + " ms");

		var uri = EcoreUtil.getURI(currentA);
		var proxyA = ProxyFactory.eINSTANCE.createA();
		((MinimalEObjectImpl) proxyA).eSetProxyURI(uri);

		double[] resolutionTimes = new double[REPETITION_DEEP_PROXY_RESOLUTION];
		for (var idx = 0; idx < REPETITION_DEEP_PROXY_RESOLUTION; idx++) {
			a.setProxyNoCon(proxyA);
			var nanos = System.nanoTime();
			var b = a.getProxyNoCon();
			resolutionTimes[idx] = System.nanoTime() - nanos;
			assertEquals(currentA, b);
		}

		return resolutionTimes;
	}

	private Stats calculateStats(double[] values) {
		return new Stats(StatUtils.mean(values), Math.sqrt(StatUtils.variance(values)));
	}
}
