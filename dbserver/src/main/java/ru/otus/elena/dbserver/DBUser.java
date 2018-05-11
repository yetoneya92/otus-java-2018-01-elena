
package ru.otus.elena.dbserver;

import java.util.Random;
import ru.otus.elena.cache.IdleTimeCache;
import ru.otus.elena.dbandcache.dataset.Baby;
import ru.otus.elena.dbandcache.dataset.Phone;
import ru.otus.elena.dbandcache.dbservice.DBServiceNative;

public class DBUser {
    private static Random generator=new Random();
    private Thread writerThread;
    private Thread readerThread;
    private  DBServiceNative service;
    private IdleTimeCache cache;
    static volatile int stopWrite;
    static volatile int stopRead;
    
    public void useDB() {
        service = new DBServiceNative("localhost", "db_example", "me", "me");
        service.deleteAllTables();
        service.createTable(Phone.class);
        service.createTable(Baby.class);
        cache = new IdleTimeCache(1000, 10000);
        service.setCache(cache);
        JsonWriter writer=new JsonWriter();
        Runnable writeData = () -> {
            s:
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    stopWrite = 1;                  
                    break s;
                }
                int suffix = generator.nextInt(1000);
                String name = "baby" + suffix;
                service.save(new Baby(name, new Phone(suffix)));
                writer.writeJson(cache.getCacheSize(),cache.getHitCount(), cache.getMissCount());
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    stopWrite = 1;

                }
            }
            
        };
        Runnable readData = () -> {
            m:
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    stopRead = 1;
                    break m;
                }
                int id = generator.nextInt(1000);
                Baby re = service.loadById(id, Baby.class);
                System.out.println(cache.getHitCount());
                System.out.println(cache.getCacheSize());
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    stopRead = 1;
                }
            }
            
        };
        writerThread = new Thread(writeData);
        readerThread = new Thread(readData);
        writerThread.start();
        readerThread.start();

    }


    public void stopUse() {       
        writerThread.interrupt();
        readerThread.interrupt();
        s: while (true) {
            if (stopWrite+stopRead > 1) {
                
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    
                }
                System.out.println("shutdown");
                service.shutDown();
                break s;
            }
        }

    }

    public IdleTimeCache getCache() {
        return cache;
    }
}
