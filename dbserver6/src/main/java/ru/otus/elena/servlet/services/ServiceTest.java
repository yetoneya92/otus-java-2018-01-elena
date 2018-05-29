
package ru.otus.elena.servlet.services;

import java.util.Random;
import org.springframework.stereotype.Component;
import ru.otus.elena.dbservice.dataset.Baby;
import ru.otus.elena.dbservice.dataset.Phone;
import ru.otus.elena.dbservice.interfaces.Service;

@Component
public class ServiceTest {
    private static Random generator=new Random();
    private Thread writerThread;
    private Thread readerThread;
    private volatile int stopWrite=0;
    private volatile int stopRead=0;
    private  Service service;

    public ServiceTest() {
    }

    public ServiceTest(Service service) {
        this.service = service;
    }
    
    public void setService(Service service){
        this.service=service;        
    }

    public void testDB() {               
        service.createTable(Phone.class);
        service.createTable(Baby.class);
        Runnable writeData = () -> {
            s:
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    stopWrite=1;
                    break s;
                }
                int suffix = generator.nextInt(1000);
                String name = "baby" + suffix;
                service.save(new Baby(name, new Phone(suffix)));
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        };
        Runnable readData = () -> {
            m:
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    stopRead=1;
                    break m;
                }
                int id = generator.nextInt(1000);
                Baby re = service.loadById(id, Baby.class);

                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }           
        };
        writerThread = new Thread(writeData);
        readerThread = new Thread(readData);
        writerThread.start();
        readerThread.start();
        
    }


    public void stopTest() {       
        writerThread.interrupt();
        readerThread.interrupt();
        while(stopWrite+stopRead<2){
            ;
        }
        service.deleteTable(Baby.class);
        service.deleteTable(Phone.class);
    }
}
