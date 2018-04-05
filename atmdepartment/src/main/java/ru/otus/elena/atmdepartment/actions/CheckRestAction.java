
package ru.otus.elena.atmdepartment.actions;

import java.util.HashMap;
import java.util.Map;
import ru.otus.elena.atmdepartment.ATM.ATMDepartment;
import ru.otus.elena.atmdepartment.actions.intefaces.DepartmentAction;

public class CheckRestAction implements DepartmentAction{

    @Override
    public void visit(ATMDepartment department) {
        department.getCheckResults().clear();
        Map<Integer,Map<Integer,Integer>>map=new HashMap<>();
        department.getMashines().forEach((k,v)->map.put(k, v.getCurrentState()));
        department.getCheckResults().putAll(map);
    }
 
}
