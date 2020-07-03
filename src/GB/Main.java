package GB;

import java.util.Arrays;

public class Main {
    static final int size = 10000000;
    static final int h = size / 2;


    public static void main(String[] args) {
        float[] arr = new float[size];
        Arrays.fill(arr, 1);
        System.out.println("Метод в один поток:");
        runSimple(arr);
        System.out.println("\nМетод в два потока:");
        runDuo(arr);
        System.out.println("Конец программы");
    }

    static void runSimple(float[] array){
        long a = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            array[i] = (float)(array[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
        System.out.println("Время на цикл расчета: " + (System.currentTimeMillis() - a));
    }

    static void runDuo(float[] array){
        float[] a1 = new float[h];
        float[] a2 = new float[h];

        long a = System.currentTimeMillis();
        System.arraycopy(array, 0, a1, 0, h);
        System.arraycopy(array, h, a2, 0, h);
        System.out.println("Время разбивки массива: " + (System.currentTimeMillis() - a));

        Thread part1 = new Thread(new Runnable() {
            @Override
            public void run() {
                long a = System.currentTimeMillis();
                for (int i = 0; i < a1.length; i++) {
                    a1[i] = (float)(a1[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
                }
                System.out.println("Время на цикл расчета 1 потока: " + (System.currentTimeMillis() - a));
            }
        });

        Thread part2 = new Thread(new Runnable() {
            @Override
            public void run() {
                long a = System.currentTimeMillis();
                for (int i = 0; i < a2.length; i++) {
                    a2[i] = (float)(a2[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
                }
                System.out.println("Время на цикл расчета 2 потока: " + (System.currentTimeMillis() - a));
            }
        });

        part1.start();
        part2.start();

        try {
            part1.join();
            part2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        a = System.currentTimeMillis();
        System.arraycopy(a1, 0, array, 0, h);
        System.arraycopy(a2, 0, array, h, h);
        System.out.println("Время склейки массива: " + (System.currentTimeMillis() - a));
    }
}
