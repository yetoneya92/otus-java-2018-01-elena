/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.otus.elena.atmdepartment;

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

public class Main {

    public static void main(String[] args) {
       
        ATMDepartment department = new ATMDepartment();
        department.addMashines(5);        
        department.doAction(new SetInitialStateAction(1000));

        department.doAction(new CheckInitialStateAction());
        department.printCheckResult();       
        
        ArrayList<Customer>listCustomer=new ArrayList<>();
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
    }


}
