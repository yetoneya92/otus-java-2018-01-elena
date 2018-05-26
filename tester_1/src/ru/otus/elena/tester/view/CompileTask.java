
package ru.otus.elena.tester.view;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import javafx.concurrent.Task;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class CompileTask  extends Task<Void>{
   // final List<Path>pathList;  
    final Path folder;
    //final Path javaFile;
    private final String SOURCE_CODE;
    private static String CLASS_NAME;
    
    
    public CompileTask(List<Path>pathList,String content,String className){
        //this.pathList=pathList;
        folder=pathList.get(1);
        //javaFile=pathList.get(0);
        SOURCE_CODE=content;
        CLASS_NAME=className;
    }

    @Override
    protected Void call() throws Exception {
        Path dir=Paths.get(folder.toString(),"classes");
        File outputDir = dir.toFile();
        outputDir.mkdir();
        System.out.println(CLASS_NAME);
        doCompilation(outputDir);
        //invoke(outputDir); 
        return null;
    }

    public void doCompilation(File outputDir) throws IOException, ClassNotFoundException {       
        SimpleJavaFileObject fileObject = new DynamicJavaSourceCodeObject(CLASS_NAME, SOURCE_CODE);
        JavaFileObject javaFileObjects[] = new JavaFileObject[]{fileObject};      
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager stdFileManager = compiler.getStandardFileManager(null, Locale.getDefault(), null);
        Iterable compilationUnits = Arrays.asList(javaFileObjects);
        String[] compileOptions = new String[]{"-d", outputDir.getAbsolutePath()};
        Iterable compilationOptions = Arrays.asList(compileOptions);       
        DiagnosticCollector diagnostics = new DiagnosticCollector();
        CompilationTask compilerTask = compiler.getTask(null, stdFileManager, diagnostics, compilationOptions, null, compilationUnits);
        boolean status = compilerTask.call();
        if (!status) {          
            for (Diagnostic diagnostic:(List<Diagnostic>)diagnostics.getDiagnostics()) {
                System.err.format("Error on line %d in %s", diagnostic.getLineNumber(), diagnostic);
            }
        }
        stdFileManager.close(); 
    }
 

 
    private static void invoke(File outputDir) throws MalformedURLException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        URL[] urls = new URL[]{outputDir.toURI().toURL()};
        URLClassLoader ucl = new URLClassLoader(urls);
        Class clazz = ucl.loadClass(CLASS_NAME);
        Method main = clazz.getMethod("main", String[].class);
        String[] mainArgs = new String[]{};
        System.out.format("invoking %s.main()%n", clazz.getName());
        main.invoke(null, (Object) mainArgs);
    }
 


class DynamicJavaSourceCodeObject extends SimpleJavaFileObject {
    private String qualifiedName;
    private String sourceCode;
 
    protected DynamicJavaSourceCodeObject(String name, String code) {
        super(URI.create("string:///" + name.replaceAll("\\.", "/") + Kind.SOURCE.extension), Kind.SOURCE);
        this.qualifiedName = name;
        this.sourceCode = code;
    }
 
    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return sourceCode;
    }
 
    public String getQualifiedName() {
        return qualifiedName;
    }
 
    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }
 
    public String getSourceCode() {
        return sourceCode;
    }
 
    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }
}}