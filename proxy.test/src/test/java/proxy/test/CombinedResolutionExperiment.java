package proxy.test;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import proxy.ProxyFactory;
import proxy.test.data.ModelLoadResult;

public class CombinedResolutionExperiment {
    private static final int REPETITION_COMBINED_RESOLUTION = 100;

	@BeforeAll
	public static void initialize() {
		ProxyTest.initialize();
	}

    @Test
	public void measureCombinedProxyResolution() throws IOException {
        System.out.println("Combined proxy resolution");
		int[] levels = {0, 8, 15, 18};
		ModelLoadResult[] result = new ModelLoadResult[levels.length];
        var resolutionContainer = ModelGenerator.createResource(URI.createURI("model://ac.xmi"));

		for (int idx = 0; idx < levels.length; idx++) {
			long noElements = ModelGenerator.calculateNumberOfElements(levels[idx]);
			System.out.format("Starting with %d elements.%n", noElements);

			var resourceContainer = ModelGenerator.createResource(
                URI.createFileURI(ProxyTest.OUTPUT_PATH.resolve("ac-" + noElements + ".xmi").toString()));
			var lastElement = ModelGenerator.createExponentiallyGrowingModel(resourceContainer.root(), levels[idx]);

            var res = resourceContainer.resource();
			res.save(null);
			long fileSize = Files.size(Paths.get(res.getURI().toFileString()));

			var proxyA = ProxyFactory.eINSTANCE.createA();
            ((InternalEObject) proxyA).eSetProxyURI(EcoreUtil.getURI(lastElement));

			double[] times = new double[REPETITION_COMBINED_RESOLUTION];

			for (int repetition = 0; repetition < times.length; repetition++) {
				System.out.format("Repetition %d (%d elements) of combined resolution.%n", repetition, noElements);
				res.unload();
                resolutionContainer.root().setProxyNoCon(proxyA);

				long millis = System.currentTimeMillis();
				var resolved = resolutionContainer.root().getProxyNoCon();
				times[repetition] = System.currentTimeMillis() - millis;
                assertFalse(resolved.eIsProxy());
			}

			result[idx] = new ModelLoadResult(levels[idx], noElements, fileSize, times, ProxyTest.calculateStats(times));
		}

		Files.writeString(ProxyTest.OUTPUT_PATH.resolve("combined-resolution.json"), new Gson().toJson(result));
	}
}
