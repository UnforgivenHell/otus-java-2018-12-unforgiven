package ru.otus.ATMDepartment;

import ru.otus.ATM.ATM;

public interface ATMDepartment {

    void addATM(ATM atm);

    void removeATMById(String atmId);

    void loadInitialState();

    Long getBalance();

    ATM getATMById(String atmId);
}
