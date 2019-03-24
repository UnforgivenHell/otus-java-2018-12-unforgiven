package ru.otus;

import com.google.gson.Gson;
import ru.otus.testClasses.Person;

import java.util.ArrayList;
import java.util.HashMap;

public class MainClass {
    public static void main(String[] args) {

        testmygson(null);
        testmygson(new String("123"));
        testmygson(new Integer(3));
        testmygson(new Boolean(false));
        testmygson(new Person("Ivanov", "Ivan", "Ivanovich", 10));

        ArrayList<Integer> arrayListInteger = new ArrayList<Integer>();
        arrayListInteger.add(0);
        arrayListInteger.add(1);
        testmygson(arrayListInteger);

        ArrayList<String> arrayListString = new ArrayList<String>();
        arrayListString.add("One");
        arrayListString.add("Two");
        testmygson(arrayListString);
    }

    static void testmygson(Object object) {
        Gson gson = new Gson();
        String mg = mygson.toJson(object);
        String g = gson.toJson(object);

        System.out.println("MYGSON : " + mg);
        System.out.println("GSON   : " + g);
        System.out.println("------------------------------------");
    }

}