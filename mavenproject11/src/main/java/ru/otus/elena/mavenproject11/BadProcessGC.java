package ru.otus.elena.mavenproject11;

import com.sun.management.GarbageCollectionNotificationInfo;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.management.ListenerNotFoundException;
import javax.management.Notification;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;


public class BadProcessGC {
      
    static int num;

    public static void main(String[] args) {
        try {
            if (args.length > 0) {
                num = Integer.parseInt(args[0]);
            } else {
                num = 1000;
            }

        } catch (NumberFormatException nfe) {
            return;
        }
        MemoryUtil mem = new MemoryUtil();
        mem.startGCMonitor();

        Runnable garbageMaker = () -> {
            int counter = 0;
            ArrayList<String> str = new ArrayList<>();
            for (int k = 0; k < num; k++) {
                str.add("aaa");
                String[] numbers1 = new String[1000000];
                for (int j = 0; j < num; j++) {
                    int[] numbers2 = new int[1000000];
                    for (int i = 0; i < num; i++) {
                        int[] numbers3 = new int[1000000];
                        for (int l = 0; l < num; l++) {
                            int[] numbers4 = new int[1000000];
                        }

                    }
                    //PrintStream out = new PrintStream(System.out);
                    // out.println(counter);

                }
            }
            MemoryUtil.stopGCMonitor();
        };
        Thread t=new Thread(garbageMaker);
        t.start();
        try {
            t.join();
        } catch (InterruptedException ex) {
              MemoryUtil.stopGCMonitor();
                return;
        }
    }
}

class MemoryUtil {

    static Map<String, List<Long>> results = new HashMap<>();
    private static NotificationListener gcHandler = new NotificationListener() {
        @Override
        public void handleNotification(Notification notification, Object handback) {
            if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                GarbageCollectionNotificationInfo gcInfo = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
                String gName=gcInfo.getGcName();
                StringBuilder sb = new StringBuilder();
                sb.append("[").append(gcInfo.getGcAction()).append(" / ").append(gcInfo.getGcCause())
                        .append(" / ").append(gcInfo.getGcName()).append(" / (");
                sb.append(") -> (");
                sb.append("), ").append(gcInfo.getGcInfo().getDuration()).append(" ms]");
                System.out.println(sb.toString());
                if (results.containsKey(gName)) {
                    List<Long>list=results.get(gName);
                    list.set(0,list.get(0)+1L);
                    list.set(1,list.get(1)+gcInfo.getGcInfo().getDuration());
                } else {
                    results.put(gName, new ArrayList<Long>() {
                        {
                            add(1L);
                            add(gcInfo.getGcInfo().getDuration());
                        }
                    });
                }
            }
        }

    };

    public static void startGCMonitor() {
        for (GarbageCollectorMXBean mBean : ManagementFactory.getGarbageCollectorMXBeans()) {
            ((NotificationEmitter) mBean).addNotificationListener(gcHandler, null, null);
        }
    }

    public static void stopGCMonitor() {
        for(Map.Entry<String,List<Long>>entry:results.entrySet()){
            System.out.println(entry.getKey()+": total CollectionCount="+entry.getValue().get(0)
                    +" total duration="+entry.getValue().get(1)+" ms");
        }
        for (GarbageCollectorMXBean mBean : ManagementFactory.getGarbageCollectorMXBeans()) {
            try {
                ((NotificationEmitter) mBean).removeNotificationListener(gcHandler);
            } catch (ListenerNotFoundException e) {
            }
        }
    }
}  

