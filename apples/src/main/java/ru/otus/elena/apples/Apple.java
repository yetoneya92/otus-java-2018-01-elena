/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.otus.elena.apples;

/**
 *
 * @author Lena
 */

    
import java.util.Objects;

import com.google.common.collect.ComparisonChain;

class Apple implements Comparable<Apple>{
    private int size;
    private String color;
    public Apple(int size,String color){
       this.size=size;
       this.color=color;
    }

    @Override
    public int compareTo(Apple t) {
        return ComparisonChain.start()
                .compare(size,t.size)
                .compare(color, t.color)
                .result();
    }
    @Override
    public int hashCode(){
        return Objects.hash(size,color);
    }
    @Override
    public boolean equals(Object obj){
        if(obj==null||getClass()!=obj.getClass()){
            return false;
        }
        Apple other=(Apple)obj;
        return Objects.equals(size,other.size)&&Objects.equals(color,other.color);
    }
    @Override
    public String toString(){
        return "size="+size+"color="+color;
                
    }
    
        

}
