package ru.otus.hw021;

import java.util.function.Supplier;

@SuppressWarnings("MismatchedReadAndWriteOfArray")
class Benchmark {

    private int size;
    private int timeWait;
    private long memBefore;
    private long memAfter;
    Object[] array;

    Benchmark(int size, int timeWait) {
        this.size = size;
        this.timeWait = timeWait;
    }

    void setSize(int size) throws InterruptedException{
        this.size = size;
    }

    void run(Supplier<Object> supplier) throws InterruptedException {

        String type = supplier.get().getClass().getSimpleName();
        System.out.printf("type: %s%n", type);

        initArray();
        this.memBefore = getMem();

        for (int i = 0; i < this.array.length; i++) {
            this.array[i] = supplier.get();
        }

        this.memAfter = getMem();
        clearArray();
        measureAndOut();
    }

    private long getMem() throws InterruptedException {
        System.gc();
        Thread.sleep(this.timeWait);
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }

    private void measureAndOut() throws InterruptedException {
        long allSize = Math.round((double) (this.memAfter - this.memBefore));
        long allMbSize = Math.round((double) (allSize / 1024 / 1024));
        long elementSize = Math.round((double) (allSize) / size);
        System.out.printf("allSize: %,d b; %,d mb\n", allSize, allMbSize);
        System.out.printf("elementSize: %,d b\n", elementSize);
        System.out.println("-----------------");
    }

    private void initArray() throws InterruptedException {
        this.array = new Object[this.size];
    }

    private void clearArray() throws InterruptedException {
        this.array = null;
    }
}