
package ru.otus.elena.customeractions;

import java.util.Map;
import java.util.Random;
import ru.otus.elena.atmdepartment.ATM.ATMImpl;
import ru.otus.elena.atmdepartment.actions.intefaces.CustomerAction;
import static ru.otus.elena.customeractions.WithdrawAction.randomGenerator;

public class PayInAction implements CustomerAction {

    static Random randomGenerator = new Random();
    private Map<Integer,Integer> amount;

    public PayInAction(Map<Integer,Integer>amount) {
        this.amount = amount;
    }

    @Override
    public void visit(Customer customer) {
        int num = 1 + randomGenerator.nextInt(customer.getATMDepartment().getMashines().size());
        ATMImpl atm = customer.getATMDepartment().getMashines().get(num);
        String result=atm.payIn(amount);
        customer.getATMDepartment().getHistoryList().add(result);
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
