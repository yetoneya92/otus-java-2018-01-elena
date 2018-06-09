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
public class Main {

    public static void main(String[] args) {
        Apple goodApple = new Apple(10, "red");
        Apple badApple = new Apple(10, "blue");
        int com = badApple.compareTo(goodApple);
        if (com == -1) {
            System.out.println("яблоки разные");
        }
    }

}
