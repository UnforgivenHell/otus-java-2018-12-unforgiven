package ru.otus.hw031;

import java.util.*;

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

        Collections.addAll(myArrayString, "11", "22");
        System.out.printf("Added collection [11, 22] lasted: %s%n", myArrayString);

        System.out.printf("Sublist (2, 8): %s%n", myArrayString.subList(2, 8));

        myArrayString.clear();
        System.out.printf("Cleared array: %s%n", myArrayString);

        MyArrayList<Integer> myArrayInteger = new MyArrayList<>();
        for(int i = 0; i < 10; i++) {
            myArrayInteger.add(rn.nextInt(1000));
        }
        System.out.printf("Before sorted array: %s%n", myArrayInteger);
        Collections.sort(myArrayInteger);
        System.out.printf("After sorted array: %s%n", myArrayInteger);

        MyArrayList<Integer> myArrayIntegerSmall = new MyArrayList<>();
        myArrayIntegerSmall.addAll(Arrays.asList(new String[5]));
        MyArrayList<Integer> myArrayIntegerBig = new MyArrayList<>();
        Collections.addAll(myArrayIntegerBig, 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15);

        myArrayString.copy(myArrayIntegerSmall, myArrayInteger);
        System.out.printf("Copied small array: %s%n", myArrayIntegerSmall);

        Collections.copy(myArrayIntegerBig, myArrayInteger);
        System.out.printf("Copied big array: %s%n", myArrayIntegerBig);
    }
}