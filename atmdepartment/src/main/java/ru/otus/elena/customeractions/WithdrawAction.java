
package ru.otus.elena.customeractions;

import java.util.Random;
import ru.otus.elena.atmdepartment.ATM.ATMImpl;
import ru.otus.elena.atmdepartment.actions.intefaces.CustomerAction;

public class WithdrawAction implements CustomerAction{
    static Random randomGenerator=new Random();
    private int sum;
    public WithdrawAction(int sum){       
        this.sum=sum;
    }
    
    @Override
    public void visit(Customer customer) {              
        int num=1+randomGenerator.nextInt(customer.getATMDepartment().getMashines().size());
        ATMImpl atm=customer.getATMDepartment().getMashines().get(num);
        String result="ATM_number="+atm.getATM_number()+" "+atm.withdrawMoney(sum);
        customer.getATMDepartment().getHistoryList().add(result);
    }

}
