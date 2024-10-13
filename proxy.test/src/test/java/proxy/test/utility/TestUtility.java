package proxy.test.utility;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.math3.stat.StatUtils;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import proxy.test.data.Stats;

public final class TestUtility {
	private TestUtility() {}

	public static final Path OUTPUT_PATH = Paths.get("target");
	
	public static void initialize() {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
		ModelGenerator.resetResourceSet();
	}

	public static Stats calculateStats(double[] values) {
		return new Stats(StatUtils.mean(values), Math.sqrt(StatUtils.variance(values)));
	}
}
