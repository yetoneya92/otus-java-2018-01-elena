
package ru.otus.elena.atmdepartment.ATM;

public class ATMImpl extends ATM {
    private int ATM_number;
    
    
    public ATMImpl(int ATM_number){
        this.ATM_number=ATM_number;
         
    }

    public int getATM_number() {
        return ATM_number;
    }

}
