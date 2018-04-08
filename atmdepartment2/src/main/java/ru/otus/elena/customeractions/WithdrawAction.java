
package ru.otus.elena.customeractions;

import java.util.Random;
import ru.otus.elena.atmdepartment.ATM.ATMImpl;
import ru.otus.elena.atmdepartment.actions.intefaces.CustomerAction;

public class WithdrawAction implements CustomerAction{
    
    private int sum;
    public WithdrawAction(int sum){       
        this.sum=sum;
    }
    
    @Override
    public void visit(Customer customer) {                     
        ATMImpl atm=customer.getATM();
        String result=atm.withdrawMoney(sum);
        
        
    }

}
