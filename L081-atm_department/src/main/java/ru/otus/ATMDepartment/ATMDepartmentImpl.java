package ru.otus.ATMDepartment;

import ru.otus.ATM.ATM;

import java.util.ArrayList;
import java.util.List;

public class ATMDepartmentImpl implements ATMDepartment {

    List<ATM> atmList;

    public ATMDepartmentImpl() {
        atmList = new ArrayList<>();
    }

    @Override
    public ATM getATMById(String atmId) {
        for (ATM atm : atmList) {
            if (atm.getAtmId() == atmId) {
                return atm;
            }
        }
        return null;
    }

    @Override
    public void addATM(ATM atm) {
        if (!atmList.contains(atm)) {
            atmList.add(atm);
        }
    }

    @Override
    public void removeATMById(String atmId) {
        for (ATM atm : atmList) {
            if (atm.getAtmId() == atmId) {
                atmList.remove(0);
                return;
            }
        }
    }

    @Override
    public void loadInitialState() {
        for(ATM atm :atmList) {
            atm.loadInitialState();
        }
    }

    @Override
    public Long getBalance() {
        Long result = 0L;
        for (ATM atm : atmList) {
            result += atm.getBalance();
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Список подключенных банкоматов \n");
        for (ATM atm : atmList){
            result.append("  ").append(atm.getAtmId()).append(" баланс: ").append(String.format("%,d", atm.getBalance())).append("\n");
        }
        result.append("Баланс всех банкоматов: ").append(String.format("%,d", getBalance()));
        return String.valueOf(result);
    }
}