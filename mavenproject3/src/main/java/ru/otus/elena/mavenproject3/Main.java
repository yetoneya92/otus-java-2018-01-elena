
package ru.otus.elena.mavenproject3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

class Stream extends Thread {

    private InputStream is;
    private String type;

    Stream(InputStream is, String type) {
        this.is = is;
        this.type = type;
    }

    @Override
    public void run() {
        try (InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr)) {
            String line = null;
            while ((!Thread.interrupted())&&((line=br.readLine())!=null)) {                   
                    System.out.println(type + " > " + line);
                
            }
            return;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

public class Main {

    public static void main(String args[]) {
        try {
            String cmd = null;
            String[] commands = {"proc.bat","proc1.bat","proc2.bat","proc3.bat","proc4.bat"};
            for (int i = 0; i < commands.length; i++) {
                cmd=commands[i];
                Process proc = new ProcessBuilder(cmd).start();
                Stream errorS = new Stream(proc.getErrorStream(), "ERROR");
                Stream outputS = new Stream(proc.getInputStream(), "OUTPUT");
                errorS.start();
                outputS.start();
                proc.waitFor();
                proc.destroy();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}