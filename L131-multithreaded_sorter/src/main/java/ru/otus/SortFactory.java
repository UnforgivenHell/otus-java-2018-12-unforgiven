package ru.otus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class SortFactory {
    private final int numberOfThreads;
    private final List<Integer>[] partitionList;
    private final List<Integer> sortBuf;
    private final Monitor monitor = new Monitor();

    class Monitor {
        private int index = -1;
        private boolean transfer = false;
        private int counter = 0;

        void mergeArray() {
            System.out.println("merged " + this.index);
            List<Integer> mergeList = SortFactory.mergeArrays(partitionList[index], sortBuf);
            sortBuf.clear();
            sortBuf.addAll( mergeList );
            counter++;
        }
    }

    private SortFactory (List<Integer> srcList, int numberOfThreads) {
        int parts = srcList.size() / numberOfThreads;
        this.numberOfThreads = numberOfThreads;
        sortBuf = new ArrayList<>();
        partitionList = new ArrayList[parts];

        for (int i = 0; i <= numberOfThreads - 1; i++)
            partitionList[i] = new ArrayList<>(srcList.subList(i * parts, (i + 1) * parts));
    }

    public static List<Integer> getSortedArray (List<Integer> srcList, int numberOfThreads) {
        SortFactory factory = new SortFactory(srcList, numberOfThreads);
        return factory.runThreadsAndGetList();
    }

    private List<Integer> runThreadsAndGetList() {
        Thread mergeThread = new Thread(new MergeThread( numberOfThreads ));
        mergeThread.start();

        for (int i = 0; i <= numberOfThreads - 1; i++) {
            new Thread(new SortThread(i)).start();
        }

        while(mergeThread.isAlive()) {
            // do nothing, still merging
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

    class MergeThread implements Runnable {
        final int totalThreads;
        MergeThread(int totalThreads) {
            this.totalThreads = totalThreads;
        }

        @Override
        public void run() {
            synchronized (monitor) {
                   while  (true) {
                       System.out.println("wait for merge");
                       if (!monitor.transfer) {
                           try {
                               monitor.wait();
                           } catch (InterruptedException e) {
                               e.printStackTrace();
                           }
                       }

                       monitor.mergeArray( );

                       if (monitor.counter >= this.totalThreads) {
                           System.out.println("all threads processed");
                           break;
                       }

                       monitor.transfer = false;
                       monitor.notifyAll();
                   }
            }
        }
    }

    class SortThread implements Runnable {
        final int index;

        SortThread(int index) {
            this.index = index;
        }

        @Override
        public void run() {
            Collections.sort(partitionList[index]);
            System.out.println("wait for pool " + index);

            synchronized (monitor) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                while (monitor.transfer) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("sorted " + index);
                monitor.index = index;
                monitor.transfer = true;
                monitor.notifyAll();
            }
        }
    }
}