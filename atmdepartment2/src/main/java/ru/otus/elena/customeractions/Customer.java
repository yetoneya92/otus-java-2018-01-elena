package ru.otus.elena.customeractions;

import ru.otus.elena.atmdepartment.ATM.ATMImpl;
import ru.otus.elena.atmdepartment.actions.intefaces.CustomerAction;

public class Customer {
    
    private static int customerNumber;
    private int customerId;
    private ATMImpl atm;

    public Customer(ATMImpl atm) {
        this.customerId = ++customerNumber;
        this.atm=atm;      
    }
    public void doAction(CustomerAction action) {
        action.visit(this);
    }

    public int getCustomerId() {
        return customerId;
    }
    public ATMImpl getATM(){
        return atm;
    }
    
}
