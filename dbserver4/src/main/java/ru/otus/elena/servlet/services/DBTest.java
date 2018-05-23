
package ru.otus.elena.servlet.services;

import java.util.Random;
import org.springframework.stereotype.Component;
import ru.otus.elena.dbservice.dataset.Baby;
import ru.otus.elena.dbservice.dataset.Phone;
import ru.otus.elena.dbservice.interfaces.DBService;

@Component
public class DBTest {
    private static Random generator=new Random();
    private Thread writerThread;
    private Thread readerThread;
    final private  DBService service;    

    public DBTest(DBService service) {
        this.service = service;
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
    }
}
