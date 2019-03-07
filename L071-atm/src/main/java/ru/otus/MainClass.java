package ru.otus;

import ru.otus.ATM.ATM;
import ru.otus.ATM.ATMImpl;

import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class MainClass {
    public static void main(String[] args) {
        ATM atm = new ATMImpl("Test_01");

        Incass(atm);
        atm.cashAdvance(-1);
        atm.cashAdvance(10000000);
        atm.cashAdvance(20);
        atm.cashAdvance(atm.getBalance());

        Incass(atm);
        atm.cashAdvance(400);
        atm.cashAdvance(900);
        atm.cashAdvance(2200);
        atm.cashAdvance(22500);
    }

    static void Incass(ATM atm){
        Random rn = new Random();
        Integer maxCount = 100;
        Integer cassetId = rn.nextInt(6);
        Map<Integer, Integer> cassette = new TreeMap<>();

        if (cassetId != 0) {
            cassette.put(100, rn.nextInt(maxCount));
        }
        if (cassetId != 1) {
            cassette.put(200, rn.nextInt(maxCount));
        }
        if (cassetId != 2) {
            cassette.put(500, rn.nextInt(maxCount));
        }
        if (cassetId != 3) {
            cassette.put(1000, rn.nextInt(maxCount));
        }
        if (cassetId != 4) {
            cassette.put(5000, rn.nextInt(maxCount));
        }
        if (cassetId != 5) {
            cassette.put(2000, rn.nextInt(maxCount));
        }
        atm.incass(cassette);
    }
}