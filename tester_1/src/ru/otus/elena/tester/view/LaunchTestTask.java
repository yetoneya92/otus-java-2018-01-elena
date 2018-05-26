

package ru.otus.elena.tester.view;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import ru.otus.elena.tester.myannotations.After;
import ru.otus.elena.tester.myannotations.Before;
import ru.otus.elena.tester.myannotations.Test;

public class LaunchTestTask  extends Task<Void>{
    final private ObservableList<String> results;
    final private List<String> list = new ArrayList<>();
    final private Method[]methods;   
    final private Class clazz;    
    final private String[] annotationnames=
    {"@ru.otus.elena.tester.myannotations.Before()",
        "@ru.otus.elena.tester.myannotations.Test()",
                "@ru.otus.elena.tester.myannotations.After()"};

    public LaunchTestTask(ObservableList<String> results, Class clazz) {
        this.clazz = clazz;
        methods = clazz.getDeclaredMethods();
        this.results = results;
    }

    @Override
    protected Void call() throws Exception {
        try {
            Object obj = clazz.newInstance();
            s1:
            for (Method m : methods) {
                StringBuilder result = new StringBuilder();               
                Annotation[] an = m.getDeclaredAnnotations();
                m.getAnnotation(Before.class);
                System.out.println("ann="+an[0]);
                if (an.length == 0) {                   
                    this.cancel();
                    return null;
                }
                
                if (annotationnames[1].equals(m.getAnnotations()[0].toString())) {
                    for (Method n : methods) {

                        if (annotationnames[0].equals(n.getAnnotations()[0].toString())) {
                            result.append(n.getName()).append("=");
                            try {
                                n.invoke(obj);
                                result.append("success  ");
                            } catch (InvocationTargetException ite) {
                                result.append("Test failed: " + ite.getCause().getMessage()).append("  ");
                            list.add(result.toString());
                            break s1;
                        }
                    }
                }

                result.append(m.getName()).append("=");
                try {
                    m.invoke(obj);
                    result.append("success  ");
                } catch (InvocationTargetException ite) {
                    result.append("Test failed: " + ite.getCause().getMessage()).append("  ");
                }
                s3:
                for (Method p : methods) {
                    if (annotationnames[2].equals(p.getAnnotations()[0].toString())) {
                        result.append(p.getName()).append("=");
                        try {
                            p.invoke(obj);
                            result.append("success  ");
                            list.add(result.toString());
                        } catch (InvocationTargetException ite) {
                            result.append("Test failed: " + ite.getCause().getMessage()).append("  ");
                            list.add(result.toString());
                            System.out.println(list);        
                            break s1;
                        }
                        }
                    }
                }
            }
            return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    protected void succeeded() {
        results.addAll(list);
        // System.out.println(results);
    }
}

