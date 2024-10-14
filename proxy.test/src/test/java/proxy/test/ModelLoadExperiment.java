package proxy.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.eclipse.emf.common.util.URI;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import proxy.test.data.ModelLoadResult;
import proxy.test.utility.ChartsUtility;
import proxy.test.utility.ModelGenerator;
import proxy.test.utility.TestUtility;

public class ModelLoadExperiment {
	private static final int REPETITION_MODEL_LOAD = 100;

	@BeforeAll
	public static void initialize() {
		TestUtility.initialize();
	}

    @Test
	public void measureModelLoad() throws IOException {
		System.out.println("Model Load");
		var outputDir = TestUtility.OUTPUT_PATH.resolve("model-load");
		Files.createDirectories(outputDir);
		int[] levels = {0, 8, 15, 18, 20};
		ModelLoadResult[] result = new ModelLoadResult[levels.length];

		for (int idx = 0; idx < levels.length; idx++) {
			long noElements = ModelGenerator.calculateNumberOfElements(levels[idx]);
			System.out.format("Starting with %d elements%n", noElements);

			var resourceContainer = ModelGenerator.createResource(
				URI.createFileURI(outputDir.resolve("a-" + noElements + ".xmi").toString()));
			ModelGenerator.createExponentiallyGrowingModel(resourceContainer.root(), levels[idx]);
			
			var res = resourceContainer.resource();
			res.save(null);
			long fileSize = Files.size(Paths.get(res.getURI().toFileString()));

			double[] times = new double[REPETITION_MODEL_LOAD];

			for (int repetition = 0; repetition < times.length; repetition++) {
				System.out.format("Repetition %d (%d elements)%n", repetition, noElements);
				res.unload();
				long nanos = System.nanoTime();
				res.load(null);
				times[repetition] = System.nanoTime() - nanos;
			}

			result[idx] = new ModelLoadResult(levels[idx], noElements, fileSize, times);
		}

		Files.writeString(outputDir.resolve("model-load.json"), new Gson().toJson(result));
		ChartsUtility.createChartsForModelLoadResult(result, outputDir);
	}
}
