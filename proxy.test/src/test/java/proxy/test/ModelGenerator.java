package proxy.test;

import proxy.A;
import proxy.ProxyFactory;

public final class ModelGenerator {
    private ModelGenerator() {}

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

    public long calculateNumberOfElements(int level) {
        long no = 1;
        for (int idx = 1; idx <= level + 1; idx++) {
            no += Math.pow(2, idx);
        }
        return no;
    }
}
