
package ru.otus.elena.customeractions;

import java.util.Map;
import ru.otus.elena.atmdepartment.ATM.ATMImpl;
import ru.otus.elena.atmdepartment.actions.intefaces.CustomerAction;

public class PayInAction implements CustomerAction {    
    private Map<Integer,Integer> amount;

    public PayInAction(Map<Integer,Integer>amount) {
        this.amount = amount;
    }

    @Override
    public void visit(Customer customer) {        
        ATMImpl atm = customer.getATM();
        String result=atm.payIn(amount);
        
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
