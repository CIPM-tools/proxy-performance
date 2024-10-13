package proxy.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import proxy.ProxyFactory;
import proxy.test.data.Stats;
import proxy.test.utility.ModelGenerator;
import proxy.test.utility.ProxyResolutionUtility;
import proxy.test.utility.TestUtility;

public class UnresolvableResolutionExperiment {
    private static record UnresolvableUriResolutionResult(String uri, double[] times, Stats stats) {};

    @BeforeAll
    public static void initialize() {
        TestUtility.initialize();
    }

    @Test
	public void measureUnresolvableProxyResolution() throws IOException {
        System.out.println("Unresolvable Proxy Resolution");
        String output = "unresolvable";
		ProxyResolutionUtility.measureProxyResolution(true, output);

        var rounds = 2;
        UnresolvableUriResolutionResult[] unresolvableUriResults = new UnresolvableUriResolutionResult[rounds * 2];
		for (var idx = 0; idx < rounds; idx++) {
			unresolvableUriResults[idx * rounds] = measureUnresolvableProxyResolutionWithArbitraryUri(ProxyResolutionUtility.REPETITION_PROXY_RESOLUTION * 10, "models://B.json");
			unresolvableUriResults[idx * rounds + 1] = measureUnresolvableProxyResolutionWithArbitraryUri(ProxyResolutionUtility.REPETITION_PROXY_RESOLUTION * 10, "models://C.java#//@noProxyCon/@noProxyCon/@noProxyCon/@noProxyCon/@noProxyCon/@noProxyCon/@noProxyCon/@noProxyCon/@noProxyCon/@noProxyCon");
		}
        Files.writeString(TestUtility.OUTPUT_PATH.resolve(output).resolve("unresolvable-uri.json"), new Gson().toJson(unresolvableUriResults));
	}

    private UnresolvableUriResolutionResult measureUnresolvableProxyResolutionWithArbitraryUri(int repetitions, String uri) {
        var a = ModelGenerator.createResource(URI.createURI("model://un.xmi")).root();
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

		var stats = TestUtility.calculateStats(times);
		System.out.format("Unresolvable: %f ns (std. %f ns) for %s%n", stats.mean(), stats.std(), uri);

        return new UnresolvableUriResolutionResult(uri, times, stats);
	}
}
