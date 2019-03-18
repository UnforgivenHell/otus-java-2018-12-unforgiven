package ru.otus;

import ru.otus.ATM.ATM;
import ru.otus.ATM.ATMImpl;
import ru.otus.ATMDepartment.ATMDepartment;
import ru.otus.ATMDepartment.ATMDepartmentImpl;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class MainClass {
    public static void main(String[] args) {
        String sep = "------------------------------";
        ATMDepartment atmDepartment = new ATMDepartmentImpl();
        ATM atm;

        atm = new ATMImpl("Test_01");
        Incass(atm);
        atmDepartment.addATM(atm);

        atm = new ATMImpl("Test_02");
        Incass(atm);
        atmDepartment.addATM(atm);

        atm = new ATMImpl("Test_03");
        Incass(atm);
        atmDepartment.addATM(atm);

        atm = new ATMImpl("Test_04");
        Incass(atm);
        atmDepartment.addATM(atm);

        System.out.println(sep);
        System.out.println(atmDepartment);

        System.out.println(sep);
        atmDepartment.getATMById("Test_01").cashAdvance(2200);
        atmDepartment.getATMById("Test_03").cashAdvance(6200);
        atmDepartment.getATMById("Test_01").cashAdvance(5200);
        atmDepartment.getATMById("Test_04").cashAdvance(10200);
        atmDepartment.getATMById("Test_01").cashAdvance(1200);
        atmDepartment.getATMById("Test_02").cashAdvance(4000);
        atmDepartment.getATMById("Test_03").cashAdvance(10200);
        atmDepartment.getATMById("Test_04").cashAdvance(15200);

        System.out.println(sep);
        System.out.println(atmDepartment);
        System.out.println("Удаляем банкомат: Test_01");
        atmDepartment.removeATMById("Test_01");

        System.out.println(sep);
        System.out.println("Востановление последней инкассации на всех подключенных банкоматах");
        atmDepartment.loadInitialState();
        System.out.println(atmDepartment);
        System.out.println(sep);
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