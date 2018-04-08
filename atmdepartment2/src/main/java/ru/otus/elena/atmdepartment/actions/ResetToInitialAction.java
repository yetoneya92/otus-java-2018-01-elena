
package ru.otus.elena.atmdepartment.actions;

import java.util.HashMap;
import java.util.Map;
import static ru.otus.elena.atmdepartment.ATM.ATM.DENOMINATIONS;
import ru.otus.elena.atmdepartment.ATM.ATMDepartment;
import ru.otus.elena.atmdepartment.actions.intefaces.DepartmentAction;

public class ResetToInitialAction  implements DepartmentAction{

    @Override
    public void visit(ATMDepartment department) {      
        
        final Map<Integer, Map<Integer, Integer>> differencies = new HashMap<>();
        
        department.getMashines()
                .forEach(e -> {
                    Map<Integer, Integer> map = new HashMap<>();
                    for (int den : DENOMINATIONS) {
                        map.put(den, (e.getCurrentState().get(den) - e.getInitialState().get(den)));
                        e.getCurrentState().put(den, e.getInitialState().get(den));
                    }
                    differencies.put(e.getATM_number(), map);

                });
        department.getResetValues().putAll(differencies);        
    }
}
