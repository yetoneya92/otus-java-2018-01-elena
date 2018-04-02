
package ru.otus.elena.atmmashine;

import java.util.Map;
import javafx.application.Application;

public class ATM {

    public Coffer coffer;

    public ATM() {
        coffer=new Coffer();       
    }
    public void setInitialState(int number){
        coffer.setInitialState(number);
    }
    public Map<Integer,Integer> getInitialState(){
        return coffer.getInitialContent();
    }
    public Map<Integer,Integer> getCurrentState(){
        return coffer.getContent();
    }
    public int getBalance(){        
        return coffer.getBalance();
    }
    public void launchTerminal(){
        Runnable launchTerminal=()->Application.launch(MainApp.class);
        new Thread(launchTerminal).start();
        MainApp mainApp=MainApp.waitMainApp();        
        mainApp.setCoffer(coffer);      
    }

}
