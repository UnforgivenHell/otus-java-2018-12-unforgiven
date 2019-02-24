package ru.otus.hw051.tests;

import ru.otus.hw051.annotations.After;
import ru.otus.hw051.annotations.Before;
import ru.otus.hw051.annotations.Test;

public class FrameworkTest2 {
    private static final String RED_BOLD = "\033[31m";
    private static final String ANSI_RESET = "\u001B[0m";

    private Integer countFail ;
    private Integer countSuccess;

    @Before
    public void start() {
        countFail = 0;
        countSuccess = 0;
    }

    @Test
    public void test1() {
        System.out.println("Test1");
        try {
            Integer i = 1 / 0;
        }
        catch (ArithmeticException ae) {
            countFail += 1;
        }

    }
    @Test
    public void test2() {
        System.out.println("Test2");
        comparison(2+2, 4);
        comparison(2+3, 5);
        comparison(2+4, 10);
        comparison(10+10, 20);
    }

    @After
    public void finish() {
        if (countSuccess >= 1){
            System.out.printf("Success: %d%s\n", countSuccess, ANSI_RESET);
        }
        if (countFail >= 1){
            System.out.printf("%sError: %d%s\n", RED_BOLD, countFail, ANSI_RESET);
        }
    }

    private void comparison(int a, int b){
        if (a == b) {
            countSuccess += 1;
        } else {
            countFail += 1;
        }
    }
}
