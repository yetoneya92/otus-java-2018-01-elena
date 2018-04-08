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
import ru.otus.elena.atmdepartment.ATM.ATM;
import ru.otus.elena.atmdepartment.actions.CheckInitialAction;
import ru.otus.elena.atmdepartment.actions.CheckCurrentAction;
import ru.otus.elena.atmdepartment.actions.ResetToInitialAction;
import ru.otus.elena.atmdepartment.actions.SetInitialAction;
import ru.otus.elena.customeractions.Customer;
import ru.otus.elena.customeractions.PayInAction;
import ru.otus.elena.customeractions.WithdrawAction;

public class Main {

    public static void main(String[] args) {
       
        ATMDepartment department = new ATMDepartment();
        department.addMashines(5);        
        department.doAction(new SetInitialAction(1000));        
        department.doAction(new CheckInitialAction());
        department.printCheckResult();       
        
        ArrayList<Customer>listCustomer=new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            listCustomer.add(new Customer(department.getATM()));
        }
        listCustomer.forEach(customer -> customer.doAction(new WithdrawAction(19999)));
        Customer newCustomer=new Customer(department.getATM());
        department.doAction(new CheckCurrentAction());
        department.printCheckResult();
       // department.printBalance();
       
        //department.printCustomerActionHistory();

        Map<Integer, Integer> toPay = new HashMap<>();
        for (int den : ATM.DENOMINATIONS) {
            toPay.put(den, 20);
        }
        toPay.merge(ATM.DENOMINATIONS[1], 30, Integer::sum);

        listCustomer.forEach(customer -> customer.doAction(new PayInAction(toPay)));
        
        //department.printCustomerActionHistory();
        department.doAction(new CheckCurrentAction());
        //department.printCheckResult();
        
        department.doAction(new ResetToInitialAction());      
        department.printResetValues();
        
        department.doAction(new CheckCurrentAction());
        department.printCheckResult();
        
        
    }


}
