
package ru.otus.elena.atmdepartment.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import ru.otus.elena.atmdepartment.ATM.ATMDepartment;
import ru.otus.elena.atmdepartment.ATM.ATMImpl;
import ru.otus.elena.atmdepartment.actions.CheckInitialStateAction;
import ru.otus.elena.atmdepartment.actions.CheckRestAction;
import ru.otus.elena.atmdepartment.actions.SetInitialStateAction;
import ru.otus.elena.customeractions.Customer;
import ru.otus.elena.customeractions.PayInAction;
import ru.otus.elena.customeractions.WithdrawAction;

public class ATMDepartmentTest {

    public static void main(String[] args) {
        ATMDepartment department = new ATMDepartment();
        department.addMashines(5);
        department.doAction(new SetInitialStateAction(1000));

        department.doAction(new CheckInitialStateAction());
        department.printCheckResult();

        ArrayList<Customer> listCustomer = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            listCustomer.add(new Customer(department));
        }
        listCustomer.forEach(customer -> customer.doAction(new WithdrawAction(19999)));

        department.doAction(new CheckRestAction());
        department.printCheckResult();
        department.printBalance();
        department.printHistory();

        Map<Integer, Integer> toPay = new HashMap<>();
        for (int den : ATMImpl.DENOMINATIONS) {
            toPay.put(den, 20);
        }
        toPay.merge(ATMImpl.DENOMINATIONS[1], 30, Integer::sum);

        listCustomer.forEach(customer -> customer.doAction(new PayInAction(toPay)));
        department.printHistory();
        department.doAction(new CheckRestAction());
        department.printCheckResult();
        
        toPay.clear();
        toPay.put(7,2);
        listCustomer.get(0).doAction(new PayInAction(toPay));       
        listCustomer.get(1).doAction(new WithdrawAction(-100));
        department.printHistory();
        department.doAction(new SetInitialStateAction((2)));
        for(int i=0; i<20;i++){
        listCustomer.get(2).doAction(new WithdrawAction(1));
        listCustomer.get(2).doAction(new WithdrawAction(2));}
        department.printHistory();
    }
}
