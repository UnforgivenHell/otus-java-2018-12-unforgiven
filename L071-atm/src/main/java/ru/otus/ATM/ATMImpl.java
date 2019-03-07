package ru.otus.ATM;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class ATMImpl implements ATM {
    private String atmId;

    private Map<Integer, Integer> cassette;

    public ATMImpl(String atmId) {
        this.cassette = new TreeMap<>(Collections.reverseOrder());
        this.atmId = atmId;
    }

    @Override
    public Map<Integer, Integer> getCassette() {
        return cassette;
    }

    @Override
    public Integer getBalance() {
        Integer balance = 0;
        for (Map.Entry<Integer, Integer> cell : cassette.entrySet()) {
            balance += cell.getKey() * cell.getValue();
        }
        return balance;
    }

    @Override
    public Integer getCount(Map<Integer, Integer> cassette, Integer banknote){
        Integer vRes;
        if (cassette.containsKey(banknote)) {
            vRes = cassette.get(banknote);
        } else {
            vRes = 0;
        }

        return vRes;
    }
    @Override
    public Map<Integer, Integer> getBonknatescashAdvance(Integer amount){
        Integer balance = getBalance();

        if (amount < 0) {
            System.out.printf("Сумма не может быть меньше нуля");
            return null;
        } else if (balance < amount) {
            System.out.printf("Недостаточно средств в банкомате");
            return null;
        }
        Map<Integer, Integer> banknotes = new TreeMap<>();

        for (Integer key : cassette.keySet()) {
            while ( amount >= key && ((getCount(cassette, key) - getCount(banknotes, key)) > 0)) {
                banknotes.merge(key, 1, Integer::sum);
                amount -= key;
            }
        }
        if (amount > 0) {
            System.out.printf("Невозможно выдать указанную сумму");
            return null;
        }

        for (Integer key : banknotes.keySet()) {
            cassette.merge(key, -1 * getCount(banknotes, key), Integer::sum);
        }
        return banknotes;
    }

    @Override
    public void cashAdvance(Integer amount){
        Map<Integer, Integer> banknotes;
        System.out.printf("Запрос на выдачу: " + amount + ". ");
        banknotes = getBonknatescashAdvance(amount);
        if (banknotes != null) {
            System.out.printf("Выдали банкнотами: " + banknotes);
        }

        System.out.printf("\n");
        System.out.printf("  Остаток: " + toString() +"\n");
    }

    @Override
    public void incass(Map<Integer, Integer> cassette){
        this.cassette.putAll(cassette);
        System.out.println("Инкасация: " + toString());
    }

    @Override
    public String toString() {
        return "Банкомат (" + this.atmId +") {" + "Банкноты = " + cassette + ", Баланс=" + getBalance() + "}";
    }
}