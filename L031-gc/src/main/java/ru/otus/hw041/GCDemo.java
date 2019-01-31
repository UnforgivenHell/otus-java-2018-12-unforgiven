package ru.otus.hw041;

/*
VM Options:
-Xms18m -Xmx18m -XX:MaxMetaspaceSize=256m
*/

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

public class GCDemo
{
    public static void main(String[] args) throws Exception
    {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("ru.otus:type=Benchmark");
        Benchmark mbean = new Benchmark();
        String dashedLine = "-------------------------";
        long startTime = System.nanoTime();
        long elapsedTime;
        int size = 100;

        for(int i = 0; i < args.length; i++) {
            if (i == 0) {
                size = Integer.parseInt(args[i]);
            }
        }

        mbs.registerMBean(mbean, name);
        mbean.setSize(size);

        try {
            mbean.subscribeToGcEvents();
            mbean.run();
        }
        catch (OutOfMemoryError e) {
            Thread.sleep(6000);
            System.out.println(dashedLine + "      TOTAL GC PARAM     " + dashedLine);
            System.out.println("totalTimes: " + Benchmark.totalTimes);
            System.out.println("totalRuns: " + Benchmark.totalRuns);
        }
        finally {
            elapsedTime = System.nanoTime() - startTime;
            System.out.println("runTimes: " + TimeUnit.SECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS) + " second");
            System.out.println(dashedLine + " APPLICATION GOT CRASHED " + dashedLine);
        }
    }
}