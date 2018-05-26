/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.otus.elena.tester.view;

import java.util.Set;
import javafx.concurrent.Task;
import com.sun.codemodel.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import ru.otus.elena.tester.Tester;
import ru.otus.elena.tester.myannotations.After;
import ru.otus.elena.tester.myannotations.Before;
import ru.otus.elena.tester.myannotations.Test;

public class CreateTestTemplateTask  extends Task<Void>{
    private final String packageName;
    private final  Set<String>methodNames;
    private final String choosenPath;
    private final  String folder;
    protected final List<Path> toTest;
    private Path pathToTest;
    private final Tester tester;
    private String name;
    public CreateTestTemplateTask(Set<String>methodNames,String packageName,String choosenPath,String folder,List<Path> toTest,Tester tester){
        this.methodNames=methodNames;
        this.packageName=packageName;
        this.choosenPath=choosenPath;
        this.folder=folder;
        this.toTest=toTest;
        this.tester=tester;
    }
    
    @Override
    protected Void call() {
        try {           
            String className = choosenPath.substring(choosenPath.lastIndexOf(File.separator) + 1).replaceAll(".class", "");
            String testClassName = className + "Test";
            String nameForPath = testClassName + ".java";
            //String toImport = packageName + "." + className;
            name=packageName+"."+testClassName;
            pathToTest = Paths.get(folder,"java",packageName,nameForPath);
            Path pathToDir=Paths.get(folder,"java");
            boolean b1 = Files.deleteIfExists(pathToTest);
            File file = pathToDir.toFile();
            file.mkdirs();
            buildClass(file, name);            
            return null;
        } catch (Exception e) {
            System.out.println("Exception" + e.getMessage());
            return null;
        }

    }

    private void buildClass(File file, String name) {
        try {
            JCodeModel codeModel = new JCodeModel();
 
            JDefinedClass dc = codeModel._class(name);

            JClass test=codeModel.ref(Test.class);
            JClass before = codeModel.ref(Before.class);
            JClass after = codeModel.ref(After.class);
            JMethod setUp=dc.method(JMod.PUBLIC, codeModel.VOID, "setUp");
            setUp.annotate(Before.class);
            JMethod tearDown=dc.method(JMod.PUBLIC, codeModel.VOID, "tearDown");
            tearDown.annotate(After.class);
           
            String testMethod = null;
            Method method = null;
            for (String s : methodNames) {
                method = Tester.methods.get(s);
                testMethod = method.getName() + "Test";
            }
            JMethod m = dc.method(JMod.PUBLIC, codeModel.VOID, testMethod);
            m.annotate(Test.class);
            //JBlock executerBlock = m.body();
           // executerBlock.staticInvoke(cl2, testMethod);
           
            //buildTestMethod(method, m);
            codeModel.build(file);
        } catch (JClassAlreadyExistsException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void buildTestMethod(Method method, JMethod jmethod) {
        //m.body()._return(JExpr.lit(5));
    }

    @Override
    protected void succeeded() {
        toTest.add(pathToTest);
        toTest.add(Paths.get(folder));
        try {           
            tester.showTestTemplate();
            TesterTestController tController = tester.getTesterTestController();
            tController.setClassName(name);
            tController.setText();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
