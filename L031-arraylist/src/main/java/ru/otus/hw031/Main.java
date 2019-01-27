package ru.otus.hw031;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Random rn = new Random();
        MyArrayList<String> myArrayString = new MyArrayList<>();
        int indexRemove1 = Math.round(rn.nextInt(args.length));
        int indexRemove2 = Math.round(rn.nextInt(args.length));
        String stringRemove = "";

        for(int i = 0; i < args.length; i++) {
            String str = args[i];
            myArrayString.add(str);
            if (indexRemove2 == i) {
                stringRemove = str;
            }
        }
        System.out.println("Array: " + myArrayString);

        myArrayString.remove(indexRemove1);
        System.out.printf("Removed element by index %d: %s%n", indexRemove1, myArrayString);

        myArrayString.remove(stringRemove);
        System.out.printf("Removed element by name \"%s\": %s%n", stringRemove, myArrayString);

        myArrayString.addAll(2, Arrays.asList(new String[]{"Q", "W", "E", "R", "T", "Y"}));
        System.out.printf("Added collection [Q, W, E, R, T, Y] to position 2: %s%n", myArrayString);

        myArrayString.addAll(Arrays.asList(new String[]{"11", "22"}));
        System.out.printf("Added collection [11, 22] lasted: %s%n", myArrayString);

        System.out.println("Sublist (2, 8): " + myArrayString.subList(2, 8));

        myArrayString.clear();
        System.out.println("Cleared array: " + myArrayString);

        MyArrayList<Integer> myArrayInteger = new MyArrayList<>();
        for(int i = 0; i < 20; i++) {
            myArrayInteger.add(rn.nextInt(1000));
        }
        System.out.println("Before sorted array: " + myArrayInteger);
        myArrayInteger.sort(null);
        System.out.println("After sorted array: " + myArrayInteger);

        MyArrayList<Integer> myArrayIntegerSmall = new MyArrayList<>();
        myArrayIntegerSmall.addAll(Arrays.asList(new String[10]));
        MyArrayList<Integer> myArrayIntegerBig = new MyArrayList<>();
        myArrayIntegerBig.addAll(Arrays.asList(new String[30]));

        myArrayString.copy(myArrayIntegerSmall, myArrayInteger);
        System.out.println("Copied small array: " + myArrayIntegerSmall);

        myArrayString.copy(myArrayIntegerBig, myArrayInteger);
        System.out.println("Copied big array: " + myArrayIntegerBig);
    }
}