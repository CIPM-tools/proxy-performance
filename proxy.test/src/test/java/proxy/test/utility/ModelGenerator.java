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

	/**
	 * This method creates a model. The given level determines the model size, which is exponentially growing.
	 * This means that for a given level, level + 1 would result in a model, which contains 2^(level + 2) more elements
	 * than the model for level. For level == 0, the model contains three elements (the root element and two additional elements).
	 * 
	 * @param root The root element of the model.
	 * @param level The level for which an exponentially growing number of model elements is generated.
	 * @return One of the deepest elements within the generated model.
	 */
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

	/**
	 * For createExponentiallyGrowingModel, this method calculates the number of model elements that the
	 * generated model will contain.
	 * 
	 * @param level The level for which the number of model elements is calculated.
	 * @return The number of model elements.
	 */
    public static long calculateNumberOfElements(int level) {
        long no = 1;
        for (int idx = 1; idx <= level + 1; idx++) {
            no += Math.pow(2, idx);
        }
        return no;
    }

	/**
	 * This method creates a linear model. This means that the root element points to an element, which points to the next one,
	 * and so on. The length determines how many elements are created and chained in the model.
	 * 
	 * @param root The root element of the model.
	 * @param length The length of the model.
	 * @return The deepest element in the model.
	 */
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
