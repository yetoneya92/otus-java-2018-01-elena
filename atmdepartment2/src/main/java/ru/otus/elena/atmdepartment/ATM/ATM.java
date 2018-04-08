
package ru.otus.elena.atmdepartment.ATM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ATM {
    
    public static final int[] DENOMINATIONS = {500, 100, 50, 25, 10, 5, 3, 1};
    private final Map<Integer, Integer> initialState;
    private final Map<Integer, Integer> currentState;
    private final ArrayList<String>messages;
    protected boolean isChanged;
    private String message;
    public ATM() {
        messages=new ArrayList<>();
        initialState = new HashMap<>();
        currentState = new HashMap<>();
        for (int denomination : DENOMINATIONS) {
            initialState.put(denomination, 0);
            currentState.put(denomination, 0);
        }
    }

    protected ArrayList<String> getMessages() {
        return messages;
    }

    protected boolean getIsChanged() {
        return isChanged;
    }

    protected void resetIsChanged() {
        isChanged = false;
    }

    public Map<Integer, Integer> getInitialState() {
        return initialState;
    }

    public Map<Integer, Integer> getCurrentState() {
        return currentState;
    }

    protected int getBalance() {
        int[] balance = new int[1];
        currentState.forEach((k, v) -> balance[0] += k * v);
        return balance[0];
    }

    public synchronized String withdrawMoney(int sum) {
        
        if (sum <= 0 || sum > Integer.MAX_VALUE) {
            message = "невозможно выдать указанную сумму";
            commitChanges(message);
        }
        if (sum > getBalance()) {
            message = "недостаточно средств для выдачи суммы";
            commitChanges(message);
            return message;
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
                        message = "Выдано: " + sum;
                        commitChanges(message);
                        return message;
                    }
                } else {
                    sumRest = sumRest - denominationNumber * DENOMINATIONS[i];
                    currentState.put(DENOMINATIONS[i], 0);
                }
            }
        }
        message = "невозможно выдать указанную сумму имеющимися купюрами";
        commitChanges(message);
        return message;
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
            message=result.append("Внесено: " + sum[0]).toString();
            commitChanges(message);
            return message;
        } catch (Exception e) {
            message= "невозможно выполнить операцию";
            commitChanges(message);
            return message;
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
    public void commitChanges(String message){
        messages.add(message);
        isChanged=true;
        
    }
}
