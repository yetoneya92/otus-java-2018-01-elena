package ru.otus.elena.tester.main;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import ru.otus.elena.tester.myannotations.After;
import ru.otus.elena.tester.myannotations.Before;
import ru.otus.elena.tester.myannotations.Test;

public class Tester {

    public static ArrayList<String> doWork(Class<?> clazz) {
        
        ArrayList<String> list = new ArrayList<>();
        if (clazz == null) {
            list.add("tests don't exist");
            return list;
        }
        Method[] methods = clazz.getMethods();
        if (methods.length == 0) {
            list.add("tests don't exist");
            return list;
        }
        try {
            List<Method> beforeMethods = new ArrayList<>();
            List<Method> testMethods = new ArrayList<>();
            List<Method> afterMethods = new ArrayList<>();

            s1:
            for (Method m : methods) {
                Annotation b = m.getAnnotation(Before.class);
                Annotation t = m.getAnnotation(Test.class);
                Annotation a = m.getAnnotation(After.class);
                if (b != null) {
                    beforeMethods.add(m);
                    continue s1;
                } else if (t != null) {
                    testMethods.add(m);
                    continue s1;
                } else if (a != null) {
                    afterMethods.add(m);
                    continue s1;
                }
            }
            s2:
            for (Method m : testMethods) {
                StringBuilder result = new StringBuilder();
                Object obj = clazz.newInstance();
                for (Method n : beforeMethods) {
                    try {
                        n.invoke(obj);
                    } catch (InvocationTargetException ite) {
                        result.append(n.getName() + " = Test failed: " + ite.getCause().getMessage());
                        break s2;
                    }
                }
                try {
                    result.append(m.getName()).append(" = ");
                    m.invoke(obj);
                    result.append("success");
                } catch (InvocationTargetException ite) {
                    result.append("Test failed: " + ite.getCause().getMessage());
                }
                for (Method n : afterMethods) {
                    try {
                        n.invoke(obj);
                        list.add(result.toString());                        
                    } catch (InvocationTargetException ite) {
                        result.append(n.getName() + " = Test failed: " + ite.getCause().getMessage());
                        break s2;
                    }
                }
            }
            
        }catch(Exception e) {
            //System.out.println(e.getMessage());
            list.add(e.getMessage());
            return list;
        }
        return list;
    }
}    
            

 