package ru.otus.elena.tester.main;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import ru.otus.elena.tester.myannotations.After;
import ru.otus.elena.tester.myannotations.Before;
import ru.otus.elena.tester.myannotations.Test;

public class Tester {

    public static ArrayList<String> doWork(Class<?> clazz) {
        
        ArrayList<String> list = new ArrayList<>();
        if (clazz == null) {
            list.add("tests not exist");
            return list;
        }   
        Method[] methods = clazz.getMethods();
        if(methods.length==0) {
            list.add("tests not exist");
            return list;
        }
        try {
        Annotation annotation = null;
        Object obj = null;
        
            s1:
            for (Method m : methods) {
                StringBuilder result = new StringBuilder();
                annotation = m.getAnnotation(Test.class);
                if (annotation == null) {
                    continue s1;
                }
                obj = clazz.newInstance();
                s2:
                for (Method n : methods) {
                    annotation = n.getAnnotation(Before.class);
                    if (annotation != null) {
                        try {
                            n.invoke(obj);
                        } catch (InvocationTargetException ite) {
                            result.append(n.getName() + " = Test failed: " + ite.getCause().getMessage());
                            break s1;
                        }
                        try {
                            result.append(m.getName()).append(" = ");
                            m.invoke(obj);
                            result.append("success  ");                            
            
                            break s2;
                        } catch (InvocationTargetException ite) {
                            result.append("Test failed: " + ite.getCause().getMessage()).append("  ");
                            break s2;
                        }

                    }
                }
                s3:
                for (Method n : methods) {
                     annotation = n.getAnnotation(After.class);
                    if (annotation != null) {
                        try {
                            n.invoke(obj);
                            list.add(result.toString());
                        } catch (InvocationTargetException ite) {
                            result.append(n.getName() + " = Test failed: " + ite.getCause().getMessage());
                            break s1;
                        }
                    }

                }

            }
        } catch (Exception e) {
            //System.out.println(e.getMessage());
            list.add(e.getMessage());
            return list;
        }
        return list;
    }
}   
 