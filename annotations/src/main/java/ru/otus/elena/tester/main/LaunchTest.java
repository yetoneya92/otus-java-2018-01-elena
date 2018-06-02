package ru.otus.elena.tester.main;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import ru.otus.elena.tester.myannotations.After;
import ru.otus.elena.tester.myannotations.Before;
import ru.otus.elena.tester.myannotations.Test;

public class LaunchTest {

    public static ArrayList<String> doWork(Class<?> clazz) {
        ArrayList<String> list = new ArrayList<>();
        try {           
            Object obj = clazz.newInstance();
            Method[] methods = clazz.getMethods();
            Annotation[] an;
            for (Method m : methods) {
                an = m.getDeclaredAnnotations();
                if (an.length != 0) {
                    if (an[0].annotationType().equals(Before.class)) {
                        m.invoke(obj);
                    }
                }
            }
            s:
            for (Method m : methods) {
                StringBuilder result = new StringBuilder();
                an = m.getAnnotations();
                if (an.length != 0) {
                    if (an[0].annotationType().equals(Test.class)) {
                        result.append(m.getName()).append("=");
                        try {
                            m.invoke(obj);
                            result.append("success");
                            list.add(result.toString());
                        } catch (InvocationTargetException ite) {
                            result.append("Test failed: " + ite.getCause().getMessage());
                            list.add(result.toString());
                            continue s;
                        }
                    }
                }
            }
            for (Method m : methods) {
                an = m.getAnnotations();
                if (an.length != 0) {
                    if (an[0].annotationType().equals(After.class)) {
                        m.invoke(obj);
                    }
                }
            }
            return list;
        } catch (Exception e) {
            list.add(e.getMessage());
            return list;
        }
    }
}
