
package ru.otus.elena.parallelsort2;

import java.util.Arrays;
import java.util.Random;

public class ParallelSort {

    private  int[][] unsorted;
    private int[]sorted;
    boolean isFirst=true;
    int threads;
    int arrayLength;
    

    public void test1() {
        int size = 23;      
        int[] numbers = new int[size];
        for (int i = 0; i < size; i++) {
            numbers[i] = i;
        }
        System.out.println(Arrays.toString(numbers));
        split(numbers, 4);
        System.out.println(Arrays.deepToString(unsorted));       
    }

    public void sort(int[] numbers, int threads) {
        if (!checkData(numbers, threads)) {
            sorted=null;
            return;
        }
        if (numbers.length < threads * 2||threads==1) {
            Arrays.sort(numbers);
            sorted=numbers;
            return;
        }       
        this.arrayLength = numbers.length;
        this.threads=threads;
        split(numbers, threads);
        doWork();
        merge();

    }

    private boolean checkData(int[] numbers, int threads) {
        if (threads == 0 || numbers.length == 0) {
            return false;
        }
        
        return true;
    }

    private void split(int[] numbers, int threadNumber) {       
        if (isFirst) {          
            unsorted = new int[threadNumber][];
            isFirst=false;
        }      
        int newSize = numbers.length / threadNumber;
        unsorted[threadNumber - 1] = new int[newSize];
        System.arraycopy(numbers, 0, unsorted[threadNumber - 1], 0, newSize);
        int[] newArray = new int[numbers.length - newSize];
        System.arraycopy(numbers, newSize, newArray, 0, numbers.length - newSize);
        //System.out.println(Arrays.toString(unsorted[threadNumber-1]));
        threadNumber--;
        if (threadNumber == 0) {
            isFirst=true;
            return;
        }
        split(newArray, threadNumber);

    }


    private void doWork() {
        try {
            for (int i = 0; i < threads; i++) {
                final int[] ts = unsorted[i];
                Runnable sort = () -> Arrays.sort(ts);
                Thread thread = new Thread(sort);
                thread.start();
                thread.join();
            }

        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }

    }
    private void merge() {
        sorted = new int[arrayLength];
        int[] acc = unsorted[0];
        for (int i = 1; i < threads; i++) {
            acc = merge(acc, unsorted[i]);
            if (i == threads - 1) {
                System.arraycopy(acc, 0, sorted, 0, arrayLength);
            }
        }

    }
    private int[] merge(int[]leftArray,int[]rightArray){
        int[]acc=new int[leftArray.length+rightArray.length];
        int lcursor=0;
        int rcursor = 0;
        int counter = 0;
        s:
        while (lcursor < leftArray.length && rcursor < rightArray.length) {
            if (leftArray[lcursor] <= rightArray[rcursor]) {
                acc[counter] = leftArray[lcursor];
                //System.out.println(acc[counter]);
                lcursor++;
            } else {
                acc[counter] = rightArray[rcursor];
                //System.out.println(acc[counter]);
                rcursor++;
            }
            counter++;
            if (lcursor == leftArray.length) {
                System.arraycopy(rightArray, rcursor, acc, counter, rightArray.length-rcursor);
                break s;
            }
            if (rcursor == rightArray.length) {
                System.arraycopy(leftArray, lcursor, acc, counter, leftArray.length-lcursor);
                break s;
            }
        }
        
        return acc;
    }
    
    public int[] getSorted() {
        return sorted;
    }
}
