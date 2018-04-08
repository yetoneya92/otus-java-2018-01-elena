
package ru.otus.elena.atmdepartment.actions;

import ru.otus.elena.atmdepartment.ATM.ATMDepartment;
import ru.otus.elena.atmdepartment.actions.intefaces.DepartmentAction;

public class SetInitialAction implements DepartmentAction{
    public int notesNumber;
    
    public SetInitialAction(int notesNumber){
        this.notesNumber = notesNumber;
    }

    @Override
    public void visit(ATMDepartment department) {        
        department.getMashines()
                .forEach(e -> {
                    e.getCurrentState().forEach((t,u)->{e.getCurrentState().put(t,notesNumber);});
                    e.getInitialState().forEach((t,u)->{e.getInitialState().put(t,notesNumber);});
                });
        
        
    }
}
