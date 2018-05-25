package ru.otus.elena.parallelsort2;

import java.util.Random;

public class Main {
    private static final Random GENERATOR = new Random();
    private static final int SIZE = 1000000;
    private static final int THREAD_NUMBER = 3;

    public static void main(String[] args) {
        ParallelSort sort = new ParallelSort();
        int size = SIZE + GENERATOR.nextInt(SIZE);
        int threads = THREAD_NUMBER + GENERATOR.nextInt(THREAD_NUMBER);
        int[] numbers = new int[size];
        for (int i = 0; i < size; i++) {
            numbers[i] = GENERATOR.nextInt(SIZE);
        }
        sort.sort(numbers, threads);
        int[] sorted = sort.getSorted();
        for (int i = 0; i < sorted.length; i += sorted.length/ 20) {
            System.out.println(sorted[i]);
        }
    }
}
