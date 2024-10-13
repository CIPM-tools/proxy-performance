package proxy.test.utility;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

import proxy.A;
import proxy.ProxyFactory;

public final class ModelGenerator {
	public static record ResourceCreationResult(Resource resource, A root) {};

	private static ResourceSet internalTestSet = new ResourceSetImpl();

    private ModelGenerator() {}

	public static void resetResourceSet() {
		internalTestSet = new ResourceSetImpl();
	}

	public static ResourceCreationResult createResource(URI uri) {
		var aResource = internalTestSet.createResource(uri);
		var a = ProxyFactory.eINSTANCE.createA();
		aResource.getContents().add(a);
		return new ResourceCreationResult(aResource, a);
    }

    public static A createExponentiallyGrowingModel(A root, int level) {
		var nextAOne = ProxyFactory.eINSTANCE.createA();
		root.setNoProxyCon(nextAOne);
		var nextATwo = ProxyFactory.eINSTANCE.createA();
		root.setProxyCon(nextATwo);
		if (level > 0) {
			var r = createExponentiallyGrowingModel(nextAOne, level - 1);
			createExponentiallyGrowingModel(nextATwo, level - 1);
			return r;
		} else {
			return nextAOne;
		}
	}

    public static long calculateNumberOfElements(int level) {
        long no = 1;
        for (int idx = 1; idx <= level + 1; idx++) {
            no += Math.pow(2, idx);
        }
        return no;
    }

	public static A createLinearModel(A root, int length) {
		var currentA = root;
		for (var idx = 0; idx < length; idx++) {
			var nextA = ProxyFactory.eINSTANCE.createA();
			currentA.setNoProxyCon(nextA);
			currentA = nextA;
		}
		return currentA;
	}
}
