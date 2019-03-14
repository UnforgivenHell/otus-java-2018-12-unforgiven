package ru.otus.Cache;

public class MyCacheMain {

    public static void main(String[] args) throws InterruptedException {
        int size = 20;
        int sleep = 200;

        MyCacheEngine<Integer, MyClassForTest> cacheEngine = new MyCacheEngineImpl<>(size, sleep);
        MyClassForTest element;

        for (int i = 0; i < cacheEngine.getMaxElements(); i++) {
            cacheEngine.put(i, new MyClassForTest(i));
        }


        for (int i = 0; i < cacheEngine.getMaxElements(); i++) {
            element = cacheEngine.get(i);
            System.out.println("String for " + i + ": " + (element != null ? element.getIntVar() : "null"));
        }
        System.out.println("-----------------------");

        for (int c = 0; c <= 2; c++) {
            for (int i = 0; i < cacheEngine.getMaxElements() / 2; i++) {
                int value = i * 2;
                element = cacheEngine.get(value);
                System.out.println("String for " + value + ": " + (element != null ? element.getIntVar() : "null"));
            }
            if (c == 1) {
                element = cacheEngine.get(cacheEngine.getMaxElements() - 1);
            }
            Thread.sleep(100);
        }
        System.out.println("-----------------------");

        for (int i = 0; i < cacheEngine.getMaxElements(); i++) {
            element = cacheEngine.get(i);
            System.out.println("String for " + i + ": " + (element != null ? element.getIntVar() : "null"));
        }
        System.out.println("-----------------------");

        System.out.println("Cache hits: " + cacheEngine.getHitCount());
        System.out.println("Cache misses: " + cacheEngine.getMissCount());

        cacheEngine.dispose();
    }

    private static void print(MyCacheEngine<Integer, MyClassForTest> cacheEngine) {
        for (int i = 0; i < cacheEngine.getMaxElements() / 2; i++) {
            MyClassForTest element = cacheEngine.get(i * 2);
            System.out.println("String for " + i + ": " + (element != null ? element.getIntVar() : "null"));
        }
    }

}
