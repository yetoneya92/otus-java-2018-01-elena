
package ru.otus.elena.atmmashine;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Coffer {
    //String parol;
    int balance;
    final  private Map<Integer, Integer> initialCofferContent;
    final private Map<Integer, Integer> cofferContent;
    private String message;
    private final int[] DENOMINATIONS = {500, 100, 50, 25, 10, 5, 3, 1};
    private final int DENSUM=694;

    public Coffer() {
        initialCofferContent=new ConcurrentHashMap<>();
        cofferContent = new ConcurrentHashMap<>();
    }


    public void setInitialState(int num) {       
            for (int i = 0; i < DENOMINATIONS.length; i++) {
                cofferContent.put(DENOMINATIONS[i], num);
                initialCofferContent.put(DENOMINATIONS[i], num);
            
        }
            synchronized(this){
                balance=num*DENSUM;
            }
    }
    public Map<Integer,Integer> getInitialContent(){
        return initialCofferContent;
    }
     public Map<Integer,Integer> getContent(){
        return cofferContent;
    }

    private void setBalance(int balance) {
        synchronized (this) {
            this.balance = balance;
        }
    }
    
    public int getBalance() {       
        return balance;
        
    }
    
    public void addMoney(int denomination){
        cofferContent.merge(denomination,1,Integer::sum);        
        setBalance(balance+denomination);
        
    }
 
    public  synchronized String withdrawMoney(int sum) {       
        if (sum > balance) {
            message="недостаточно средств на счете";          
            return message;
        }
        int sumRest=sum;
        int amount = 0;
        for(int i=0;i<DENOMINATIONS.length;i++)
        if (sumRest >= DENOMINATIONS[i]) {
            amount = sumRest /DENOMINATIONS[i] ;
            int cofferAmount = cofferContent.getOrDefault(DENOMINATIONS[i], 0);
            if (cofferAmount >= amount) {
                sumRest = sumRest - amount * DENOMINATIONS[i];
                cofferContent.put(DENOMINATIONS[i], cofferAmount - amount);
                if (sumRest == 0) {
                    message="Заберите деньги";                 
                    setBalance(balance-sum);
                        return message;
                    }
                } else {
                    sumRest = sumRest - cofferAmount * DENOMINATIONS[i];
                    cofferContent.put(DENOMINATIONS[i], 0);
                }
            }
        message = "невозможно выдать указанную сумму имеющимися купюрами";
        return message;
    }
}
