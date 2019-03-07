package ru.otus.ATM;

import java.util.Map;

public interface ATM {
    Map<Integer, Integer> getCassette();

    Integer getBalance();

    Integer getCount(Map<Integer, Integer> cassette, Integer banknote);

    Map<Integer, Integer> getBonknatescashAdvance(Integer amount);

    void cashAdvance(Integer amount);

    void incass(Map<Integer, Integer> cassette);
}
