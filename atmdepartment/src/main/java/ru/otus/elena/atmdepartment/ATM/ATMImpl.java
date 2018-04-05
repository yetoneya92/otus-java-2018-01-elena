
package ru.otus.elena.atmdepartment.ATM;

import java.util.HashMap;
import java.util.Map;


public class ATMImpl {

    private int ATM_number;
    public static final int[] DENOMINATIONS = {500, 100, 50, 25, 10, 5, 3, 1};
    private final Map<Integer, Integer> initialState;
    private final Map<Integer, Integer> currentState;

    public ATMImpl(int ATM_number) {
        this.ATM_number = ATM_number;
        initialState = new HashMap<>();
        currentState = new HashMap<>();
        for (int denomination : DENOMINATIONS) {
            initialState.put(denomination, 0);
            currentState.put(denomination, 0);
        }
    }

    public Map<Integer, Integer> getInitialState() {
        return initialState;
    }

    public Map<Integer, Integer> getCurrentState() {
        return currentState;
    }

    public int getATM_number() {
        return ATM_number;
    }

    public int getBalance() {
        int[] balance = new int[1];
        currentState.forEach((k, v) -> balance[0] += k * v);
        return balance[0];
    }

    public synchronized String withdrawMoney(int sum) {       
        if (sum <= 0 || sum > Integer.MAX_VALUE) {
            return "невозможно выдать указанную сумму";
        }
        if (sum > getBalance()) {
            return "недостаточно средств для выдачи суммы";
            
        }
        int sumRest = sum;
        int amount = 0;
        for (int i = 0; i < DENOMINATIONS.length; i++) {
            if (sumRest >= DENOMINATIONS[i]) {
                amount = sumRest / DENOMINATIONS[i];
                int denominationNumber = currentState.getOrDefault(DENOMINATIONS[i], 0);
                if (denominationNumber >= amount) {
                    sumRest = sumRest - amount * DENOMINATIONS[i];
                    currentState.put(DENOMINATIONS[i], denominationNumber - amount);
                    if (sumRest == 0) {
                        return "Выдано: " + sum;                     
                    }
                } else {
                    sumRest = sumRest - denominationNumber * DENOMINATIONS[i];
                    currentState.put(DENOMINATIONS[i], 0);
                }
            }
        }
        return "невозможно выдать указанную сумму имеющимися купюрами";
    }

    public String payIn(Map<Integer, Integer> toPay) {
        try{
        final StringBuilder result=new StringBuilder();
        final int[]sum=new int[1];
        sum[0]=0;
        toPay.forEach((k, v) -> {
            if (checkDenomination(k)) {
                    currentState.merge(k, v, Integer::sum);
                    sum[0] += k * v;

                } else {
                    result.append(k + ": "+v+" фальшивых банкнот\n");
                }
            });
            result.append("Внесено: " + sum[0]);
            return result.toString();
        } catch (Exception e) {
            return "невозможно выполнить операцию";
        }
    }

    private static boolean checkDenomination(int forCheck) {
        for (int den : DENOMINATIONS) {
            if (den == forCheck) {
                return true;
            }
        }
        return false;
    }
}
