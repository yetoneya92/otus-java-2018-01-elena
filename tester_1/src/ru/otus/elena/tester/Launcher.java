
package ru.otus.elena.tester;

import javafx.application.Application;

public class Launcher implements Cloneable {

    private Tester tester = null;
    
    public static void main(String[] args) {

        new Launcher().launchTester();
    }

    public void launchTester() {
        if (tester != null) {
        } else {
            Runnable launchTester = () -> Application.launch(Tester.class);
            new Thread(launchTester).start();
            tester = Tester.waitTester();

        }
    }
}
