package proxy.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.math3.stat.StatUtils;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import proxy.A;
import proxy.ProxyFactory;

public class ProxyTest {
	private static record Stats(double mean, double std) {};

	private static final int REPETITION_NO_PROXY_RESOLUTION = 1000000;
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

		var proxyStat = calculateStats(proxyMeasurements);
		var noProxyStat = calculateStats(noProxyMeasurements);
		System.out.format("With proxy resolution: %f ns (std. %f ns), Without proxy resolution: %f ns (std. %f ns)%n",
			proxyStat.mean(), proxyStat.std(), noProxyStat.mean(), noProxyStat.std());
	}

	@ParameterizedTest
	@ValueSource(ints = {0, 10, 100, 1000, 5000, 10000, 50000})
	public void testDeepProxyResolution(int hierarchyLength) {
		var currentA = a;
		for (var idx = 0; idx < hierarchyLength; idx++) {
			var nextA = ProxyFactory.eINSTANCE.createA();
			currentA.setNoProxyCon(nextA);
			currentA = nextA;
		}

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

		var stats = calculateStats(resolutionTimes);
		System.out.println("Deep resolution time: " + stats.mean() + " ns (std. " + stats.std() + " ns) for " + hierarchyLength + " levels");
	}

	private Stats calculateStats(double[] values) {
		return new Stats(StatUtils.mean(values), Math.sqrt(StatUtils.variance(values)));
	}
}
