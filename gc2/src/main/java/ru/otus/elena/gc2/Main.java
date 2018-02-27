package ru.otus.elena.gc2;


import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;


public class Main {
    public static void main(String... args) throws Exception {
        System.out.println("Starting pid: " + ManagementFactory.getRuntimeMXBean().getName());

        int size = 5000000;

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("ru.otus.elena:type=Benchmark");
        Benchmark mbean = new Benchmark();
        mbs.registerMBean(mbean, name);

        mbean.setSize(size);
        mbean.run();


        
    }

}