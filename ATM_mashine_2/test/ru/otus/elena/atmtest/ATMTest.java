/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.otus.elena.atmtest;

import ru.otus.elena.atmmashine.ATM;

public class ATMTest {
    public static void main(String[] args) {
        System.out.println("start");
        ATM atm=new ATM();                
        atm.setInitialState(100);
        atm.launchTerminal();
        System.out.println(atm.getBalance());
    }
}
