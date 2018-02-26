/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.otus.elena.obfuscation;

import java.util.ArrayList;

class Stack<E> extends ArrayList<E> {

    void push(E element) {
        
        if (this.isEmpty()) {
            this.add(element);
        } else {
            int si = this.size();
            this.add(null);
            for (int i = si - 1; i >= 0; i--) {               
               this.set(i+1, this.get(i));
            }
            this.set(0, element);
        }
        System.out.println(this);
    }

    E pop() {
        if (this.isEmpty()) {
            return null;
        } else {
            E result = super.get(0);
            super.remove(0);
            return result;
        }
    }

    public boolean isEmpty() {
        return super.isEmpty();
    }
}
