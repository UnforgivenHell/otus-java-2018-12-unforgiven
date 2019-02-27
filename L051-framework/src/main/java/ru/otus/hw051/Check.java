package ru.otus.hw051;

public class Check {

    public static void checkTrue(String message, boolean condition) {
        if (!condition) {
            fail(message);
        }
    }

    public static void checkTrue(boolean condition) {
        checkTrue((String)null, condition);
    }

    public static void checkFalse(String message, boolean condition) {
        checkTrue(message, !condition);
    }

    public static void checkFalse(boolean condition) {
        checkFalse((String)null, condition);
    }

    public static void fail(String message) {
        if (message == null) {
            throw new RuntimeException();
        } else {
            throw new RuntimeException(message);
        }
    }
}