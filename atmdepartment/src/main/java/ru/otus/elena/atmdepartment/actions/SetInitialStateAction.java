
package ru.otus.elena.atmdepartment.actions;

import ru.otus.elena.atmdepartment.ATM.ATMDepartment;
import ru.otus.elena.atmdepartment.actions.intefaces.DepartmentAction;

public class SetInitialStateAction implements DepartmentAction{
    public int notesNumber;
    
    public SetInitialStateAction(int notesNumber){
        this.notesNumber = notesNumber;
    }

    @Override
    public void visit(ATMDepartment department) {
        department.getMashines()
                .forEach((k, v) -> {
                    v.getCurrentState().forEach((t,u)->{v.getCurrentState().put(t,notesNumber);});
                    v.getInitialState().forEach((t,u)->{v.getInitialState().put(t,notesNumber);});
                });
    }

}
