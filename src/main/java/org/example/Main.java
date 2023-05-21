package org.example;

import java.util.*;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) {
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            Runnable logic = () -> {
                String text = generateRoute("RLRFR", 100);
                int include = (int) text.chars()
                        .filter(c -> c == 'R')
                        .count();
                System.out.println(include);

                synchronized (sizeToFreq) {
                    if (sizeToFreq.containsKey(include)) {
                        int count = sizeToFreq.get(include);
                        count++;
                        sizeToFreq.put(include, count);
                    } else {
                        sizeToFreq.put(include, 1);
                    }
                }
            };
            Thread thread = new Thread(logic);
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        int include = 0;
        int maxValue = 0;
        for (int key : sizeToFreq.keySet()) {
            if (sizeToFreq.get(key) > maxValue) {
                maxValue = sizeToFreq.get(key);
                include = key;
            }
        }
        sizeToFreq.remove(include);

        System.out.println("Самое частое количество повторений " + include + " (встретилось " + maxValue + " раз)");
        System.out.println("Другие размеры:");
        for (int key : sizeToFreq.keySet()) {
            System.out.println("- " + key + " (" + sizeToFreq.get(key) + " раз)");
        }
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}