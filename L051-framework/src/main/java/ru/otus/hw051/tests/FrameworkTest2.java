package ru.otus.hw051.tests;

import ru.otus.hw051.Check;
import ru.otus.hw051.annotations.After;
import ru.otus.hw051.annotations.Before;
import ru.otus.hw051.annotations.Test;

public class FrameworkTest2 {

    private Integer countFail ;
    private Integer countSuccess;

    @Before
    public void start() {
        System.out.println("Start");
    }

    @Test
    public void test1() {
        System.out.println("Test1");
        Integer i = 1 / 0;


    }
    @Test
    public void test2() {
        System.out.println("Test2");
        Check.checkTrue(1==1);
    }

    @Test
    public void test3() {
        System.out.println("Test3");
        Check.checkTrue(1==2);
    }

    @After
    public void finish() {
        System.out.println("finish");

    }

}
