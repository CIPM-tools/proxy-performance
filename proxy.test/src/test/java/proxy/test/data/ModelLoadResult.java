package proxy.test.data;

import proxy.test.ProxyTest.Stats;

public record ModelLoadResult(int noLevels, long noElements, long modelSize, double[] times, Stats stats) {
}
