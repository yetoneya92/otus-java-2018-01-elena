/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.otus.elena.gc2;

import java.util.ArrayList;


class Benchmark implements BenchmarkMBean {
    private volatile int size = 0;
    private ArrayList<String>strs=new ArrayList<>();    
    String s="abc";
    int counter=0;
    @SuppressWarnings("InfiniteLoopStatement")
    void run() { 
        System.out.println("Starting the loop");
        while (true) {
            int local = size;
            Object[] array = new Object[local];
            System.out.println("Array of size: " + array.length + " created");
            for (int i = 0; i < local; i++) {
                array[i] = new String(s);
            if(i%100==0)strs.add(s);
            counter++;
            
            }
            
            System.out.println("Created " + local + " objects.");
            System.out.println("added " + counter + " string.");
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void setSize(int size) {
        this.size = size;
    }

}