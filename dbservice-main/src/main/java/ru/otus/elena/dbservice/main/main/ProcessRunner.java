
package ru.otus.elena.dbservice.main.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProcessRunner {

    private StringBuffer out = new StringBuffer();
    private Process process;
    private String processName;
    
    
    public ProcessRunner(String processName){
        this.processName=processName;
    }

    public void start(String command) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(command.split(" "));
        pb.redirectErrorStream(true);
        process = pb.start();
        
        StreamListener output = new StreamListener(process.getInputStream(), "OUTPUT="+processName);
        output.start();
    }

    public void stop() {       
        process.destroy();
    }

    public String getOutput() {
        return out.toString();
    }
    public void clearOutput(){
        out=new StringBuffer();
    }

    private class StreamListener extends Thread {
        private final Logger logger = Logger.getLogger(StreamListener.class.getName());
        private final InputStream is;
        private final String type;

        private StreamListener(InputStream is, String type) {
            this.is = is;
            this.type = type;
            this.setDaemon(true);
        }

        @Override
        public void run() {
            try (InputStreamReader isr = new InputStreamReader(is)) {                
                BufferedReader br = new BufferedReader(isr);
                String line;
                while ((line = br.readLine()) != null) {
                    out.append(type).append('>').append(line).append('\n');                    
                }
            } catch (IOException e) {
                out.append(e.getMessage());
                logger.log(Level.SEVERE, e.getMessage());
            }
        }
    }
}
