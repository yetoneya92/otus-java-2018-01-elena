
package ru.otus.elena.tester.view;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import ru.otus.elena.tester.Tester;

public class GetMethodsTask extends Task<Void> {

    final private ObservableList<String> methodNames;
    final private Class clazz;
    final private Map<String,Method> methods;
    
    

    public GetMethodsTask(ObservableList<String> methodNames,Map<String,Method> methods, Class clazz) {
        this.clazz=clazz;
        this.methodNames = methodNames;
        this.methods=methods;
    }

    @Override
    protected Void call() throws Exception {
        //System.out.println("ClassName=" + clazz.getCanonicalName());
        for (Method m : clazz.getDeclaredMethods()) {
            String s = Modifier.toString(m.getModifiers()) + " "
                    + m.getReturnType().getCanonicalName() + " "
                    + m.getName() + " " + Arrays.toString(m.getParameters());
            methods.put(s, m);
            //System.out.println(s);
        }

        return null;

    }

    @Override
    protected void succeeded() {
        Tester.methods.putAll(methods);
        for(String s:methods.keySet())
        methodNames.add(s);

    }
}