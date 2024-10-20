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
import proxy.test.utility.ChartsUtility;
import proxy.test.utility.ModelGenerator;
import proxy.test.utility.TestUtility;

/**
 * This experiment measures the performance of a proxy object's resolution when a model file needs
 * to be loaded and the element is searched within the loaded model.
 */
public class CombinedResolutionExperiment {
    private static final int REPETITION_COMBINED_RESOLUTION = 100;

	@BeforeAll
	public static void initialize() {
		TestUtility.initialize();
	}

    @Test
	public void measureCombinedProxyResolution() throws IOException {
        System.out.println("Combined proxy resolution");
        var outputDir = TestUtility.OUTPUT_PATH.resolve("combined-resolution");
        Files.createDirectories(outputDir);
		int[] levels = {0, 8, 15, 18};
		ModelLoadResult[] result = new ModelLoadResult[levels.length];
        var resolutionContainer = ModelGenerator.createResource(URI.createURI("model://ac.xmi"));

		// Iterate over each level. A level determines the model size.
		for (int idx = 0; idx < levels.length; idx++) {
			long noElements = ModelGenerator.calculateNumberOfElements(levels[idx]);
			System.out.format("Starting with %d elements.%n", noElements);

			// First, create a new model, and save it.
			var resourceContainer = ModelGenerator.createResource(
                URI.createFileURI(outputDir.resolve("ac-" + noElements + ".xmi").toString()));
			var lastElement = ModelGenerator.createExponentiallyGrowingModel(resourceContainer.root(), levels[idx]);

            var res = resourceContainer.resource();
			res.save(null);
			long fileSize = Files.size(Paths.get(res.getURI().toFileString()));

			// Create a proxy object pointing to the deepest element within the previously created model.
			var proxyA = ProxyFactory.eINSTANCE.createA();
            ((InternalEObject) proxyA).eSetProxyURI(EcoreUtil.getURI(lastElement));

			double[] times = new double[REPETITION_COMBINED_RESOLUTION];

			// Perform the actual measurements.
			for (int repetition = 0; repetition < times.length; repetition++) {
				System.out.format("Repetition %d (%d elements) of combined resolution.%n", repetition, noElements);
				// Unload the model first. This ensures that it must be loaded again.
				res.unload();
                resolutionContainer.root().setProxyNoCon(proxyA);

				long nanos = System.nanoTime();
				// By accessing the proxy object, it is resolved
				// (i.e., the other model is loaded, and the element is searched within the loaded model).
				var resolved = resolutionContainer.root().getProxyNoCon();
				times[repetition] = System.nanoTime() - nanos;
                assertFalse(resolved.eIsProxy());
			}

			result[idx] = new ModelLoadResult(levels[idx], noElements, fileSize, times);
		}

		// Save the results.
		Files.writeString(outputDir.resolve("combined-resolution.json"), new Gson().toJson(result));
        ChartsUtility.createChartsForModelLoadResult(result, outputDir);
	}
}
