package ru.otus.elena.obfuscation;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Lena
 */
public class Main {
        public static void main(String[] args) {
        Stack<String> stack = new Stack<String>();
        stack.push("summer");
        stack.push("autumn");
        stack.push("winter");
        stack.push("spring");
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.isEmpty());

    }
    
}
