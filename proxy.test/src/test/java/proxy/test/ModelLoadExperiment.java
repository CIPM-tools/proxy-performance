package proxy.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.eclipse.emf.common.util.URI;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import proxy.test.data.ModelLoadResult;

public class ModelLoadExperiment {
	private static final int REPETITION_MODEL_LOAD = 100;

	@BeforeAll
	public static void initialize() {
		ProxyTest.initialize();
	}

    @Test
	public void measureModelLoad() throws IOException {
		int[] levels = {0, 15, 18, 20};
		ModelLoadResult[] result = new ModelLoadResult[levels.length];

		for (int idx = 0; idx < levels.length; idx++) {
			long noElements = ModelGenerator.calculateNumberOfElements(levels[idx]);
			System.out.format("Starting with %d elements%n", noElements);

			var resourceContainer = ModelGenerator.createResource(URI.createFileURI(ProxyTest.OUTPUT_PATH.resolve("a-" + noElements + ".xmi").toAbsolutePath().toString()));
			ModelGenerator.createExponentiallyGrowingModel(resourceContainer.root(), levels[idx]);
			
			var res = resourceContainer.resource();
			res.save(null);
			long fileSize = Files.size(Paths.get(res.getURI().toFileString()));

			double[] times = new double[REPETITION_MODEL_LOAD];

			for (int repetition = 0; repetition < times.length; repetition++) {
				System.out.format("Repetition %d (%d elements)%n", repetition, noElements);
				res.unload();
				long millis = System.currentTimeMillis();
				res.load(null);
				times[repetition] = System.currentTimeMillis() - millis;
			}

			result[idx] = new ModelLoadResult(levels[idx], noElements, fileSize, times, ProxyTest.calculateStats(times));
		}

		Files.writeString(ProxyTest.OUTPUT_PATH.resolve("model-load.json"), new Gson().toJson(result));
	}
}
