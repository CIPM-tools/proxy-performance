package proxy.test;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import proxy.test.utility.ProxyResolutionUtility;
import proxy.test.utility.TestUtility;

/**
 * This experiment measures the performance for searching an element within a model.
 */
public class DeepResolutionExperiment {
    @BeforeAll
    public static void initialize() {
        TestUtility.initialize();
    }

    @Test
	public void measureDeepProxyResolution() throws IOException {
        System.out.println("Deep Proxy Resolution");
		ProxyResolutionUtility.measureProxyResolution(false, "deep-resolution");
	}
}
