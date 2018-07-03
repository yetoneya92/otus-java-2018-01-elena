
package ru.otus.elena.dbservice.services;

import java.util.ArrayList;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.elena.dbservice.dataset.Baby;
import ru.otus.elena.dbservice.dataset.Phone;
import ru.otus.elena.dbservice.dbservice.LoadResult;
import ru.otus.elena.dbservice.interfaces.Service;

@Component
public class ServiceTest {
    private static Random generator=new Random();
    private Thread writerThread;
    private Thread readerThread;
    @Autowired
    private  Service service;    

    public ServiceTest() {

    }

    public void testDB() {
        service.createTable(Phone.class);
        service.createTable(Baby.class);
        Runnable writeData = () -> {
            s:
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    break s;
                }
                int suffix = generator.nextInt(1000);
                String name = "baby" + suffix;
                ArrayList<String>messages=service.save(new Baby(name, new Phone(suffix,suffix/2)));
                System.out.println(messages);
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
                    break m;
                }
                int id = generator.nextInt(1000);
                LoadResult result= service.loadById(id, Baby.class);
                System.out.println(result.getObject()+" "+result.getMessages());
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
    }
}
