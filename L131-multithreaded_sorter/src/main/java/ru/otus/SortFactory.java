package ru.otus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class SortFactory {
    private final int numberOfThreads;
    private final List<Integer>[] partitionList;

    private SortFactory (List<Integer> srcList, int numberOfThreads) {
        int parts = srcList.size() / numberOfThreads;
        this.numberOfThreads = numberOfThreads;
        partitionList = new ArrayList[parts];

        for (int i = 0; i <= numberOfThreads - 1; i++)
            partitionList[i] = new ArrayList<>(srcList.subList(i * parts, (i + 1) * parts));
    }

    public static List<Integer> getSortedArray (List<Integer> srcList, int numberOfThreads) throws InterruptedException {
        SortFactory factory = new SortFactory(srcList, numberOfThreads);
        return factory.runThreadsAndGetList();
    }

    private List<Integer> runThreadsAndGetList() throws InterruptedException {
        Thread[] threads = new Thread[numberOfThreads];

        for (int i = 0; i <= numberOfThreads - 1; i++) {
            threads[i] = new Thread(new SortThread(i));
            threads[i].start();
        }

        for (int i = 0; i < numberOfThreads; i++) {
            threads[i].join();
        }

        List<Integer> sortBuf = mergeArrays(partitionList[0], partitionList[1]);

        for (int i = 2; i < numberOfThreads ; i++) {
            sortBuf = mergeArrays(sortBuf, partitionList[i]);
        }

        return sortBuf;
    }

    private static List<Integer> mergeArrays(List<Integer> firstList,List<Integer> secondList) {
        List<Integer> result = new ArrayList<>( firstList.size() + secondList.size());

        int firstIndex = 0;
        int secondIndex = 0;

        while (firstIndex < firstList.size() && secondIndex < secondList.size()) {
            if (firstList.get(firstIndex) < secondList.get(secondIndex)) {
                result.add(firstList.get(firstIndex++));
            } else {
                result.add(secondList.get(secondIndex++));
            }
        }
        while (firstIndex < firstList.size()) {
            result.add(firstList.get(firstIndex++));
        }
        while (secondIndex < secondList.size()) {
            result.add(secondList.get(secondIndex++));
        }
        return result;
    }

    class SortThread implements Runnable {
        final int index;

        SortThread(int index) {
            this.index = index;
        }

        @Override
        public void run() {
            Collections.sort(partitionList[this.index]);
        }
    }
}