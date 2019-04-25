package ru.otus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;

import static java.lang.System.currentTimeMillis;

class MainClass {
    private static final int numberOfThreads = 4;
    private static final int size = 10_000;
    private static final int maxRandomValue = 100_000;

    public static void main(String[] args) {
        List<Integer> srcList = new ArrayList<>();

        for (int i=0; i < size; i++) {
            srcList.add(ThreadLocalRandom.current().nextInt(0, maxRandomValue));
        }

        System.out.println("initial array");
        System.out.println(srcList.stream()
                .map(Object::toString)
                .collect(Collectors.joining(",")));

        List<Integer> oneThreadList = new ArrayList<>(srcList);
        Collections.sort(oneThreadList);
        System.out.println("one thread sorted array");
        System.out.println(oneThreadList.stream()
                .map(Object::toString)
                .collect(Collectors.joining(",")));

        List<Integer> multiThreadList = SortFactory.getSortedArray(srcList, numberOfThreads);
        System.out.println("multi thread sorted array");
        System.out.println(multiThreadList.stream()
                .map(Object::toString)
                .collect(Collectors.joining(",")));

        System.out.println("Lists equal: " + CollectionUtils.isEqualCollection(oneThreadList, multiThreadList));
    }
}