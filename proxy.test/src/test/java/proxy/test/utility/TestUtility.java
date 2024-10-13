package proxy.test.utility;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

public final class TestUtility {
	private TestUtility() {}

	public static final Path OUTPUT_PATH = Paths.get("target");
	
	public static void initialize() {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
		ModelGenerator.resetResourceSet();
	}
}
