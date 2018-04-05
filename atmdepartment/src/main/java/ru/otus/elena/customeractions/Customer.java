package ru.otus.elena.customeractions;

import ru.otus.elena.atmdepartment.ATM.ATMDepartment;
import ru.otus.elena.atmdepartment.actions.intefaces.CustomerAction;

public class Customer {
    
    private static int customerNumber;
    private int customerId;
    private ATMDepartment department;

    public Customer(ATMDepartment department) {
        this.customerId = ++customerNumber;
        this.department = department;
    }

    public void doAction(CustomerAction action) {
        action.visit(this);
    }

    public int getCustomerId() {
        return customerId;
    }

    public ATMDepartment getATMDepartment() {
        return department;
    }

}
