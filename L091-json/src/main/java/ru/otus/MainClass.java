package ru.otus;

import com.google.gson.Gson;
import ru.otus.testClasses.Person;

import java.util.ArrayList;

public class MainClass {
    public static void main(String[] args) {

        testMyGson(null);
        testMyGson(new String("123"));
        testMyGson(new Integer(3));
        testMyGson(new Boolean(false));
        testMyGson(new Person("Ivanov", "Ivan", "Ivanovich", 10));

        ArrayList<Integer> arrayListInteger = new ArrayList<Integer>();
        arrayListInteger.add(0);
        arrayListInteger.add(1);
        testMyGson(arrayListInteger);

        ArrayList<String> arrayListString = new ArrayList<String>();
        arrayListString.add("One");
        arrayListString.add("Two");
        testMyGson(arrayListString);
    }

    static void testMyGson(Object object) {
        Gson gson = new Gson();
        String mg = MyGson.toJson(object);
        String g = gson.toJson(object);

        System.out.println("MYGSON : " + mg);
        System.out.println("GSON   : " + g);
        System.out.println("------------------------------------");
    }

}