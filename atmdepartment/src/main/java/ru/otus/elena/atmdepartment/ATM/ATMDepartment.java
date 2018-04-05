
package ru.otus.elena.atmdepartment.ATM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import ru.otus.elena.atmdepartment.actions.intefaces.DepartmentAction;

public class ATMDepartment {
    private Map<Integer,ATMImpl>mashines;
    private Map<Integer,Map<Integer,Integer>>checkResults;
    private ArrayList<String>historyList;
    public ATMDepartment() {
        this.checkResults = new HashMap<>();
        this.mashines=new HashMap<>();
        this.historyList=new ArrayList<>();
    }
    
    public void addMashines(int mashinesNumber){                
        for (int i = 1; i <= mashinesNumber; i++) {
            mashines.put(i,new ATMImpl(i));
        }
    }
    
    public Map<Integer,ATMImpl> getMashines(){
        return mashines;
    }
    public Map<Integer,Map<Integer,Integer>> getCheckResults(){
        return checkResults;
    }
    public ArrayList<String> getHistoryList(){
        return historyList;
    }

    public void doAction(DepartmentAction action) {
        action.visit(this);
    }

    public void printCheckResult() {
           getCheckResults()
                .forEach((k, v) -> {
                    System.out.println("ATM_number=" + k);
                    v.forEach((t, u) -> {
                        System.out.print("[" + t + "=" + u + "] ");
                    });
                    System.out.println();
                });
    }


    public void printBalance(){
                getMashines()
                .forEach((k, v) -> {
                    System.out.println("ATM_number=" + k + " balance=" + v.getBalance());

                });
    }
    public void printHistory(){
        historyList.forEach(string->{System.out.println(string);});
    }
}
