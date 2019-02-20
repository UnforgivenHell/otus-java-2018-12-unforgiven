package ru.otus.hw051.tests;

import ru.otus.hw051.annotations.After;
import ru.otus.hw051.annotations.Before;
import ru.otus.hw051.annotations.Test;

public class FrameworkTest
{
    private String localString = "";

    @Before
    public void start() {
        this.localString += "start";
        print("Before");
    }

    @Test
    public void firstTest() {
        this.localString += ", firstTest";
        print("Test");
    }

    @Test
    public void secondTest() {
        this.localString += ", secondTest";
        print("Test");
    }

    @Test
    public void thirdTest() {
        this.localString += ", thirdTest";
        print("Test");
    }

    @After
    public void finish() {
        this.localString += ", finish";
        print("After");
    }

    private void print(String annotation){
        System.out.println("@" + annotation + ": " + this.localString);
        if (annotation == "After") {
            System.out.println("----------------------------------------");
        }
    }
}