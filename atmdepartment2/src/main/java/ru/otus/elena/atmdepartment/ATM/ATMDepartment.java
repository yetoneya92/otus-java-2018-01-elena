
package ru.otus.elena.atmdepartment.ATM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import ru.otus.elena.atmdepartment.actions.intefaces.DepartmentAction;

public class ATMDepartment {
    static Random randomGenerator = new Random();
    private List<ATMImpl>mashines;
    private Map<Integer,Map<Integer,Integer>>checkResults;
    private Map<Integer,Map<Integer,Integer>>resetValues;
    private ArrayList<String>customerActionHistory;
    public ATMDepartment() {
        this.checkResults = new HashMap<>();
        this.mashines = new ArrayList<>();
        this.customerActionHistory = new ArrayList<>();
        this.resetValues=new HashMap<>();
    }

    public void doAction(DepartmentAction action) {
        action.visit(this);
    }

    public void addMashines(int mashinesNumber) {
        for (int i = 1; i <= mashinesNumber; i++) {
            mashines.add(new ATMImpl(i));
        }
    }

    public List<ATMImpl> getMashines() {
        return mashines;
    }

    public ATMImpl getATM() {
        int num = randomGenerator.nextInt(getMashines().size());
        return mashines.get(num);
    }

    public Map<Integer, Map<Integer, Integer>> getCheckResults() {
        return checkResults;
    }
        public Map<Integer, Map<Integer, Integer>> getResetValues() {
        return resetValues;
    }


    public void clearHistory(){
        customerActionHistory.clear();
    }

    public void printResetValues() {
          printResult(resetValues);
        
    }

    public void printCheckResult() {
        printResult(checkResults);
    }

    private void printResult(Map<Integer, Map<Integer, Integer>> map) {
        map.forEach((k, v) -> {
            System.out.println("ATM_number=" + k);
            v.forEach((t, u) -> {
                System.out.print("[" + t + "=" + u + "] ");
            });
            System.out.println();
        });
    }

    public void printBalance(){
                mashines
                .forEach(e -> {
                    System.out.println("ATM_number=" + e.getATM_number() + " balance=" + e.getBalance());

                });
    }

    public void printCustomerActionHistory() {
        getCustomActionResults();
        customerActionHistory.forEach(string -> {
            System.out.println(string);
        });
    }

    private void getCustomActionResults() {
        mashines.forEach(e -> {
            if (e.getIsChanged()) {
                customerActionHistory.addAll(e.getMessages());
                e.getMessages().clear();
                e.resetIsChanged();
            }
        });
    }
}
