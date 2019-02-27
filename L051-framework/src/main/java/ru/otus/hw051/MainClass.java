package ru.otus.hw051;

import ru.otus.hw051.framework.TestFrameworkRunner;
import java.io.IOException;

public class MainClass {

    public static void main(String[] args) throws IOException {
        TestFrameworkRunner testRunner = new TestFrameworkRunner();
        testRunner.runTestsByPackageName("ru.otus.hw051.tests");
    }

}
