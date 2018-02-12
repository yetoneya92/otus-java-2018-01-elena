/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.otus.elena.mavenproject7;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.util.function.IntFunction;

/**
 * VM options -Xmx512m -Xms512m
 * <p>
 * Runtime runtime = Runtime.getRuntime();
 * long mem = runtime.totalMemory() - runtime.freeMemory();
 * <p>
 * System.gc()
 * <p>
 * jconsole, connect to pid
 */
@SuppressWarnings({"RedundantStringConstructorCall", "InfiniteLoopStatement"})
public class Main<T> {
    static Runtime runtime = Runtime.getRuntime();
    public static void main(String... args) throws InterruptedException, IOException {
        int size=10000000;
        long mem1=getMemorySize();
        String[]empty=new String[size];
        for(int i=0;i<size;i++){
            empty[i]="";
        }
        long mem2=getMemorySize();
        System.out.println("emptyStringSize="+(mem2-mem1)/size);
                mem1=getMemorySize();
        Object[]objects=new Object[size];
        for(int i=0;i<size;i++){
            objects[i]=new Object();
        }
        mem2=getMemorySize();
        System.out.println("ObjectSize="+(mem2-mem1)/size);
        
        String str="";
        System.out.println("emptyStringSize="+getMemorySize(str));
        
        int number=5;
        System.out.println("numberSize="+getMemorySize(number));
        
        for(int i=10; i<=100000000;i*=10){
            String[]cont=new String[i];
            System.out.println("String["+i+"] container size: "+getMemorySize());
            System.out.println("String["+i+"] container size: "+getMemorySize(cont));
            
            String[]strings=makeArray(i,"abc",String[]::new);
            System.out.println("String["+i+"] size: "+getMemorySize());
            System.out.println("String["+i+"] size: "+getMemorySize(strings));
        }
        
      
    }

    public static Long getMemorySize() throws InterruptedException {
        System.gc();
        Thread.sleep(10);
        long mem = runtime.totalMemory() - runtime.freeMemory();
        return mem;
    }

    public static int getMemorySize(Serializable... objects) throws IOException {
        try (ByteArrayOutputStream byteObject = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteObject)) {
            for(Serializable o:objects)
            objectOutputStream.writeObject(o);
            objectOutputStream.flush();
            return byteObject.toByteArray().length;
        }
    }
           
    public static <T> T[] makeArray(int n, T obj, IntFunction<T[]> constr){
        T[] result= constr.apply(n);
        for (int i=0; i<n; i++){
            result[i]=obj;            
        }
        return result;
    }

}
