package ru.otus.elena.parallelsort1;

import java.util.Arrays;
import java.util.Random;

public class Main {

    private static final Random GENERATOR = new Random();
    private static final int SIZE = 1000000;

    public static void main(String[] args) {
        int[] numbers = new int[SIZE];
        for (int i = 0; i < SIZE; i++) {
            numbers[i] = GENERATOR.nextInt(SIZE);
        }
        for (int i = 0; i < 10; i++) {
            System.out.println(numbers[i]);
        }
        Arrays.parallelSort(numbers);
        for (int i = 0; i < 10; i++) {
            System.out.println(numbers[i]);
        }
    }
}
