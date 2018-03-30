
package ru.otus.elena.bancomat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Coffer {

    int balance;
    private Map<Integer, Integer> cofferContent = new ConcurrentHashMap<>();
    private String message;
    private final int[]DENOMINATIONS={500,100,50,25,10,5,3,1};
    
    private void setBalance(int balance){
       this.balance=balance;
    }
    
    public int getBalance() {       
        return balance;
        
    }
    
    public void addMoney(int denomination){
        cofferContent.merge(denomination,1,Integer::sum);
        int bal=balance+denomination;
        setBalance(bal);
        
    }
 
    public String withdrawMoney(int sum) {       
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
