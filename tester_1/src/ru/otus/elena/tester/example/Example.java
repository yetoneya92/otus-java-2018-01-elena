
package ru.otus.elena.tester.example;

public class Example {

    public int sum(int a, int b) {
        return a + b < Integer.MAX_VALUE ? (a + b) : Integer.MAX_VALUE;
    }

    public int sub(int a, int b) {
        return a - b > Integer.MIN_VALUE ? (a - b) : Integer.MIN_VALUE;
    }
    
    public int experience(int a, int b){
        return (a+b)/a-b;
    }
}
