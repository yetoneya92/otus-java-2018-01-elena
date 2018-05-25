
package ru.otus.elena.parallelsort2;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Random;
import org.junit.Test;

public class ParallelSortTest {
private static final Random GENERATOR = new Random();    
    public ParallelSortTest() {
    }
    
    @Test
    public void testSplit() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException{
        System.out.println("test Split");
        ParallelSort instance = new ParallelSort(); 
                int size = 23;      
        int[] numbers = new int[size];
        for (int i = 0; i < size; i++) {
            numbers[i] = i;
        }
        System.out.println(Arrays.toString(numbers));
        Class[]parameterTypes={int[].class,int.class};
        Method methodsplit=ParallelSort.class.getDeclaredMethod("split", parameterTypes);
        methodsplit.setAccessible(true);
        methodsplit.invoke(instance,numbers,4);
        Field fieldunsorted=instance.getClass().getDeclaredField("unsorted");
        fieldunsorted.setAccessible(true);
        int[][]unsorted=(int[][])fieldunsorted.get(instance);       
        System.out.println(Arrays.deepToString(unsorted));       
    

    }

    @Test
    public void testSort() {
        System.out.println("test Sort");
        ParallelSort instance = new ParallelSort();           
        int size = 22;      
        int[] numbers = new int[size];
        for (int i = 0; i < size; i++) {
            numbers[i] = GENERATOR.nextInt(200);
        }
        System.out.println(Arrays.toString(numbers));
        instance.sort(numbers, 4);       
        int[]sorted=instance.getSorted();
        System.out.println(Arrays.toString(sorted));
    }

   
}
