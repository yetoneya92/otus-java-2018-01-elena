
package ru.otus.elena.atmdepartment.actions;

import java.util.HashMap;
import java.util.Map;
import ru.otus.elena.atmdepartment.ATM.ATMDepartment;
import ru.otus.elena.atmdepartment.actions.intefaces.DepartmentAction;

public class CheckInitialAction implements DepartmentAction{

    @Override
    public void visit(ATMDepartment department) {
        department.getCheckResults().clear();
        Map<Integer, Map<Integer, Integer>> map = new HashMap<>();
        department.getMashines().forEach((e) -> map.put(e.getATM_number(), e.getInitialState()));
        department.getCheckResults().putAll(map);
    }
}

